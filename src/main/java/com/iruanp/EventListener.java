package com.iruanp;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()){
            return;
        }
        Connection.writeLine(MsgTypes.GroupMsgFormatter(event.getPlayer().getName(), event.getMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Connection.writeLine(MsgTypes.GroupMsgFormatter("System", event.getPlayer().getName()+"加入了游戏"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Connection.writeLine(MsgTypes.GroupMsgFormatter("System", event.getPlayer().getName()+"离开了游戏"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Connection.writeLine(MsgTypes.GroupMsgFormatter("System", event.getDeathMessage()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        AdvancementProgress progress = event.getPlayer().getAdvancementProgress(advancement);
        String AdvancementKey = advancement.getKey().getKey();

        if (!AdvancementKey.startsWith("recipes/")) {
            Connection.writeLine(MsgTypes.GroupMsgFormatter("System", event.getPlayer().getName() + "获得了成就："+AdvancementKey));
        }
    }
}
