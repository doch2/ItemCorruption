package com.dochsoft.itemcorruption.command;

import com.dochsoft.itemcorruption.Reference;
import com.dochsoft.itemcorruption.config.MainConfig;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddFreshItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = ((Player) sender).getPlayer();

        if (!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }

        if (player.isOp()) {
            if (args.length == 0) {
                ItemStack itemStack = player.getItemInHand();

                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    player.sendMessage(Reference.prefix_error + "등록할 아이템을 손에 든 후 다시 시도해주세요.");
                } else if (!Reference.itemList.contains(itemStack.getTypeId() + ":" + itemStack.getData().getData())) {
                    player.sendMessage(Reference.prefix_error + "현재 아이템이 부패 아이템으로 등록되어 있지 않습니다. 아이템 확인 후 다시 시도해주세요.");
                } else {
                    Reference.freshItemList.remove(itemStack.getTypeId() + ":" + itemStack.getData().getData());
                    Reference.freshItemList.add(itemStack.getTypeId() + ":" + itemStack.getData().getData());
                    MainConfig.setFreshItemList(itemStack);
                    player.sendMessage(Reference.prefix_normal + "아이템이 정상적으로 신선도가 표시되게 등록되었습니다.");
                }
            } else {
                player.sendMessage("§6/신선도 §7- 신선도가 적용될 아이템을 지정합니다.");
            }
        } else {
            player.sendMessage(Reference.COMMAND_NOTALLOW_NOOP_MESSAGE);
        }

        return false;
    }
}
