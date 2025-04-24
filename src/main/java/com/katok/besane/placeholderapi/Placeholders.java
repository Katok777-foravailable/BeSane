package com.katok.besane.placeholderapi;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Placeholders extends PlaceholderExpansion {
    private final HashMap<UUID, Float> playersSanity;

    public Placeholders(HashMap<UUID, Float> playersSanity) {
        this.playersSanity = playersSanity;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "besane";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Katok777";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        switch(params) {
            case("sanity"):
                if(!playersSanity.containsKey(player.getUniqueId())) return "100"; // если игрока нет в списке, значит он полностью "спокоен"
                String percents = String.valueOf(playersSanity.get(player.getUniqueId()));
                return percents.substring(0, Math.min(5, percents.length()));
            // ещё плейсхолдеров?
        }
        return null;
    }
}
