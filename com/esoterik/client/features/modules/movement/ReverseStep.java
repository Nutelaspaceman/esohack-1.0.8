/*
 * Decompiled with CFR 0.150.
 */
package com.esoterik.client.features.modules.movement;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;

public class ReverseStep
extends Module {
    private final Setting<Integer> speed = this.register(new Setting<Integer>("Speed", 10, 1, 20));

    public ReverseStep() {
        super("ReverseStep", "Go down", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        block5: {
            block4: {
                if (ReverseStep.fullNullCheck()) break block4;
                if (ReverseStep.mc.field_71439_g.func_70090_H()) break block4;
                if (ReverseStep.mc.field_71439_g.func_180799_ab()) break block4;
                if (!ReverseStep.mc.field_71439_g.func_70617_f_()) break block5;
            }
            return;
        }
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            ReverseStep.mc.field_71439_g.field_70181_x -= (double)((float)this.speed.getValue().intValue() / 10.0f);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ReverseStep.mc.field_71439_g.field_70181_x = 0.0;
    }
}

