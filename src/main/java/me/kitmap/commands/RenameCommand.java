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
        if(args.length == 0){
            return false;
        }
        if(cmd.getName().equalsIgnoreCase("rename") && sender instanceof Player) {
            Player player = (Player) sender;
            if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType()
                != Material.AIR) {
                ItemStack item = player.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                StringBuilder displayName = new StringBuilder();
                for(int i=0;i<args.length - 1;i++) {
                    displayName.append(args[i]);
                    displayName.append(" ");
                }
                displayName.append(args[args.length - 1]);
                meta.setDisplayName(ChatColor.RESET + displayName.toString());
                item.setItemMeta(meta);
                player.sendMessage("Item Renamed");
                return true;
            }
        }
        return false;
    }
}
