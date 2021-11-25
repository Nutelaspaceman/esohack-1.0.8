/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.play.client.CPacketInput
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.player;

import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.PushEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.MathUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam
extends Module {
    public Setting<Double> speed = this.register(new Setting<Double>("Speed", 0.5, 0.1, 5.0));
    public Setting<Boolean> view = this.register(new Setting<Boolean>("3D", false));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    public Setting<Boolean> disable = this.register(new Setting<Boolean>("Logout/Off", true));
    private static Freecam INSTANCE = new Freecam();
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    public Freecam() {
        super("Freecam", "Look around freely.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Freecam getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Freecam();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (!Freecam.fullNullCheck()) {
            this.oldBoundingBox = Freecam.mc.field_71439_g.func_174813_aQ();
            Freecam.mc.field_71439_g.func_174826_a(new AxisAlignedBB(Freecam.mc.field_71439_g.field_70165_t, Freecam.mc.field_71439_g.field_70163_u, Freecam.mc.field_71439_g.field_70161_v, Freecam.mc.field_71439_g.field_70165_t, Freecam.mc.field_71439_g.field_70163_u, Freecam.mc.field_71439_g.field_70161_v));
            if (Freecam.mc.field_71439_g.func_184187_bx() != null) {
                this.riding = Freecam.mc.field_71439_g.func_184187_bx();
                Freecam.mc.field_71439_g.func_184210_p();
            }
            this.entity = new EntityOtherPlayerMP((World)Freecam.mc.field_71441_e, Freecam.mc.field_71449_j.func_148256_e());
            this.entity.func_82149_j((Entity)Freecam.mc.field_71439_g);
            this.entity.field_70177_z = Freecam.mc.field_71439_g.field_70177_z;
            this.entity.field_70759_as = Freecam.mc.field_71439_g.field_70759_as;
            this.entity.field_71071_by.func_70455_b(Freecam.mc.field_71439_g.field_71071_by);
            Freecam.mc.field_71441_e.func_73027_a(69420, (Entity)this.entity);
            this.position = Freecam.mc.field_71439_g.func_174791_d();
            this.yaw = Freecam.mc.field_71439_g.field_70177_z;
            this.pitch = Freecam.mc.field_71439_g.field_70125_A;
            Freecam.mc.field_71439_g.field_70145_X = true;
        }
    }

    @Override
    public void onDisable() {
        if (!Freecam.fullNullCheck()) {
            Freecam.mc.field_71439_g.func_174826_a(this.oldBoundingBox);
            if (this.riding != null) {
                Freecam.mc.field_71439_g.func_184205_a(this.riding, true);
            }
            if (this.entity != null) {
                Freecam.mc.field_71441_e.func_72900_e((Entity)this.entity);
            }
            if (this.position != null) {
                Freecam.mc.field_71439_g.func_70107_b(this.position.field_72450_a, this.position.field_72448_b, this.position.field_72449_c);
            }
            Freecam.mc.field_71439_g.field_70177_z = this.yaw;
            Freecam.mc.field_71439_g.field_70125_A = this.pitch;
            Freecam.mc.field_71439_g.field_70145_X = false;
        }
    }

    @Override
    public void onUpdate() {
        Freecam.mc.field_71439_g.field_70145_X = true;
        Freecam.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        Freecam.mc.field_71439_g.field_70747_aH = this.speed.getValue().floatValue();
        double[] dir = MathUtil.directionSpeed(this.speed.getValue());
        if (Freecam.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || Freecam.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) {
            Freecam.mc.field_71439_g.field_70159_w = dir[0];
            Freecam.mc.field_71439_g.field_70179_y = dir[1];
        } else {
            Freecam.mc.field_71439_g.field_70159_w = 0.0;
            Freecam.mc.field_71439_g.field_70179_y = 0.0;
        }
        Freecam.mc.field_71439_g.func_70031_b(false);
        if (this.view.getValue().booleanValue() && !Freecam.mc.field_71474_y.field_74311_E.func_151470_d() && !Freecam.mc.field_71474_y.field_74314_A.func_151470_d()) {
            Freecam.mc.field_71439_g.field_70181_x = this.speed.getValue() * -MathUtil.degToRad(Freecam.mc.field_71439_g.field_70125_A) * (double)Freecam.mc.field_71439_g.field_71158_b.field_192832_b;
        }
        if (Freecam.mc.field_71474_y.field_74314_A.func_151470_d()) {
            Freecam.mc.field_71439_g.field_70181_x += this.speed.getValue().doubleValue();
        }
        if (Freecam.mc.field_71474_y.field_74311_E.func_151470_d()) {
            Freecam.mc.field_71439_g.field_70181_x -= this.speed.getValue().doubleValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.disable.getValue().booleanValue()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }
}

