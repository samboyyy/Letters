package com.sampreet.letters.helpers;

import com.sampreet.letters.Letters;
import com.sampreet.letters.hooks.LuckPermsHook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MessagesHelper {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Letters plugin;

    public MessagesHelper(Letters plugin) {
        this.plugin = plugin;
    }

    public String getRandomMessage(String path) {
        if (path == null || plugin == null)
            return null;

        List<String> messages = plugin.getConfig().getStringList(path);
        List<String> validMessages = messages.stream()
                .filter(msg -> msg != null && !msg.trim().isEmpty())
                .toList();

        if (validMessages.isEmpty())
            return null;

        return validMessages.get(ThreadLocalRandom.current().nextInt(validMessages.size()));
    }

    public String resolveRandomMessage(@NotNull Player player, String key) {
        String message = getRandomMessage("player_specific." + player.getName() + "." + key);

        if (message == null) {
            String primaryGroup = LuckPermsHook.getPrimaryGroup(player);

            if (primaryGroup != null)
                message = getRandomMessage("group_based." + primaryGroup + "." + key);
        }

        if (message == null)
            message = getRandomMessage("default." + key);

        return message;
    }

    public Component translateColors(String message) {
        if (message == null)
            return null;

        Component messageComponent = MM.deserialize(message);

        String legacySerialized = LegacyComponentSerializer.legacyAmpersand().serialize(messageComponent);

        return LegacyComponentSerializer.legacyAmpersand().deserialize(legacySerialized);
    }
}