package com.katok.besane.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;

public interface IConfigManager {
    public static String color(String message) {
        if (message == null) {
            return "";
        }

        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = matcher.group(1);
            String replacement = ChatColor.of("#" + color).toString();
            message = message.replace("&#" + color, replacement);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static Component colorComponent(String message) {
        Component result = Component.empty();

        if (message == null) {
            return result;
        }

        {
            Pattern pattern = Pattern.compile("&([A-Za-z0-9])");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                String color = matcher.group(1);
                message = message.replaceAll("&" + color, "§" + color);
            }
        }

        List<String> colors_text = new ArrayList<>(List.of(message.split("&#")));

        for(int i = 1; i < colors_text.size(); i++) {
            colors_text.set(i, "#" + colors_text.get(i));
        }

        for(String text: colors_text) {
            Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6})");
            Matcher matcher = pattern.matcher(text);
            if(matcher.find()) {
                String color = matcher.group(1);
                result = result.append(Component.text(text.replaceAll("#" + color, "")).color(TextColor.fromHexString("#" + color)));
            } else {
                result = result.append(Component.text(text));
            }
        }
        return result;
    }

    public void loadConfigFile(String path) throws FileNotFoundException, IOException, InvalidConfigurationException;

    @Nullable
    public YamlConfiguration getConfiguration(String path);

    public void saveConfiguration(String path) throws IOException;
}