package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public abstract class Legendary {

	private final Main plugin;

	public Legendary(Main plugin){
		this.plugin = plugin;
	}

    abstract ItemStack getItem();

    boolean hasName(ItemStack is, String name) {
		return is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name);
	}
	
	boolean isHittable(Player player) {
		return player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2;
	}
	
	void reduceDura(Player player, ItemStack is, int consumedDura ) {
		if(is.getDurability() > is.getType().getMaxDurability() - consumedDura*2){
			is.setAmount(0);
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
		} else {
			is.setDurability((short) (is.getDurability() + consumedDura));
		}
        player.updateInventory();
    }
	
	boolean hasEnoughArrows(Player player, int arrows) {
		int arrowCount = 0;
		for(ItemStack item: player.getInventory()) {
			if(item != null && item.getType() == Material.ARROW) {
				arrowCount += item.getAmount();
			}
		}
		if(arrowCount < arrows) {
			player.sendMessage(ChatColor.RED + "Need " + ChatColor.YELLOW + arrows + ChatColor.RED +
					" arrows, but you only have " + ChatColor.YELLOW + arrowCount);
			return false;
		} else {
			return true;
		}
	}
}