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
import com.jroossien.boxx.options.Options;
import com.jroossien.boxx.options.SingleOption;
import org.bukkit.entity.Player;

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
        } else {
            for (SingleOption option : Options.getOptions()) {
                SingleOption clone = (SingleOption)option.clone();
                if (clone.parse(obj) && clone.hasValue()) {
                    return clone.getDisplayValue();
                }
            }
        }

        return obj.toString();
    }

}
