/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Rojoss <http://jroossien.com>
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jroossien.boxx.nms.sign;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.nms.util.NMSUtil_V1_10_R1;
import com.jroossien.boxx.util.Reflection;
import com.jroossien.boxx.util.protocol.TinyProtocol;
import io.netty.channel.Channel;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenSignEditor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignGUI_V1_10_R1 implements SignGUI {

    private TinyProtocol protocol;
    private Reflection.FieldAccessor<String[]> SIGN_LINES = Reflection.getField("{nms}.PacketPlayInUpdateSign", String[].class, 0);
    private Reflection.FieldAccessor<BlockPosition> SIGN_LOCATION = Reflection.getField("{nms}.PacketPlayInUpdateSign", BlockPosition.class, 0);

    private Map<UUID, SignGUICallback> callbacks = new HashMap<>();

    public SignGUI_V1_10_R1() {
        protocol = new TinyProtocol(Boxx.get()) {
            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                if (SIGN_LINES.hasField(packet) && SIGN_LOCATION.hasField(packet)) {
                    BlockPosition loc = SIGN_LOCATION.get(packet);
                    if (loc.getX() == 0 && loc.getY() == 0 && loc.getZ() == 0) {
                        onEdit(sender.getUniqueId(), SIGN_LINES.get(packet));
                        return null;
                    }
                }
                return super.onPacketInAsync(sender, channel, packet);
            }
        };
    }

    @Override
    public void show(Player player, SignGUICallback callback) {
        callbacks.put(player.getUniqueId(), callback);

        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(new BlockPosition(0,0,0));
        NMSUtil_V1_10_R1.sendPacket(player, packet);
    }

    @Override
    public boolean onEdit(UUID uuid, String[] lines) {
        SignGUICallback callback = callbacks.get(uuid);
        if (callback == null) {
            return false;
        }
        callbacks.remove(uuid);
        callback.onEdit(lines);
        return true;
    }
}
