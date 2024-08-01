package com.iruanp;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class MaxChatBusClient extends JavaPlugin {
    private String server_ip;
    private int server_port;
    private String group;
    private String name;
    private String token;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadConfig();
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Connection.init(server_ip, server_port, token, group, name, this::handleMessage);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Connection.client.disconnect();
    }

    public void loadConfig() {
        FileConfiguration config = this.getConfig();

        server_ip = config.getString("server_ip");
        server_port = config.getInt("server_port");
        group = config.getString("group");
        name = config.getString("name");
        token = config.getString("token");
    }

    void handleMessage(String message){
        try {
            JSONObject json = new JSONObject(message);
            getLogger().info("Received:"+ message);
            if (json.getString("type").contentEquals("text")){
                getServer().spigot().broadcast(new TextComponent(json.getString("content")));
            }
        } catch(Exception ignored) {

        }

    }
}
