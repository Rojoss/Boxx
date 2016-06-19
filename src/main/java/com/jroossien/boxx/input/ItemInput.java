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

package com.jroossien.boxx.input;

import com.jroossien.boxx.input.internal.*;
import com.jroossien.boxx.options.SingleOption;
import com.jroossien.boxx.util.item.EItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class ItemInput extends Input<EItem> {

    @EventHandler
    private void inventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (!inputMap.containsKey(player.getUniqueId())) {
            return;
        }
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        InputData inputData = inputMap.get(player.getUniqueId());

        EItem item = new EItem(event.getCurrentItem());
        if (inputData.getValidator() != null) {
            if (!inputData.getValidator().parse(item)) {
                player.sendMessage(inputData.getValidator().getError());
                event.setCancelled(true);
                return;
            }
        }

        inputData.getCallback().onSubmit(new EItem(event.getCurrentItem()));
        inputMap.remove(player.getUniqueId());
        event.setCancelled(true);
    }

    public static void get(UUID uuid, InputCallback<EItem> callback) {
        InputManager.getInput(ItemInput.class).create(uuid, callback);
    }

    public static void get(UUID uuid, SingleOption validator, InputCallback<EItem> callback) {
        InputManager.getInput(ItemInput.class).create(uuid, validator, callback);
    }
}
