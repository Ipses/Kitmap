package me.kitmap.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.kitmap.Main;

public class KitCommand implements CommandExecutor {
	
	private final Main plugin;

	public KitCommand(Main plugin){
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] arg2 ) {
		if(cmd.getName().equalsIgnoreCase("kit") && sender instanceof Player) {
			Player player = (Player) sender;	
			 for(int i = 0; i < plugin.getIronKit().getContents().length; ++i)
			        player.getInventory().setItem(i, plugin.getIronKit().getContents()[i]);
			 	player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			return true;
		}
		return false;
	}
}