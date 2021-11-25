/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.entity.RenderPlayer
 */
package com.esoterik.client.mixin.mixins;

import com.esoterik.client.esohack;
import com.esoterik.client.features.modules.render.Nametags;
import com.esoterik.client.features.modules.render.ViewModel;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method={"renderEntityName"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if (Nametags.getInstance().isOn()) {
            info.cancel();
        }
    }

    @Shadow
    public ModelPlayer func_177087_b() {
        return new ModelPlayer(0.0f, false);
    }

    @Redirect(method={"renderRightArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    private void renderRightArm(ModelRenderer modelRenderer, float scale) {
        if (esohack.moduleManager.isModuleEnabled(ViewModel.class)) {
            ModelPlayer modelplayer = this.func_177087_b();
            modelplayer.field_178723_h.field_78795_f = ViewModel.getINSTANCE().rotationX.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78796_g = ViewModel.getINSTANCE().rotationY.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78808_h = ViewModel.getINSTANCE().rotationZ.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.func_78785_a(scale);
        } else {
            modelRenderer.func_78785_a(scale);
        }
    }

    @Redirect(method={"renderRightArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal=1))
    private void renderRightArmWear(ModelRenderer modelRenderer, float scale) {
        if (esohack.moduleManager.isModuleEnabled(ViewModel.class)) {
            ModelPlayer modelplayer = this.func_177087_b();
            modelplayer.field_178723_h.field_78795_f = ViewModel.getINSTANCE().rotationX.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78796_g = ViewModel.getINSTANCE().rotationY.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78808_h = ViewModel.getINSTANCE().rotationZ.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.func_78785_a(scale);
        } else {
            modelRenderer.func_78785_a(scale);
        }
    }

    @Redirect(method={"renderLeftArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    private void renderLeftArm(ModelRenderer modelRenderer, float scale) {
        if (esohack.moduleManager.isModuleEnabled(ViewModel.class)) {
            ModelPlayer modelplayer = this.func_177087_b();
            modelplayer.field_178723_h.field_78795_f = ViewModel.getINSTANCE().rotationX.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78796_g = ViewModel.getINSTANCE().rotationY.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78808_h = ViewModel.getINSTANCE().rotationZ.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.func_78785_a(scale);
        } else {
            modelRenderer.func_78785_a(scale);
        }
    }

    @Redirect(method={"renderLeftArm"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal=1))
    private void renderLeftArmWear(ModelRenderer modelRenderer, float scale) {
        if (esohack.moduleManager.isModuleEnabled(ViewModel.class)) {
            ModelPlayer modelplayer = this.func_177087_b();
            modelplayer.field_178723_h.field_78795_f = ViewModel.getINSTANCE().rotationX.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78796_g = ViewModel.getINSTANCE().rotationY.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.field_78808_h = ViewModel.getINSTANCE().rotationZ.getValue().floatValue() * 8.0f;
            modelplayer.field_178723_h.func_78785_a(scale);
        } else {
            modelRenderer.func_78785_a(scale);
        }
    }
}

