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

import com.jroossien.boxx.aliases.Particles;
import com.jroossien.boxx.messages.Msg;
import com.jroossien.boxx.messages.Param;
import com.jroossien.boxx.options.SingleOption;
import com.jroossien.boxx.util.Parse;
import com.jroossien.boxx.util.ParticleEffect;
import com.jroossien.boxx.util.Utils;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleO extends SingleOption<ParticleEffect, ParticleO> {

    @Override
    public boolean parse(CommandSender sender, String input) {
        String[] split = input.split(":");

        Integer amount = 1;
        Double speed = 1d;
        Vector offset = new Vector(0,0,0);
        Object data = null;

        Particle particle = Particles.get(split[0]);
        if (particle == null) {
            error = Msg.getString("particle.invalid-type", Param.P("input", split[0]), Param.P("types", Utils.getAliasesString("particle.entry", Particles.getAliasMap())));
            return false;
        }

        if (split.length > 1 && !split[1].isEmpty()) {
            amount = Parse.Int(split[1]);
            if (amount == null) {
                error = Msg.getString("particle.invalid-amount", Param.P("input", split[1]));
                return false;
            }
        }

        if (split.length > 2 && !split[2].isEmpty()) {
            speed = Parse.Double(split[2]);
            if (speed == null) {
                error = Msg.getString("particle.invalid-speed", Param.P("input", split[2]));
                return false;
            }
        }

        if (split.length > 3 && !split[3].isEmpty()) {
            VectorO vectorOption = new VectorO();
            if (!vectorOption.parse(split[3])) {
                error = vectorOption.getError();
                return false;
            }
            offset = vectorOption.getValue();
        }

        if (split.length > 4 && !split[4].isEmpty()) {
            if (particle.getDataType() == null) {
                error = Msg.getString("particle.invalid-data", Param.P("input", split[5]));
                return false;
            }
            if (particle.getDataType().equals(MaterialData.class)) {
                MaterialO materialOption = new MaterialO();
                if (!materialOption.parse(split[4])) {
                    error = materialOption.getError();
                    return false;
                }
                data = materialOption.getValue();
            } else if (particle.getDataType().equals(ItemStack.class)) {
                ItemO itemOption = new ItemO();
                if (!itemOption.parse(split[4])) {
                    error = itemOption.getError();
                    return false;
                }
                data = (ItemStack)itemOption.getValue();
            }
            if (data == null) {
                error = Msg.getString("particle.invalid-data", Param.P("input", split[5]));
                return false;
            }
        }

        value = new ParticleEffect(particle, amount, offset.getX(), offset.getY(), offset.getZ(), speed, data);
        return true;
    }

    @Override
    public String serialize() {
        return serialize(getValue());
    }

    public static String serialize(ParticleEffect particle) {
        String data = "";
        if (particle.hasData()) {
            if (particle.getData() instanceof MaterialData) {
                data = "," + MaterialO.serialize((MaterialData)particle.getData());
            } else if (particle.getData() instanceof ItemStack) {
                data = "," + MaterialO.serialize(((ItemStack)particle.getData()).getData());
            }
        }
        return particle == null ? null : particle.getParticle().toString() + ":" + particle.getAmount() + "," + particle.getSpeed() + "," +
                particle.getOffsetX() + "," + particle.getOffsetY() + "," + particle.getOffsetZ() + data;
    }

    @Override
    public String getTypeName() {
        return "Particle";
    }

    @Override
    public List<String> onComplete(CommandSender sender, String prefix, String input) {
        List<String> suggestions = new ArrayList<>();

        String[] data = input.split(":", -1);
        if (data.length <= 1) {
            for (String key : Particles.getAliasMap().keySet()) {
                String name = key.replace(" ", "");
                if (name.toLowerCase().startsWith(data[0].toLowerCase())) {
                    suggestions.add(prefix + name);
                }
            }
            if (data[0].length() > 0) {
                for (List<String> aliases : Particles.getAliasMap().values()) {
                    for (String alias : aliases) {
                        String name = alias.replace(" ", "");
                        if (name.toLowerCase().startsWith(data[0].toLowerCase())) {
                            suggestions.add(prefix + name);
                        }
                    }
                }
            }
        } else if (data.length <= 2) {
            int[] amounts = new int[] {0, 1, 10, 20, 30, 40, 50, 100};
            for (int amount : amounts) {
                if (Integer.toString(amount).startsWith(data[1].toLowerCase())) {
                    suggestions.add(prefix + data[0] + ":" + amount);
                }
            }
        } else if (data.length <= 3) {
            double[] speeds = new double[] {0, 0.1d, 0.5d, 1};
            for (double speed : speeds) {
                if (Double.toString(speed).startsWith(data[2].toLowerCase())) {
                    suggestions.add(prefix + data[0] + ":" + data[1] + ":" + speed);
                }
            }
        } else if (data.length <= 4) {
            String[] vectors = new String[] {"0,0,0", "0.1,0.1,0.1", "0.5,0.5,0.5", "1,1,1", "2,2,2"};
            for (String vector : vectors) {
                if (vector.startsWith(data[3].toLowerCase())) {
                    suggestions.add(prefix + data[0] + ":" + data[1] + ":" + data[2] + ":" + vector);
                }
            }
        } else if (data.length <= 5) {
            Particle particle = Particles.get(data[0]);
            if (particle != null && particle.getDataType() != null) {
                if (particle.getDataType().equals(MaterialData.class) || particle.getDataType().equals(ItemStack.class)) {
                    MaterialO material = new MaterialO().blocks(particle.getDataType().equals(MaterialData.class));
                    suggestions.addAll(material.onComplete(sender, prefix + data[0] + ":" + data[1] + ":" + data[2] + ":" + data[3] + ":", data[4]));
                }
            }
        }

        Collections.sort(suggestions);
        return suggestions;
    }

    @Override
    public ParticleO clone() {
        return super.cloneData(new ParticleO());
    }
}
