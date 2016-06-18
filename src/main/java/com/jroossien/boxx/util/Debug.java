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

package com.jroossien.boxx.util;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.options.single.*;
import com.jroossien.boxx.util.cuboid.Cuboid;
import com.jroossien.boxx.util.entity.EEntity;
import com.jroossien.boxx.util.entity.EntityStack;
import com.jroossien.boxx.util.item.EItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Quick and simple way to send debug messages.
 * Debug messages can be logged, broadcasted or send to specific players.
 */
public class Debug {

    /**
     * Log the debug message to console.
     *
     * @param objects The object(s) to debug. (Can also be just a string or multiple strings)
     */
    public static  void log(String prefix, Object... objects) {
        Boxx.get().log(getDebugMsg(objects));
    }

    /**
     * Broadcast the debug message to the entire server.
     *
     * @param objects The object(s) to debug. (Can also be just a string or multiple strings)
     */
    public static void bc(Object... objects) {
        Boxx.get().getServer().broadcastMessage(getDebugMsg(objects));
    }

    /**
     * Send the debug message to the specified player.
     *
     * @param objects The object(s) to debug. (Can also be just a string or multiple strings)
     */
    public static void send(Player player, Object... objects) {
        player.sendMessage(getDebugMsg(objects));
    }

    /**
     * Send the debug message to the specified players.
     *
     * @param objects The object(s) to debug. (Can also be just a string or multiple strings)
     */
    public static void send(Player[] players, Object... objects) {
        for (Player player : players) {
            player.sendMessage(getDebugMsg(objects));
        }
    }

    /**
     * Send the debug message to the specified players.
     *
     * @param objects The object(s) to debug. (Can also be just a string or multiple strings)
     */
    public static void send(Collection<Player> players, Object... objects) {
        for (Player player : players) {
            player.sendMessage(getDebugMsg(objects));
        }
    }



    private static String getDebugMsg(Object... objects) {
        if (objects == null || objects.length == 0) {
            return Str.color("&7[&8DEBUG&7] &4No value");

        }

        List<String> strings = new ArrayList<>();
        for (Object obj : objects) {
            strings.add(objToString(obj));
        }

        return Str.color("&7[&8DEBUG&7] &f" + Str.implode(strings, " "));
    }

    public static String objToString(Object obj) {
        if (obj == null) {
            return "&4null";
        }

        if (obj instanceof String) {
            return obj.toString().isEmpty() ? "&8'&7empty str&8'" : obj.toString();
        }

        List<String> strings = new ArrayList<>();
        if (obj instanceof Collection) {
            for (Object key : ((Collection)obj)) {
                strings.add(objToString(key));
            }
            return "Collection[" + ((Collection)obj).size() + "]<" + Str.implode(strings, ", ") + ">";
        } else if (obj instanceof Map) {
            for (Object key : ((Map)obj).keySet()) {
                strings.add(objToString(key) + ":" + objToString(((Map)obj).get(key)));
            }
            return "Map[" + ((Map)obj).size() + "]<" + Str.implode(strings, ", ") + ">";
        } else if (obj instanceof Block) {
            return BlockO.display((Block)obj);
        } else if (obj instanceof Color) {
            return ColorO.display((Color)obj);
        } else if (obj instanceof Cuboid) {
            return CuboidO.display((Cuboid)obj);
        } else if (obj instanceof Enchant) {
            return EnchantO.display((Enchant)obj);
        } else if (obj instanceof Entity) {
            return EntityO.serialize(new EEntity((Entity)obj));
        } else if (obj instanceof EEntity) {
            return EntityO.serialize((EEntity)obj);
        } else if (obj instanceof EntityStack) {
            return EntityStackO.serialize((EntityStack)obj);
        } else if (obj instanceof FireworkEffect) {
            return FireworkO.display((FireworkEffect)obj);
        } else if (obj instanceof ItemStack) {
            return ItemO.serialize(new EItem((ItemStack)obj));
        } else if (obj instanceof Location) {
            return LocationO.display((Location)obj);
        } else if (obj instanceof MaterialData) {
            return MaterialO.display((MaterialData)obj);
        } else if (obj instanceof Player) {
            return PlayerO.display((Player)obj);
        } else if (obj instanceof OfflinePlayer) {
            return OfflinePlayerO.display((OfflinePlayer)obj);
        } else if (obj instanceof ParticleEffect) {
            return ParticleO.serialize((ParticleEffect)obj);
        } else if (obj instanceof PotionEffect) {
            return PotionO.display((PotionEffect)obj);
        } else if (obj instanceof org.bukkit.util.Vector) {
            return VectorO.display((org.bukkit.util.Vector)obj);
        } else if (obj instanceof World) {
            return WorldO.display((World)obj);
        } else if (obj instanceof Integer) {
            return IntO.display((Integer)obj);
        } else if (obj instanceof DoubleO) {
            return DoubleO.display((Double)obj);
        } else if (obj instanceof Float) {
            return DoubleO.display(Double.valueOf((Float)obj));
        } else if (obj instanceof Boolean) {
            return BoolO.display((Boolean)obj);
        }

        return obj.toString();
    }

}
