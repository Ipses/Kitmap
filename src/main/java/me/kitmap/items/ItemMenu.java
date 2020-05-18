package me.kitmap.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.kitmap.commands.ItemCommand;
import net.md_5.bungee.api.ChatColor;

public class ItemMenu implements Listener {
	
	@EventHandler
	public void onGUIClick(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		ClickType click = ev.getClick();
		Inventory gui = ev.getClickedInventory();
		ItemStack is = ev.getCurrentItem();
		
		if(gui == null || is == null || is.getType() == Material.AIR) {
			return;
		}
		if(gui.getName().equals(ItemCommand.itemPage1.getName()) && is.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
			ev.setCancelled(true);
			player.openInventory(ItemCommand.itemPage2);
			return;
		}
		if(gui.getName().equals(ItemCommand.itemPage2.getName())  && is.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Previous Page")) {
			ev.setCancelled(true);
			player.openInventory(ItemCommand.itemPage1);
			return;
		}
		if(gui.getName().equals(ItemCommand.itemPage2.getName()) && is.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
			ev.setCancelled(true);
			player.openInventory(ItemCommand.itemPage3);
			return;
		}
		if(gui.getName().equals(ItemCommand.itemPage3.getName()) && is.hasItemMeta()){
			if(is.containsEnchantment(Enchantment.DEPTH_STRIDER)) {
				ev.setCancelled(true);
				player.getInventory().addItem(is);
				return;
			}
			if(is.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Previous Page")) {
				ev.setCancelled(true);
				player.openInventory(ItemCommand.itemPage2);
				return;
			}
			if(is.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
				ev.setCancelled(true);
				player.openInventory(ItemCommand.itemPage1);
				return;
			}
		}
		
		if(gui.getName().equals(ItemCommand.itemPage1.getName()) || gui.getName().equals(ItemCommand.itemPage2.getName()) ||
				gui.getName().equals(ItemCommand.itemPage3.getName())) {
			ev.setCancelled(true);
			if(is.equals(null) || is.getType() == Material.EMPTY_MAP) return;
			if(click.isRightClick()) player.getInventory().addItem(is);
		}
		
	}
}
