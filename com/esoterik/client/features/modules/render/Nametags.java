/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.opengl.GL11
 */
package com.esoterik.client.features.modules.render;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.RenderUtil;
import java.util.Objects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class Nametags
extends Module {
    private final Setting<Boolean> health = this.register(new Setting<Boolean>("Health", true));
    private final Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    private final Setting<Float> scaling = this.register(new Setting<Float>("Size", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(20.0f)));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private final Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", true));
    private final Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", true));
    private final Setting<Boolean> gamemode = this.register(new Setting<Boolean>("Gamemode", false));
    private final Setting<Boolean> entityID = this.register(new Setting<Boolean>("ID", false));
    private final Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", true));
    private final Setting<Boolean> sneak = this.register(new Setting<Boolean>("SneakColor", false));
    private final Setting<Boolean> heldStackName = this.register(new Setting<Boolean>("StackName", false));
    private final Setting<Boolean> whiter = this.register(new Setting<Boolean>("White", false));
    private final Setting<Boolean> scaleing = this.register(new Setting<Boolean>("Scale", false));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), v -> this.scaleing.getValue()));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.valueOf(false), v -> this.scaleing.getValue()));
    private static Nametags INSTANCE = new Nametags();

    public Nametags() {
        super("Nametags", "Better Nametags", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Nametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Nametags();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        for (EntityPlayer player : Nametags.mc.field_71441_e.field_73010_i) {
            if (player == null || player.equals((Object)Nametags.mc.field_71439_g) || !player.func_70089_S() || player.func_82150_aj() && !this.invisibles.getValue().booleanValue()) continue;
            double x = this.interpolate(player.field_70142_S, player.field_70165_t, event.getPartialTicks()) - Nametags.mc.func_175598_ae().field_78725_b;
            double y = this.interpolate(player.field_70137_T, player.field_70163_u, event.getPartialTicks()) - Nametags.mc.func_175598_ae().field_78726_c;
            double z = this.interpolate(player.field_70136_U, player.field_70161_v, event.getPartialTicks()) - Nametags.mc.func_175598_ae().field_78723_d;
            this.renderNameTag(player, x, y, z, event.getPartialTicks());
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.func_70093_af() ? 0.5 : 0.7;
        Entity camera = mc.func_175606_aa();
        assert (camera != null);
        double originalPositionX = camera.field_70165_t;
        double originalPositionY = camera.field_70163_u;
        double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, delta);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, delta);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.func_70011_f(x + Nametags.mc.func_175598_ae().field_78730_l, y + Nametags.mc.func_175598_ae().field_78731_m, z + Nametags.mc.func_175598_ae().field_78728_n);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (double)this.scaling.getValue().floatValue() * (distance * (double)this.factor.getValue().floatValue())) / 1000.0;
        if (distance <= 8.0 && this.smartScale.getValue().booleanValue()) {
            scale = 0.0245;
        }
        if (!this.scaleing.getValue().booleanValue()) {
            scale = (double)this.scaling.getValue().floatValue() / 100.0;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a((float)1.0f, (float)-1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.func_179114_b((float)(-Nametags.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)Nametags.mc.func_175598_ae().field_78732_j, (float)(Nametags.mc.field_71474_y.field_74320_O == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179147_l();
        if (this.rect.getValue().booleanValue()) {
            RenderUtil.drawRect(-width - 2, -(this.renderer.getFontHeight() + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.func_179084_k();
        ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
        if (renderMainHand.func_77962_s() && (renderMainHand.func_77973_b() instanceof ItemTool || renderMainHand.func_77973_b() instanceof ItemArmor)) {
            renderMainHand.field_77994_a = 1;
        }
        if (this.heldStackName.getValue().booleanValue() && !renderMainHand.field_190928_g && renderMainHand.func_77973_b() != Items.field_190931_a) {
            String stackName = renderMainHand.func_82833_r();
            int stackNameWidth = this.renderer.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.0f);
            this.renderer.drawStringWithShadow(stackName, -stackNameWidth, -(this.getBiggestArmorTag(player) + 20.0f), -1);
            GL11.glScalef((float)1.5f, (float)1.5f, (float)1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.getValue().booleanValue()) {
            GlStateManager.func_179094_E();
            int xOffset = -8;
            for (ItemStack stack : player.field_71071_by.field_70460_b) {
                if (stack == null) continue;
                xOffset -= 8;
            }
            xOffset -= 8;
            ItemStack renderOffhand = player.func_184592_cb().func_77946_l();
            if (renderOffhand.func_77962_s() && (renderOffhand.func_77973_b() instanceof ItemTool || renderOffhand.func_77973_b() instanceof ItemArmor)) {
                renderOffhand.field_77994_a = 1;
            }
            this.renderItemStack(renderOffhand, xOffset, -26);
            xOffset += 16;
            for (ItemStack stack : player.field_71071_by.field_70460_b) {
                if (stack == null) continue;
                ItemStack armourStack = stack.func_77946_l();
                if (armourStack.func_77962_s() && (armourStack.func_77973_b() instanceof ItemTool || armourStack.func_77973_b() instanceof ItemArmor)) {
                    armourStack.field_77994_a = 1;
                }
                this.renderItemStack(armourStack, xOffset, -26);
                xOffset += 16;
            }
            this.renderItemStack(renderMainHand, xOffset, -26);
            GlStateManager.func_179121_F();
        }
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.getDisplayColour(player));
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a((float)1.0f, (float)1500000.0f);
        GlStateManager.func_179121_F();
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179086_m((int)256);
        RenderHelper.func_74519_b();
        Nametags.mc.func_175599_af().field_77023_b = -150.0f;
        GlStateManager.func_179118_c();
        GlStateManager.func_179126_j();
        GlStateManager.func_179129_p();
        mc.func_175599_af().func_180450_b(stack, x, y);
        mc.func_175599_af().func_175030_a(Nametags.mc.field_71466_p, stack, x, y);
        Nametags.mc.func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179089_o();
        GlStateManager.func_179141_d();
        GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.func_179097_i();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.func_179121_F();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y - 8;
        if (stack.func_77973_b() == Items.field_151153_ao && stack.func_77962_s()) {
            this.renderer.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        NBTTagList enchants = stack.func_77986_q();
        for (int index = 0; index < enchants.func_74745_c(); ++index) {
            short id = enchants.func_150305_b(index).func_74765_d("id");
            short level = enchants.func_150305_b(index).func_74765_d("lvl");
            Enchantment enc = Enchantment.func_185262_c((int)id);
            if (enc == null) continue;
            String encName = enc.func_190936_d() ? (Object)TextFormatting.RED + enc.func_77316_c((int)level).substring(11).substring(0, 1).toLowerCase() : enc.func_77316_c((int)level).substring(0, 1).toLowerCase();
            encName = encName + level;
            this.renderer.drawStringWithShadow(encName, x * 2, enchantmentY, -1);
            enchantmentY -= 8;
        }
        if (DamageUtil.hasDurability(stack)) {
            int percent = DamageUtil.getRoundedDamage(stack);
            String color = percent >= 60 ? "\u00a7a" : (percent >= 25 ? "\u00a7e" : "\u00a7c");
            this.renderer.drawStringWithShadow(color + percent + "%", x * 2, enchantmentY, -1);
        }
    }

    private float getBiggestArmorTag(EntityPlayer player) {
        ItemStack renderOffHand;
        Enchantment enc;
        int index;
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (ItemStack stack : player.field_71071_by.field_70460_b) {
            float encY = 0.0f;
            if (stack != null) {
                NBTTagList enchants = stack.func_77986_q();
                for (index = 0; index < enchants.func_74745_c(); ++index) {
                    short id = enchants.func_150305_b(index).func_74765_d("id");
                    enc = Enchantment.func_185262_c((int)id);
                    if (enc == null) continue;
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (!(encY > enchantmentY)) continue;
            enchantmentY = encY;
        }
        ItemStack renderMainHand = player.func_184614_ca().func_77946_l();
        if (renderMainHand.func_77962_s()) {
            float encY = 0.0f;
            NBTTagList enchants = renderMainHand.func_77986_q();
            for (int index2 = 0; index2 < enchants.func_74745_c(); ++index2) {
                short id = enchants.func_150305_b(index2).func_74765_d("id");
                Enchantment enc2 = Enchantment.func_185262_c((int)id);
                if (enc2 == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        if ((renderOffHand = player.func_184592_cb().func_77946_l()).func_77962_s()) {
            float encY = 0.0f;
            NBTTagList enchants = renderOffHand.func_77986_q();
            for (index = 0; index < enchants.func_74745_c(); ++index) {
                short id = enchants.func_150305_b(index).func_74765_d("id");
                enc = Enchantment.func_185262_c((int)id);
                if (enc == null) continue;
                encY += 8.0f;
                arm = true;
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (float)(arm ? 0 : 20) + enchantmentY;
    }

    private String getDisplayTag(EntityPlayer player) {
        String name = player.func_145748_c_().func_150254_d();
        if (name.contains(mc.func_110432_I().func_111285_a())) {
            name = "You";
        }
        if (!this.health.getValue().booleanValue()) {
            return name;
        }
        float health = EntityUtil.getHealth((Entity)player);
        String color = health > 18.0f ? "\u00a7a" : (health > 16.0f ? "\u00a72" : (health > 12.0f ? "\u00a7e" : (health > 8.0f ? "\u00a76" : (health > 5.0f ? "\u00a7c" : "\u00a74"))));
        String pingStr = "";
        if (this.ping.getValue().booleanValue()) {
            try {
                int responseTime = Objects.requireNonNull(mc.func_147114_u()).func_175102_a(player.func_110124_au()).func_178853_c();
                pingStr = pingStr + responseTime + "ms ";
            }
            catch (Exception responseTime) {
                // empty catch block
            }
        }
        String popStr = " ";
        if (this.totemPops.getValue().booleanValue()) {
            popStr = popStr + esohack.totemPopManager.getTotemPopString(player);
        }
        String idString = "";
        if (this.entityID.getValue().booleanValue()) {
            idString = idString + "ID: " + player.func_145782_y() + " ";
        }
        String gameModeStr = "";
        if (this.gamemode.getValue().booleanValue()) {
            gameModeStr = player.func_184812_l_() ? gameModeStr + "[C] " : (player.func_175149_v() || player.func_82150_aj() ? gameModeStr + "[I] " : gameModeStr + "[S] ");
        }
        name = Math.floor(health) == (double)health ? name + color + " " + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead") : name + color + " " + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        return pingStr + idString + gameModeStr + name + popStr;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;
        if (this.whiter.getValue().booleanValue()) {
            colour = -1;
        }
        if (esohack.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.func_82150_aj()) {
            colour = -1113785;
        } else if (player.func_70093_af() && this.sneak.getValue().booleanValue()) {
            colour = -6481515;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }
}

