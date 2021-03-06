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

package com.jroossien.boxx.options.map;

import com.jroossien.boxx.options.MapOption;
import com.jroossien.boxx.options.single.CuboidO;
import com.jroossien.boxx.util.cuboid.Cuboid;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class CuboidMO extends MapOption<Cuboid, CuboidMO, CuboidO> {

    public Map<String, Cuboid> getValues(World world) {
        Map<String, Cuboid> values = new HashMap<>();
        for (String key : this.values.keySet()) {
            values.put(key, getValue(key, world));
        }
        return values;
    }

    public Cuboid getValue(String key, World world) {
        Cuboid c = getValue(key);
        if (c == null || world == null) {
            return c;
        }
        c.setWorld(world);
        return c;
    }

    @Override
    public CuboidO getSingleOption() {
        return new CuboidO();
    }

    @Override
    public CuboidMO clone() {
        return cloneData(new CuboidMO());
    }
}
