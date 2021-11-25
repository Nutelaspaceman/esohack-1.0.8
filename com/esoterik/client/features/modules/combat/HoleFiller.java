/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.InventoryUtil;
import com.esoterik.client.util.RenderUtil;
import com.esoterik.client.util.TestUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleFiller
extends Module {
    private static BlockPos PlayerPos;
    private Setting<Double> range = this.register(new Setting<Integer>("Range", (Integer)4.5, (Integer)0.1, 6));
    private Setting<Boolean> smart = this.register(new Setting<Boolean>("Smart", false));
    private Setting<Integer> smartRange = this.register(new Setting<Integer>("Smart Range", 4));
    private BlockPos render;
    private Entity renderEnt;
    private EntityPlayer closestTarget;
    private int newSlot;
    double d;
    private static boolean isSpoofingAngles;
    private static float yaw;
    private static float pitch;
    private static HoleFiller INSTANCE;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
        this.setInstance();
    }

    public static HoleFiller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleFiller();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        Object packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).field_149476_e = yaw;
            ((CPacketPlayer)packet).field_149473_f = pitch;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onUpdate() {
        if (HoleFiller.mc.field_71441_e == null) {
            return;
        }
        if (this.smart.getValue().booleanValue()) {
            this.findClosestTarget();
        }
        List<BlockPos> blocks = this.findCrystalBlocks();
        BlockPos q = null;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            return;
        }
        int originalSlot = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
        for (BlockPos blockPos : blocks) {
            if (!HoleFiller.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(blockPos)).isEmpty()) continue;
            if (this.smart.getValue().booleanValue() && this.isInRange(blockPos)) {
                q = blockPos;
                continue;
            }
            q = blockPos;
        }
        this.render = q;
        if (q != null && HoleFiller.mc.field_71439_g.field_70122_E) {
            HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot == -1 ? eChestSot : obbySlot;
            HoleFiller.mc.field_71442_b.func_78765_e();
            this.lookAtPacket((double)q.func_177958_n() + 0.5, (double)q.func_177956_o() - 0.5, (double)q.func_177952_p() + 0.5, (EntityPlayer)HoleFiller.mc.field_71439_g);
            TestUtil.placeBlock(this.render);
            if (HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c != originalSlot) {
                HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
                HoleFiller.mc.field_71442_b.func_78765_e();
            }
            HoleFiller.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
            HoleFiller.resetRotation();
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render != null) {
            RenderUtil.drawBoxESP(this.render, Colors.INSTANCE.getCurrentColor(), false, Colors.INSTANCE.getCurrentColor(), 2.0f, true, true, 150, true, -0.9, false, false, false, false, 255);
        }
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        HoleFiller.setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean IsHole(BlockPos blockPos) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 0, 0);
        BlockPos boost3 = blockPos.func_177982_a(0, 0, -1);
        BlockPos boost4 = blockPos.func_177982_a(1, 0, 0);
        BlockPos boost5 = blockPos.func_177982_a(-1, 0, 0);
        BlockPos boost6 = blockPos.func_177982_a(0, 0, 1);
        BlockPos boost7 = blockPos.func_177982_a(0, 2, 0);
        BlockPos boost8 = blockPos.func_177963_a(0.5, 0.5, 0.5);
        BlockPos boost9 = blockPos.func_177982_a(0, -1, 0);
        return !(HoleFiller.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || HoleFiller.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a || HoleFiller.mc.field_71441_e.func_180495_p(boost7).func_177230_c() != Blocks.field_150350_a || HoleFiller.mc.field_71441_e.func_180495_p(boost3).func_177230_c() != Blocks.field_150343_Z && HoleFiller.mc.field_71441_e.func_180495_p(boost3).func_177230_c() != Blocks.field_150357_h || HoleFiller.mc.field_71441_e.func_180495_p(boost4).func_177230_c() != Blocks.field_150343_Z && HoleFiller.mc.field_71441_e.func_180495_p(boost4).func_177230_c() != Blocks.field_150357_h || HoleFiller.mc.field_71441_e.func_180495_p(boost5).func_177230_c() != Blocks.field_150343_Z && HoleFiller.mc.field_71441_e.func_180495_p(boost5).func_177230_c() != Blocks.field_150357_h || HoleFiller.mc.field_71441_e.func_180495_p(boost6).func_177230_c() != Blocks.field_150343_Z && HoleFiller.mc.field_71441_e.func_180495_p(boost6).func_177230_c() != Blocks.field_150357_h || HoleFiller.mc.field_71441_e.func_180495_p(boost8).func_177230_c() != Blocks.field_150350_a || HoleFiller.mc.field_71441_e.func_180495_p(boost9).func_177230_c() != Blocks.field_150343_Z && HoleFiller.mc.field_71441_e.func_180495_p(boost9).func_177230_c() != Blocks.field_150357_h);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HoleFiller.mc.field_71439_g.field_70165_t), Math.floor(HoleFiller.mc.field_71439_g.field_70163_u), Math.floor(HoleFiller.mc.field_71439_g.field_70161_v));
    }

    public BlockPos getClosestTargetPos() {
        if (this.closestTarget != null) {
            return new BlockPos(Math.floor(this.closestTarget.field_70165_t), Math.floor(this.closestTarget.field_70163_u), Math.floor(this.closestTarget.field_70161_v));
        }
        return null;
    }

    private void findClosestTarget() {
        List playerList = HoleFiller.mc.field_71441_e.field_73010_i;
        this.closestTarget = null;
        for (EntityPlayer target : playerList) {
            if (target == HoleFiller.mc.field_71439_g || esohack.friendManager.isFriend(target.func_70005_c_()) || !EntityUtil.isLiving((Entity)target) || target.func_110143_aJ() <= 0.0f) continue;
            if (this.closestTarget == null) {
                this.closestTarget = target;
                continue;
            }
            if (!(HoleFiller.mc.field_71439_g.func_70032_d((Entity)target) < HoleFiller.mc.field_71439_g.func_70032_d((Entity)this.closestTarget))) continue;
            this.closestTarget = target;
        }
    }

    private boolean isInRange(BlockPos blockPos) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)this.getSphere(HoleFiller.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        return positions.contains((Object)blockPos);
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList positions = NonNullList.func_191196_a();
        if (this.smart.getValue().booleanValue() && this.closestTarget != null) {
            positions.addAll((Collection)this.getSphere(this.getClosestTargetPos(), this.smartRange.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        } else if (!this.smart.getValue().booleanValue()) {
            positions.addAll((Collection)this.getSphere(HoleFiller.getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        }
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.func_177958_n();
        int cy = loc.func_177956_o();
        int cz = loc.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = HoleFiller.mc.field_71439_g.field_70177_z;
            pitch = HoleFiller.mc.field_71439_g.field_70125_A;
            isSpoofingAngles = false;
        }
    }

    @Override
    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        HoleFiller.resetRotation();
        super.onDisable();
    }

    static {
        INSTANCE = new HoleFiller();
    }
}

