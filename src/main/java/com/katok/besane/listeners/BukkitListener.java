package com.katok.besane.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.katok.besane.config.IConfigManager;
import com.katok.besane.events.PlayerEnterInPanicHeightEvent;
import com.katok.besane.events.PlayerLeaveFromPanicHeightEvent;

public class BukkitListener implements Listener {
    private final IConfigManager configManager;
    private final HashMap<UUID, Float> playersSanity;

    public BukkitListener(IConfigManager configManager, HashMap<UUID, Float> playersSanity) {
        this.configManager = configManager;
        this.playersSanity = playersSanity;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        // мы узнаем высоту каждый раз, из-за того, что кто-то может перезагрузить конфиг, и значение уже будет другое
        int panicHeight = configManager.getConfiguration("config.yml").getInt("panic-height");

        if(e.getPlayer().getLocation().getBlockY() < panicHeight) {
            if(playersSanity.containsKey(e.getPlayer().getUniqueId())) return;

            PlayerEnterInPanicHeightEvent event = new PlayerEnterInPanicHeightEvent(playersSanity.get(e.getPlayer().getUniqueId()), e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) return;

            playersSanity.put(e.getPlayer().getUniqueId(), 100f);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(!playersSanity.containsKey(e.getPlayer().getUniqueId())) return;

        PlayerLeaveFromPanicHeightEvent event = new PlayerLeaveFromPanicHeightEvent(playersSanity.get(e.getPlayer().getUniqueId()), e.getPlayer());
        Bukkit.getPluginManager().callEvent(event);

        playersSanity.remove(e.getPlayer().getUniqueId());
    }
}
