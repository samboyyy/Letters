package com.sampreet.letters;

import com.sampreet.letters.helpers.MessagesHelper;
import com.sampreet.letters.hooks.LuckPermsHook;
import com.sampreet.letters.hooks.PlaceholderApiHook;
import com.sampreet.letters.hooks.VanishHook;
import com.sampreet.letters.listeners.AsyncChatListener;
import com.sampreet.letters.listeners.PlayerDeathListener;
import com.sampreet.letters.listeners.PlayerJoinListener;
import com.sampreet.letters.listeners.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Letters extends JavaPlugin {

    private MessagesHelper messagesHelper;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().getBoolean("enabled", true)) {
            logString("system.status.disabled");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.messagesHelper = new MessagesHelper(this);
        PlaceholderApiHook.checkPlaceholderAPI(this);
        LuckPermsHook.checkLuckPerms(this);
        VanishHook.checkVanishPlugin(this);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new AsyncChatListener(this), this);

        logString("system.lifecycle.enable");
    }

    @Override
    public void onDisable() {
        logString("system.lifecycle.disable");
    }

    private void logString(String path) {
        String message = getConfig().getString(path);
        if (message == null || message.isBlank())
            return;

        message = message.replace("<version>", getDescription().getVersion());
        getLogger().info(message);
    }

    public MessagesHelper getMessages() {
        return messagesHelper;
    }
}