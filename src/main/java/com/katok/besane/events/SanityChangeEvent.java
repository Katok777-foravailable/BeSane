package com.katok.besane.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SanityChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;
    private final int panicHeight;
    private final Float currentSanity;
    private final Player player;
    private Float decreaseSanity;

    public SanityChangeEvent(int panicHeight, Float currentSanity, Player player, Float decreaseSanity) {
        this.panicHeight = panicHeight;
        this.currentSanity = currentSanity;
        this.player = player;
        this.decreaseSanity = decreaseSanity;
    }

    public Player getPlayer() {
        return player;
    }

    public Float getCurrentSanity() {
        return currentSanity;
    }

    public int getpanicHeight() {
        return panicHeight;
    }

    public Float getDecreaseSanity() {
        return decreaseSanity;
    }

    public void setDecreaseSanity(Float decreaseSanity) {
        this.decreaseSanity = decreaseSanity;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}