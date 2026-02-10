package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {
    private final Letters plugin;

    public PlayerJoinListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

        String joinMessage = null;

        if (!event.getPlayer().hasPlayedBefore()) {
            joinMessage =
                    plugin.getMessages().resolveRandomMessage(event.getPlayer(), "first-join");
        }

        if (joinMessage == null) {
            joinMessage =
                    plugin.getMessages().resolveRandomMessage(event.getPlayer(), "join");
        }

        joinMessage =
                PlaceholderApiHook.usePlaceholderAPI(event.getPlayer(), joinMessage);

        Component joinMessageComponent =
                plugin.getMessages().translateColors(joinMessage);

        joinMessageComponent =
                PlaceholdersHelper.setPlaceholders(
                        joinMessageComponent,
                        event,
                        plugin
                );

        event.joinMessage(joinMessageComponent);
    }
}