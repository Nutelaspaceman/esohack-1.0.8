/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
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
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Skeleton
extends Module {
    private final Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<Integer>("Green", 255, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    private final Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Boolean> colorFriends = this.register(new Setting<Boolean>("Friends", true));
    private final Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    private static Skeleton INSTANCE = new Skeleton();
    private final Map<EntityPlayer, float[][]> rotationList = new HashMap<EntityPlayer, float[][]>();

    public Skeleton() {
        super("Skeleton", "Draws a nice Skeleton.", Module.Category.RENDER, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Skeleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Skeleton();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RenderUtil.GLPre(this.lineWidth.getValue().floatValue());
        for (EntityPlayer player : Skeleton.mc.field_71441_e.field_73010_i) {
            if (player == null || player == mc.func_175606_aa() || !player.func_70089_S() || player.func_70608_bn() || player.func_82150_aj() && !this.invisibles.getValue().booleanValue() || this.rotationList.get((Object)player) == null || !(Skeleton.mc.field_71439_g.func_70068_e((Entity)player) < 2500.0)) continue;
            this.renderSkeleton(player, this.rotationList.get((Object)player), EntityUtil.getColor((Entity)player, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue()));
        }
        RenderUtil.GlPost();
    }

    public void onRenderModel(RenderEntityModelEvent event) {
        if (event.getStage() == 0 && event.entity instanceof EntityPlayer && event.modelBase instanceof ModelBiped) {
            ModelBiped biped = (ModelBiped)event.modelBase;
            float[][] rotations = RenderUtil.getBipedRotations(biped);
            EntityPlayer player = (EntityPlayer)event.entity;
            this.rotationList.put(player, rotations);
        }
    }

    private void renderSkeleton(EntityPlayer player, float[][] rotations, Color color) {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179094_E();
        GlStateManager.func_179131_c((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)player, mc.func_184121_ak());
        double pX = interp.field_72450_a;
        double pY = interp.field_72448_b;
        double pZ = interp.field_72449_c;
        GlStateManager.func_179137_b((double)pX, (double)pY, (double)pZ);
        GlStateManager.func_179114_b((float)(-player.field_70761_aq), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)(player.func_70093_af() ? -0.235 : 0.0));
        float sneak = player.func_70093_af() ? 0.6f : 0.75f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)-0.125, (double)sneak, (double)0.0);
        if (rotations[3][0] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[3][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[3][1] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[3][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[3][2] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[3][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)(-sneak), (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.125, (double)sneak, (double)0.0);
        if (rotations[4][0] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[4][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[4][1] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[4][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[4][2] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[4][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)(-sneak), (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179137_b((double)0.0, (double)0.0, (double)(player.func_70093_af() ? 0.25 : 0.0));
        GlStateManager.func_179094_E();
        double sneakOffset = 0.0;
        if (player.func_70093_af()) {
            sneakOffset = -0.05;
        }
        GlStateManager.func_179137_b((double)0.0, (double)sneakOffset, (double)(player.func_70093_af() ? -0.01725 : 0.0));
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)-0.375, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[1][0] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[1][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[1][1] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[1][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[1][2] != 0.0f) {
            GlStateManager.func_179114_b((float)(-rotations[1][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.375, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[2][0] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[2][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        if (rotations[2][1] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[2][1] * 57.295776f), (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (rotations[2][2] != 0.0f) {
            GlStateManager.func_179114_b((float)(-rotations[2][2] * 57.295776f), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)-0.5, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.0, (double)((double)sneak + 0.55), (double)0.0);
        if (rotations[0][0] != 0.0f) {
            GlStateManager.func_179114_b((float)(rotations[0][0] * 57.295776f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)0.3, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179121_F();
        GlStateManager.func_179114_b((float)(player.func_70093_af() ? 25.0f : 0.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        if (player.func_70093_af()) {
            sneakOffset = -0.16175;
        }
        GlStateManager.func_179137_b((double)0.0, (double)sneakOffset, (double)(player.func_70093_af() ? -0.48025 : 0.0));
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.0, (double)sneak, (double)0.0);
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)-0.125, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.125, (double)0.0, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.0, (double)sneak, (double)0.0);
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.0, (double)0.55, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)0.0, (double)((double)sneak + 0.55), (double)0.0);
        GlStateManager.func_187447_r((int)3);
        GL11.glVertex3d((double)-0.375, (double)0.0, (double)0.0);
        GL11.glVertex3d((double)0.375, (double)0.0, (double)0.0);
        GlStateManager.func_187437_J();
        GlStateManager.func_179121_F();
        GlStateManager.func_179121_F();
    }
}

