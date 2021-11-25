/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreenOptionsSounds
 *  net.minecraft.client.gui.GuiVideoSettings
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 */
package com.esoterik.client.features.modules.movement;

import com.esoterik.client.event.events.KeyEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown
extends Module {
    public Setting<Boolean> guiMove = this.register(new Setting<Boolean>("GuiMove", true));
    public Setting<Boolean> noSlow = this.register(new Setting<Boolean>("NoSlow", true));
    public Setting<Boolean> soulSand = this.register(new Setting<Boolean>("SoulSand", true));
    private static NoSlowDown INSTANCE = new NoSlowDown();
    private static KeyBinding[] keys = new KeyBinding[]{NoSlowDown.mc.field_71474_y.field_74351_w, NoSlowDown.mc.field_71474_y.field_74368_y, NoSlowDown.mc.field_71474_y.field_74370_x, NoSlowDown.mc.field_71474_y.field_74366_z, NoSlowDown.mc.field_71474_y.field_74314_A, NoSlowDown.mc.field_71474_y.field_151444_V};

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoSlowDown getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlowDown();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        block2: {
            block3: {
                if (!this.guiMove.getValue().booleanValue()) break block2;
                if (!(NoSlowDown.mc.field_71462_r instanceof GuiOptions) && !(NoSlowDown.mc.field_71462_r instanceof GuiVideoSettings) && !(NoSlowDown.mc.field_71462_r instanceof GuiScreenOptionsSounds) && !(NoSlowDown.mc.field_71462_r instanceof GuiContainer) && !(NoSlowDown.mc.field_71462_r instanceof GuiIngameMenu)) break block3;
                for (KeyBinding bind : keys) {
                    KeyBinding.func_74510_a((int)bind.func_151463_i(), (boolean)Keyboard.isKeyDown((int)bind.func_151463_i()));
                }
                break block2;
            }
            if (NoSlowDown.mc.field_71462_r != null) break block2;
            for (KeyBinding bind : keys) {
                if (Keyboard.isKeyDown((int)bind.func_151463_i())) continue;
                KeyBinding.func_74510_a((int)bind.func_151463_i(), (boolean)false);
            }
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.noSlow.getValue().booleanValue() && NoSlowDown.mc.field_71439_g.func_184587_cr() && !NoSlowDown.mc.field_71439_g.func_184218_aH()) {
            event.getMovementInput().field_78902_a *= 5.0f;
            event.getMovementInput().field_192832_b *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onKeyEvent(KeyEvent event) {
        if (this.guiMove.getValue().booleanValue() && event.getStage() == 0 && !(NoSlowDown.mc.field_71462_r instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }
}

