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

package com.jroossien.boxx.messages;

import com.jroossien.boxx.util.Debug;
import com.jroossien.boxx.util.Utils;

/**
 * Parameter used for messages.
 * The parameter key will be replaced with the value in the message.
 * <p/>
 * <p>For example for the string 'Teleported to &lt;x&gt; &lt;y&gt; &lt;z&gt;'
 * You would set three parameters like {@code Param.P("x", loc.getX())} etc.
 * <p/>
 * <b>Notice how there are no angle brackets around the name for params!</b>
 */
public class Param {

    private final String param;
    private final Object value;

    /**
     * Construct a new parameter.
     *
     * @param param The name of the parameter.
     * The name should not contain the angle brackets (&lt;&gt;).
     * @param value The value of the paramter.
     * This will be put instead of the param name.
     */
    public Param(String param, Object value) {
        this.param = param;
        this.value = value;
    }

    /**
     * Get the parameter name.
     *
     * @return The name of the parameter.
     */
    public String getParam() {
        return param;
    }

    /**
     * Get the parameter value.
     *
     * @return The value of the parameter.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Get the string value of the parameter.
     * This uses {@link Utils#getString(Object)}
     *
     * @return The string value of the parameter.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Shortcut for creating a parameter.
     *
     * @param param The name of the parameter.
     * The name should not contain the angle brackets (&lt;&gt;).
     * @param value The value of the paramter.
     * This will be put instead of the param name.
     * @return new {@link Param} instance with the specified param name and value.
     * @see {@link #Param(String, Object)}
     */
    public static Param P(String param, Object value) {
        return new Param(param, value);
    }
}
