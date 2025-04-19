package com.katok.besane.config;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

public interface IConfigManager {
    public void loadConfigFile(String path) throws FileNotFoundException, IOException, InvalidConfigurationException;

    @Nullable
    public YamlConfiguration getConfiguration(String path);
}