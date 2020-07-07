package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public abstract class Legendary {

    public abstract ItemStack getItem();

    protected boolean hasName(ItemStack item, String name) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(name);
	}
	
	protected boolean isHittable(Player player) {
		return player.getNoDamageTicks() < player.getMaximumNoDamageTicks() / 2;
	}
	
	protected void reduceDura(Player player, ItemStack is, int consumedDura ) {
		if(is.getDurability() > is.getType().getMaxDurability() - consumedDura*2){
			is.setAmount(0);
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
		} else {
			is.setDurability((short) (is.getDurability() + consumedDura));
		}
        player.updateInventory();
    }
	
	protected boolean hasEnoughArrows(Player player, int arrows) {
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
