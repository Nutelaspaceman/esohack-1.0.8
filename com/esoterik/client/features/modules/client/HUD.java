/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.client;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.Render2DEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.ColorUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.MathUtil;
import com.esoterik.client.util.RenderUtil;
import com.esoterik.client.util.Timer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD
extends Module {
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("ColorSync", false));
    public Setting<Boolean> rainbow = this.register(new Setting<Object>("Rainbow", Boolean.valueOf(false), v -> this.colorSync.getValue() == false));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<Object>("Speed", Integer.valueOf(70), Integer.valueOf(0), Integer.valueOf(400), v -> this.rainbow.getValue() != false && this.colorSync.getValue() == false));
    public Setting<Boolean> potionIcons = this.register(new Setting<Boolean>("RemovePotionIcons", Boolean.valueOf(true), "Draws Potion Icons."));
    private final Setting<Boolean> watermark = this.register(new Setting<Boolean>("Watermark", Boolean.valueOf(false), "WaterMark"));
    private final Setting<Boolean> arrayList = this.register(new Setting<Boolean>("ArrayList", Boolean.valueOf(false), "Lists the active modules."));
    private final Setting<Boolean> serverBrand = this.register(new Setting<Boolean>("ServerBrand", Boolean.valueOf(false), "Brand of the server you are on."));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", Boolean.valueOf(false), "Your response time to the server."));
    private final Setting<Boolean> tps = this.register(new Setting<Boolean>("TPS", Boolean.valueOf(false), "Ticks per second of the server."));
    private final Setting<Boolean> fps = this.register(new Setting<Boolean>("FPS", Boolean.valueOf(false), "Your frames per second."));
    private final Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", Boolean.valueOf(false), "Your current coordinates"));
    private final Setting<Boolean> direction = this.register(new Setting<Boolean>("Direction", Boolean.valueOf(false), "The Direction you are facing."));
    private final Setting<Boolean> speed = this.register(new Setting<Boolean>("Speed", Boolean.valueOf(false), "Your Speed"));
    private final Setting<Boolean> potions = this.register(new Setting<Boolean>("Potions", Boolean.valueOf(false), "Your Speed"));
    public Setting<Boolean> textRadar = this.register(new Setting<Boolean>("TextRadar", Boolean.valueOf(false), "A TextRadar"));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", Boolean.valueOf(false), "ArmorHUD"));
    private final Setting<Boolean> percent = this.register(new Setting<Object>("Percent", Boolean.valueOf(false), v -> this.armor.getValue()));
    private final Setting<Boolean> totems = this.register(new Setting<Boolean>("Totems", Boolean.valueOf(false), "TotemHUD"));
    private final Setting<Greeter> greeter = this.register(new Setting<Greeter>("Greeter", Greeter.NONE, "Greets you."));
    public Setting<Boolean> time = this.register(new Setting<Boolean>("Time", Boolean.valueOf(false), "The time"));
    public Setting<Integer> hudRed = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> hudGreen = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> hudBlue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    private static HUD INSTANCE = new HUD();
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.field_190929_cY);
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    private final Timer timer = new Timer();
    private boolean shadow = true;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static HUD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUD();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.timer.passedMs(Managers.getInstance().textRadarUpdates.getValue().intValue())) {
            this.players = this.getTextRadarPlayers();
            this.timer.reset();
        }
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onRender2D(Render2DEvent event) {
        int x2;
        String text;
        int i;
        float f;
        char[] stringToCharArray;
        int[] arrayOfInt;
        if (HUD.fullNullCheck()) {
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.color = this.colorSync.getValue() != false ? ColorUtil.toARGB(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), 255) : ColorUtil.toRGBA(this.hudRed.getValue(), this.hudGreen.getValue(), this.hudBlue.getValue());
        String whiteString = "\u00a7f";
        if (this.watermark.getValue().booleanValue()) {
            arrayOfInt = new int[]{1};
            String string = esohack.getName() + " v" + "1.0.8";
            stringToCharArray = string.toCharArray();
            f = 0.0f;
            for (char c : stringToCharArray) {
                this.renderer.drawString(String.valueOf(c), 2.0f + f, 2.0f, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        int j = 0;
        if (this.arrayList.getValue().booleanValue()) {
            arrayOfInt = new int[]{1};
            f = 0.0f;
            for (i = 0; i < esohack.moduleManager.sortedModules.size(); ++i) {
                Module module = esohack.moduleManager.sortedModules.get(i);
                String text2 = module.getDisplayName() + (module.getDisplayInfo() != null ? " [\u00a7f" + module.getDisplayInfo() + "\u00a7r" + "]" : "");
                this.renderer.drawString(text2, width - 2 - this.renderer.getStringWidth(text2), 2 + j * 10, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                arrayOfInt[0] = arrayOfInt[0] + 1;
                ++j;
            }
        }
        int n = i = HUD.mc.field_71462_r instanceof GuiChat ? 14 : 0;
        if (this.serverBrand.getValue().booleanValue()) {
            void var16_25;
            text = "Server brand " + esohack.serverManager.getServerBrand();
            arrayOfInt = new int[]{1};
            stringToCharArray = text.toCharArray();
            f = 0.0f;
            i += 10;
            char[] text2 = stringToCharArray;
            int n2 = text2.length;
            boolean bl = false;
            while (++var16_25 < n2) {
                char c = text2[var16_25];
                this.renderer.drawString(String.valueOf(c), (float)(width - this.renderer.getStringWidth(text)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        if (this.potions.getValue().booleanValue()) {
            ArrayList<String> effects = new ArrayList<String>();
            for (PotionEffect potionEffect : esohack.potionManager.getOwnPotions()) {
                text = esohack.potionManager.getPotionString(potionEffect);
                effects.add(text);
            }
            Collections.sort(effects, Comparator.comparing(String::length));
            for (x2 = effects.size() - 1; x2 >= 0; --x2) {
                i += 10;
                text = (String)effects.get(x2);
                arrayOfInt = new int[]{1};
                f = 0.0f;
                for (char c4 : stringToCharArray = text.toCharArray()) {
                    this.renderer.drawString(String.valueOf(c4), (float)(width - this.renderer.getStringWidth(text)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                    f += (float)this.renderer.getStringWidth(String.valueOf(c4));
                    arrayOfInt[0] = arrayOfInt[0] + 1;
                }
            }
        }
        if (this.speed.getValue().booleanValue()) {
            void var16_29;
            text = "Speed " + esohack.speedManager.getSpeedKpH() + " km/h";
            arrayOfInt = new int[]{1};
            stringToCharArray = text.toCharArray();
            f = 0.0f;
            i += 10;
            char[] effects = stringToCharArray;
            x2 = effects.length;
            boolean bl = false;
            while (++var16_29 < x2) {
                char c = effects[var16_29];
                this.renderer.drawString(String.valueOf(c), (float)(width - this.renderer.getStringWidth(text)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        if (this.time.getValue().booleanValue()) {
            void var16_31;
            text = "Time " + new SimpleDateFormat("h:mm a").format(new Date());
            arrayOfInt = new int[]{1};
            stringToCharArray = text.toCharArray();
            f = 0.0f;
            i += 10;
            char[] effects = stringToCharArray;
            x2 = effects.length;
            boolean bl = false;
            while (++var16_31 < x2) {
                char c = effects[var16_31];
                this.renderer.drawString(String.valueOf(c), (float)(width - this.renderer.getStringWidth(text)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        if (this.tps.getValue().booleanValue()) {
            void var16_33;
            text = "TPS " + esohack.serverManager.getTPS();
            arrayOfInt = new int[]{1};
            stringToCharArray = text.toCharArray();
            f = 0.0f;
            i += 10;
            char[] effects = stringToCharArray;
            x2 = effects.length;
            boolean bl = false;
            while (++var16_33 < x2) {
                char c = effects[var16_33];
                this.renderer.drawString(String.valueOf(c), (float)(width - this.renderer.getStringWidth(text)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        String fpsText = "FPS " + Minecraft.field_71470_ab;
        String text3 = "Ping " + esohack.serverManager.getPing();
        if (this.fps.getValue().booleanValue()) {
            void var17_45;
            arrayOfInt = new int[]{1};
            stringToCharArray = fpsText.toCharArray();
            f = 0.0f;
            i += 10;
            char[] x2 = stringToCharArray;
            int n3 = x2.length;
            boolean bl = false;
            while (++var17_45 < n3) {
                char c5 = x2[var17_45];
                this.renderer.drawString(String.valueOf(c5), (float)(width - this.renderer.getStringWidth(fpsText)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c5));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        if (this.ping.getValue().booleanValue()) {
            void var17_47;
            arrayOfInt = new int[]{1};
            stringToCharArray = text3.toCharArray();
            f = 0.0f;
            i += 10;
            char[] x2 = stringToCharArray;
            int n4 = x2.length;
            boolean bl = false;
            while (++var17_47 < n4) {
                char c = x2[var17_47];
                this.renderer.drawString(String.valueOf(c), (float)(width - this.renderer.getStringWidth(text3)) + f - 2.0f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
                f += (float)this.renderer.getStringWidth(String.valueOf(c));
                arrayOfInt[0] = arrayOfInt[0] + 1;
            }
        }
        boolean inHell = HUD.mc.field_71441_e.func_180494_b(HUD.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
        int n5 = (int)HUD.mc.field_71439_g.field_70165_t;
        int n6 = (int)HUD.mc.field_71439_g.field_70163_u;
        int posZ = (int)HUD.mc.field_71439_g.field_70161_v;
        float nether = !inHell ? 0.125f : 8.0f;
        int hposX = (int)(HUD.mc.field_71439_g.field_70165_t * (double)nether);
        int hposZ = (int)(HUD.mc.field_71439_g.field_70161_v * (double)nether);
        esohack.notificationManager.handleNotifications(height - (i + 16));
        i = HUD.mc.field_71462_r instanceof GuiChat ? 14 : 0;
        String coordinates = n5 + ", " + n6 + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
        text3 = (this.direction.getValue() != false ? esohack.rotationManager.getDirection4D(false) + " " : "") + (this.coords.getValue() != false ? coordinates : "") + "";
        arrayOfInt = new int[]{1};
        stringToCharArray = text3.toCharArray();
        f = 0.0f;
        i += 10;
        for (char c6 : stringToCharArray) {
            this.renderer.drawString(String.valueOf(c6), 2.0f + f, height - i, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
            f += (float)this.renderer.getStringWidth(String.valueOf(c6));
            arrayOfInt[0] = arrayOfInt[0] + 1;
        }
        if (this.armor.getValue().booleanValue()) {
            this.renderArmorHUD(this.percent.getValue());
        }
        if (this.totems.getValue().booleanValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue() != Greeter.NONE) {
            this.renderGreeter();
        }
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }

    public void renderGreeter() {
        int width = this.renderer.scaledWidth;
        String text = "";
        switch (this.greeter.getValue()) {
            case TIME: {
                text = text + MathUtil.getTimeOfDay() + HUD.mc.field_71439_g.getDisplayNameString();
                break;
            }
            case LONG: {
                text = text + "looking swag today, " + HUD.mc.field_71439_g.getDisplayNameString() + " :^)";
                break;
            }
            default: {
                text = text + "Welcome " + HUD.mc.field_71439_g.getDisplayNameString();
            }
        }
        int[] arrayOfInt = new int[]{1};
        char[] stringToCharArray = text.toCharArray();
        float f = 0.0f;
        for (char c : stringToCharArray) {
            this.renderer.drawString(String.valueOf(c), (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f + f, 2.0f, this.rainbow.getValue() != false ? ColorUtil.rainbow(arrayOfInt[0] * HUD.getInstance().rainbowSpeed.getValue()).getRGB() : this.color, true);
            f += (float)this.renderer.getStringWidth(String.valueOf(c));
            arrayOfInt[0] = arrayOfInt[0] + 1;
        }
    }

    public void renderTotemHUD() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = HUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += HUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            int i = width / 2;
            boolean iteration = false;
            int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(totem, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", x + 19 - 2 - this.renderer.getStringWidth(totems + ""), y + 9, 0xFFFFFF);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }

    public void renderArmorHUD(boolean percent) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (HUD.mc.field_71439_g.func_70090_H() && HUD.mc.field_71442_b.func_78763_f() ? 10 : 0);
        for (ItemStack is : HUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(HUD.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
            if (!percent) continue;
            int dmg = 0;
            int itemDurability = is.func_77958_k() - is.func_77952_i();
            float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
            float red = 1.0f - green;
            dmg = percent ? 100 - (int)(red * 100.0f) : itemDurability;
            this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        this.shouldIncrement = true;
    }

    public static enum Greeter {
        NONE,
        TIME,
        LONG;

    }
}

