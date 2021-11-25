/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.crash.CrashReport
 *  org.lwjgl.opengl.Display
 */
package com.esoterik.client.mixin.mixins;

import com.esoterik.client.esohack;
import com.esoterik.client.features.gui.MainMenu;
import com.esoterik.client.features.modules.client.CustomMainMenu;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.util.Util;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft
implements Util {
    @Shadow
    public abstract void func_147108_a(@Nullable GuiScreen var1);

    @Inject(method={"Lnet/minecraft/client/Minecraft;getLimitFramerate()I"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLimitFramerateHook(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (Managers.getInstance().unfocusedCpu.getValue().booleanValue() && !Display.isActive()) {
                callbackInfoReturnable.setReturnValue(Managers.getInstance().cpuFPS.getValue());
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    @Redirect(method={"runGameLoop"}, at=@At(value="INVOKE", target="Lorg/lwjgl/opengl/Display;sync(I)V"))
    public void syncHook(int maxFps) {
        if (Managers.getInstance().betterFrames.getValue().booleanValue()) {
            Display.sync((int)Managers.getInstance().betterFPS.getValue());
        } else {
            Display.sync((int)maxFps);
        }
    }

    @Redirect(method={"run"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReportHook(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Inject(method={"runTick()V"}, at={@At(value="RETURN")})
    private void runTick(CallbackInfo callbackInfo) {
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiMainMenu && esohack.moduleManager.isModuleEnabled(CustomMainMenu.class)) {
            Minecraft.func_71410_x().func_147108_a((GuiScreen)new MainMenu());
        }
    }

    @Inject(method={"displayGuiScreen"}, at={@At(value="HEAD")})
    private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
        if (screen instanceof GuiMainMenu && esohack.moduleManager.isModuleEnabled(CustomMainMenu.class)) {
            this.func_147108_a(new MainMenu());
        }
    }

    @Inject(method={"shutdown"}, at={@At(value="HEAD")})
    public void shutdownHook(CallbackInfo info) {
        this.unload();
    }

    private void unload() {
        System.out.println("Shutting down: saving configuration");
        esohack.onUnload();
        System.out.println("Configuration saved.");
    }
}

