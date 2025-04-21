package com.katok.besane.sanity;

import org.bukkit.plugin.java.JavaPlugin;

public interface ISanityManager {
    public boolean isRunned();
    public void run(JavaPlugin instance, int delay) throws BukkitTaskException;
    public void cancel() throws BukkitTaskException;
}
