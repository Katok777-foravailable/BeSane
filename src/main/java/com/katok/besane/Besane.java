package com.katok.besane;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.katok.besane.config.ConfigManager;
import com.katok.besane.config.IConfigManager;

public final class Besane extends JavaPlugin {
    public final IConfigManager configManager = new ConfigManager(this);

    @Override
    public void onEnable() {
        loadPlugin();

        loadListeners();
    }

    public void loadPlugin() {
        try {
            configManager.loadConfigFile("config.yml");
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Не удалось загрузить конфигурацию!");
            e.printStackTrace();
        }
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(null, null); // TODO: зарегестрировать листенеры
    }
}
