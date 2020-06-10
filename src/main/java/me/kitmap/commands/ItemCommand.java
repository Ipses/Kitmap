package me.kitmap.commands;

import me.kitmap.Main;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ItemCommand implements CommandExecutor {

	private final Main plugin;

	public ItemCommand(Main plugin){
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] arg2 ) {
		if(cmd.getName().equalsIgnoreCase("items") && sender instanceof Player) {
			Player player = (Player) sender;		
			player.openInventory(this.plugin.getItemPage1());
			return true;
		}
		return false;
	}
}
