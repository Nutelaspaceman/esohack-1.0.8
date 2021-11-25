/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.render;

import com.esoterik.client.event.events.PerspectiveEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AspectRatio
extends Module {
    private Setting<Double> aspect;

    public AspectRatio() {
        super("AspectRatio", "Changes the aspect ratio (p100 stretch res fortnite)", Module.Category.RENDER, true, false, false);
        this.aspect = this.register(new Setting<Double>("Ratio", (double)AspectRatio.mc.field_71443_c / (double)AspectRatio.mc.field_71440_d, 0.0, 3.0));
    }

    @SubscribeEvent
    public void onPerspectiveEvent(PerspectiveEvent event) {
        event.setAspect(this.aspect.getValue().floatValue());
    }
}

