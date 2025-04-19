package com.katok.besane.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class ConfigManager implements IConfigManager {
    private final JavaPlugin instance;

    private final HashMap<String, YamlConfiguration> configs = new HashMap<>();

    public ConfigManager(JavaPlugin instance) {
        this.instance = instance;
    }

    @Override
    public void loadConfigFile(String path) throws FileNotFoundException, IOException, InvalidConfigurationException {
        File configFile = new File(instance.getDataFolder() + File.pathSeparator + path);

        if(!configFile.exists()) instance.saveResource(path, false);

        YamlConfiguration configCfg = new YamlConfiguration();
        configCfg.load(configFile);

        if(configs.containsKey(path)) {
            configs.replace(path, configCfg);
        } else {
            configs.put(path, configCfg);
        }
    }

    @Nullable
    @Override
    public YamlConfiguration getConfiguration(String path) {
        return configs.get(path);
    }
}
