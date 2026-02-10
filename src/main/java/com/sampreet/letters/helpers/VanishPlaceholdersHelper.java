package com.sampreet.letters.helpers;

import com.sampreet.letters.Letters;
import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VanishPlaceholdersHelper {

    private VanishPlaceholdersHelper() {
    }

    public static Component setPlaceholders(Component message, Event event, Letters plugin) {
        if (message == null || message.equals(Component.empty())) {
            return null;
        }

        Player player = extractPlayer(event);
        if (player != null) {
            message = PlaceholdersHelper.replaceLiteral(
                    message,
                    "<player>",
                    PlaceholdersHelper.playerComponent(player)
            );
        }

        return PlaceholdersHelper.setPlaceholders(message, plugin);
    }

    private static Player extractPlayer(Event event) {
        if (event instanceof PlayerShowEvent join) {
            return join.getPlayer();
        }
        if (event instanceof PlayerHideEvent quit) {
            return quit.getPlayer();
        }
        return null;
    }
}