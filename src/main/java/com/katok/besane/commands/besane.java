package com.katok.besane.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.katok.besane.config.IConfigManager;

import me.clip.placeholderapi.PlaceholderAPI;

public class besane implements TabExecutor { // ТА ЗНАЮ Я ЧТО КЛАССЫ С БОЛЬШОЙ БУКВЫ ПИШУТЬСЯ, НО ЭТО КОМАНДА!
    private final IConfigManager configManager;
    private final Supplier<Boolean> reloadPlugin;
    private final HashMap<UUID, Float> playersSanity;

    public besane(IConfigManager configManager, Supplier<Boolean> reloadPlugin, HashMap<UUID, Float> playersSanity) {
        this.configManager = configManager;
        this.reloadPlugin = reloadPlugin;
        this.playersSanity = playersSanity;
    }

    public String format(String message, OfflinePlayer player) {
        if (message == null) {
            return "";
        }

        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String replacement = switch (matcher.group(1)) {
                case("name") -> player.getName();
                case("sanity") -> "%besane_sanity%";
                default -> matcher.group();
            };
            message = message.replace(matcher.group(), replacement);
        }
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        if(args.length == 0) {
            help(sender, command, label, args);
            return true;
        }

        switch(args[0]) {
            case("reload"):
                reload(sender, command, label, args);
                break;
            case("setheight"):
                setheight(sender, command, label, args);
                break;
            case("sanity"):
                sanity(sender, command, label, args);
                break;
            default:
                help(sender, command, label, args);
                break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            return List.of("reload", "setheight", "sanity");
        }
        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).toList();
    }

    private void help(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        String result = "";

        for(String text: configManager.getConfiguration("config.yml").getConfigurationSection("messages").getStringList("help")) {
            result += text + "\n";
        }

        sender.sendMessage(IConfigManager.color(result));
    }

    private void reload(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        if(reloadPlugin.get()) {
            sender.sendMessage(IConfigManager.colorComponent(configManager.getConfiguration("config.yml").getString("messages.reload-successfull")));
        } else {
            sender.sendMessage(IConfigManager.colorComponent("&#FF0000Не удалось перезагрузить конфигурацию плагина. Отправьте логи с консоли разработчику.\n&#5fd45fТелеграмм -> @Gaci_muchi\n"));
        }
    }

    private void setheight(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        YamlConfiguration config = configManager.getConfiguration("config.yml");
        if(args.length < 2) {
            help(sender, command, label, args);
            return;
        }

        if(!StringUtils.isNumeric(args[1])) {
            help(sender, command, label, args);
            return;
        }

        config.set("panic-height", Integer.parseInt(args[1]));
        try {
            configManager.saveConfiguration("config.yml");
            sender.sendMessage(IConfigManager.colorComponent(configManager.getConfiguration("config.yml").getString("messages.setheight-successfull")));
        } catch (IOException e) {
            sender.sendMessage(IConfigManager.colorComponent("&#FF0000Не удалось открыть конфигурацию плагина. Отправьте логи с консоли разработчику.\n&#5fd45fТелеграмм -> @Gaci_muchi\n"));
            e.printStackTrace();
        }
    }

    private void sanity(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        if(args.length < 2) {
            help(sender, command, label, args);
            return;
        }
        if(args[1].equalsIgnoreCase("set")) {
            sanitySet(sender, command, label, args);
            return;
        }
        YamlConfiguration config = configManager.getConfiguration("config.yml");
        OfflinePlayer player = Bukkit.getPlayer(args[1]);
        if(player == null) {
            playerDontExist(sender, command, label, args);
            return;
        }

        sender.sendMessage(IConfigManager.colorComponent(format(config.getString("messages.player-sanity"), player)));
    }

    private void sanitySet(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        if(args.length < 4) {
            help(sender, command, label, args);
            return;
        }

        YamlConfiguration config = configManager.getConfiguration("config.yml");
        OfflinePlayer player = Bukkit.getPlayer(args[2]);
        if(player == null) {
            playerDontExist(sender, command, label, args);
            return;
        }

        if(!StringUtils.isNumeric(args[3])) {
            help(sender, command, label, args);
            return;
        }

        float sanity = Float.parseFloat(args[3]);

        if(sanity >= 100) {
            if(playersSanity.containsKey(player.getUniqueId())) playersSanity.remove(player.getUniqueId());
        } else {
            if(!playersSanity.containsKey(player.getUniqueId())) {
                playersSanity.put(player.getUniqueId(), 100f);
            }

            playersSanity.replace(player.getUniqueId(), Math.max(0, sanity));
        }
        
        sender.sendMessage(IConfigManager.colorComponent(format(config.getString("messages.player-set-sanity"), player)));
    }

    private void playerDontExist(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
    @NotNull String[] args) {
        sender.sendMessage(IConfigManager.colorComponent(configManager.getConfiguration("config.yml").getString("messages.player-dont-exist")));
    }
}