/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.InventoryUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class Offhand
extends Module {
    public Setting<OffhandItem> mode = this.register(new Setting<OffhandItem>("Mode", OffhandItem.TOTEM));
    public Setting<Float> health = this.register(new Setting<Float>("Health", Float.valueOf(16.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    public Setting<Float> holeHealth = this.register(new Setting<Float>("HoleHealth", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> swordGap = this.register(new Setting<Boolean>("SwordGap", true));
    public Setting<Float> swordGapHealth = this.register(new Setting<Object>("SwordGapMinHealth", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.swordGap.getValue()));
    public Setting<Boolean> totemOnLethalCrystal = this.register(new Setting<Boolean>("LethalCrystalSwitch", true));
    public Item offhandItem;
    public Item holdingItem;
    public Item lastItem;
    public int crystals;
    public int gapples;
    public int totems;

    public Offhand() {
        super("Offhand", "Automatically puts items into your offhand", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onUpdate() {
        float currentHealth = Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj();
        if (this.isLethalCrystal(currentHealth) && this.totemOnLethalCrystal.getValue().booleanValue()) {
            this.lastItem = this.offhandItem;
            this.offhandItem = Items.field_190929_cY;
        } else if (Offhand.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && Offhand.mc.field_71474_y.field_74313_G.func_151470_d() && currentHealth > this.swordGapHealth.getValue().floatValue()) {
            this.lastItem = this.offhandItem;
            this.offhandItem = Items.field_151153_ao;
        } else if (EntityUtil.isSafe((Entity)Offhand.mc.field_71439_g)) {
            if (currentHealth <= this.holeHealth.getValue().floatValue()) {
                this.offhandItem = Items.field_190929_cY;
            } else {
                switch (this.mode.currentEnumName().toLowerCase()) {
                    case "totem": {
                        this.offhandItem = Items.field_190929_cY;
                        break;
                    }
                    case "crystal": {
                        this.offhandItem = Items.field_185158_cP;
                        break;
                    }
                    case "gapple": {
                        this.offhandItem = Items.field_151153_ao;
                    }
                }
            }
        } else if (currentHealth <= this.health.getValue().floatValue()) {
            this.offhandItem = Items.field_190929_cY;
        } else {
            switch (this.mode.currentEnumName().toLowerCase()) {
                case "totem": {
                    this.offhandItem = Items.field_190929_cY;
                    break;
                }
                case "crystal": {
                    this.offhandItem = Items.field_185158_cP;
                    break;
                }
                case "gapple": {
                    this.offhandItem = Items.field_151153_ao;
                }
            }
        }
        this.doSwitch();
    }

    private boolean isLethalCrystal(float health) {
        for (Entity e : Offhand.mc.field_71441_e.field_72996_f) {
            if (!(e instanceof EntityEnderCrystal) || !(health <= DamageUtil.calculateDamage(e, (Entity)Offhand.mc.field_71439_g))) continue;
            return true;
        }
        return false;
    }

    private void doSwitch() {
        this.holdingItem = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b();
        this.crystals = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
        this.gapples = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
        this.totems = Offhand.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (this.holdingItem.equals((Object)Items.field_185158_cP)) {
            this.crystals += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_185158_cP).mapToInt(ItemStack::func_190916_E).sum();
        } else if (this.holdingItem.equals((Object)Items.field_151153_ao)) {
            this.gapples += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_151153_ao).mapToInt(ItemStack::func_190916_E).sum();
        } else if (this.holdingItem.equals((Object)Items.field_190929_cY)) {
            this.totems += Offhand.mc.field_71439_g.field_71071_by.field_184439_c.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        }
        if (Offhand.mc.field_71462_r instanceof GuiContainer) {
            return;
        }
        int slot = InventoryUtil.findItemInventorySlot(this.offhandItem, false);
        if (slot != -1) {
            if (this.holdingItem == this.offhandItem) {
                return;
            }
            if (this.offhandItem.equals((Object)Items.field_185158_cP) && this.crystals > 0) {
                this.switchItem(slot);
            }
            if (this.offhandItem.equals((Object)Items.field_151153_ao) && this.gapples > 0) {
                this.switchItem(slot);
            }
            if (this.offhandItem.equals((Object)Items.field_190929_cY) && this.totems > 0) {
                this.switchItem(slot);
            }
        }
    }

    private void switchItem(int slot) {
        int returnSlot = -1;
        if (slot == -1) {
            return;
        }
        Offhand.mc.field_71442_b.func_187098_a(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        for (int i = 0; i < 45; ++i) {
            if (!Offhand.mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) continue;
            returnSlot = i;
            break;
        }
        if (returnSlot != -1) {
            Offhand.mc.field_71442_b.func_187098_a(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        }
        Offhand.mc.field_71442_b.func_78765_e();
    }

    public static enum OffhandItem {
        TOTEM,
        CRYSTAL,
        GAPPLE;

    }
}

