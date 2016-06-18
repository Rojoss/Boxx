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

package com.jroossien.boxx.options;

import com.jroossien.boxx.options.single.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a map with all the different {@link SingleOption} types.
 */
public class Options {

    private static Map<String, SingleOption> options = new HashMap<>();

    static {
        // Register all the build in gameboxx options.
        register(new BlockO());
        register(new BoolO());
        register(new ColorO());
        register(new CuboidO());
        register(new DoubleO());
        register(new EnchantO());
        register(new EntityO());
        register(new EntityStackO());
        register(new FireworkO());
        register(new IntO());
        register(new ItemO());
        register(new LocationO());
        register(new MaterialO());
        register(new OfflinePlayerO());
        register(new ParticleO());
        register(new PlayerO());
        register(new PotionO());
        register(new StringO());
        register(new VectorO());
        register(new WorldO());
    }

    /**
     * Register an option so that it can be referenced by it's type throughout the API.
     *
     * @param option A instance of the option that needs to be registered. (will be used for creating clones and such)
     * @return true if the option has been registered and false if it was already registered.
     */
    public static boolean register(SingleOption option) {
        if (options.containsKey(option.getTypeName().toLowerCase())) {
            return false;
        }
        options.put(option.getTypeName().toLowerCase(), option);
        return true;
    }

    /**
     * Unregister an option by it's option type string.
     *
     * @param optionType The type of option that should be unregistered.
     * @return true if the option has been unregistered and false if it wasn't found.
     */
    public static boolean unregister(String optionType) {
        if (!options.containsKey(optionType.toLowerCase())) {
            return false;
        }
        options.remove(optionType.toLowerCase());
        return true;
    }

    /**
     * Get an array with all the different options that have been registered.
     *
     * NOTE: If you're gonna use these options make sure to clone them before you do so!
     *
     * @return An array with all the registered option instances.
     */
    public static SingleOption[] getOptions() {
        return options.values().toArray(new SingleOption[options.size()]);
    }

    /**
     * Get an array of all the different option types that have been registered.
     *
     * @return Array with option types as strings.
     */
    public static String[] getOptionTypes() {
        return options.keySet().toArray(new String[options.size()]);
    }

    /**
     * Try to get a {@link SingleOption} with the specified type.
     * If no option for the type is found null will be returned.
     *
     * @param type The type of the option to retrieve.
     * @return A clone of the {@link SingleOption} for the specified type.
     */
    public SingleOption getOption(String type) {
        if (!options.containsKey(type.toLowerCase())) {
            return null;
        }
        return (SingleOption)options.get(type.toLowerCase()).clone();
    }
}
