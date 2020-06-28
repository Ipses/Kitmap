package me.kitmap.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements Listener, CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("rename") && sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType()
                != Material.AIR) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                StringBuilder displayName = new StringBuilder();
                displayName.append(args[0]);
                for (int i = 1; i < args.length; ++i) {
                    displayName.append(" ");
                    displayName.append(args[i]);
                } // "diamond sword blah" len = 3 i < 3 - 1 = 2



                for (int i = 0; i < args.length - 1; i++) {
                    displayName.append(args[i]);
                    displayName.append(" ");
                } // "diamond sword blah" len = 3 i < 3 - 1 = 2
                displayName.append(args[args.length - 1]);
                meta.setDisplayName(ChatColor.RESET + displayName.toString());
                item.setItemMeta(meta);
                player.sendMessage("Item Renamed");
                return true;
            }
        }
        return false;
    }

//    public int postfix(int i) {
//
//    }

    public void prefix() {
        int a = 0;
        int b = 0;

        a = a + 1; // prefix ++i
        // return a; -> 1
        int temp = b; // postfix i++
        b = b + 1;
        // return temp; -> 0

        int[] intArr = new int[3];
        intArr[0] = 5;
        intArr[1] = 8;
        intArr[2] = 2;

        int i = 0;
        int j = 0;
        a = intArr[++i];
        // a = 8

        b = intArr[j++];
        // b = 5
    }
}
