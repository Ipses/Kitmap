package me.kitmap.items.legendary;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class PluviasTempest extends Legendary implements Listener {
	
	public static final String name = ChatColor.RESET + "Pluvia's Tempest";

	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getDamager();
			Player victim = (Player) ev.getEntity();
			ItemStack chest = victim.getInventory().getChestplate();
			if(hasName(chest, name) && isHittable(victim)) {
				if(Math.random() < 0.25) victim.setHealth(Math.min(victim.getHealth() + 1, 20));
				if(Math.random() < 0.15) player.setHealth(Math.min(player.getHealth() + 1, 20));
			}
		}
	}
}
