/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.MapItemRenderer
 *  net.minecraft.world.storage.MapData
 */
package com.esoterik.client.mixin.mixins;

import com.esoterik.client.features.modules.render.NoRender;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.world.storage.MapData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MapItemRenderer.class})
public class MixinMapItemRenderer {
    @Inject(method={"renderMap"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(MapData mapdataIn, boolean noOverlayRendering, CallbackInfo p_Callback) {
        if (NoRender.getInstance().maps.getValue().booleanValue()) {
            p_Callback.cancel();
        }
    }
}

