package me.kitmap.items.legendary;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Legendary {
	
	boolean hasName(ItemStack is, String name) {
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
	
	boolean isHittable(Player player) {
		return player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2;
	}
	
	void reduceDura(Player player, ItemStack is, int consumedDura ) {
		if(is.getDurability() > is.getType().getMaxDurability() - consumedDura*2){
			//player.getInventory().removeItem(is);
			is.setAmount(0);
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
		} else {
			is.setDurability((short) (is.getDurability() + consumedDura));
		}
	}
	
	boolean hasEnoughArrows(Player player, int arrows) {
		int arrowCount = 0;
		for(ItemStack item: player.getInventory()) {
			if(item != null && item.getType() == Material.ARROW) {
				arrowCount += item.getAmount();
			}
		}
		if(arrowCount < arrows) {
			player.sendMessage(ChatColor.RED + "Need " + ChatColor.YELLOW + arrows + ChatColor.RED + " arrows, but you only have " + ChatColor.YELLOW + arrowCount);
			return false;
		} else {
			return true;
		}
	}
}
