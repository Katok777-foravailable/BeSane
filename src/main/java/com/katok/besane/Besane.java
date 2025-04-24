package com.katok.besane;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.katok.besane.config.ConfigManager;
import com.katok.besane.config.IConfigManager;
import com.katok.besane.listeners.BukkitListener;
import com.katok.besane.placeholderapi.Placeholders;
import com.katok.besane.sanity.BukkitTaskException;
import com.katok.besane.sanity.ISanityManager;
import com.katok.besane.sanity.SanityManager;
import com.katok.besane.commands.besane;

public final class Besane extends JavaPlugin {
    public final IConfigManager configManager = new ConfigManager(this);
    public final HashMap<UUID, Float> playersSanity = new HashMap<>();
    public final ISanityManager sanityManager = new SanityManager(playersSanity, configManager);

    @Override
    public void onEnable() {
        loadPlugin();
        registerCommands();
        loadListeners();
        startSanityTask();
        registerPlaceholders();
    }

    @Override
    public void onDisable() {
        cancelSanityTask();
    }

    public boolean loadPlugin() {
        try {
            configManager.loadConfigFile("config.yml");
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Не удалось загрузить конфигурацию!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new BukkitListener(configManager, playersSanity), this);
    }

    private void startSanityTask() {
        try {
            sanityManager.run(this, 20);
        } catch (BukkitTaskException e) {
            e.printStackTrace();
        }
    }

    private void cancelSanityTask() {
        try {
            sanityManager.cancel();;
        } catch (BukkitTaskException e) {
            e.printStackTrace();
        }
    }

    private void registerPlaceholders() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) return;
        
        new Placeholders(playersSanity).register();
    }

    private void registerCommands() {
        getCommand("besane").setExecutor(new besane(configManager, this::loadPlugin, playersSanity));
    }
}
