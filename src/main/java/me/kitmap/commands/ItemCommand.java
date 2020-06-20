package me.kitmap.commands;

import me.kitmap.Main;
import me.kitmap.items.itembuilder.LegendaryBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class ItemCommand implements CommandExecutor {

	private final LegendaryBuilder legendaryBuilder;

	public ItemCommand(LegendaryBuilder legendaryBuilder){
		this.legendaryBuilder = legendaryBuilder;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] arg2 ) {
		if(cmd.getName().equalsIgnoreCase("items") && sender instanceof Player) {
			Player player = (Player) sender;		
			player.openInventory(this.legendaryBuilder.getItemPage1());
			return true;
		}
		return false;
	}
}
