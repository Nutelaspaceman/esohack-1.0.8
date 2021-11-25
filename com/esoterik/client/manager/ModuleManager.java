/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package com.esoterik.client.manager;

import com.esoterik.client.event.events.Render2DEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.gui.esohackGui;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.ClickGui;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.modules.client.Components;
import com.esoterik.client.features.modules.client.CustomMainMenu;
import com.esoterik.client.features.modules.client.HUD;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.features.modules.client.Notifications;
import com.esoterik.client.features.modules.client.RPC;
import com.esoterik.client.features.modules.combat.AutoArmor;
import com.esoterik.client.features.modules.combat.AutoCrystal;
import com.esoterik.client.features.modules.combat.AutoLog;
import com.esoterik.client.features.modules.combat.AutoTrap;
import com.esoterik.client.features.modules.combat.Criticals;
import com.esoterik.client.features.modules.combat.HoleFiller;
import com.esoterik.client.features.modules.combat.KillAura;
import com.esoterik.client.features.modules.combat.Offhand;
import com.esoterik.client.features.modules.combat.Surround;
import com.esoterik.client.features.modules.misc.AutoKit;
import com.esoterik.client.features.modules.misc.ChatModifier;
import com.esoterik.client.features.modules.misc.DonkeyNotifier;
import com.esoterik.client.features.modules.misc.GuiBlur;
import com.esoterik.client.features.modules.misc.Spammer;
import com.esoterik.client.features.modules.movement.AntiWeb;
import com.esoterik.client.features.modules.movement.NoSlowDown;
import com.esoterik.client.features.modules.movement.ReverseStep;
import com.esoterik.client.features.modules.movement.Speed;
import com.esoterik.client.features.modules.movement.Sprint;
import com.esoterik.client.features.modules.movement.Step;
import com.esoterik.client.features.modules.movement.Velocity;
import com.esoterik.client.features.modules.player.Burrow;
import com.esoterik.client.features.modules.player.FakePlayer;
import com.esoterik.client.features.modules.player.FastPlace;
import com.esoterik.client.features.modules.player.MCP;
import com.esoterik.client.features.modules.player.Replenish;
import com.esoterik.client.features.modules.player.Speedmine;
import com.esoterik.client.features.modules.render.AspectRatio;
import com.esoterik.client.features.modules.render.CameraClip;
import com.esoterik.client.features.modules.render.CrystalChams;
import com.esoterik.client.features.modules.render.ESP;
import com.esoterik.client.features.modules.render.Fullbright;
import com.esoterik.client.features.modules.render.HoleESP;
import com.esoterik.client.features.modules.render.Nametags;
import com.esoterik.client.features.modules.render.NoRender;
import com.esoterik.client.features.modules.render.ViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    boolean hasRun = false;

    public void init() {
        modules.add(new AutoTrap());
        modules.add(new Offhand());
        modules.add(new Criticals());
        modules.add(new AutoArmor());
        modules.add(new Surround());
        modules.add(new HoleFiller());
        modules.add(new AutoCrystal());
        modules.add(new KillAura());
        modules.add(new AutoLog());
        modules.add(new ChatModifier());
        modules.add(new Spammer());
        modules.add(new DonkeyNotifier());
        modules.add(new AutoKit());
        modules.add(new GuiBlur());
        modules.add(new Velocity());
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new Sprint());
        modules.add(new NoSlowDown());
        modules.add(new Speed());
        modules.add(new AntiWeb());
        modules.add(new FakePlayer());
        modules.add(new Speedmine());
        modules.add(new Replenish());
        modules.add(new MCP());
        modules.add(new FastPlace());
        modules.add(new Burrow());
        modules.add(new AspectRatio());
        modules.add(new NoRender());
        modules.add(new Fullbright());
        modules.add(new Nametags());
        modules.add(new CameraClip());
        modules.add(new ViewModel());
        modules.add(new ESP());
        modules.add(new HoleESP());
        modules.add(new CrystalChams());
        modules.add(new Colors());
        modules.add(new Notifications());
        modules.add(new HUD());
        modules.add(new ClickGui());
        modules.add(new Managers());
        modules.add(new Components());
        modules.add(new RPC());
        modules.add(new CustomMainMenu());
    }

    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public void enableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).enable();
        }
    }

    public void disableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module)module).isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        modules.stream().filter(Module::listening).forEach(((EventBus)MinecraftForge.EVENT_BUS)::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void onLogout() {
        modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof esohackGui) {
            return;
        }
        modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    public static void onServerUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(Module::onServerUpdate);
    }
}

