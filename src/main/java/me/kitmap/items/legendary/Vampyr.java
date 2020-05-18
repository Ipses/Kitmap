package me.kitmap.items.legendary;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Vampyr {
	private static HashMap<UUID,Long> timer = new HashMap<>();

	@EventHandler
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent ev) {
		if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player && 
				isItem(((Player) ev.getDamager()).getInventory().getItemInMainHand())) {
			Player player = (Player) ev.getDamager();
			ev.setDamage(ev.getDamage()*0.75);
			if(Math.random() < 0.4) {
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Vampyr: " + ChatColor.WHITE + "You sucked some blood");
				player.setHealth(Math.min(20, player.getHealth() + 1));
			}
		}
	}
	private static final String name = ChatColor.RESET + "Vampyr";
	private static boolean isItem(ItemStack is) {
		
		if(is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}	
}
