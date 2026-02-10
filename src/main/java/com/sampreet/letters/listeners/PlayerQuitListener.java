package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitListener implements Listener {
    private final Letters plugin;

    public PlayerQuitListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {

        String quitMessage =
                plugin.getMessages().resolveRandomMessage(event.getPlayer(), "quit");

        quitMessage =
                PlaceholderApiHook.usePlaceholderAPI(event.getPlayer(), quitMessage);

        Component quitMessageComponent =
                plugin.getMessages().translateColors(quitMessage);

        quitMessageComponent =
                PlaceholdersHelper.setPlaceholders(
                        quitMessageComponent,
                        event,
                        plugin
                );

        event.quitMessage(quitMessageComponent);
    }
}