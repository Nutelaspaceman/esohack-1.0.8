/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.esoterik.client.features.modules.player;

import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.InventoryUtil;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Burrow
extends Module {
    private final Setting<Boolean> auto = this.register(new Setting<Boolean>("Auto", false));
    private final Setting<Double> range = this.register(new Setting<Object>("Range", Double.valueOf(3.0), Double.valueOf(1.0), Double.valueOf(10.0), v -> this.auto.getValue()));
    private final Setting<Double> offset = this.register(new Setting<Double>("Offset", 7.0, -20.0, 20.0));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    private final Setting<Boolean> silent = this.register(new Setting<Boolean>("Silent", false));
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OBBY));
    private Set<EntityPlayer> entities = new HashSet<EntityPlayer>();
    private BlockPos originalPos;
    private int oldSlot = -1;
    Block returnBlock = null;

    public Burrow() {
        super("Burrow", "TPs you into a block", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.entities.clear();
        this.originalPos = new BlockPos(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u, Burrow.mc.field_71439_g.field_70161_v);
        switch (this.mode.getValue()) {
            case OBBY: {
                this.returnBlock = Blocks.field_150343_Z;
                break;
            }
            case ECHEST: {
                this.returnBlock = Blocks.field_150477_bB;
                break;
            }
            case CHEST: {
                this.returnBlock = Blocks.field_150486_ae;
            }
        }
        if (Burrow.mc.field_71441_e.func_180495_p(new BlockPos(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u, Burrow.mc.field_71439_g.field_70161_v)).func_177230_c().equals((Object)this.returnBlock) || this.intersectsWithEntity(this.originalPos)) {
            this.toggle();
            return;
        }
        this.oldSlot = Burrow.mc.field_71439_g.field_71071_by.field_70461_c;
    }

    @Override
    public void onUpdate() {
        if (Burrow.fullNullCheck()) {
            return;
        }
        EntityPlayer target = EntityUtil.getClosestEnemy(this.range.getValue());
        if (target == null) {
            this.entities.clear();
        }
        if (this.auto.getValue().booleanValue() && target != null) {
            if (!this.entities.contains((Object)target)) {
                this.originalPos = new BlockPos(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u, Burrow.mc.field_71439_g.field_70161_v);
                this.oldSlot = Burrow.mc.field_71439_g.field_71071_by.field_70461_c;
                this.burrow();
                this.entities.add(target);
            }
        } else if (!this.auto.getValue().booleanValue()) {
            this.burrow();
            this.toggle();
        }
    }

    private void burrow() {
        switch (this.mode.getValue()) {
            case OBBY: {
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
                    Command.sendMessage("Can't find obby in hotbar!");
                    this.toggle();
                    break;
                }
                InventoryUtil.switchToHotbarSlot(InventoryUtil.findHotbarBlock(BlockObsidian.class), (boolean)this.silent.getValue());
                break;
            }
            case ECHEST: {
                if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                    Command.sendMessage("Can't find echest in hotbar!");
                    this.toggle();
                    break;
                }
                InventoryUtil.switchToHotbarSlot(InventoryUtil.findHotbarBlock(BlockEnderChest.class), (boolean)this.silent.getValue());
            }
        }
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u + 0.41999998688698, Burrow.mc.field_71439_g.field_70161_v, true));
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u + 0.7531999805211997, Burrow.mc.field_71439_g.field_70161_v, true));
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u + 1.00133597911214, Burrow.mc.field_71439_g.field_70161_v, true));
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u + 1.16610926093821, Burrow.mc.field_71439_g.field_70161_v, true));
        BlockUtil.placeBlock(this.originalPos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Burrow.mc.field_71439_g.field_70165_t, Burrow.mc.field_71439_g.field_70163_u + this.offset.getValue(), Burrow.mc.field_71439_g.field_70161_v, false));
        Burrow.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Burrow.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        Burrow.mc.field_71439_g.func_70095_a(false);
        InventoryUtil.switchToHotbarSlot(this.oldSlot, false);
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : Burrow.mc.field_71441_e.field_72996_f) {
            if (entity.equals((Object)Burrow.mc.field_71439_g) || entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    public static enum Mode {
        OBBY,
        ECHEST,
        CHEST;

    }
}

