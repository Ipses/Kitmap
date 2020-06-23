package me.kitmap.items;

import me.kitmap.Main;
import me.kitmap.items.itembuilder.LegendaryBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class ItemMenu implements Listener {

	private final LegendaryBuilder legendaryBuilder;

	public ItemMenu(LegendaryBuilder legendaryBuilder){
		this.legendaryBuilder = legendaryBuilder;
	}

	@EventHandler
	public void onGUIClick(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		ClickType click = ev.getClick();
		Inventory gui = ev.getClickedInventory();
		ItemStack item = ev.getCurrentItem();
		
		if(gui == null || item == null || item.getType() == Material.AIR) {
			return;
		}
		if(gui.getName().equals("Koth Loot")){
			ev.setCancelled(true);
		}
		if(gui.getName().equals(this.legendaryBuilder.getItemPage1().getName()) && item.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
			ev.setCancelled(true);
			player.openInventory(this.legendaryBuilder.getItemPage2());
			return;
		}
		if(gui.getName().equals(this.legendaryBuilder.getItemPage2().getName())  && item.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Previous Page")) {
			ev.setCancelled(true);
			player.openInventory(this.legendaryBuilder.getItemPage1());
			return;
		}
		if(gui.getName().equals(this.legendaryBuilder.getItemPage2().getName()) && item.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
			ev.setCancelled(true);
			player.openInventory(this.legendaryBuilder.getItemPage3());
			return;
		}
		if(gui.getName().equals(this.legendaryBuilder.getItemPage3().getName()) && item.hasItemMeta()){
			if(item.containsEnchantment(Enchantment.DEPTH_STRIDER)) {
				ev.setCancelled(true);
				player.getInventory().addItem(item);
				return;
			}
			if(item.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Previous Page")) {
				ev.setCancelled(true);
				player.openInventory(this.legendaryBuilder.getItemPage2());
				return;
			}
			if(item.getItemMeta().getDisplayName().equals(ChatColor.RESET + "Next Page")) {
				ev.setCancelled(true);
				player.openInventory(this.legendaryBuilder.getItemPage1());
				return;
			}
		}
		
		if(gui.getName().equals(this.legendaryBuilder.getItemPage1().getName()) || gui.getName().equals(this.legendaryBuilder.getItemPage2().getName()) ||
				gui.getName().equals(this.legendaryBuilder.getItemPage3().getName())) {
			ev.setCancelled(true);
			if(item.getType() == Material.EMPTY_MAP) return;
			if(click.isRightClick()) player.getInventory().addItem(item);
		}
	}
}
