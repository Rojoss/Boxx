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

package com.jroossien.boxx.input.internal;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.input.*;

import java.util.HashMap;
import java.util.Map;

public class InputManager {

    private Boxx boxx;
    private static Map<Class<? extends Input>, Input> inputClasses = new HashMap<>();

    public InputManager(Boxx boxx) {
        this.boxx = boxx;

        registerInputHandler(new BlockInput());
        registerInputHandler(new ChatInput());
        registerInputHandler(new EntityInput());
        registerInputHandler(new ItemInput());
    }

    public void registerInputHandler(Input inputClass) {
        if (inputClasses.containsKey(inputClass.getClass())) {
            return;
        }
        inputClasses.put(inputClass.getClass(), inputClass);
        boxx.getServer().getPluginManager().registerEvents(inputClass, boxx);
    }

    public static Input getInput(Class<? extends Input> type) {
        return inputClasses.get(type);
    }
}
