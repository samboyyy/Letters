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
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {

        String deathMessage =
                plugin.getMessages().resolveRandomMessage(event.getPlayer(), "death");

        deathMessage =
                PlaceholderApiHook.usePlaceholderAPI(event.getPlayer(), deathMessage);

        Component deathMessageComponent =
                plugin.getMessages().translateColors(deathMessage);

        deathMessageComponent =
                PlaceholdersHelper.setPlaceholders(
                        deathMessageComponent,
                        event,
                        plugin
                );

        event.deathMessage(deathMessageComponent);
    }
}