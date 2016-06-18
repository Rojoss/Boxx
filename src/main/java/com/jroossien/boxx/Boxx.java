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

package com.jroossien.boxx;

import com.jroossien.boxx.commands.BoxxCmd;
import com.jroossien.boxx.commands.api.CmdRegistration;
import com.jroossien.boxx.commands.api.exception.CmdAlreadyRegisteredException;
import com.jroossien.boxx.config.PluginCfg;
import com.jroossien.boxx.menu.Menu;
import com.jroossien.boxx.messages.Language;
import com.jroossien.boxx.messages.MessageConfig;
import com.jroossien.boxx.nms.NMS;
import com.jroossien.boxx.nms.NMSVersion;
import com.jroossien.boxx.util.Parse;
import com.jroossien.boxx.util.cuboid.Cuboid;
import com.jroossien.boxx.util.cuboid.SelectionManager;
import com.jroossien.boxx.util.entity.EntityTag;
import com.jroossien.boxx.util.item.GlowEnchant;
import com.jroossien.boxx.util.item.ItemTag;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Boxx extends JavaPlugin {

    private static Boxx instance;
    private Vault vault;
    private Economy economy;

    private Language language = null;

    private SelectionManager sm;

    private PluginCfg cfg;

    private final Logger log = Logger.getLogger("GameBoxx");

    @Override
    public void onDisable() {
        CmdRegistration.unregister(this);
        GlowEnchant.unregister();
        instance = null;
        log("disabled");
    }

    @Override
    public void onEnable() {
        instance = this;
        log.setParent(this.getLogger());

        if (!NMS.get().isCompatible()) {
            error("Failed to load GameBoxx because your server version isn't supported!");
            error("This version of GameBoxx supports the following server versions: " + Parse.Array((Object[]) NMSVersion.values()));
            error("Your server version: " + NMS.get().getVersionString());
            getPluginLoader().disablePlugin(this);
            return;
        }

        if (!loadEconomy()) {
            warn("Failed to load Economy from Vault!");
            warn("Everything will work just fine except features that use economy like shops.");
        }

        cfg = new PluginCfg("plugins/GameBoxx/GameBoxx.yml");

        if (!setupLanguage()) {
            warn("Invalid language specified in the config. Falling back to " + language.getName() + " [" + language.getID() + "]!");
        } else {
            log("Using " + language.getName() + " [" + language.getID() + "] as language!");
        }
        loadMessages();

        ItemTag.registerDefaults();
        EntityTag.registerDefaults();

        sm = new SelectionManager();

        registerCommands();
        registerListeners();

        log("loaded successfully");
    }

    private void registerCommands() {
        File configFile = new File(getDataFolder(), "commands.yml");
        try {
            CmdRegistration.register(this, new BoxxCmd(configFile));
        } catch (CmdAlreadyRegisteredException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Menu.Events(), this);
        getServer().getPluginManager().registerEvents(sm.getListener(), this);
    }

    public boolean setupLanguage() {
        language = Language.find(getCfg().language);
        if (language == null) {
            language = Language.ENGLISH;
            return false;
        }
        return true;
    }

    private void loadMessages() {
        new MessageConfig(this, "messages");
        new MessageConfig(this, "commands");
        new MessageConfig(this, "parser");
    }

    private boolean loadEconomy() {
        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin != null) {
            vault = (Vault) vaultPlugin;
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
        return economy != null;
    }

    public void log(Object msg) {
        log.info("[Boxx] " + msg.toString());
    }

    public void warn(Object msg) {
        log.warning("[Boxx] " + msg.toString());
    }

    public void error(Object msg) {
        log.severe("[Boxx] " + msg.toString());
    }

    public static Boxx get() {
        return instance;
    }

    public Vault getVault() {
        return vault;
    }

    public Economy getEco() {
        return economy;
    }

    /**
     * Get the language used for messages.
     * If there was an error loading language files it will use the fall back english.
     *
     * @return active language used for messages.
     */
    public Language getLanguage() {
        if (language == null) {
            return Language.ENGLISH;
        }
        return language;
    }


    /**
     * Get the {@link SelectionManager} for getting {@link Cuboid} selections and such.
     *
     * @return The {@link SelectionManager}
     */
    public SelectionManager getSM() {
        return sm;
    }

    public PluginCfg getCfg() {
        return cfg;
    }
}
