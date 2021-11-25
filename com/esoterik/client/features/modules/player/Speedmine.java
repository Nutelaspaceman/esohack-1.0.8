/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.esoterik.client.features.modules.player;

import com.esoterik.client.esohack;
import com.esoterik.client.event.events.BlockEvent;
import com.esoterik.client.event.events.PacketEvent;
import com.esoterik.client.event.events.Render3DEvent;
import com.esoterik.client.features.modules.Module;
import com.esoterik.client.features.modules.client.Colors;
import com.esoterik.client.features.setting.Setting;
import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.RenderUtil;
import com.esoterik.client.util.Timer;
import java.awt.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speedmine
extends Module {
    public Setting<Boolean> tweaks = this.register(new Setting<Boolean>("Tweaks", true));
    public Setting<Boolean> reset = this.register(new Setting<Boolean>("Reset", true));
    public Setting<Boolean> noBreakAnim = this.register(new Setting<Boolean>("NoBreakAnim", false));
    public Setting<Boolean> noDelay = this.register(new Setting<Boolean>("NoDelay", false));
    public Setting<Boolean> noSwing = this.register(new Setting<Boolean>("NoSwing", false));
    public Setting<Boolean> noTrace = this.register(new Setting<Boolean>("NoTrace", false));
    public Setting<Boolean> allow = this.register(new Setting<Boolean>("AllowMultiTask", false));
    public Setting<Boolean> pickaxe = this.register(new Setting<Object>("Pickaxe", Boolean.valueOf(true), v -> this.noTrace.getValue()));
    public Setting<Boolean> doubleBreak = this.register(new Setting<Boolean>("DoubleBreak", false));
    public Setting<Boolean> render = this.register(new Setting<Boolean>("Render", false));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(false), v -> this.render.getValue()));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.render.getValue()));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> this.box.getValue() != false && this.render.getValue() != false));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue() != false && this.render.getValue() != false));
    private static Speedmine INSTANCE = new Speedmine();
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private final Timer timer = new Timer();
    private boolean isMining = false;
    private BlockPos lastPos = null;
    private EnumFacing lastFacing = null;

    public Speedmine() {
        super("Speedmine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Speedmine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    @Override
    public void onTick() {
        if (!(this.currentPos == null || Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).equals((Object)this.currentBlockState) && Speedmine.mc.field_71441_e.func_180495_p(this.currentPos).func_177230_c() != Blocks.field_150350_a)) {
            this.currentPos = null;
            this.currentBlockState = null;
        }
    }

    @Override
    public void onUpdate() {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue().booleanValue()) {
            Speedmine.mc.field_71442_b.field_78781_i = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue().booleanValue()) {
            Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue().booleanValue() && Speedmine.mc.field_71474_y.field_74313_G.func_151470_d() && !this.allow.getValue().booleanValue()) {
            Speedmine.mc.field_71442_b.field_78778_j = false;
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.render.getValue().booleanValue() && this.currentPos != null) {
            Color color = new Color(255, 255, 255, 255);
            Color readyColor = Colors.INSTANCE.isEnabled() ? Colors.INSTANCE.getCurrentColor() : new Color(125, 105, 255, 255);
            RenderUtil.drawBoxESP(this.currentPos, this.timer.passedMs((int)(2000.0f * esohack.serverManager.getTpsFactor())) ? readyColor : color, false, color, this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            CPacketPlayerDigging packet;
            if (this.noSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            if (this.noBreakAnim.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()) != null && packet.func_179715_a() != null) {
                try {
                    for (Entity entity : Speedmine.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(packet.func_179715_a()))) {
                        if (!(entity instanceof EntityEnderCrystal)) continue;
                        this.showAnimation();
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.func_179715_a(), packet.func_179714_b());
                }
                if (packet.func_180762_c().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && this.reset.getValue().booleanValue() && Speedmine.mc.field_71442_b.field_78770_f > 0.1f) {
            Speedmine.mc.field_71442_b.field_78778_j = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue().booleanValue()) {
            BlockPos above;
            if (BlockUtil.canBreak(event.pos)) {
                if (this.currentPos == null) {
                    this.currentPos = event.pos;
                    this.currentBlockState = Speedmine.mc.field_71441_e.func_180495_p(this.currentPos);
                    this.timer.reset();
                }
                Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                event.setCanceled(true);
            }
            if (this.doubleBreak.getValue().booleanValue() && BlockUtil.canBreak(above = event.pos.func_177982_a(0, 1, 0)) && Speedmine.mc.field_71439_g.func_70011_f((double)above.func_177958_n(), (double)above.func_177956_o(), (double)above.func_177952_p()) <= 5.0) {
                Speedmine.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.facing));
                Speedmine.mc.field_71442_b.func_187103_a(above);
                Speedmine.mc.field_71441_e.func_175698_g(above);
            }
        }
    }

    private void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }

    public void showAnimation() {
        this.showAnimation(false, null, null);
    }

    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}

