/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.Timer;
import java.util.Objects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Criticals
extends Module {
    private Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.PACKET));
    public Setting<Boolean> noDesync = this.register(new Setting<Boolean>("NoDesync", true));
    private Setting<Integer> packets = this.register(new Setting<Object>("Packets", 2, 1, 4, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    private Setting<Integer> desyncDelay = this.register(new Setting<Object>("DesyncDelay", 10, 0, 500, v -> this.mode.getValue() == Mode.PACKET, "Amount of packets you want to send."));
    public Setting<Boolean> cancelFirst = this.register(new Setting<Boolean>("CancelFirst32k", true));
    public Setting<Integer> delay32k = this.register(new Setting<Object>("32kDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> this.cancelFirst.getValue()));
    private Timer timer = new Timer();
    private Timer timer32k = new Timer();
    private boolean firstCanceled = false;
    private boolean resetTimer = false;

    public Criticals() {
        super("Criticals", "Scores criticals for you", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (!this.cancelFirst.getValue().booleanValue()) {
            this.firstCanceled = false;
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK) {
            if (this.firstCanceled) {
                this.timer32k.reset();
                this.resetTimer = true;
                this.timer.setMs(this.desyncDelay.getValue() + 1);
                this.firstCanceled = false;
                return;
            }
            if (this.resetTimer && !this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                return;
            }
            if (this.resetTimer && this.timer32k.passedMs(this.delay32k.getValue().intValue())) {
                this.resetTimer = false;
            }
            if (!this.timer.passedMs(this.desyncDelay.getValue().intValue())) {
                return;
            }
            if (!(!Criticals.mc.field_71439_g.field_70122_E || Criticals.mc.field_71474_y.field_74314_A.func_151470_d() || !(packet.func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase) && this.noDesync.getValue().booleanValue() || Criticals.mc.field_71439_g.func_70090_H() || Criticals.mc.field_71439_g.func_180799_ab())) {
                if (this.mode.getValue() == Mode.PACKET) {
                    switch (this.packets.getValue()) {
                        case 1: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + (double)0.1f, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 2: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0625101, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 1.1E-5, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 3: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0625101, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0125, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            break;
                        }
                        case 4: {
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.1625, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 4.0E-6, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 1.0E-6, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                            Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer());
                            Criticals.mc.field_71439_g.func_71009_b(Objects.requireNonNull(packet.func_149564_a((World)Criticals.mc.field_71441_e)));
                            break;
                        }
                    }
                } else {
                    Criticals.mc.field_71439_g.func_70664_aZ();
                }
                this.timer.reset();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        JUMP,
        PACKET;

    }
}

