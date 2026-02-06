package com.sampreet.letters;

import org.bukkit.plugin.java.JavaPlugin;

public final class Letters extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().getBoolean("enabled", true)) {
            logString("system.status.disabled");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

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
}