package com.katok.besane.sanity;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import com.katok.besane.config.IConfigManager;

public class SanityManager implements ISanityManager {
    private final HashMap<UUID, Float> playersSanity;
    private final IConfigManager configManager;
    
    private SanityTask sanityTask;
    private boolean isRunned = false;

    public SanityManager(HashMap<UUID, Float> playersSanity, IConfigManager configManager) {
        this.playersSanity = playersSanity;
        this.configManager = configManager;
        this.sanityTask = new SanityTask(playersSanity, configManager);
    }

    @Override
    public boolean isRunned() {
        return isRunned;
    }

    @Override
    public void run(JavaPlugin instance, int delay) throws BukkitTaskException {
        if(isRunned()) {
            throw new BukkitTaskException("Failed to start running task");
        };

        sanityTask.runTaskTimer(instance, delay, delay);

        isRunned = true;
    }

    @Override
    public void cancel() throws BukkitTaskException {
        if(!isRunned()) {
            throw new BukkitTaskException("Failed to stop unlaunched task");
        };

        sanityTask.cancel();
        sanityTask = new SanityTask(playersSanity, configManager);

        isRunned = false;
    }
}
