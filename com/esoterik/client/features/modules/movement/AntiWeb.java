/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockWeb
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 */
package com.esoterik.client.features.modules.movement;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.BlockCollisionBoundingBoxEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.movement.Step;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class AntiWeb
extends Module {
    public Setting<Boolean> disableBB = this.register(new Setting<Boolean>("AddBB", true));
    public Setting<Double> bbOffset = this.register(new Setting<Double>("BoxOffset", 0.04, -1.0, 1.0));
    public Setting<Boolean> onGround = this.register(new Setting<Boolean>("OnGround", true));
    public Setting<Double> motionX = this.register(new Setting<Double>("MotionX", 0.8, -1.0, 5.0));
    public Setting<Double> motionY = this.register(new Setting<Double>("MotionY", -0.05, 0.0, 10.0));

    public AntiWeb() {
        super("AntiWeb", "Modifies movement in webs", Module.Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void bbEvent(BlockCollisionBoundingBoxEvent event) {
        if (AntiWeb.nullCheck()) {
            return;
        }
        if (AntiWeb.mc.field_71441_e.func_180495_p(event.getPos()).func_177230_c() instanceof BlockWeb && this.disableBB.getValue().booleanValue()) {
            event.setCanceled(true);
            event.setBoundingBox(Block.field_185505_j.func_191195_a(0.0, this.bbOffset.getValue().doubleValue(), 0.0));
        }
    }

    @Override
    public void onUpdate() {
        if (AntiWeb.mc.field_71439_g.field_70134_J && !esohack.moduleManager.isModuleEnabled(Step.class)) {
            if (Keyboard.isKeyDown((int)AntiWeb.mc.field_71474_y.field_74311_E.field_74512_d)) {
                AntiWeb.mc.field_71439_g.field_70134_J = true;
                AntiWeb.mc.field_71439_g.field_70181_x *= this.motionY.getValue().doubleValue();
            } else if (this.onGround.getValue().booleanValue()) {
                AntiWeb.mc.field_71439_g.field_70122_E = false;
            }
            if (Keyboard.isKeyDown((int)AntiWeb.mc.field_71474_y.field_74351_w.field_74512_d) || Keyboard.isKeyDown((int)AntiWeb.mc.field_71474_y.field_74368_y.field_74512_d) || Keyboard.isKeyDown((int)AntiWeb.mc.field_71474_y.field_74370_x.field_74512_d) || Keyboard.isKeyDown((int)AntiWeb.mc.field_71474_y.field_74366_z.field_74512_d)) {
                AntiWeb.mc.field_71439_g.field_70134_J = false;
                AntiWeb.mc.field_71439_g.field_70159_w *= this.motionX.getValue().doubleValue();
                AntiWeb.mc.field_71439_g.field_70179_y *= this.motionX.getValue().doubleValue();
            }
        }
    }
}

