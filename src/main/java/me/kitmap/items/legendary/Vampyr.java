package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Vampyr extends Legendary implements Listener {

	private static HashMap<UUID,Long> timer = new HashMap<>();
	private static final String NAME = ChatColor.RESET + "Vampyr";

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent ev) {
		if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player && 
				hasName(((Player) ev.getDamager()).getInventory().getItemInMainHand(), NAME)) {
			Player player = (Player) ev.getDamager();
			ev.setDamage(ev.getDamage()*0.75);
			if(Math.random() < 0.4) {
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Vampyr: " + ChatColor.WHITE + "You sucked some blood");
				player.setHealth(Math.min(20, player.getHealth() + 1));
			}
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack vampyr = new ItemStack(Material.IRON_SWORD);
		ItemMeta vampyrItemMeta = vampyr.getItemMeta();
		List<String> vampyrLore = new ArrayList<String>();
		vampyrLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Legendary Weapon");
		vampyrLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Has a 40% chance to heal 0.5 heart");
		vampyrLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Does 25% less damage");
		vampyrItemMeta.setLore(vampyrLore);
		vampyrItemMeta.setDisplayName(net.md_5.bungee.api.ChatColor.RESET + "Vampyr");
		vampyr.setItemMeta(vampyrItemMeta);
		return vampyr;
	}
}
