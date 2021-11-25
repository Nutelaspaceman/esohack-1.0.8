/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package com.esoterik.client.util;

import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.Minecraftable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class CombatUtil
implements Minecraftable {
    public static EntityPlayer getTarget(float range) {
        EntityPlayer currentTarget = null;
        int size = CombatUtil.mc.field_71441_e.field_73010_i.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer player = (EntityPlayer)CombatUtil.mc.field_71441_e.field_73010_i.get(i);
            if (EntityUtil.isntValid((Entity)player, range)) continue;
            if (currentTarget == null) {
                currentTarget = player;
                continue;
            }
            if (!(CombatUtil.mc.field_71439_g.func_70068_e((Entity)player) < CombatUtil.mc.field_71439_g.func_70068_e((Entity)currentTarget))) continue;
            currentTarget = player;
        }
        return currentTarget;
    }

    public static boolean isInHole(EntityPlayer entity) {
        return CombatUtil.isBlockValid(new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return CombatUtil.isBedrockHole(blockPos) || CombatUtil.isObbyHole(blockPos) || CombatUtil.isBothHole(blockPos);
    }

    public static int isInHoleInt(EntityPlayer entity) {
        BlockPos playerPos = new BlockPos(entity.func_174791_d());
        if (CombatUtil.isBedrockHole(playerPos)) {
            return 1;
        }
        if (CombatUtil.isObbyHole(playerPos) || CombatUtil.isBothHole(playerPos)) {
            return 2;
        }
        return 0;
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = CombatUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150343_Z) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = CombatUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150357_h) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = CombatUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z) continue;
            return false;
        }
        return true;
    }
}

