package me.kitmap.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ItemCommand implements CommandExecutor {
	
	public static String items = "items";
	public static Inventory itemPage1 = Bukkit.createInventory(null, 54, "Legendaries 1");
	public static Inventory itemPage2 = Bukkit.createInventory(null, 54, "Legendaries 2");
	public static Inventory itemPage3 = Bukkit.createInventory(null, 54, "MineZ Items");
	public static Inventory armors = Bukkit.createInventory(null, 54, "Legendary Armors");
	public static Inventory elites = Bukkit.createInventory(null, 54, "Elite Legendaries");
	public static Inventory normalItems = Bukkit.createInventory(null, 54, "Normal MineZ Items");
	
	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] arg2 ) {
		if(cmd.getName().equalsIgnoreCase("items") && sender instanceof Player) {
			Player player = (Player) sender;		
			player.openInventory(itemPage1);
			return true;
		}
		return false;
	}
}
