package com.dochsoft.itemcorruption;

import com.dochsoft.itemcorruption.config.MainConfig;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Reference {
    public static final String prefix_normal = "§6[ItemCorruption] §r";
    public static final String prefix_error = "§c§l[ItemCorruption] §7";
    public static String COMMAND_CMD_NOT_PRACTICE_MESSAGE = "버킷창에서는 실행되지 않으니, 게임에서 명령어를 실행해주시기 바랍니다";
    public static String COMMAND_NOTALLOW_NOOP_MESSAGE = "관리자 명령어입니다. 명령어를 이용하실려면 관리자 권한을 취득해주세요";

    public static final String ConfigFolder = "plugins/itemCorruption/";

    public static List<String> itemList = new ArrayList();
    public static List<String> freshItemList = new ArrayList();
    public static HashMap<String, Integer> itemTimeList = new HashMap();

    public static List<String> itemTypeList = new ArrayList();

    public static Integer isServerRunningApplyTimeChest;
    public static boolean isServerRunningFirstPlayer;

    public static Integer freshTimePercentageFreshness;
    public static Integer freshTimePercentageNormal;
    public static Integer freshTimePercentageSpoil;
    public static Integer freshTimePercentageCorruption;
    public static String freshRewardPercentageFreshness;
    public static String freshRewardPercentageNormal;
    public static String freshRewardPercentageSpoil;
    public static String freshRewardPercentageCorruption;

    public static HashMap<String, String> freshHangeulName = new HashMap();

    public static void valuableInit() {
        freshHangeulName.put("freshness", "§6신선함:");
        freshHangeulName.put("normal", "§6보통:");
        freshHangeulName.put("spoil", "§6상함:");
        freshHangeulName.put("corruption", "§6부패함:");
    }

    public static void itemLoreUpdate(Inventory inventory, int elapsedTime) {
        for (int i=0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                final ItemStack itemStack = inventory.getItem(i);
                String itemCode = Integer.toString(itemStack.getTypeId()) + ":" + String.valueOf(itemStack.getData().getData());
                final ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = itemMeta.getLore();
                Boolean loreIncludeTime = false;
                int time = 0;
                int applyTime = -1; //-1은 소멸예정 로어가 포함되어 있지 않다는 것, 즉 소멸 할 수 있는 아이템이 아님
                List<String> newLore = new Vector<>();
                if (lore != null) {
                    for (int i22=0; i22 < lore.size(); i22++) {
                        if (lore.get(i22).contains(MainConfig.ITEM_TIME_LORE)) {
                            loreIncludeTime = true;
                            time = getTimeNumber(lore.get(i22));
                            if ((time - elapsedTime) < 0) { applyTime = 0; } else { applyTime = (time - elapsedTime); }
                        }
                    }

                    for (int i2=0; i2 < Reference.itemTypeList.size(); i2++) {
                        if (lore.get(0).contains(Reference.itemTypeList.get(i2))) {
                            newLore.add(lore.get(0));
                        }
                    }

                    for (int i3=0; i3 < Reference.freshItemList.size(); i3++) { //신선도 로어 맨 위에 추가
                        if (itemCode.equals(Reference.freshItemList.get(i3))) {
                            newLore.add(freshHangeulName.get(getFreshnessDegree(itemTimeList.get(Reference.freshItemList.get(i3)), applyTime)) + MainConfig.FRESH_REWARD_LORE + getFreshRewardPercentage(getFreshnessDegree(itemTimeList.get(Reference.freshItemList.get(i3)), applyTime)) + "%");
                            newLore.add(MainConfig.FRESH_NEXT_TIME_LORE + getNextTimeFreshnessDegree(getFreshnessDegree(itemTimeList.get(Reference.freshItemList.get(i3)), applyTime), itemTimeList.get(Reference.freshItemList.get(i3)), applyTime));
                        }
                    }

                    for (int i2=0; i2 < lore.size(); i2++) {
                        Boolean isLoreItemType = false;
                        for (int i3=0; i3 < Reference.itemTypeList.size(); i3++) {
                            if (lore.get(i2).contains(Reference.itemTypeList.get(i3))) {
                                isLoreItemType = true;
                            }
                        }

                        if (!lore.get(i2).contains(MainConfig.ITEM_TIME_LORE) && !lore.get(i2).contains(MainConfig.FRESH_REWARD_LORE) && !lore.get(i2).contains(MainConfig.FRESH_NEXT_TIME_LORE) && !isLoreItemType) {
                            newLore.add(lore.get(i2));
                        }
                    }

                }

                if (loreIncludeTime) {
                    if (time <= 0) {
                        removeInventoryItems(inventory, i);
                    } else {
                        newLore.add(MainConfig.ITEM_TIME_LORE + getTimeText(applyTime, "corruption"));
                    }

                    itemMeta.setLore(newLore);
                    itemStack.setItemMeta(itemMeta);
                } else {
                    for (int i2=0; i2 < Reference.itemList.size(); i2++) {
                        if (itemCode.equals(Reference.itemList.get(i2))) {
                            time = itemTimeList.get(Reference.itemList.get(i2));

                            for (int i3=0; i3 < Reference.freshItemList.size(); i3++) {
                                if (itemCode.equals(Reference.freshItemList.get(i3))) {
                                    newLore.add("§6" + freshHangeulName.get(getFreshnessDegree(time, time)) + MainConfig.FRESH_REWARD_LORE + getFreshRewardPercentage(getFreshnessDegree(time, time)) + "%");
                                    newLore.add(MainConfig.FRESH_NEXT_TIME_LORE + getNextTimeFreshnessDegree(getFreshnessDegree(time, time), time, time));
                                }
                            }

                            newLore.add(MainConfig.ITEM_TIME_LORE + getTimeText(time, "corruption"));
                            itemMeta.setLore(newLore);
                            itemStack.setItemMeta(itemMeta);
                        }
                    }
                }
            }
        }
    }

    private static String getFreshRewardPercentage(String freshnessDegree) {
        if (freshnessDegree.equalsIgnoreCase("freshness")) {
            return freshRewardPercentageFreshness;
        } else if (freshnessDegree.equalsIgnoreCase("normal")) {
            return freshRewardPercentageNormal;
        } else if (freshnessDegree.equalsIgnoreCase("spoil")) {
            return freshRewardPercentageSpoil;
        } else if (freshnessDegree.equalsIgnoreCase("corruption")) {
            return freshRewardPercentageCorruption;
        }

        return "0";
    }

    private static String getFreshnessDegree(int defaultTime, int nowTime) {
        double percentage = ((defaultTime - nowTime) / (double) defaultTime) * 100;

        if (freshTimePercentageFreshness > percentage) {
            return "freshness";
        } else if (freshTimePercentageNormal > percentage) {
            return "normal";
        } else if (freshTimePercentageSpoil > percentage) {
            return "spoil";
        } else {
            return "corruption";
        }
    }

    private static String getNextTimeFreshnessDegree(String freshnessDegree, int defaultTime, int nowTime) {
        int nextTime = 0;

        if (freshnessDegree.equalsIgnoreCase("freshness")) {
            nextTime = (int) (defaultTime - (defaultTime * (freshTimePercentageNormal / 100.0)));
        } else if (freshnessDegree.equalsIgnoreCase("normal")) {
            nextTime = (int) (defaultTime - (defaultTime * (freshTimePercentageSpoil / 100.0)));
        } else if (freshnessDegree.equalsIgnoreCase("spoil")) {
            nextTime = (int) (defaultTime - (defaultTime * (freshTimePercentageCorruption / 100.0)));
        } else if (freshnessDegree.equalsIgnoreCase("corruption")) {
            nextTime = 0;
        }

        return getTimeText(nowTime - nextTime, "fresh");
    }

    public static String getTimeText(int time, String mod) { //모드 2가지 - corruption, fresh
        String result = "";
        int year = time / 31536000;
        int day = (time - (year * 31536000)) / 86400;
        int hour = (time - (year * 31536000) - (day * 86400)) / 3600;
        int minute = (time - (year * 31536000) - (day * 86400) - (hour * 3600)) / 60;
        int second = (time - year * 31536000) - (day * 86400) - (hour * 3600) - (minute * 60);
        if (year != 0) { result = result + getTimeTextOnlyNumber(year) + "년 ";}
        if (day != 0) { result = result + getTimeTextOnlyNumber(day) + "일 ";}
        if (hour != 0) { result = result + getTimeTextOnlyNumber(hour) + "시간 ";}
        if (minute != 0) { result = result + getTimeTextOnlyNumber(minute) + "분 ";}
        if (second != 0) { result = result + getTimeTextOnlyNumber(second) + "초";}

        if (mod.equalsIgnoreCase("fresh")) {
            if (year==0 && day==0 && hour==0 && minute==0 && second!=0) {
                return "1분 이내";
            } else if (second != 0) {
                return result.substring(0, result.lastIndexOf(" "));
            } else {
                return result;
            }
        } else {
            return result;
        }
    }

    public static String getTimeTextOnlyNumber(int number) {
        if (number >= 10) {
            return Integer.toString(number);
        } else {
            return "0" + Integer.toString(number);
        }
    }

    public static Integer getTimeNumber(String text) {
        int year = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (text.contains("년")) { year = Integer.parseInt(text.substring(text.lastIndexOf("년") - 2, text.lastIndexOf("년"))) * 31536000; }
        if (text.contains("일")) { day = Integer.parseInt(text.substring(text.lastIndexOf("일") - 2, text.lastIndexOf("일"))) * 86400; }
        if (text.contains("시간")) { hour = Integer.parseInt(text.substring(text.lastIndexOf("시간") - 2, text.lastIndexOf("시간"))) * 3600; }
        if (text.contains("분")) { minute = Integer.parseInt(text.substring(text.lastIndexOf("분") - 2, text.lastIndexOf("분"))) * 60; }
        if (text.contains("초")) { second = Integer.parseInt(text.substring(text.lastIndexOf("초") - 2, text.lastIndexOf("초"))); }

        return year + day + hour + minute + second;
    }

    public static void removeInventoryItems(Inventory playerInv, Integer invLoc) {
        ItemStack[] items = playerInv.getContents();
        items[invLoc] = new ItemStack(Material.AIR);
        playerInv.setContents(items);
    }
}
