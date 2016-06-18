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

package com.jroossien.boxx.nms;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.nms.annotation.NMSDependant;
import com.jroossien.boxx.nms.chat.Chat;
import com.jroossien.boxx.nms.entity.EntityUtils;
import com.jroossien.boxx.nms.item.ItemUtils;
import com.jroossien.boxx.nms.sign.SignGUI;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public class NMS {

    private static NMS instance = null;
    private String versionString = "unknown";
    private NMSVersion version;

    private Chat chat;
    private EntityUtils entityUtils;
    private ItemUtils itemUtils;
    private SignGUI signGUI;

    private NMS() {
        try {
            versionString = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

            version = NMSVersion.fromString(versionString);

            chat = (Chat) loadFromNMS(Chat.class);
            entityUtils = (EntityUtils) loadFromNMS(EntityUtils.class);
            itemUtils = (ItemUtils)loadFromNMS(ItemUtils.class);
            signGUI = (SignGUI)loadFromNMS(SignGUI.class);

        } catch (ArrayIndexOutOfBoundsException ignored) {}
    }

    public Chat getChat() {
        return chat;
    }

    public EntityUtils getEntityUtils() {
        return entityUtils;
    }

    public ItemUtils getItemUtils() {
        return itemUtils;
    }

    public SignGUI getSignGUI() {
        return signGUI;
    }


    public NMSVersion getVersion() {
        return version;
    }

    public String getVersionString() {
        return versionString;
    }

    public boolean isCompatible() {
        return version != null;
    }

    public static NMS get() {
        if (instance == null) {
            instance = new NMS();
        }
        return instance;
    }

    public <T> Object loadFromNMS(Class<T> dep) {
        if (!dep.isAnnotationPresent(NMSDependant.class)) return null;
        NMSDependant nmsDependant = dep.getAnnotation(NMSDependant.class);
        Class<?> impl = null;
        try {
            impl = Class.forName(nmsDependant.implementationPath() + "." + dep.getSimpleName() + "_" + version);
            return impl.newInstance();
        } catch (ClassNotFoundException e) {
            Boxx.get().error("The current version is not supported: " + version + ".\n" + e.getMessage());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return impl;
    }

    public <T> Object loadFromNMS(Class<T> dep, Object... objects) {
        if (!dep.isAnnotationPresent(NMSDependant.class)) return null;
        NMSDependant nmsDependant = dep.getAnnotation(NMSDependant.class);
        Class<?> impl = null;
        try {
            impl = Class.forName(nmsDependant.implementationPath() + "." + dep.getSimpleName() + "_" + version);
            return ConstructorUtils.invokeConstructor(impl, objects);
        } catch (ClassNotFoundException e) {
            Boxx.get().error("The current version is not supported: " + version + ".\n" + e.getMessage());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return impl;
    }

}
