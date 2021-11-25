/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Mouse
 */
package com.esoterik.client.features.gui;

import com.esoterik.client.esohack;
import com.esoterik.client.features.gui.components.Component;
import com.esoterik.client.features.gui.components.items.Item;
import com.esoterik.client.features.gui.components.items.buttons.ModuleButton;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.ClickGui;
import com.esoterik.client.features.modules.client.Colors;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class esohackGui
extends GuiScreen {
    private static esohackGui phobosGui;
    private final ArrayList<Component> components = new ArrayList();
    private static esohackGui INSTANCE;

    public esohackGui() {
        this.setInstance();
        this.load();
    }

    public static esohackGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new esohackGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static esohackGui getClickGui() {
        return esohackGui.getInstance();
    }

    private void load() {
        int x = -84;
        for (final Module.Category category : esohack.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 90, 4, true){

                @Override
                public void setupItems() {
                    esohack.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton((Module)module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort((item1, item2) -> item1.getName().compareTo(item2.getName())));
    }

    public void updateModule(Module module) {
        block0: for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
                continue block0;
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.checkMouseWheel();
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        this.func_73733_a(0, 0, sr.func_78326_a(), sr.func_78328_b(), 0, ClickGui.getInstance().colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor().getRGB() : new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().alpha.getValue() / 2).getRGB());
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void func_73864_a(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void func_146286_b(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean func_73868_f() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void func_73869_a(char typedChar, int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    static {
        INSTANCE = new esohackGui();
    }
}

