package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PluviasTempest extends Legendary implements Listener {

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Pluvia's Tempest";

	public PluviasTempest(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getDamager();
			Player victim = (Player) ev.getEntity();
			ItemStack chest = victim.getInventory().getChestplate();
			if(hasName(chest, NAME) && isHittable(victim)) {
				if(Math.random() < 0.25) victim.setHealth(Math.min(victim.getHealth() + 1, 20));
				if(Math.random() < 0.15) player.setHealth(Math.min(player.getHealth() + 1, 20));
			}
		}
	}


	public ItemStack getItem() {
		ItemStack pluviastempest = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta pluviastempestItemMeta = pluviastempest.getItemMeta();
		List<String> pluviastempestLore = new ArrayList<String>();
		pluviastempestLore.add(ChatColor.AQUA + "Pluvia's Set");
		pluviastempestLore.add(ChatColor.AQUA + "Legendary Armor");
		pluviastempestLore.add(ChatColor.BLUE + "Has a 25% chance to heal you when you get hit.");
		pluviastempestLore.add(ChatColor.BLUE + "Has a 15% chance to heal the player who attacked you.");
		pluviastempestItemMeta.setLore(pluviastempestLore);
		pluviastempestItemMeta.setDisplayName(NAME);
		pluviastempest.setItemMeta(pluviastempestItemMeta);
		return pluviastempest;
	}
}
