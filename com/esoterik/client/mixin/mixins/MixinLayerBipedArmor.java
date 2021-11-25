/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerArmorBase
 *  net.minecraft.client.renderer.entity.layers.LayerBipedArmor
 *  net.minecraft.inventory.EntityEquipmentSlot
 */
package com.esoterik.client.mixin.mixins;

import com.esoterik.client.features.modules.render.NoRender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LayerBipedArmor.class})
public abstract class MixinLayerBipedArmor
extends LayerArmorBase<ModelBiped> {
    public MixinLayerBipedArmor(RenderLivingBase<?> rendererIn) {
        super(rendererIn);
    }

    @Inject(method={"setModelSlotVisible"}, at={@At(value="HEAD")}, cancellable=true)
    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo info) {
        NoRender noArmor = NoRender.getInstance();
        if (noArmor.isOn() && noArmor.noArmor.getValue() != NoRender.NoArmor.NONE) {
            info.cancel();
            switch (slotIn) {
                case HEAD: {
                    model.field_78116_c.field_78806_j = false;
                    model.field_178720_f.field_78806_j = false;
                    break;
                }
                case CHEST: {
                    model.field_78115_e.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    model.field_178723_h.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    model.field_178724_i.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    break;
                }
                case LEGS: {
                    model.field_78115_e.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    model.field_178721_j.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    model.field_178722_k.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    break;
                }
                case FEET: {
                    model.field_178721_j.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                    model.field_178722_k.field_78806_j = noArmor.noArmor.getValue() != NoRender.NoArmor.ALL;
                }
            }
        }
    }
}

