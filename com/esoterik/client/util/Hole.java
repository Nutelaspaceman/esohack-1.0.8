/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.esoterik.client.util;

import net.minecraft.util.math.BlockPos;

public class Hole {
    BlockPos blockPos;
    Type type;

    public Hole(BlockPos blockPos, Type type) {
        this.blockPos = blockPos;
        this.type = type;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public Type getType() {
        return this.type;
    }

    public static enum Type {
        Obsidian,
        Bedrock,
        Mixed;

    }
}

