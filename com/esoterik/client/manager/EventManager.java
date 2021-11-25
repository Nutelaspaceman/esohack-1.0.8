/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 *  org.lwjgl.input.Keyboard
 */
package com.esoterik.client.manager;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.DeathEvent;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.Render2DEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.event.events.TotemPopEvent;
import com.esoterik.client.event.events.UpdateWalkingPlayerEvent;
import com.esoterik.client.features.Feature;
import com.esoterik.client.features.command.Command;
import com.esoterik.client.features.modules.client.Managers;
import com.esoterik.client.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class EventManager
extends Feature {
    private final Timer timer = new Timer();
    private final Timer logoutTimer = new Timer();
    private AtomicBoolean tickOngoing = new AtomicBoolean(false);

    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!EventManager.fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals((Object)EventManager.mc.field_71439_g)) {
            esohack.potionManager.update();
            esohack.totemPopManager.onUpdate();
            esohack.inventoryManager.update();
            esohack.holeManager.update();
            esohack.moduleManager.onUpdate();
            esohack.timerManager.update();
            if (this.timer.passedMs(Managers.getInstance().moduleListUpdates)) {
                esohack.moduleManager.sortModules(true);
                this.timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        esohack.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        esohack.moduleManager.onLogout();
        esohack.totemPopManager.onLogout();
        esohack.potionManager.onLogout();
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        esohack.moduleManager.onTick();
        for (EntityPlayer player : EventManager.mc.field_71441_e.field_73010_i) {
            if (player == null || player.func_110143_aJ() > 0.0f) continue;
            MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
            esohack.totemPopManager.onDeath(player);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            esohack.speedManager.updateValues();
            esohack.rotationManager.updateRotations();
            esohack.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            esohack.rotationManager.restoreRotations();
            esohack.positionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (event.getStage() != 0) {
            return;
        }
        esohack.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).func_149160_c() == 35 && packet.func_149161_a((World)EventManager.mc.field_71441_e) instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)EventManager.mc.field_71441_e);
            MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
            esohack.totemPopManager.onTotemPop(player);
            esohack.potionManager.onTotemPop(player);
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && !EventManager.fullNullCheck() && this.logoutTimer.passedS(1.0) && !SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)(packet = (SPacketPlayerListItem)event.getPacket()).func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.func_179768_b())) {
            return;
        }
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            esohack.serverManager.update();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.field_71424_I.func_76320_a("client");
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        GlStateManager.func_179097_i();
        GlStateManager.func_187441_d((float)1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        esohack.moduleManager.onRender3D(render3dEvent);
        GlStateManager.func_187441_d((float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        EventManager.mc.field_71424_I.func_76319_b();
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            esohack.textManager.updateResolution();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            esohack.moduleManager.onRender2D(render2DEvent);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            esohack.moduleManager.onKeyPressed(Keyboard.getEventKey());
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    esohack.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage("\u00a7cAn error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }
}

