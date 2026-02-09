package com.sampreet.letters.listeners;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import de.myzelyam.api.vanish.PlayerShowEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlayerShowListener implements Listener {
    private final Letters plugin;

    public PlayerShowListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerShow(@NotNull PlayerShowEvent playerShowEvent) {
        if (playerShowEvent.isSilent()) return;

        String showMessage = plugin.getMessages().resolveRandomMessage(playerShowEvent.getPlayer(), "show");

        showMessage = PlaceholderApiHook.usePlaceholderAPI(playerShowEvent.getPlayer(), showMessage);
        Component showMessageComponent = plugin.getMessages().translateColors(showMessage);
        showMessageComponent = PlaceholdersHelper.setPlaceholders(showMessageComponent, playerShowEvent, plugin);

        playerShowEvent.setSilent(true);

        if (showMessageComponent != null)
            Bukkit.broadcast(showMessageComponent);
    }
}