/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package com.esoterik.client.features.modules.render;

import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.event.events.RenderEntityModelEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.RenderUtil;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP
extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OUTLINE));
    private final Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    private final Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    private final Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    private final Setting<Boolean> items = this.register(new Setting<Boolean>("Items", false));
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Integer>("BoxAlpha", 120, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(2.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Boolean> colorFriends = this.register(new Setting<Boolean>("Friends", true));
    private final Setting<Boolean> self = this.register(new Setting<Boolean>("Self", true));
    private final Setting<Boolean> onTop = this.register(new Setting<Boolean>("onTop", true));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private static ESP INSTANCE = new ESP();

    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.items.getValue().booleanValue()) {
            int i = 0;
            for (Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (!(entity instanceof EntityItem) || !(ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0)) continue;
                Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.func_184121_ak());
                AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179097_i();
                GlStateManager.func_179120_a((int)770, (int)771, (int)0, (int)1);
                GlStateManager.func_179090_x();
                GlStateManager.func_179132_a((boolean)false);
                GL11.glEnable((int)2848);
                GL11.glHint((int)3154, (int)4354);
                GL11.glLineWidth((float)1.0f);
                RenderGlobal.func_189696_b((AxisAlignedBB)bb, (float)((float)this.red.getValue().intValue() / 255.0f), (float)((float)this.green.getValue().intValue() / 255.0f), (float)((float)this.blue.getValue().intValue() / 255.0f), (float)((float)this.boxAlpha.getValue().intValue() / 255.0f));
                GL11.glDisable((int)2848);
                GlStateManager.func_179132_a((boolean)true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() != 0 || event.entity == null || event.entity.func_82150_aj() && this.invisibles.getValue() == false || this.self.getValue() == false && event.entity.equals((Object)ESP.mc.field_71439_g) || this.players.getValue() == false && event.entity instanceof EntityPlayer || this.animals.getValue() == false && EntityUtil.isPassive(event.entity) || !this.mobs.getValue().booleanValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof EntityPlayer)) {
            return;
        }
        Color color = EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        boolean fancyGraphics = ESP.mc.field_71474_y.field_74347_j;
        ESP.mc.field_71474_y.field_74347_j = false;
        float gamma = ESP.mc.field_71474_y.field_74333_Y;
        ESP.mc.field_71474_y.field_74333_Y = 10000.0f;
        if (this.onTop.getValue().booleanValue()) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue().floatValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderTwo();
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            RenderUtil.renderFive();
        } else {
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode((int)1032, (int)6913);
            } else {
                GL11.glPolygonMode((int)1028, (int)6913);
            }
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GlStateManager.func_179112_b((int)770, (int)771);
            GlStateManager.func_179131_c((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getAlpha());
            GlStateManager.func_187441_d((float)this.lineWidth.getValue().floatValue());
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!this.onTop.getValue().booleanValue()) {
            event.modelBase.func_78088_a(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
        }
        try {
            ESP.mc.field_71474_y.field_74347_j = fancyGraphics;
            ESP.mc.field_71474_y.field_74333_Y = gamma;
        }
        catch (Exception exception) {
            // empty catch block
        }
        event.setCanceled(true);
    }

    public static enum Mode {
        WIREFRAME,
        OUTLINE;

    }
}

