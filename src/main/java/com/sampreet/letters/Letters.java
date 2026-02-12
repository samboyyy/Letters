package com.sampreet.letters;

import com.sampreet.letters.commands.LettersCommand;
import com.sampreet.letters.commands.WhisperCommand;
import com.sampreet.letters.helpers.MessagesHelper;
import com.sampreet.letters.hooks.LuckPermsHook;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import com.sampreet.letters.hooks.VanishHook;
import com.sampreet.letters.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Letters extends JavaPlugin {

    private MessagesHelper messagesHelper;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        if (!getConfig().getBoolean("enabled", true)) {
            logString("messages.status.disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.messagesHelper = new MessagesHelper(this);

        PlaceholderApiHook.checkPlaceholderAPI(this);
        LuckPermsHook.checkLuckPerms(this);
        VanishHook.checkVanishPlugin(this);

        LettersCommand lettersCommand = new LettersCommand(this);
        Objects.requireNonNull(getCommand("letters")).setExecutor(lettersCommand);
        Objects.requireNonNull(getCommand("letters")).setTabCompleter(lettersCommand);

        Objects.requireNonNull(getCommand("tell")).setExecutor(new WhisperCommand(this));

        getServer().getPluginManager()
                .registerEvents(new PlayerJoinListener(this), this);

        getServer().getPluginManager()
                .registerEvents(new PlayerQuitListener(this), this);

        getServer().getPluginManager()
                .registerEvents(new PlayerDeathListener(this), this);

        getServer().getPluginManager()
                .registerEvents(new AsyncChatListener(this), this);

        getServer().getPluginManager()
                .registerEvents(new PlayerAdvancementDoneListener(this), this);

        logString("messages.lifecycle.enable");
    }

    @Override
    public void onDisable() {
        logString("messages.lifecycle.disable");
    }

    private void logString(String path) {

        String message = getConfig().getString(path);

        if (message == null || message.isBlank()) {
            return;
        }

        message = message.replace("<version>", getDescription().getVersion());
        getLogger().info(message);
    }

    public MessagesHelper getMessages() {
        return messagesHelper;
    }
}