package com.sampreet.letters.commands;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class LettersCommand implements CommandExecutor, TabCompleter {
    private final Letters plugin;

    public LettersCommand(Letters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] strings
    ) {
        if (strings.length == 0) {
            sendMessage(commandSender, "messages.errors.no_command");
            return true;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            if (!commandSender.hasPermission("letters.reload")) {
                sendMessage(commandSender, "messages.errors.no_permission");
                return true;
            }

            plugin.reloadConfig();
            sendMessage(commandSender, "messages.commands.reload");
            return true;
        }

        sendMessage(commandSender, "messages.errors.invalid_command");
        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] strings
    ) {

        if (strings.length == 1) {

            if (!commandSender.hasPermission("letters.reload"))
                return Collections.emptyList();

            if ("reload".startsWith(strings[0].toLowerCase()))
                return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }

    private void sendMessage(CommandSender sender, String path) {

        String message =
                plugin.getConfig().getString(path);

        if (message == null || message.isBlank())
            return;

        if (sender instanceof Player player)
            message =
                    PlaceholderApiHook.usePlaceholderAPI(player, message);

        Component messageComponent =
                plugin.getMessages().translateColors(message);

        messageComponent =
                PlaceholdersHelper.setPlaceholders(
                        messageComponent,
                        plugin
                );

        if (messageComponent == null)
            return;

        sender.sendMessage(messageComponent);
    }
}