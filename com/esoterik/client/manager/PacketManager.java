/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package com.esoterik.client.manager;

import com.esoterik.client.features.Feature;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.Packet;

public class PacketManager
extends Feature {
    private final List<Packet<?>> noEventPackets = new ArrayList();

    public void sendPacketNoEvent(Packet<?> packet) {
        if (packet != null && !PacketManager.nullCheck()) {
            this.noEventPackets.add(packet);
            PacketManager.mc.field_71439_g.field_71174_a.func_147297_a(packet);
        }
    }

    public boolean shouldSendPacket(Packet<?> packet) {
        if (this.noEventPackets.contains(packet)) {
            this.noEventPackets.remove(packet);
            return false;
        }
        return true;
    }
}

