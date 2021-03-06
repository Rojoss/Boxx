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

package com.jroossien.boxx.options.single;

import com.jroossien.boxx.options.SingleOption;
import com.jroossien.boxx.util.entity.EntityParser;
import com.jroossien.boxx.util.entity.EntityStack;
import org.bukkit.command.CommandSender;

public class EntityStackO extends SingleOption<EntityStack, EntityStackO> {

    private Integer maxEntities = null;
    private boolean allowStacked = true;

    public EntityStackO max(Integer maxEntities) {
        this.maxEntities = maxEntities;
        return this;
    }

    public EntityStackO allowStacked(boolean allowStacked) {
        this.allowStacked = allowStacked;
        return this;
    }

    @Override
    public boolean parse(CommandSender sender, String input) {
        EntityParser parser = new EntityParser(input, sender, false, maxEntities, allowStacked);
        if (!parser.isValid()) {
            error = parser.getError();
            return false;
        }
        value = parser.getEntities();
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(EntityStack entityStack) {
        return entityStack == null ? null : new EntityParser(entityStack.getBottom()).getString();
    }

    @Override
    public String getTypeName() {
        return "EntityStack";
    }

    @Override
    public EntityStackO clone() {
        return super.cloneData(new EntityStackO().max(maxEntities).allowStacked(allowStacked));
    }
}
