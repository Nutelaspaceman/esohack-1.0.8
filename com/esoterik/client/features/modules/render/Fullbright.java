/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.network.play.server.SPacketEntityEffect
 *  net.minecraft.potion.PotionEffect
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.render;

import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fullbright
extends Module {
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.GAMMA));
    public Setting<Boolean> effects = this.register(new Setting<Boolean>("Effects", false));
    private float previousSetting = 1.0f;

    public Fullbright() {
        super("Fullbright", "Makes your game brighter.", Module.Category.RENDER, true, false, false);
    }

    @Override
    public void onEnable() {
        this.previousSetting = Fullbright.mc.field_71474_y.field_74333_Y;
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue() == Mode.GAMMA) {
            Fullbright.mc.field_71474_y.field_74333_Y = 1000.0f;
        }
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.field_71439_g.func_70690_d(new PotionEffect(MobEffects.field_76439_r, 5210));
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.POTION) {
            Fullbright.mc.field_71439_g.func_184589_d(MobEffects.field_76439_r);
        }
        Fullbright.mc.field_71474_y.field_74333_Y = this.previousSetting;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() == 0 && event.getPacket() instanceof SPacketEntityEffect && this.effects.getValue().booleanValue()) {
            SPacketEntityEffect packet = (SPacketEntityEffect)event.getPacket();
            if (Fullbright.mc.field_71439_g != null && packet.func_149426_d() == Fullbright.mc.field_71439_g.func_145782_y() && (packet.func_149427_e() == 9 || packet.func_149427_e() == 15)) {
                event.setCanceled(true);
            }
        }
    }

    public static enum Mode {
        GAMMA,
        POTION;

    }
}

