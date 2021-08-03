package com.dochsoft.itemcorruption.command;

import com.dochsoft.itemcorruption.Reference;
import com.dochsoft.itemcorruption.config.MainConfig;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddCorruptionItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = ((Player) sender).getPlayer();

        if (!(sender instanceof Player)) {
            sender.sendMessage(Reference.prefix_error + Reference.COMMAND_CMD_NOT_PRACTICE_MESSAGE);
            return true;
        }

        if (player.isOp()) {
            if (args.length == 1) {
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    player.sendMessage(Reference.prefix_error + "등록할 아이템을 손에 든 후 다시 시도해주세요.");
                } else {
                    ItemStack itemStack = player.getItemInHand();
                    Reference.itemList.remove(itemStack.getTypeId() + ":" + itemStack.getData().getData());
                    Reference.itemTimeList.remove(itemStack.getTypeId() + ":" + itemStack.getData().getData());
                    Reference.itemList.add(itemStack.getTypeId() + ":" + itemStack.getData().getData());
                    Reference.itemTimeList.put(itemStack.getTypeId() + ":" + itemStack.getData().getData(), Integer.parseInt(args[0]));
                    MainConfig.setItemCorruptionTime(itemStack, Integer.parseInt(args[0]));
                    player.sendMessage(Reference.prefix_normal + "아이템이 정상적으로 " + args[0] + "초 부패되게 등록되었습니다.");
                }
            } else {
                player.sendMessage("§6/소멸시간 §b<아이템소멸시간> §7- 부패될 아이템을 지정합니다.");
            }
        } else {
            player.sendMessage(Reference.COMMAND_NOTALLOW_NOOP_MESSAGE);
        }

        return false;
    }
}
