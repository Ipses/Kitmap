package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RobbersBlade implements Listener{
	@EventHandler
	public void onEntityDamageByEntity (EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player && isItem(((Player) ev.getDamager()).getInventory().getItemInMainHand())) {
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
			for(int i=0;i<itemSlots.size();i++) {
				player.sendMessage("itemSlots: " + itemSlots.get(i));
			}
			int randInt = itemSlots.get(new Random().nextInt(itemSlots.size()));
			player.sendMessage("randInt = " + randInt);
			player.getInventory().setItemInMainHand(inv.getItem(randInt));
			inv.setItem(randInt, new ItemStack(Material.AIR));
			player.sendMessage(ChatColor.RED + "You stole an item from " + ChatColor.YELLOW + victim.getName());
		}
	}
	private static final String name = ChatColor.RESET + "Robber's Blade";
	private boolean isItem(ItemStack is) {
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
