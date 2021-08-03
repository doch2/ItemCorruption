package com.dochsoft.itemcorruption.config;

import com.dochsoft.itemcorruption.Reference;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PlayerConfig {
    public static FileConfiguration data;

    public static void createPlayerConfig(Player player) {
        File file = new File(Reference.ConfigFolder + "player/" + player.getName() + ".yml");
        File folder = new File(Reference.ConfigFolder);
        File folder2 = new File(Reference.ConfigFolder + "player/");
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                folder2.mkdir();
                file.createNewFile();
                data.set("ServerOutTime", 1);
                data.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getPlayerServerOutTime(Player player) {
        File file = new File(Reference.ConfigFolder + "player/" + player.getName() + ".yml");
        data = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            Integer temp = (int) data.get("ServerOutTime");
            return temp.longValue();
        }

        return 1; //만약 파일이 없을 경우
    }

    public static void setPlayerServerOutTime(Player player, long nowTime) {
        File file = new File(Reference.ConfigFolder + "player/" + player.getName() + ".yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
            }
            data.set("ServerOutTime", nowTime);
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
