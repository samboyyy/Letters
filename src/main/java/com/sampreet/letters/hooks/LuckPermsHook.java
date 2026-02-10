package com.sampreet.letters.hooks;

import com.sampreet.letters.Letters;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class LuckPermsHook {

    private static boolean LP_INSTALLED;
    private static UserManager LP;

    private LuckPermsHook() {
    }

    public static void checkLuckPerms(Letters plugin) {

        LP_INSTALLED =
                Bukkit.getPluginManager().getPlugin("LuckPerms") != null;

        if (LP_INSTALLED) {
            LP = LuckPermsProvider.get().getUserManager();
        }

        String messagePath =
                LP_INSTALLED
                        ? "messages.dependencies.luckperms.found"
                        : "messages.dependencies.luckperms.not_found";

        String message = plugin.getConfig().getString(messagePath);

        if (message == null || message.isBlank()) {
            return;
        }

        if (LP_INSTALLED) {
            plugin.getLogger().info(message);
        } else {
            plugin.getLogger().warning(message);
        }
    }

    public static @Nullable String getPrimaryGroup(Player player) {

        if (!LP_INSTALLED || LP == null) {
            return null;
        }

        User user = LP.getUser(player.getUniqueId());

        return user == null
                ? null
                : user.getPrimaryGroup().toLowerCase();
    }
}