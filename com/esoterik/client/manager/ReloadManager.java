/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.manager;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
extends Feature {
    public String prefix;

    public void init(String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage("\u00a7cesohack has been unloaded. Type " + prefix + "reload to reload.");
        }
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = (CPacketChatMessage)event.getPacket()).func_149439_c().startsWith(this.prefix) && packet.func_149439_c().contains("reload")) {
            esohack.load();
            event.setCanceled(true);
        }
    }
}

