/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.item.ItemStack
 */
package com.esoterik.client.mixin.mixins;

import com.esoterik.client.features.Feature;
import com.esoterik.client.features.modules.render.ViewModel;
import com.esoterik.client.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderItem.class})
public abstract class MixinItemRenderer
implements Util {
    @Inject(method={"renderItemModel"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift=At.Shift.BEFORE)})
    private void test(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (((Boolean)ViewModel.getINSTANCE().enabled.getValue()).booleanValue() && Minecraft.func_71410_x().field_71474_y.field_74320_O == 0 && !Feature.fullNullCheck()) {
            GlStateManager.func_179152_a((float)ViewModel.getINSTANCE().sizeX.getValue().floatValue(), (float)ViewModel.getINSTANCE().sizeY.getValue().floatValue(), (float)ViewModel.getINSTANCE().sizeZ.getValue().floatValue());
            GlStateManager.func_179114_b((float)(ViewModel.getINSTANCE().rotationX.getValue().floatValue() * 360.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(ViewModel.getINSTANCE().rotationY.getValue().floatValue() * 360.0f), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.func_179114_b((float)(ViewModel.getINSTANCE().rotationZ.getValue().floatValue() * 360.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.func_179109_b((float)ViewModel.getINSTANCE().positionX.getValue().floatValue(), (float)ViewModel.getINSTANCE().positionY.getValue().floatValue(), (float)ViewModel.getINSTANCE().positionZ.getValue().floatValue());
        }
    }
}

