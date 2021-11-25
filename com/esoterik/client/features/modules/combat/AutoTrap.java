/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.mixin.accessor.ICPacketPlayer;
import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.InventoryUtil;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoTrap
extends Module {
    boolean north;
    boolean south;
    boolean east;
    boolean west;
    int delay = 0;
    int delayToggle = 0;
    int startingHand;
    int obsidianSlot = 0;
    public Setting<Float> placeRange = this.register(new Setting<Float>("PlaceRange", Float.valueOf(5.5f), Float.valueOf(0.0f), Float.valueOf(8.0f)));
    public Setting<Integer> placeDelay = this.register(new Setting<Integer>("PlaceDelay", 2, 0, 20));
    public Setting<Integer> toggleTicks = this.register(new Setting<Integer>("ToggleTicks", 2, 0, 20));
    public Setting<Boolean> noGhostBlocks = this.register(new Setting<Boolean>("AntiDesync", true));
    public Setting<Boolean> directionalTrap = this.register(new Setting<Boolean>("DirectionalTrap", true));
    public Setting<HeadFixModes> headFixMode = this.register(new Setting<HeadFixModes>("HeadFixMode", HeadFixModes.AUTO));
    public static boolean isPlacing = false;
    boolean packetsBeingSent;
    private EntityPlayer closestTarget;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    public AutoTrap() {
        super("AutoTrap", "Traps other players", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (this.closestTarget == null) {
            return;
        }
        BlockPos posc = new BlockPos(this.closestTarget.func_174791_d());
        ++this.delay;
        ++this.delayToggle;
        Vec3d[] placeDirection = null;
        if (this.directionalTrap.getValue().booleanValue()) {
            if (BlockUtil.findBlockFacingLocationPlayer(posc) == 1) {
                placeDirection = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0), new Vec3d(1.0, 1.0, 0.0)};
            }
            if (BlockUtil.findBlockFacingLocationPlayer(posc) == 2) {
                placeDirection = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0), new Vec3d(-1.0, 1.0, 0.0)};
            }
            if (BlockUtil.findBlockFacingLocationPlayer(posc) == 3) {
                placeDirection = new Vec3d[]{new Vec3d(0.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 2.0, 0.0), new Vec3d(0.0, 1.0, 1.0)};
            }
            if (BlockUtil.findBlockFacingLocationPlayer(posc) == 4) {
                placeDirection = new Vec3d[]{new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0), new Vec3d(0.0, 1.0, -1.0)};
            }
        } else {
            placeDirection = new Vec3d[]{new Vec3d(1.0, -1.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 0.0)};
        }
        if (placeDirection == null) {
            return;
        }
        if (posc.func_177954_c(AutoTrap.mc.field_71439_g.field_70165_t, AutoTrap.mc.field_71439_g.field_70163_u, AutoTrap.mc.field_71439_g.field_70161_v) > (double)this.placeRange.getValue().floatValue()) {
            return;
        }
        for (void var6_6 : placeDirection) {
            BlockPos pos;
            if (this.delay < this.placeDelay.getValue() || !AutoTrap.mc.field_71441_e.func_190527_a(Blocks.field_150343_Z, pos = new BlockPos(this.closestTarget.func_174791_d().func_178787_e((Vec3d)var6_6)), false, EnumFacing.UP, (Entity)AutoTrap.mc.field_71439_g)) continue;
            isPlacing = true;
            AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = this.obsidianSlot;
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            if (this.headFixMode.getValue().equals((Object)HeadFixModes.ENABLED) && (var6_6.equals((Object)new Vec3d(-1.0, 2.0, 0.0)) || var6_6.equals((Object)new Vec3d(1.0, 2.0, 0.0)) || var6_6.equals((Object)new Vec3d(0.0, 2.0, 1.0)) || var6_6.equals((Object)new Vec3d(0.0, 2.0, -1.0)))) {
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoTrap.mc.field_71439_g.field_70165_t, AutoTrap.mc.field_71439_g.field_70163_u + 0.41999998688698, AutoTrap.mc.field_71439_g.field_70161_v, true));
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoTrap.mc.field_71439_g.field_70165_t, AutoTrap.mc.field_71439_g.field_70163_u + 0.7531999805211997, AutoTrap.mc.field_71439_g.field_70161_v, true));
            }
            if (this.headFixMode.getValue().equals((Object)HeadFixModes.AUTO) && AutoTrap.mc.field_71439_g.field_70163_u <= this.closestTarget.field_70163_u - 0.6 && (var6_6.equals((Object)new Vec3d(-1.0, 2.0, 0.0)) || var6_6.equals((Object)new Vec3d(1.0, 2.0, 0.0)) || var6_6.equals((Object)new Vec3d(0.0, 2.0, 1.0)) || var6_6.equals((Object)new Vec3d(0.0, 2.0, -1.0)))) {
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoTrap.mc.field_71439_g.field_70165_t, AutoTrap.mc.field_71439_g.field_70163_u + 0.41999998688698, AutoTrap.mc.field_71439_g.field_70161_v, true));
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(AutoTrap.mc.field_71439_g.field_70165_t, AutoTrap.mc.field_71439_g.field_70163_u + 0.7531999805211997, AutoTrap.mc.field_71439_g.field_70161_v, true));
            }
            BlockUtil.placeBlockScaffold(pos);
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = this.startingHand;
            if (this.noGhostBlocks.getValue().booleanValue()) {
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.SOUTH));
                AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.SOUTH));
            }
            this.delay = 0;
            isPlacing = false;
        }
        if (this.delayToggle >= this.toggleTicks.getValue()) {
            this.toggle();
        }
        if (!this.packetsBeingSent) {
            Command.sendMessage("Sending Packet");
            AutoTrap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer(AutoTrap.mc.field_71439_g.field_70122_E));
            this.packetsBeingSent = true;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            if (isSpoofingAngles) {
                ((ICPacketPlayer)event.getPacket()).setYaw((float)yaw);
                ((ICPacketPlayer)event.getPacket()).setPitch((float)pitch);
            }
        } else if (isSpoofingAngles) {
            this.packetsBeingSent = false;
        }
    }

    @Override
    public void onEnable() {
        this.findClosestTarget();
        this.startingHand = AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c;
        this.delayToggle = 0;
        this.delay = 0;
        this.packetsBeingSent = true;
        int obsidianHand = InventoryUtil.findHotbarBlock(Blocks.field_150343_Z);
        if (obsidianHand == -1) {
            Command.sendMessage("No Obsidian Toggling!");
        } else {
            this.obsidianSlot = obsidianHand;
        }
        int var24 = MathHelper.func_76128_c((double)((double)(Minecraft.func_71410_x().field_71439_g.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3;
        float yaw = Minecraft.func_71410_x().field_71439_g.field_70177_z;
        if (var24 == 2) {
            // empty if block
        }
        if (var24 == 1) {
            // empty if block
        }
        if (var24 == 3) {
            // empty if block
        }
        if (var24 == 0) {
            // empty if block
        }
    }

    @Override
    public void onDisable() {
        AutoTrap.mc.field_71439_g.field_71071_by.field_70461_c = this.startingHand;
        isPlacing = false;
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = AutoTrap.calculateLookAt(px, py, pz, me);
        AutoTrap.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = AutoTrap.mc.field_71439_g.field_70177_z;
            pitch = AutoTrap.mc.field_71439_g.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.field_70165_t - px;
        double diry = me.field_70163_u - py;
        double dirz = me.field_70161_v - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    private void findClosestTarget() {
        List playerList = AutoTrap.mc.field_71441_e.field_73010_i;
        this.closestTarget = null;
        for (EntityPlayer target : playerList) {
            if (target == AutoTrap.mc.field_71439_g || esohack.friendManager.isFriend(target.func_70005_c_()) || target.func_110143_aJ() <= 0.0f) continue;
            if (this.closestTarget == null) {
                this.closestTarget = target;
                continue;
            }
            if (AutoTrap.mc.field_71439_g.func_70032_d((Entity)target) >= AutoTrap.mc.field_71439_g.func_70032_d((Entity)this.closestTarget)) continue;
            this.closestTarget = target;
        }
    }

    public static enum HeadFixModes {
        DISABLED,
        ENABLED,
        AUTO;

    }
}

