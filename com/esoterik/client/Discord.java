/*
 * Decompiled with CFR 0.150.
 */
package com.esoterik.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.esoterik.client.esohack;

public class Discord {
    public static DiscordRichPresence presence;
    private static final DiscordRPC rpc;
    private static Thread thread;

    public static void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize("851941408381206529", handlers, true, "");
        Discord.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        Discord.presence.details = esohack.getName() + " v" + "1.0.8";
        Discord.presence.state = "gang activity";
        Discord.presence.largeImageKey = "esohack";
        Discord.presence.largeImageText = "winner pov";
        rpc.Discord_UpdatePresence(presence);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                Discord.presence.largeImageKey = "esohack";
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException interruptedException) {}
            }
        }, "RPC-Callback-Handler");
        thread.start();
    }

    public static void stop() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
        rpc.Discord_Shutdown();
    }

    static {
        rpc = DiscordRPC.INSTANCE;
        presence = new DiscordRichPresence();
    }
}

