/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package com.esoterik.client.features.modules.client;

import com.esoterik.client.esohack;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.manager.FileManager;
import com.esoterik.client.util.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

public class Notifications
extends Module {
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", false));
    public Setting<Boolean> totemNoti = this.register(new Setting<Object>("TotemNoti", Boolean.valueOf(true), v -> this.totemPops.getValue()));
    public Setting<Integer> delay = this.register(new Setting<Object>("Delay", 2000, 0, 5000, v -> this.totemPops.getValue(), "Delays messages."));
    public Setting<Boolean> clearOnLogout = this.register(new Setting<Boolean>("LogoutClear", false));
    public Setting<Boolean> visualRange = this.register(new Setting<Boolean>("VisualRange", false));
    public Setting<Boolean> coords = this.register(new Setting<Object>("Coords", Boolean.valueOf(true), v -> this.visualRange.getValue()));
    public Setting<Boolean> leaving = this.register(new Setting<Object>("Leaving", Boolean.valueOf(false), v -> this.visualRange.getValue()));
    public Setting<Boolean> crash = this.register(new Setting<Boolean>("Crash", false));
    private List<EntityPlayer> knownPlayers = new ArrayList<EntityPlayer>();
    private static List<String> modules = new ArrayList<String>();
    private static final String fileName = "client/util/ModuleMessage_List.txt";
    private final Timer timer = new Timer();
    public Timer totemAnnounce = new Timer();
    private boolean check;
    private static Notifications INSTANCE = new Notifications();

    public Notifications() {
        super("Notifications", "Sends Messages.", Module.Category.CLIENT, true, true, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<EntityPlayer>();
        if (!this.check) {
            this.loadFile();
        }
    }

    @Override
    public void onUpdate() {
        if (this.visualRange.getValue().booleanValue()) {
            ArrayList tickPlayerList = new ArrayList(Notifications.mc.field_71441_e.field_73010_i);
            if (tickPlayerList.size() > 0) {
                for (EntityPlayer player : tickPlayerList) {
                    if (player.func_70005_c_().equals(Notifications.mc.field_71439_g.func_70005_c_()) || this.knownPlayers.contains((Object)player)) continue;
                    this.knownPlayers.add(player);
                    if (esohack.friendManager.isFriend(player)) {
                        Command.sendMessage("Player \u00a7a" + player.func_70005_c_() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), true);
                    } else {
                        Command.sendMessage("Player \u00a7c" + player.func_70005_c_() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), true);
                    }
                    return;
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains((Object)player)) continue;
                    this.knownPlayers.remove((Object)player);
                    if (this.leaving.getValue().booleanValue()) {
                        if (esohack.friendManager.isFriend(player)) {
                            Command.sendMessage("Player \u00a7a" + player.func_70005_c_() + "\u00a7r" + " left your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), true);
                        } else {
                            Command.sendMessage("Player \u00a7c" + player.func_70005_c_() + "\u00a7r" + " left your visual range" + (this.coords.getValue() != false ? " at (" + (int)player.field_70165_t + ", " + (int)player.field_70163_u + ", " + (int)player.field_70161_v + ")!" : "!"), true);
                        }
                    }
                    return;
                }
            }
        }
    }

    public void loadFile() {
        List<String> fileInput = FileManager.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        modules.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            modules.add(s);
        }
    }

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    public static void displayCrash(Exception e) {
        Command.sendMessage("\u00a7cException caught: " + e.getMessage());
    }
}

