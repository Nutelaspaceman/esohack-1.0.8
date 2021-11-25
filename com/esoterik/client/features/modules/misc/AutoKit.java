/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package com.esoterik.client.features.modules.misc;

import com.esoterik.client.event.events.DeathEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Bind;
import com.esoterik.client.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class AutoKit
extends Module {
    public Setting<String> primaryKit = this.register(new Setting<String>("Primary Kit", "ffa"));
    public Setting<String> secondaryKit = this.register(new Setting<String>("Secondary Kit", "duel"));
    public Setting<Bind> swapBind = this.register(new Setting<Bind>("SwapBind", new Bind(-1)));
    private boolean toggle = false;
    private boolean runThisLife = false;
    public static AutoKit INSTANCE;

    public AutoKit() {
        super("AutoKit", "Automatically does /kit <name>", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static AutoKit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoKit();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (AutoKit.mc.field_71439_g.func_70089_S() && !this.runThisLife) {
            AutoKit.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/kit " + (this.toggle ? this.secondaryKit.getValue() : this.primaryKit.getValue())));
            this.runThisLife = true;
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && this.swapBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.toggle = !this.toggle;
        }
    }

    @SubscribeEvent
    public void onEntityDeath(DeathEvent event) {
        if (event.player == AutoKit.mc.field_71439_g) {
            this.runThisLife = false;
        }
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

