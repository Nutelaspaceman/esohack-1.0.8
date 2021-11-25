/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiConfirmOpenLink
 *  net.minecraft.client.gui.GuiControls
 *  net.minecraft.client.gui.GuiCustomizeSkin
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreenOptionsSounds
 *  net.minecraft.client.gui.GuiVideoSettings
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.GuiModList
 */
package com.esoterik.client.features.modules.misc;

import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.ClickGui;
import com.esoterik.client.util.Util;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public class GuiBlur
extends Module
implements Util {
    public GuiBlur() {
        super("GUIBlur", "nigga", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onDisable() {
        if (GuiBlur.mc.field_71441_e != null) {
            GuiBlur.mc.field_71460_t.func_147706_e().func_148021_a();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onUpdate() {
        if (GuiBlur.mc.field_71441_e == null) return;
        if (!(ClickGui.getInstance().isEnabled() || GuiBlur.mc.field_71462_r instanceof GuiContainer || GuiBlur.mc.field_71462_r instanceof GuiChat || GuiBlur.mc.field_71462_r instanceof GuiConfirmOpenLink)) {
            if (!(GuiBlur.mc.field_71462_r instanceof GuiEditSign)) {
                if (!(GuiBlur.mc.field_71462_r instanceof GuiGameOver)) {
                    if (!(GuiBlur.mc.field_71462_r instanceof GuiOptions)) {
                        if (!(GuiBlur.mc.field_71462_r instanceof GuiIngameMenu)) {
                            if (!(GuiBlur.mc.field_71462_r instanceof GuiVideoSettings)) {
                                if (!(GuiBlur.mc.field_71462_r instanceof GuiScreenOptionsSounds)) {
                                    if (!(GuiBlur.mc.field_71462_r instanceof GuiControls)) {
                                        if (!(GuiBlur.mc.field_71462_r instanceof GuiCustomizeSkin)) {
                                            if (!(GuiBlur.mc.field_71462_r instanceof GuiModList)) {
                                                if (GuiBlur.mc.field_71460_t.func_147706_e() == null) return;
                                                GuiBlur.mc.field_71460_t.func_147706_e().func_148021_a();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (OpenGlHelper.field_148824_g && mc.func_175606_aa() instanceof EntityPlayer) {
            if (GuiBlur.mc.field_71460_t.func_147706_e() != null) {
                GuiBlur.mc.field_71460_t.func_147706_e().func_148021_a();
            }
            try {
                GuiBlur.mc.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
                return;
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (GuiBlur.mc.field_71460_t.func_147706_e() == null) return;
        if (GuiBlur.mc.field_71462_r != null) return;
        GuiBlur.mc.field_71460_t.func_147706_e().func_148021_a();
    }
}

