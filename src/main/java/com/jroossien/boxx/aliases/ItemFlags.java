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

package com.jroossien.boxx.aliases;

import com.jroossien.boxx.aliases.internal.AliasMap;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ItemFlags extends AliasMap<ItemFlag> {

    private ItemFlags() {
        super("ItemFlags", new File(ALIASES_FOLDER, "ItemFlags.yml"), "itemflag", "hideflags", "hideflag");
    }

    @Override
    public void onLoad() {
        add(ItemFlag.HIDE_ENCHANTS, "Enchants", "Enchantments", "Enchantment", "Enchant", "Ench", "E");
        add(ItemFlag.HIDE_ATTRIBUTES, "Attributes", "Attribute", "Attr", "Att", "A");
        add(ItemFlag.HIDE_UNBREAKABLE, "Unbreakable", "Unbreak", "Unbr", "Unb", "UB", "U");
        add(ItemFlag.HIDE_DESTROYS, "Destroys", "Destroy", "Dest", "Des", "D");
        add(ItemFlag.HIDE_PLACED_ON, "Placed On", "Place On", "PlaceO", "POn", "PO", "Placed", "Place");
        add(ItemFlag.HIDE_POTION_EFFECTS, "Potion Effects", "Potion Effect", "PEffects", "PEffect", "PotionE", "Potions", "Effects", "Potion", "Effect", "Eff", "P", "PE");
    }

    public static ItemFlag get(String string) {
        return instance()._get(string);
    }

    public static String getName(ItemFlag key) {
        return instance()._getName(key);
    }

    public static String getDisplayName(ItemFlag key) {
        return instance()._getDisplayName(key);
    }

    public static List<String> getAliases(ItemFlag key) {
        return instance()._getAliases(key);
    }

    public static Map<String, List<String>> getAliasMap() {
        return instance()._getAliasMap();
    }

    public static ItemFlags instance() {
        if (getMap(ItemFlags.class) == null) {
            aliasMaps.put(ItemFlags.class, new ItemFlags());
        }
        return (ItemFlags)getMap(ItemFlags.class);
    }
}
