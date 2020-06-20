package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.kitmap.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RobbersBlade extends Legendary implements Listener{

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Robber's Blade";

	public RobbersBlade(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player &&
				hasName(((Player) ev.getDamager()).getInventory().getItemInMainHand(), NAME)) {
			Player player = (Player)ev.getDamager();
			Player victim = (Player)ev.getEntity();
			Inventory inv = victim.getInventory();
			ArrayList<Integer> itemSlots = new ArrayList<>();
			for(int i=0; i<=35; i++) {
				if(inv.getItem(i) != null) {
					itemSlots.add(i);
				}
			}
			player.sendMessage("items: " + itemSlots.size());
			if(itemSlots.size() == 0) {
				player.sendMessage(ChatColor.RED + "You failed to steal an item from " + ChatColor.YELLOW + victim.getName());
				player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				return;
			}
			int randInt = itemSlots.get(new Random().nextInt(itemSlots.size()));
			player.sendMessage("randInt = " + randInt);
			player.getInventory().setItemInMainHand(inv.getItem(randInt));
			inv.setItem(randInt, new ItemStack(Material.AIR));
			player.sendMessage(ChatColor.RED + "You stole an item from " + ChatColor.YELLOW + victim.getName());
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack robbersblade = new ItemStack(Material.WOOD_SWORD);
		ItemMeta robbersbladeItemMeta =robbersblade.getItemMeta();
		List<String> robbersbladeLore = new ArrayList<String>();
		robbersbladeLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Legendary Weapon");
		robbersbladeLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Steals a random item from a player");
		robbersbladeLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Breaks after use");
		robbersbladeItemMeta.setLore(robbersbladeLore);
		robbersbladeItemMeta.setDisplayName(NAME);
		robbersblade.setItemMeta(robbersbladeItemMeta);
		return robbersblade;
	}
}
