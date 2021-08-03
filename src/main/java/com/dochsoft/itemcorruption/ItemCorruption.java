package com.dochsoft.itemcorruption;

import com.dochsoft.itemcorruption.command.AddCorruptionItemCommand;
import com.dochsoft.itemcorruption.command.AddFreshItemCommand;
import com.dochsoft.itemcorruption.config.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCorruption extends JavaPlugin {
    private static ItemCorruption Instance;
    BukkitEvent event = new BukkitEvent();

    @Override
    public void onEnable() {
        Instance = this;
        getCommand("소멸시간").setExecutor(new AddCorruptionItemCommand());
        getCommand("신선도").setExecutor(new AddFreshItemCommand());
        getServer().getPluginManager().registerEvents(this.event, this);

        MainConfig.createMainConfig();
        MainConfig.getMainConfig();
        MainConfig.loadCorruptionItem();
        Reference.valuableInit();

        Reference.isServerRunningApplyTimeChest = 0;
        Reference.isServerRunningFirstPlayer = false;

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Inventory playerInv = onlinePlayer.getInventory();
                    Reference.itemLoreUpdate(playerInv, 1);
                }

                if (MainConfig.getServerStopTime() != 1 && Reference.isServerRunningApplyTimeChest == 10) { ItemCorruption.applyNowTime();}
                if (Reference.isServerRunningApplyTimeChest != 11) { Reference.isServerRunningApplyTimeChest++; }

                for (World world : Bukkit.getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks()) {
                        for (BlockState blockState : chunk.getTileEntities()) {
                            if (blockState instanceof Chest) {
                                Chest chest = (Chest) blockState;
                                Inventory chestInv = chest.getBlockInventory();
                                Reference.itemLoreUpdate(chestInv, 1);
                            }
                        }
                    }
                }
            }
        }, 0L, 20L); //10틱당 돌아가게, 20틱 = 1초

        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §a활성화§r되었습니다. §r| 도치(doch1)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MainConfig. setServerStopTime(System.currentTimeMillis() / 1000);
        Bukkit.getConsoleSender().sendMessage(Reference.prefix_normal + "플러그인이 §c비활성화§r되었습니다. §r| 도치(doch1)");
    }

    public static ItemCorruption getInstance() {
        return Instance;
    }

    public static void applyNowTime() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Chest) {
                        Chest chest = (Chest) blockState;
                        Inventory chestInv = chest.getBlockInventory();
                        Reference.itemLoreUpdate(chestInv, Math.toIntExact((System.currentTimeMillis() / 1000) - MainConfig.getServerStopTime()));
                    }
                }
            }
        }
    }
}
