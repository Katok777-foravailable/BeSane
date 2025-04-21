package com.katok.besane.sanity;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.katok.besane.config.IConfigManager;
import com.katok.besane.events.SanityChangeEvent;

public class SanityTask extends BukkitRunnable {
    private final HashMap<UUID, Float> playersSanity;
    private final IConfigManager configManager;

    public SanityTask(HashMap<UUID, Float> playersSanity, IConfigManager configManager) {
        this.playersSanity = playersSanity;
        this.configManager = configManager;
    }

    @Override
    public void run() {
        int panicHeight = configManager.getConfiguration("config.yml").getInt("panic-height");
        double sanityIncrease = configManager.getConfiguration("config.yml").getDouble("sanity-increase");
        double sanityDecrease = configManager.getConfiguration("config.yml").getDouble("sanity-decrease");
        double sanityExpansion = configManager.getConfiguration("config.yml").getDouble("sanity-expansion");

        ConfigurationSection section = configManager.getConfiguration("config.yml").getConfigurationSection("sanity-decrease-effects");

        for(UUID id: playersSanity.keySet()) {
            Player player = Bukkit.getPlayer(id);
            if(player == null || !player.isOnline()) {
                playersSanity.remove(id);
                continue;
            }

            float panic = playersSanity.get(id);
            
            if(player.getLocation().getBlockY() > panicHeight) {
                if(panic > 100) {
                    playersSanity.remove(id);
                    continue;
                }

                double difference = sanityIncrease * sanityExpansion * (player.getLocation().getBlockY() - panicHeight);

                SanityChangeEvent event = new SanityChangeEvent(panicHeight, panic, player, (float) difference);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) continue;

                panic += event.getSanityChange();
                playersSanity.replace(id, panic);
            } else {
                if(panic < 0) {
                    playersSanity.replace(id, 0f);
                    continue;
                }
                if(panic == 0) continue;

                double difference = sanityDecrease * sanityExpansion * (player.getLocation().getBlockY() - panicHeight);

                SanityChangeEvent event = new SanityChangeEvent(panicHeight, panic, player, (float) difference);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) continue;

                panic += event.getSanityChange();
                playersSanity.replace(id, panic);
            }
        }

        for(UUID id: playersSanity.keySet()) {
            Player player = Bukkit.getPlayer(id);
            if(player == null || !player.isOnline()) {
                playersSanity.remove(id);
                continue;
            };

            for(String effectName: section.getKeys(false)) {
                if(section.getInt(effectName + ".percent") < playersSanity.get(id)) continue;
    
                player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effectName), 40, section.getInt(effectName + ".level") - 1));
            }
        }
        
    }
}
