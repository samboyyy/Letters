package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {
    private final Letters plugin;

    public PlayerDeathListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent playerDeathEvent) {
        String deathMessage = plugin.getMessages().resolveRandomMessage(playerDeathEvent.getPlayer(), "death");

        deathMessage = PlaceholderApiHook.usePlaceholderAPI(playerDeathEvent.getPlayer(), deathMessage);
        Component deathMessageComponent = plugin.getMessages().translateColors(deathMessage);
        deathMessageComponent = PlaceholdersHelper.setPlaceholders(deathMessageComponent, playerDeathEvent, plugin);

        playerDeathEvent.deathMessage(deathMessageComponent);
    }
}