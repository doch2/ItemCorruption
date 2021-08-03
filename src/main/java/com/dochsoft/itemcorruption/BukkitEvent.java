package com.dochsoft.itemcorruption;

import com.dochsoft.itemcorruption.config.MainConfig;
import com.dochsoft.itemcorruption.config.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class BukkitEvent implements Listener {

    @EventHandler
    public static void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerConfig.createPlayerConfig(player);

        Inventory playerInv = player.getInventory();
        Reference.itemLoreUpdate(playerInv, Math.toIntExact((System.currentTimeMillis() / 1000) - PlayerConfig.getPlayerServerOutTime(player)));

        if (!Reference.isServerRunningFirstPlayer) {
            Reference.isServerRunningApplyTimeChest = 0;
            Reference.isServerRunningFirstPlayer = true;
        }
    }

    @EventHandler
    public static void playerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerConfig.setPlayerServerOutTime(player, System.currentTimeMillis() / 1000);
    }
}
