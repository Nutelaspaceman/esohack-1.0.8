/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package com.esoterik.client.event.events;

import com.esoterik.client.event.EventStage;
import net.minecraft.entity.Entity;

public class EntityAddedEvent
extends EventStage {
    private Entity entity;

    public EntityAddedEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

