/*
 * Decompiled with CFR 0.150.
 */
package com.esoterik.client.features.modules.client;

import com.esoterik.client.Discord;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class RPC
extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> gang = this.register(new Setting<Boolean>("gang", false));

    public RPC() {
        super("RPC", "Discord rich presence", Module.Category.CLIENT, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Discord.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Discord.stop();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Discord.start();
    }
}

