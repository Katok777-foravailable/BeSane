package com.katok.besane.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.katok.besane.config.IConfigManager;
import com.katok.besane.events.PlayerEnterInPanicHeight;
import com.katok.besane.events.PlayerLeaveFromPanicHeight;

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

        if(e.getPlayer().getLocation().getBlockY() > panicHeight) {
            if(!playersSanity.containsKey(e.getPlayer().getUniqueId())) return;

            PlayerLeaveFromPanicHeight event = new PlayerLeaveFromPanicHeight(playersSanity.get(e.getPlayer().getUniqueId()), e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) return;

            playersSanity.remove(e.getPlayer().getUniqueId());
        } else {
            if(playersSanity.containsKey(e.getPlayer().getUniqueId())) return;

            PlayerEnterInPanicHeight event = new PlayerEnterInPanicHeight(playersSanity.get(e.getPlayer().getUniqueId()), e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) return;

            playersSanity.put(e.getPlayer().getUniqueId(), 0f);
        } // TODO: пс, ты из будущего, надо ещё настроить таски чтобы была усталость, сделать эффекты, и все такое
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(!playersSanity.containsKey(e.getPlayer().getUniqueId())) return;

        PlayerLeaveFromPanicHeight event = new PlayerLeaveFromPanicHeight(playersSanity.get(e.getPlayer().getUniqueId()), e.getPlayer());
        Bukkit.getPluginManager().callEvent(event);

        playersSanity.remove(e.getPlayer().getUniqueId());
    }
}
