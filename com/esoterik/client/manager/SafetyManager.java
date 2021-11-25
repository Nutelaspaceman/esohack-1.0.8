/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 */
package com.esoterik.client.manager;

import com.esoterik.client.features.Feature;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.DamageUtil;
import com.esoterik.client.util.EntityUtil;
import com.esoterik.client.util.Timer;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class SafetyManager
extends Feature
implements Runnable {
    private final Timer syncTimer = new Timer();
    private ScheduledExecutorService service;
    private final AtomicBoolean SAFE = new AtomicBoolean(false);

    @Override
    public void run() {
    }

    public void doSafetyCheck() {
        if (!SafetyManager.fullNullCheck()) {
            boolean safe = true;
            EntityPlayer closest = Managers.getInstance().safety.getValue() != false ? EntityUtil.getClosestEnemy(18.0) : null;
            EntityPlayer entityPlayer = closest;
            if (Managers.getInstance().safety.getValue().booleanValue() && closest == null) {
                this.SAFE.set(true);
                return;
            }
            ArrayList crystals = new ArrayList(SafetyManager.mc.field_71441_e.field_72996_f);
            for (Entity crystal : crystals) {
                if (!(crystal instanceof EntityEnderCrystal) || !((double)DamageUtil.calculateDamage(crystal, (Entity)SafetyManager.mc.field_71439_g) > 4.0) || closest != null && !(closest.func_70068_e(crystal) < 40.0)) continue;
                safe = false;
                break;
            }
            if (safe) {
                for (BlockPos pos : BlockUtil.possiblePlacePositions(4.0f, false, Managers.getInstance().oneDot15.getValue())) {
                    if (!((double)DamageUtil.calculateDamage(pos, (Entity)SafetyManager.mc.field_71439_g) > 4.0) || closest != null && !(closest.func_174818_b(pos) < 40.0)) continue;
                    safe = false;
                    break;
                }
            }
            this.SAFE.set(safe);
        }
    }

    public void onUpdate() {
        this.run();
    }

    public String getSafetyString() {
        if (this.SAFE.get()) {
            return "\u00a7aSecure";
        }
        return "\u00a7cUnsafe";
    }

    public boolean isSafe() {
        return this.SAFE.get();
    }

    public ScheduledExecutorService getService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0L, Managers.getInstance().safetyCheck.getValue().intValue(), TimeUnit.MILLISECONDS);
        return service;
    }
}

