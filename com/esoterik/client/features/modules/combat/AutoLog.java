/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.network.play.server.SPacketDisconnect
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoLog
extends Module {
    public Setting<Boolean> fakeKick = this.register(new Setting<Boolean>("FakeKick", false));
    public Setting<Boolean> disable = this.register(new Setting<Boolean>("DisableOnLogout", false));
    public Setting<Float> health = this.register(new Setting<Float>("Health", Float.valueOf(16.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> beds = this.register(new Setting<Boolean>("Beds", false));
    public Setting<Float> bedrange = this.register(new Setting<Object>("BedRange", Float.valueOf(4.0f), Float.valueOf(0.1f), Float.valueOf(10.0f), v -> this.beds.getValue()));
    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("LethalCrystal", false));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", false));
    float playerhealth = -1.0f;

    public AutoLog() {
        super("AutoLog", "Automatically logs you out if certain conditions are met.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (AutoLog.fullNullCheck()) {
            return;
        }
        this.playerhealth = AutoLog.mc.field_71439_g.func_110143_aJ() + AutoLog.mc.field_71439_g.func_110139_bj();
        if (this.playerhealth <= this.health.getValue().floatValue()) {
            this.logout();
            return;
        }
        for (Entity e : AutoLog.mc.field_71441_e.field_72996_f) {
            if (e instanceof EntityPlayer && this.players.getValue().booleanValue() && !e.equals((Object)AutoLog.mc.field_71439_g)) {
                if (esohack.friendManager.isFriend((EntityPlayer)e)) continue;
                this.logout();
                continue;
            }
            if (!(e instanceof EntityEnderCrystal) || !this.crystals.getValue().booleanValue() || !(this.playerhealth - DamageUtil.calculateDamage(e, (Entity)AutoLog.mc.field_71439_g) < this.health.getValue().floatValue())) continue;
            this.logout();
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        SPacketBlockChange packet;
        if (AutoLog.fullNullCheck()) {
            return;
        }
        this.playerhealth = AutoLog.mc.field_71439_g.func_110143_aJ() + AutoLog.mc.field_71439_g.func_110139_bj();
        if (event.getPacket() instanceof SPacketBlockChange && this.beds.getValue().booleanValue() && (packet = (SPacketBlockChange)event.getPacket()).func_180728_a().func_177230_c() == Blocks.field_150324_C && AutoLog.mc.field_71439_g.func_174818_b(packet.func_179827_b()) <= MathUtil.square(this.bedrange.getValue().floatValue())) {
            this.logout();
        }
    }

    private void logout() {
        if (mc.func_71356_B() || AutoLog.fullNullCheck()) {
            return;
        }
        if (this.fakeKick.getValue().booleanValue()) {
            mc.func_147114_u().func_147253_a(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
        } else {
            mc.func_147114_u().func_147253_a(new SPacketDisconnect((ITextComponent)new TextComponentString("Internal Exception: java.lang.NullPointerException")));
        }
        if (this.disable.getValue().booleanValue()) {
            this.toggle();
        }
    }
}

