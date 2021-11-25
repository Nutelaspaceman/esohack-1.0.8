/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 */
package com.esoterik.client.util;

import com.esoterik.client.util.Minecraftable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class ItemUtil
implements Minecraftable {
    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack.func_77973_b() != item) continue;
            slot = i;
        }
        return slot;
    }

    public static int getItemSlot(Class clss) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b().getClass() != clss) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemSlot(Item item) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (!ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b().equals((Object)item)) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }

    public static int getItemCount(Item item) {
        int count = 0;
        int size = ItemUtil.mc.field_71439_g.field_71071_by.field_70462_a.size();
        for (int i = 0; i < size; ++i) {
            ItemStack itemStack = (ItemStack)ItemUtil.mc.field_71439_g.field_71071_by.field_70462_a.get(i);
            if (itemStack.func_77973_b() != item) continue;
            count += itemStack.func_190916_E();
        }
        ItemStack offhandStack = ItemUtil.mc.field_71439_g.func_184592_cb();
        if (offhandStack.func_77973_b() == item) {
            count += offhandStack.func_190916_E();
        }
        return count;
    }

    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (ItemStack piece : player.field_71071_by.field_70460_b) {
            if (piece != null && !(ItemUtil.getDamageInPercent(piece) < (float)durability)) continue;
            return true;
        }
        return false;
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.func_77958_k() - stack.func_77952_i();
    }

    public static float getDamageInPercent(ItemStack stack) {
        float green = ((float)stack.func_77958_k() - (float)stack.func_77952_i()) / (float)stack.func_77958_k();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)ItemUtil.getDamageInPercent(stack);
    }

    public static boolean hasDurability(ItemStack stack) {
        Item item = stack.func_77973_b();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }
}

