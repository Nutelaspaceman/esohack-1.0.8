/*
 * Decompiled with CFR 0.150.
 */
package com.esoterik.client.features.modules.client;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class CustomMainMenu
extends Module {
    public Setting<Boolean> music = this.register(new Setting<Boolean>("StartupMusic", true));
    private static CustomMainMenu INSTANCE = new CustomMainMenu();

    public CustomMainMenu() {
        super("MainMenu", "Custom main menu", Module.Category.CLIENT, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static CustomMainMenu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomMainMenu();
        }
        return INSTANCE;
    }
}

