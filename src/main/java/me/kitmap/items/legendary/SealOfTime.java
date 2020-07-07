package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

public class SealOfTime extends Legendary implements Listener {
	
	private HashMap<UUID, Long> timer = new HashMap<>();
	private HashMap<UUID, ItemStack[]> inv = new HashMap<>();
	private HashMap<UUID, ItemStack[]> armor = new HashMap<>();
	private Main plugin;

	private static final String NAME = ChatColor.RESET + "Seal of Time";

	public SealOfTime(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		if((ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) && 
				hasName(ev.getPlayer().getInventory().getItemInMainHand(), NAME)) {
			Player player = ev.getPlayer();
			Location loc = player.getLocation();
			long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
			if(cooldown > 0) {
				player.sendMessage(String.format("%s%s%d%s",ChatColor.RED,"You need to wait ",cooldown/1000," seconds to use it again!"));
				return;
			}
			player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
			player.getWorld().playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
			player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 10);
			player.getWorld().spawnParticle(Particle.PORTAL, loc, 10);
			double health = player.getHealth();
			double saturation = player.getSaturation();
			double food = player.getFoodLevel();
			inv.put(player.getUniqueId(), copyInventory(player.getInventory()));
	        armor.put(player.getUniqueId(), copyArmors(player.getInventory()));
			timer.put(player.getUniqueId(), System.currentTimeMillis() + 60*1000); //cooldown 1m but can increase
			
			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
				public void run() {
					timer.remove(player.getUniqueId());
					}
				}, 30*20L);
			
			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
				public void run() {
					player.getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
					player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 10);
					player.getWorld().spawnParticle(Particle.PORTAL, loc, 1);
					player.teleport(loc);
					player.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
					player.getWorld().spawnParticle(Particle.PORTAL, loc, 1);
					
					player.setHealth(health);
					player.setFoodLevel((int) food);
					player.setSaturation((float) saturation);
					
					player.getInventory().setContents(inv.get(player.getUniqueId()));
					inv.remove(player.getUniqueId());
					player.getInventory().setArmorContents(armor.get(player.getUniqueId()));
					armor.remove(player.getUniqueId());
				}
			}, 5*20L);
		}
	}
	
	private ItemStack[] copyInventory(Inventory inv) {
	    ItemStack[] originalInv = inv.getContents();
	    ItemStack[] copiedInv = new ItemStack[originalInv.length];
	    for(int i = 0; i < originalInv.length; ++i)
	        if(originalInv[i] != null)
	            copiedInv[i] = new ItemStack(originalInv[i]);
	    return copiedInv;
	}
	
	private ItemStack[] copyArmors(PlayerInventory inv) {
	    ItemStack[] originalInv = inv.getArmorContents();
	    ItemStack[] copiedInv = new ItemStack[originalInv.length];
	    for(int i = 0; i < originalInv.length; ++i)
	        if(originalInv[i] != null)
	            copiedInv[i] = new ItemStack(originalInv[i]);
	    return copiedInv;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent ev) {
		Player player = ev.getPlayer();
		if(timer.containsKey(player.getUniqueId())) { // This needs a seperate hash table (for drop)
			ev.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You cannot drop items after using Seal of Time!");
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack sealoftime = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
		ItemMeta sealoftimeItemMeta = sealoftime.getItemMeta();
		List<String> sealoftimeLore = new ArrayList<String>();
		sealoftimeLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealoftimeLore.add(ChatColor.BLUE + "Right Click: Sets a recall point. After 5 seconds,");
		sealoftimeLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
		sealoftimeItemMeta.setLore(sealoftimeLore);
		sealoftimeItemMeta.setDisplayName(NAME);
		sealoftime.setItemMeta(sealoftimeItemMeta);
		return sealoftime;
	}
}
