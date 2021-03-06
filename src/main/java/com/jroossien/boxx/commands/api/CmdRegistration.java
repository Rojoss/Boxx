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

package com.jroossien.boxx.commands.api;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.commands.api.data.Argument;
import com.jroossien.boxx.commands.api.exception.CmdAlreadyRegisteredException;
import com.jroossien.boxx.commands.api.parse.SubCmdO;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Command registration.
 * <p/>
 * Call the {@link #register(Plugin, BaseCmd)} method for all custom base commands.
 * Sub commands don't have to be registered.
 * <p/>
 * Don't forget to call {@link #unregister(Plugin)} in onDisable to cleanup commands from your plugin.
 */
public class CmdRegistration {

    private static Map<Class<? extends Plugin>, Set<BaseCmd>> commands = new HashMap<>();

    /**
     * Registers the specified custom {@link BaseCmd}.
     *
     * @param plugin the plugin that owns the command.
     *               Register commands with your own plugin and not as {@link Boxx}
     * @param command The {@link BaseCmd} instance that needs to be registered.
     * @throws CmdAlreadyRegisteredException when the command is already registered.
     *                                       This is not thrown when a command with the same name is registered.
     *                                       It will only be thrown when you try to register the same command instance twice for example if you forgot to call {@link #unregister(Plugin)} on disable.
     * @throws NullPointerException when the command has a sub command option without sub commands.
     */
    public static void register(Plugin plugin, BaseCmd command) throws CmdAlreadyRegisteredException, NullPointerException {
        Set<BaseCmd> pluginCommands = commands.get(plugin.getClass());
        if (pluginCommands == null) {
            pluginCommands = new HashSet<>();
        }
        if (!pluginCommands.add(command)) {
            throw new CmdAlreadyRegisteredException(plugin, command);
        }

        command.register(plugin);
        for (Argument arg : command.getArguments().values()) {
            if (arg.option() instanceof SubCmdO) {
                SubCmd[] subs = ((SubCmdO)arg.option()).getSubCmds();
                if (subs == null || subs.length < 1) {
                    throw new NullPointerException("No sub commands registered for the '" + arg.name() + "' sub command option in the '" + command.getName() + "' command.");
                }
                for (SubCmd sub : subs) {
                    sub.register(plugin);
                }
                break;
            }
        }

        command.load();

        commands.put(plugin.getClass(), pluginCommands);
    }


    /**
     * Remove all the commands for the specified {@link Plugin}
     * <p/>
     * Call this on disable for cleanup.
     *
     * @param plugin The owning plugin.
     */
    public static void unregister(Plugin plugin) {
        commands.remove(plugin.getClass());
    }


    /**
     * Get an immutable set of registered base commands from the specified plugin.
     *
     * @param plugin The {@link Plugin} to get the commands from.
     * @return Immutable set with base commands from the specified plugin. (May be {@code null} if the plugin hasn't registered any commands)
     */
    public static Set<BaseCmd> getCommands(Plugin plugin) {
        if (!commands.containsKey(plugin.getClass())) {
            return null;
        }
        return ImmutableSet.copyOf(commands.get(plugin.getClass()));
    }

    /**
     * Get an immutable set of registered base commands from the specified plugin.
     *
     * @param plugin The {@link Plugin} class to get the commands from.
     * @return Immutable set with base commands from the specified plugin. (May be {@code null} if the plugin hasn't registered any commands)
     */
    public static Set<BaseCmd> getCommands(Class<? extends Plugin> plugin) {
        if (!commands.containsKey(plugin)) {
            return null;
        }
        return ImmutableSet.copyOf(commands.get(plugin));
    }

    /**
     * Get an immutable map with registered base commands from all plugins.
     * <p/>
     * This map contains all the custom commands from all plugins that use the command system.
     *
     * @return Immutable map with commands from all plugins where the key is the main class of the plugin and the value is the set with registered commands.
     */
    public static Map<Class<? extends Plugin>, Set<BaseCmd>> getCommands() {
        return ImmutableMap.copyOf(commands);
    }

}
