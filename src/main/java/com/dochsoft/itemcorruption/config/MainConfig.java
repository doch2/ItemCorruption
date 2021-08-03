package com.dochsoft.itemcorruption.config;

import com.dochsoft.itemcorruption.Reference;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainConfig {
    public static String ITEM_TIME_LORE = "";
    public static String FRESH_REWARD_LORE = "";
    public static String FRESH_NEXT_TIME_LORE = "";

    public static FileConfiguration data;

    public static void createMainConfig() {
        File file = new File(Reference.ConfigFolder + "config.yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
                data.set("Text.ItemLoreText", "§f소멸 예정");
                data.set("Text.FreshNextTimeLoreText", "§6다음 상태까지");
                data.set("Text.FreshRewardLoreText", "§6보상");
                List<String> itemTypeList = new Vector<>(); itemTypeList.add("등짐");itemTypeList.add("잡동사니");itemTypeList.add("재료");itemTypeList.add("도구");itemTypeList.add("음식");
                itemTypeList.add("오른손 무기");itemTypeList.add("왼손 무기");itemTypeList.add("양손 무기");
                data.set("Text.ItemTypeList", itemTypeList);
                data.set("Time.ItemList", null);
                data.set("Fresh.ItemList", null);
                data.set("Fresh.Percentage.Time.Freshness", 8);
                data.set("Fresh.Percentage.Time.Normal", 16);
                data.set("Fresh.Percentage.Time.Spoil", 66);
                data.set("Fresh.Percentage.Time.Corruption", 100);
                data.set("Fresh.Percentage.Reward.Freshness", "+30");
                data.set("Fresh.Percentage.Reward.Normal", "+15");
                data.set("Fresh.Percentage.Reward.Spoil", "-15");
                data.set("Fresh.Percentage.Reward.Corruption", "-35");
                data.set("ServerStopTime", 1);
                data.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getMainConfig() {
        File file = new File(Reference.ConfigFolder + "config.yml");
        data = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            ITEM_TIME_LORE = (String) data.get("Text.ItemLoreText") + " ";
            FRESH_REWARD_LORE =  " " + (String) data.get("Text.FreshRewardLoreText") + " ";
            FRESH_NEXT_TIME_LORE = (String) data.get("Text.FreshNextTimeLoreText") + " ";

            Reference.itemTypeList = (List<String>) data.get("Text.ItemTypeList");

            Reference.freshTimePercentageFreshness = (Integer) data.get("Fresh.Percentage.Time.Freshness");
            Reference.freshTimePercentageNormal = (Integer) data.get("Fresh.Percentage.Time.Normal");
            Reference.freshTimePercentageSpoil = (Integer) data.get("Fresh.Percentage.Time.Spoil");
            Reference.freshTimePercentageCorruption = (Integer) data.get("Fresh.Percentage.Time.Corruption");

            Reference.freshRewardPercentageFreshness = (String) data.get("Fresh.Percentage.Reward.Freshness");
            Reference.freshRewardPercentageNormal = (String) data.get("Fresh.Percentage.Reward.Normal");
            Reference.freshRewardPercentageSpoil = (String) data.get("Fresh.Percentage.Reward.Spoil");
            Reference.freshRewardPercentageCorruption = (String) data.get("Fresh.Percentage.Reward.Corruption");

        }
    }

    public static void loadCorruptionItem() {
        File file = new File(Reference.ConfigFolder + "config.yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        Reference.itemTimeList.clear();
        Reference.itemList.clear();

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
            } else {
                if (data.get("Time.ItemList") == null) {
                    Reference.itemList = new Vector<>();
                } else {
                    Reference.itemList = (ArrayList) data.get("Time.ItemList");
                }

                if (data.get("Fresh.ItemList") == null) {
                    Reference.freshItemList = new Vector<>();
                } else {
                    Reference.freshItemList = (ArrayList) data.get("Fresh.ItemList");
                }

                for (int i=0; i < Reference.itemList.size(); i++) {
                    Reference.itemTimeList.put((String) Reference.itemList.get(i), (Integer) data.get("Time." + Reference.itemList.get(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setItemCorruptionTime(ItemStack itemStack, int time) {
        File file = new File(Reference.ConfigFolder + "config.yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
            }

            String itemName = itemStack.getTypeId() + ":" + itemStack.getData().getData();
            List<String> itemList;

            if (data.get("Time.ItemList") == null) {
                itemList = new Vector<>();
            } else {
                itemList = (List<String>) data.get("Time.ItemList");
            }
            itemList.remove(itemName);
            itemList.add(itemName);


            data.set("Time." + itemName, time);
            data.set("Time.ItemList", itemList);
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setFreshItemList(ItemStack itemStack) {
        File file = new File(Reference.ConfigFolder + "config.yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
            }

            String itemName = itemStack.getTypeId() + ":" + itemStack.getData().getData();
            List<String> itemList;

            if (data.get("Fresh.ItemList") == null) {
                itemList = new Vector<>();
            } else {
                itemList = (List<String>) data.get("Fresh.ItemList");
            }
            itemList.remove(itemName);
            itemList.add(itemName);

            data.set("Fresh.ItemList", itemList);
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setServerStopTime(long nowTime) {
        File file = new File(Reference.ConfigFolder + "config.yml");
        File folder = new File(Reference.ConfigFolder);
        data = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {
                folder.mkdir();
                file.createNewFile();
            }
            data.set("ServerStopTime", nowTime);
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getServerStopTime() {
        File file = new File(Reference.ConfigFolder + "config.yml");
        data = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            return ((Integer) data.get("ServerStopTime")).longValue();
        }

        return 1; //파일이 없을 경우
    }
}
