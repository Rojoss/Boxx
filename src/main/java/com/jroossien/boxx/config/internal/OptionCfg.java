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

package com.jroossien.boxx.config.internal;

import com.jroossien.boxx.Boxx;
import com.jroossien.boxx.options.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * Configuration file for {@link Option} values.
 * Options have to be set with {@link #setOption(String, Option)}
 * The path is name the same as the name of the option.
 * Use {@link #save()} and {@link #load()} to save/load the added options.
 * It will not load options that haven't been set.
 */
public class OptionCfg {

    private File file = null;
    private YamlConfiguration config = new YamlConfiguration();

    private Long saveDelay = 0l;
    private Long lastSave = 0l;

    private Map<String, Option> options = new HashMap<>();

    /**
     * Create a new options config for the specified string file path.
     * The path must be absolute from the server directory like: 'plugins/GameBoxx/Example.yml'
     * Make sure you also put the file extension.
     * Directories and files get created automatically no need to manually check/do this.
     *
     * @param file The file for the config file. (see description)
     */
    public OptionCfg(String file) {
        setFile(file);
    }

    /**
     * Create a new options config for the specified file path.
     * Make sure you also put the file extension.
     * For example: {@code new File(plugin.getDataFolder(), "Example.yml")}
     * Directories and files get created automatically no need to manually check/do this.
     *
     * @param file the file for the config file. (see description)
     */
    public OptionCfg(File file) {
        setFile(file);
    }


    /**
     * Load the {@link YamlConfiguration} file.
     * This does not set the option values.
     * It just loads the yaml config from disk.
     */
    private void loadConfig() {
        createFile();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the {@link YamlConfiguration} file.
     * This does not set the option values in the config it just saves the config file from memory to disk.
     * If this config has a delay set and force is false it will first check the delay.
     *
     * @param force Whether or not to ignore the delay when the config has a delay.
     * @return Whether or not the config file was saved. (false when delayed or failed)
     */
    private boolean saveConfig(boolean force) {
        if (!force && saveDelay > 0 && lastSave + saveDelay > System.currentTimeMillis()) {
            return false;
        }
        lastSave = System.currentTimeMillis();
        createFile();
        try {
            config.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates the config file if it doesn't exist yet.
     * If there are exceptions the stacktrace will be printed.
     */
    private void createFile() {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        try {
            if (!file.exists()) {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Load all the options from config and save the defaults.
     *
     * @see #load(boolean)
     */
    public void load() {
        load(true);
    }

    /**
     * Load all the option values from config.
     * <b>This does not load options that haven't been added!</b>
     * If there are exceptions the stacktrace will be printed.
     *
     * @param save Whether or not to save the config after loading the values to save the defaults.
     * @return Whether or not the config file was saved. (false when save is false or it failed)
     */
    public boolean load(boolean save) {
        loadConfig();
        for (String path : options.keySet()) {
            loadOption(path, false);
        }
        if (save) {
            return saveConfig(true);
        }
        return false;
    }

    /**
     * Save all the option values to config.
     * If a delay is set it will check if the saving is still delayed and if so, it will not save.
     * Use {@link #save(boolean)} with force set to true to ignore the save delay and force save the config.
     *
     * @return Whether or not the config file was saved. (false when delayed or failed)
     */
    public boolean save() {
        return save(false);
    }

    /**
     * Save all the option values to config.
     * If a delay is set it will check if the saving is still delayed and if so, it will not save unless force is set to true.
     *
     * @param force Whether or not to ignore the delay when the config has a delay.
     * @return Whether or not the config file was saved. (false when delayed or failed)
     */
    public boolean save(boolean force) {
        for (String path : options.keySet()) {
            saveOption(path, false);
        }
        return saveConfig(force);
    }

    /**
     * Load the value from the specified path.
     * This will only load the option when the option has been added/registered with {@link #setOption(String, Option)}
     * The parse method from the option will be used to load the value.
     * <p/>
     * If there are errors when parsing the errors will be printed in the console.
     * It will keep the invalid options in the config but when retrieving the option value it will be the default value if there was an error.
     *
     * @param path the path in the config file to load the option value from.
     * @param loadConfig When calling this manually you probably want this to be true to load the config file. {@link YamlConfiguration#load(File)}
     * @return Whether or not the option was loaded successful.
     */
    public boolean loadOption(String path, boolean loadConfig) {
        Option option = getOption(path);
        if (option == null) {
            return false;
        }
        //Load config if specified.
        if (loadConfig) {
            createFile();
            try {
                config.load(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //If config doesn't have the option save the default.
        if (!config.isSet(path)) {
            saveOption(path, loadConfig);
        }

        //If the option doesn't have a default it may not be in the config still.
        if (!config.isSet(path)) {
            return false;
        }

        //Parse options.
        if (option instanceof SingleOption) {
            SingleOption singleOption = (SingleOption)option;
            String value = config.getString(path);

            if (!singleOption.parse(value)) {
                logError(path, option.getName(), option.getClass().getSimpleName(), value, option.getError());
                return false;
            }
            return true;
        } else if (option instanceof ListOption) {
            ListOption listOption = (ListOption)option;
            List<?> values = config.getList(path);

            if (!listOption.parse(false, values.toArray(new Object[values.size()]))) {
                logError(path, option.getName(), option.getClass().getSimpleName(), "See Error Index", option.getError());
                return false;
            }
            return true;
        } else if (option instanceof MapOption) {
            MapOption mapOption = (MapOption)option;
            ConfigurationSection section = config.getConfigurationSection(path);
            Map<String, Object> values = new HashMap<>();
            for (String key : section.getKeys(true)) {
                values.put(key, section.get(key));
            }
            if (!mapOption.parse(false, values)) {
                logError(path, option.getName(), option.getClass().getSimpleName(), "See Error Key", option.getError());
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Save the option in the specified path.
     * If there is no option registered for the specified path nothing will happen.
     *
     * @param path The full path of the option to save.
     * @param saveConfig When true it will save the config after setting the value.
     * It will not force save, so if the saving is on delay it won't save.
     * @return Whether or not the config was saved. (will be false if you specify save as false or if saving is on delay)
     */
    public boolean saveOption(String path, boolean saveConfig) {
        Option option = getOption(path);
        if (option == null) {
            return false;
        }
        if (option instanceof SingleOption) {
            config.set(path, ((SingleOption)option).serialize());
        } else if (option instanceof ListOption) {
            config.set(path, ((ListOption)option).serialize());
        } else if (option instanceof ListOption) {
            config.set(path, ((MapOption)option).serialize());
        }

        if (saveConfig) {
            return saveConfig(false);
        }
        return false;
    }

    /**
     * When parsing fails log full details about the error in the console.
     *
     * @param path The path of the option value.
     * @param optionName The option name. {@link Option#getName()}
     * @param optionType The option type. {@link Class#getSimpleName()}
     * @param value The option value from the config file. (which is invalid)
     * @param error The error message produced from the option parser. {@link Option#getError()}
     */
    private void logError(String path, String optionName, String optionType, String value, String error) {
        Boxx.get().error("Invalid configuration option value found! '" + file.getAbsolutePath() + "'");
        Boxx.get().error("Path: '" + path + "' Option: '" + optionName + "' Type: '" + optionType + "' Config Value: '" + value + "'");
        Boxx.get().error("Error: '" + error + "'");
    }


    /**
     * Get the configuration {@link File}
     *
     * @return The configuration {@link File}
     */
    public File getFile() {
        return file;
    }

    /**
     * Set/change the configuration file.
     *
     * @param file The absolute path for the configuration file.
     * @see #OptionCfg(String)
     */
    public void setFile(String file) {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        this.file = new File(file);
    }

    /**
     * Set/change the configuration file.
     *
     * @param file The configuration file.
     * @see #OptionCfg(File)
     */
    public void setFile(File file) {
        if (file == null) {
            new InvalidConfigurationException("File cannot be null!").printStackTrace();
        }
        this.file = file;
    }


    /**
     * Get the {@link YamlConfiguration} used for this config.
     *
     * @return The {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig() {
        return config;
    }


    /**
     * Set the save delay in milliseconds.
     * Every time the {@link #save()} method is called it will check this delay.
     * For example with a delay of 5000 it would not save the file when it was saved 3 seconds ago.
     * <b>This is not a scheduled delay it just puts the saving on cooldown for this duration!</b>
     *
     * @param saveDelay The delay in milliseconds.
     */
    public void setSaveDelay(Long saveDelay) {
        this.saveDelay = saveDelay;
    }

    /**
     * Clear the save delay by setting it to 0ms.
     */
    public void clearSaveDelay() {
        saveDelay = 0l;
    }

    /**
     * Get the save delay set with {@link #setSaveDelay(Long)}
     *
     * @return The save delay in milliseconds.
     */
    public Long getSaveDelay() {
        return saveDelay;
    }


    /**
     * Get the option at the specified path.
     * If there is no option set at the specified path this will be {@code null}!
     *
     * @param path The path of the option to return.
     * @return The option at the specified path or {@code null} when there is no option at the specified path.
     */
    public <T> T getOption(String path) {
        if (!hasOption(path)) {
            for (Option option : options.values()) {
                if (option.getName() != null && option.getName().equalsIgnoreCase(path)) {
                    return (T)option;
                }
            }
            return null;
        }
        return (T)options.get(path);
    }

    /**
     * Get a collection with all the options in this config.
     * This does not contain all the options in the config file itself.
     * It only contains options that have been set with {@link #setOption(String, Option)}
     *
     * @return {@link Collection<Option>} with options.
     */
    public Collection<Option> getOptions() {
        return options.values();
    }

    /**
     * Get the map with all the options with the path of the option as key.
     * This does not contain all the options in the config file itself.
     * It only contains options that have been set with {@link #setOption(String, Option)}
     *
     * @return {@link Map<String, Option>} with the path as key and the option as value.
     */
    public Map<String, Option> getOptionMap() {
        return options;
    }


    /**
     * Set a option and don't load the value immediately.
     *
     * @see #setOption(String, Option, boolean)
     */
    public void setOption(String path, Option option) {
        setOption(path, option, false);
    }

    /**
     * Set/register a option for the config.
     * The path is where the option will be saved in the config.
     * Just like regular {@link YamlConfiguration#set(String, Object)} the path may contain dots (.) for different sections.
     * <p/>
     * It will overwrite any previous options at the same path!
     * Use {@link #hasOption(String, boolean)} to check if this config has a option for the specified path.
     * <p/>
     * It's recommended to always give options a default value.
     * It will work without but there won't be a entry in the config file for the option till a value is set and the option is saved.
     *
     * @param path The path in the config file for the option.
     * @param option The option instance.
     * @param load When set to true the option will be loaded from config after being set.
     * Only set this true when not mass setting options otherwise it would load the config for each option.
     * It's better to set all the options and then call {@link #load()}
     */
    public void setOption(String path, Option option, boolean load) {
        options.put(path, option);
        if (load) {
            loadOption(path, true);
        }
    }

    /**
     * Check if the config has a option for the specified path.
     * This will not check if there is a value in the config file it just checks for the options set with {@link #setOption(String, Option)}
     * Use {@link #hasOption(String, boolean)} with checkConfig to true to also check if the option is set in the config.
     *
     * @param path The path of the option to check.
     * @return Whether or not the config has a option for the specified path.
     */
    public boolean hasOption(String path) {
        return hasOption(path, false);
    }

    /**
     * Check if the config has a option for the specified path.
     *
     * @param path The path of the option to check.
     * @param checkConfig When true it will also return true when the config file has the specified path.
     * @return Whether or not the config has a option for the specified path.
     */
    public boolean hasOption(String path, boolean checkConfig) {
        if (options.containsKey(path)) {
            return true;
        }
        if (config.isSet(path)) {
            return true;
        }
        return false;
    }

    /**
     * Remove/unregister a option from the config.
     *
     * @param path The path of the option to remove.
     * @param fromConfig When set to true it will also remove the value out of the config file and save it.
     */
    public void removeOption(String path, boolean fromConfig) {
        options.remove(path);
        if (fromConfig) {
            config.set(path, null);
            save(true);
        }
    }

}
