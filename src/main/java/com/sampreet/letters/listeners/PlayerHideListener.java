package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import de.myzelyam.api.vanish.PlayerHideEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlayerHideListener implements Listener {
    private final Letters plugin;

    public PlayerHideListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void noPlayerHide(@NotNull PlayerHideEvent playerHideEvent) {
        if (playerHideEvent.isSilent()) return;

        String hideMessage = plugin.getMessages().resolveRandomMessage(playerHideEvent.getPlayer(), "hide");

        hideMessage = PlaceholderApiHook.usePlaceholderAPI(playerHideEvent.getPlayer(), hideMessage);
        Component hideMessageComponent = plugin.getMessages().translateColors(hideMessage);
        hideMessageComponent = PlaceholdersHelper.setPlaceholders(hideMessageComponent, playerHideEvent, plugin);

        playerHideEvent.setSilent(true);

        if (hideMessageComponent != null)
            Bukkit.broadcast(hideMessageComponent);
    }
}