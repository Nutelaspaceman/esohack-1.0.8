/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.combat;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.UpdateWalkingPlayerEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.InventoryUtil;
import com.esoterik.client.util.MathUtil;
import com.esoterik.client.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillAura
extends Module {
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(7.0f)));
    public Setting<Boolean> ghastOnly = this.register(new Setting<Boolean>("GhastOnly", false));
    public Setting<Boolean> autoSwitch = this.register(new Setting<Boolean>("AutoSwitch", false));
    public Setting<Boolean> delay = this.register(new Setting<Boolean>("Delay", true));
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    public Setting<Boolean> stay = this.register(new Setting<Object>("Stay", Boolean.valueOf(true), v -> this.rotate.getValue()));
    public Setting<Boolean> armorBreak = this.register(new Setting<Boolean>("ArmorBreak", false));
    public Setting<Boolean> eating = this.register(new Setting<Boolean>("Eating", true));
    public Setting<Boolean> onlySharp = this.register(new Setting<Boolean>("Axe/Sword", true));
    public Setting<Boolean> teleport = this.register(new Setting<Boolean>("Teleport", false));
    public Setting<Float> raytrace = this.register(new Setting<Object>("Raytrace", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(7.0f), v -> this.teleport.getValue() == false, "Wall Range."));
    public Setting<Float> teleportRange = this.register(new Setting<Object>("TpRange", Float.valueOf(15.0f), Float.valueOf(0.1f), Float.valueOf(50.0f), v -> this.teleport.getValue(), "Teleport Range."));
    public Setting<Boolean> lagBack = this.register(new Setting<Object>("LagBack", Boolean.valueOf(true), v -> this.teleport.getValue()));
    public Setting<Boolean> teekaydelay = this.register(new Setting<Boolean>("32kDelay", false));
    public Setting<Integer> time32k = this.register(new Setting<Integer>("32kTime", 5, 1, 50));
    public Setting<Integer> multi = this.register(new Setting<Object>("32kPackets", Integer.valueOf(2), v -> this.teekaydelay.getValue() == false));
    public Setting<Boolean> multi32k = this.register(new Setting<Boolean>("Multi32k", false));
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    public Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    public Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    public Setting<Boolean> vehicles = this.register(new Setting<Boolean>("Entities", false));
    public Setting<Boolean> projectiles = this.register(new Setting<Boolean>("Projectiles", false));
    public Setting<Boolean> tps = this.register(new Setting<Boolean>("TpsSync", true));
    public Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    public Setting<Boolean> swing = this.register(new Setting<Boolean>("Swing", true));
    public Setting<Boolean> sneak = this.register(new Setting<Boolean>("State", false));
    public Setting<Boolean> info = this.register(new Setting<Boolean>("Info", true));
    private final Setting<TargetMode> targetMode = this.register(new Setting<TargetMode>("Target", TargetMode.CLOSEST));
    public Setting<Float> health = this.register(new Setting<Object>("Health", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.targetMode.getValue() == TargetMode.SMART));

    public KillAura() {
        super("Killaura", "Kills aura.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (!this.rotate.getValue().booleanValue()) {
            this.doKillaura();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.rotate.getValue().booleanValue()) {
            if (this.stay.getValue().booleanValue() && target != null) {
                esohack.rotationManager.lookAtEntity(target);
            }
            this.doKillaura();
        }
    }

    private void doKillaura() {
        int sword;
        int wait;
        if (this.onlySharp.getValue().booleanValue() && !EntityUtil.holdingWeapon((EntityPlayer)KillAura.mc.field_71439_g)) {
            target = null;
            return;
        }
        int n = this.delay.getValue() == false || EntityUtil.holding32k((EntityPlayer)KillAura.mc.field_71439_g) && this.teekaydelay.getValue() == false ? 0 : (wait = (wait = (int)((float)DamageUtil.getCooldownByWeapon((EntityPlayer)KillAura.mc.field_71439_g) * (this.tps.getValue() != false ? esohack.serverManager.getTpsFactor() : 1.0f))));
        if (!this.timer.passedMs(wait) || !this.eating.getValue().booleanValue() && KillAura.mc.field_71439_g.func_184587_cr() && (!KillAura.mc.field_71439_g.func_184592_cb().func_77973_b().equals((Object)Items.field_185159_cQ) || KillAura.mc.field_71439_g.func_184600_cs() != EnumHand.OFF_HAND)) {
            return;
        }
        if (!(this.targetMode.getValue() == TargetMode.FOCUS && target != null && (KillAura.mc.field_71439_g.func_70068_e(target) < MathUtil.square(this.range.getValue().floatValue()) || this.teleport.getValue().booleanValue() && KillAura.mc.field_71439_g.func_70068_e(target) < MathUtil.square(this.teleportRange.getValue().floatValue())) && (KillAura.mc.field_71439_g.func_70685_l(target) || EntityUtil.canEntityFeetBeSeen(target) || KillAura.mc.field_71439_g.func_70068_e(target) < MathUtil.square(this.raytrace.getValue().floatValue()) || this.teleport.getValue().booleanValue()))) {
            target = this.getTarget();
        }
        if (target == null) {
            return;
        }
        if (this.autoSwitch.getValue().booleanValue() && (sword = InventoryUtil.findHotbarBlock(ItemSword.class)) != -1) {
            InventoryUtil.switchToHotbarSlot(sword, false);
        }
        if (this.rotate.getValue().booleanValue()) {
            esohack.rotationManager.lookAtEntity(target);
        }
        if (this.teleport.getValue().booleanValue()) {
            esohack.positionManager.setPositionPacket(KillAura.target.field_70165_t, EntityUtil.canEntityFeetBeSeen(target) ? KillAura.target.field_70163_u : KillAura.target.field_70163_u + (double)target.func_70047_e(), KillAura.target.field_70161_v, true, true, this.lagBack.getValue() == false);
        }
        if (EntityUtil.holding32k((EntityPlayer)KillAura.mc.field_71439_g) && !this.teekaydelay.getValue().booleanValue()) {
            if (this.multi32k.getValue().booleanValue()) {
                for (EntityPlayer player : KillAura.mc.field_71441_e.field_73010_i) {
                    if (!EntityUtil.isValid((Entity)player, this.range.getValue().floatValue())) continue;
                    this.teekayAttack((Entity)player);
                }
            } else {
                this.teekayAttack(target);
            }
            this.timer.reset();
            return;
        }
        if (this.armorBreak.getValue().booleanValue()) {
            KillAura.mc.field_71442_b.func_187098_a(KillAura.mc.field_71439_g.field_71069_bz.field_75152_c, 9, KillAura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)KillAura.mc.field_71439_g);
            EntityUtil.attackEntity(target, this.packet.getValue(), this.swing.getValue());
            KillAura.mc.field_71442_b.func_187098_a(KillAura.mc.field_71439_g.field_71069_bz.field_75152_c, 9, KillAura.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)KillAura.mc.field_71439_g);
            EntityUtil.attackEntity(target, this.packet.getValue(), this.swing.getValue());
        } else {
            boolean sneaking = KillAura.mc.field_71439_g.func_70093_af();
            boolean sprint = KillAura.mc.field_71439_g.func_70051_ag();
            if (this.sneak.getValue().booleanValue()) {
                if (sneaking) {
                    KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)KillAura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                if (sprint) {
                    KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)KillAura.mc.field_71439_g, CPacketEntityAction.Action.STOP_SPRINTING));
                }
            }
            EntityUtil.attackEntity(target, this.packet.getValue(), this.swing.getValue());
            if (this.sneak.getValue().booleanValue()) {
                if (sprint) {
                    KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)KillAura.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (sneaking) {
                    KillAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)KillAura.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                }
            }
        }
        this.timer.reset();
    }

    private void teekayAttack(Entity entity) {
        for (int i = 0; i < this.multi.getValue(); ++i) {
            this.startEntityAttackThread(entity, i * this.time32k.getValue());
        }
    }

    private void startEntityAttackThread(Entity entity, int time) {
        new Thread(() -> {
            Timer timer = new Timer();
            timer.reset();
            try {
                Thread.sleep(time);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            EntityUtil.attackEntity(entity, true, this.swing.getValue());
        }).start();
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = this.teleport.getValue() != false ? (double)this.teleportRange.getValue().floatValue() : (double)this.range.getValue().floatValue();
        double maxHealth = 36.0;
        for (Entity entity : KillAura.mc.field_71441_e.field_72996_f) {
            if (!(this.players.getValue() != false && entity instanceof EntityPlayer || this.ghastOnly.getValue() != false && entity instanceof EntityGhast || this.animals.getValue() != false && EntityUtil.isPassive(entity) || this.mobs.getValue() != false && EntityUtil.isMobAggressive(entity) || this.vehicles.getValue() != false && EntityUtil.isVehicle(entity)) && (!this.projectiles.getValue().booleanValue() || !EntityUtil.isProjectile(entity)) || entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance) || !this.teleport.getValue().booleanValue() && !KillAura.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && KillAura.mc.field_71439_g.func_70068_e(entity) > MathUtil.square(this.raytrace.getValue().floatValue())) continue;
            if (target == null) {
                target = entity;
                distance = KillAura.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                target = entity;
                break;
            }
            if (this.targetMode.getValue() == TargetMode.SMART && EntityUtil.getHealth(entity) < this.health.getValue().floatValue()) {
                target = entity;
                break;
            }
            if (this.targetMode.getValue() != TargetMode.HEALTH && KillAura.mc.field_71439_g.func_70068_e(entity) < distance) {
                target = entity;
                distance = KillAura.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            if (this.targetMode.getValue() != TargetMode.HEALTH || !((double)EntityUtil.getHealth(entity) < maxHealth)) continue;
            target = entity;
            distance = KillAura.mc.field_71439_g.func_70068_e(entity);
            maxHealth = EntityUtil.getHealth(entity);
        }
        return target;
    }

    @Override
    public String getDisplayInfo() {
        if (this.info.getValue().booleanValue() && target instanceof EntityPlayer) {
            return target.func_70005_c_();
        }
        return null;
    }

    public static enum TargetMode {
        FOCUS,
        CLOSEST,
        HEALTH,
        SMART;

    }
}

