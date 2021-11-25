/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.esoterik.client.util;

import net.minecraft.client.Minecraft;

public interface MixinInterface {
    public static final Minecraft mc = Minecraft.func_71410_x();
    public static final boolean nullCheck = MixinInterface.mc.field_71439_g == null || MixinInterface.mc.field_71441_e == null;
}

