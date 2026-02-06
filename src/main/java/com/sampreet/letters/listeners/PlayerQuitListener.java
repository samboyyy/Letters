package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Letters plugin;

    public PlayerQuitListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        String quitMessage = plugin.getMessages().resolveRandomMessage(playerQuitEvent.getPlayer(), "quit");

        quitMessage = PlaceholderApiHook.usePlaceholderAPI(playerQuitEvent.getPlayer(), quitMessage);
        Component quitMessageComponent = plugin.getMessages().translateColors(quitMessage);
        quitMessageComponent = PlaceholdersHelper.setPlaceholders(quitMessageComponent, playerQuitEvent, plugin);

        playerQuitEvent.quitMessage(quitMessageComponent);
    }
}