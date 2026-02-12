package com.sampreet.letters.helpers;

import com.sampreet.letters.Letters;
import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholdersHelper {

    private PlaceholdersHelper() {
    }

    public static Component setPlaceholders(
            Component messageComponent,
            CommandSender sender,
            Player recipient,
            String whisperMessage,
            Letters plugin
    ) {
        if (messageComponent == null) {
            return null;
        }

        if (sender instanceof Player player) {
            messageComponent = replaceLiteral(
                    messageComponent,
                    "<sender>",
                    playerComponent(player)
            );
        }

        if (recipient != null) {
            messageComponent = replaceLiteral(
                    messageComponent,
                    "<recipient>",
                    playerComponent(recipient)
            );
        }

        if (whisperMessage != null) {
            messageComponent = replaceLiteral(
                    messageComponent,
                    "<message>",
                    Component.text(whisperMessage)
            );
        }

        return setPlaceholders(messageComponent, null, plugin);
    }

    public static @Nullable Component setPlaceholders(
            @Nullable Component message,
            @Nullable Event event,
            @Nullable Letters plugin
    ) {

        if (message == null || message.equals(Component.empty())) {
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

        if (event instanceof PlayerDeathEvent deathEvent) {

            Component deathMessage = deathEvent.deathMessage();

            message = replaceLiteral(
                    message,
                    "<message>",
                    deathMessage != null ? deathMessage : Component.empty()
            );
        }

        if (event instanceof AsyncChatEvent chatEvent) {

            message = replaceLiteral(
                    message,
                    "<message>",
                    chatEvent.message()
            );
        }

        if (event instanceof PlayerAdvancementDoneEvent advancementDoneEvent) {

            Advancement advancement = advancementDoneEvent.getAdvancement();
            AdvancementDisplay display = advancement.getDisplay();

            Component advancementComponent = advancementComponent(advancement);

            if (advancementComponent != null) {
                message = replaceLiteral(
                        message,
                        "<advancement>",
                        advancementComponent
                );
            }

            if (display != null) {

                Component colorComponent =
                        Component.empty().color(frameColor(display.frame()));

                message = replaceLiteral(
                        message,
                        "<color>",
                        colorComponent
                );
            }
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

        if (event instanceof PlayerQuitEvent quitEvent) {
            return quitEvent.getPlayer();
        }

        if (event instanceof PlayerDeathEvent deathEvent) {
            return deathEvent.getEntity();
        }

        if (event instanceof AsyncChatEvent chatEvent) {
            return chatEvent.getPlayer();
        }

        if (event instanceof PlayerAdvancementDoneEvent advancementDoneEvent) {
            return advancementDoneEvent.getPlayer();
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

    public static @Nullable Component advancementComponent(@NotNull Advancement advancement) {

        AdvancementDisplay advancementDisplay = advancement.getDisplay();

        if (advancementDisplay == null)
            return null;

        NamedTextColor advancementColor =
                frameColor(advancementDisplay.frame());

        Component advancementTitle =
                advancementDisplay.title().color(advancementColor);

        Component hover =
                advancementDisplay.title().color(advancementColor)
                        .appendNewline()
                        .append(
                                advancementDisplay.description()
                                        .color(advancementColor)
                        );

        return advancementTitle
                .hoverEvent(
                        HoverEvent.showText(hover)
                );
    }

    private static NamedTextColor frameColor(@NotNull AdvancementDisplay.Frame frame) {
        return switch (frame) {
            case TASK -> NamedTextColor.GREEN;
            case GOAL -> NamedTextColor.LIGHT_PURPLE;
            case CHALLENGE -> NamedTextColor.GOLD;
        };
    }
}