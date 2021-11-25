/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 */
package com.esoterik.client.features.modules.player;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class FastPlace
extends Module {
    private final Setting<Boolean> all = this.register(new Setting<Boolean>("AllItems", false));
    private BlockPos mousePos = null;

    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (this.all.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemEndCrystal.class)) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (FastPlace.mc.field_71474_y.field_74313_G.func_151470_d()) {
            boolean offhand = FastPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
            boolean bl = offhand;
            if (offhand || FastPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
                RayTraceResult result = FastPlace.mc.field_71476_x;
                if (result == null) {
                    return;
                }
                switch (result.field_72313_a) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = FastPlace.mc.field_71476_x.func_178782_a();
                        break;
                    }
                    case ENTITY: {
                        Entity entity;
                        if (this.mousePos == null || (entity = result.field_72308_g) == null || !this.mousePos.equals((Object)new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v))) break;
                        FastPlace.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
            }
        }
    }
}

