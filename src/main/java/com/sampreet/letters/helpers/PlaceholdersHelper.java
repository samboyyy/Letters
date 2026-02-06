package com.sampreet.letters.helpers;

import com.sampreet.letters.Letters;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholdersHelper {

    private PlaceholdersHelper() {}

    public static @Nullable Component setPlaceholders(
            @Nullable Component message,
            @Nullable Event event,
            @Nullable Letters plugin
    ) {
        if (message == null) {
            return null;
        }

        if (plugin != null) {
            message = replaceLiteral(
                    message,
                    "<version>",
                    Component.text(plugin.getDescription().getVersion())
            );
        }

        Player player = extractPlayer(event);
        if (player != null) {
            message = replaceLiteral(
                    message,
                    "<player>",
                    playerComponent(player)
            );
        }

        return message;
    }

    public static @Nullable Component setPlaceholders(
            @Nullable Component message,
            @Nullable Letters plugin
    ) {
        return setPlaceholders(message, null, plugin);
    }

    public static @NotNull Component replaceLiteral(
            @NotNull Component component,
            @NotNull String key,
            @NotNull Component value
    ) {
        return component.replaceText(builder ->
                builder.matchLiteral(key).replacement(value)
        );
    }

    private static @Nullable Player extractPlayer(@Nullable Event event) {
        if (event instanceof PlayerJoinEvent joinEvent) {
            return joinEvent.getPlayer();
        }
        return null;
    }

    public static @NotNull Component playerComponent(@NotNull Player player) {
        Component hover = Component.text(player.getName())
                .appendNewline()
                .append(Component.text("Type: Player"))
                .appendNewline()
                .append(Component.text(player.getUniqueId().toString()));

        return Component.text(player.getDisplayName())
                .hoverEvent(HoverEvent.showText(hover))
                .clickEvent(
                        ClickEvent.suggestCommand("/tell " + player.getName() + " ")
                );
    }
}