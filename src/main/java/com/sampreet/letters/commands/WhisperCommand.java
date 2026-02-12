package com.sampreet.letters.commands;

import com.sampreet.letters.Letters;
import com.sampreet.letters.helpers.PlaceholdersHelper;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WhisperCommand implements CommandExecutor {

    private final Letters plugin;

    public WhisperCommand(Letters plugin) {
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
            redirectVanilla(commandSender, s, strings);
            return true;
        }

        Player recipient = Bukkit.getPlayerExact(strings[0]);

        if (recipient == null) {
            redirectVanilla(commandSender, s, strings);
            return true;
        }

        if (strings.length < 2) {
            redirectVanilla(commandSender, s, strings);
            return true;
        }

        String whisperMessage =
                String.join(" ", strings)
                        .substring(strings[0].length())
                        .trim();

        if (whisperMessage.isEmpty()) {
            redirectVanilla(commandSender, s, strings);
            return true;
        }

        String senderMessage;

        if (commandSender instanceof Player player) {
            senderMessage =
                    plugin.getMessages().resolveRandomMessage(player, "whisper.sender");

            senderMessage =
                    PlaceholderApiHook.usePlaceholderAPI(player, senderMessage);
        } else {
            senderMessage =
                    plugin.getMessages().getRandomMessage("messages.default.whisper.sender");
        }

        Component senderMessageComponent =
                plugin.getMessages().translateColors(senderMessage);

        senderMessageComponent =
                PlaceholdersHelper.setPlaceholders(
                        senderMessageComponent,
                        commandSender,
                        recipient,
                        whisperMessage,
                        plugin
                );

        String recipientMessage =
                plugin.getMessages().resolveRandomMessage(recipient, "whisper.recipient");

        recipientMessage =
                PlaceholderApiHook.usePlaceholderAPI(recipient, recipientMessage);

        Component recipientMessageComponent =
                plugin.getMessages().translateColors(recipientMessage);

        recipientMessageComponent =
                PlaceholdersHelper.setPlaceholders(
                        recipientMessageComponent,
                        commandSender,
                        recipient,
                        whisperMessage,
                        plugin
                );

        if (senderMessageComponent != null) {
            commandSender.sendMessage(senderMessageComponent);
        }

        if (recipientMessageComponent != null) {
            recipient.sendMessage(recipientMessageComponent);
        }

        return true;
    }

    private void redirectVanilla(
            CommandSender sender,
            String label,
            String @NotNull [] args
    ) {
        Bukkit.dispatchCommand(
                sender,
                "minecraft:" + label + (args.length == 0
                        ? ""
                        : " " + String.join(" ", args))
        );
    }
}