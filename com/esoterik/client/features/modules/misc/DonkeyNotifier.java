/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityDonkey
 */
package com.esoterik.client.features.modules.misc;

import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;

public class DonkeyNotifier
extends Module {
    private static DonkeyNotifier instance;
    private Set<Entity> entities = new HashSet<Entity>();

    public DonkeyNotifier() {
        super("DonkeyNotifier", "Notifies you when a donkey is discovered", Module.Category.MISC, true, false, false);
        instance = this;
    }

    @Override
    public void onEnable() {
        this.entities.clear();
    }

    @Override
    public void onUpdate() {
        for (Entity entity : DonkeyNotifier.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityDonkey) || this.entities.contains((Object)entity)) continue;
            Command.sendMessage("Donkey Detected at: " + entity.field_70165_t + "x, " + entity.field_70163_u + "y, " + entity.field_70161_v + "z.");
            this.entities.add(entity);
        }
    }
}

