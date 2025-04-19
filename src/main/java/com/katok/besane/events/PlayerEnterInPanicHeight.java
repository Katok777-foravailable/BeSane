package com.katok.besane.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEnterInPanicHeight extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Float currentSanity;
    private final Player player;
    
    private boolean isCancelled = false;

    public PlayerEnterInPanicHeight(Float currentSanity, Player player) {
        this.currentSanity = currentSanity;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Float getCurrentSanity() {
        return currentSanity;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
