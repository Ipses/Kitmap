package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.kitmap.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class SealOfSpace extends Legendary implements Listener {

	private Main plugin;
	private String name;
	private ConcurrentHashMap<UUID, Long> timer = new ConcurrentHashMap<>();
	private static final String NAME = ChatColor.RESET + "Seal of Space";

	public SealOfSpace(Main plugin) {
		super(plugin);
		this.name = NAME;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		if((ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) && 
				hasName(ev.getPlayer().getInventory().getItemInMainHand())) {
			Player player = ev.getPlayer();
			Block targetBlock = player.getTargetBlock(null, 250);
		    Location loc = targetBlock.getLocation();
		    long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
			if(cooldown > 0) {
				player.sendMessage(String.format("%s%s%d%s",ChatColor.RED,"You need to wait ",cooldown/1000," seconds to use it again!"));
				return;
			}
		    if(loc.distance(player.getLocation()) > 20) {
		    	player.sendMessage(ChatColor.RED + "Too far to teleport. Must be within 20 blocks.");
		    	player.sendMessage("distance: " + loc.distance(player.getLocation()));
		    } else {
		    	Location tpLoc = targetBlock.getLocation().add(0, 1, 0);
				Vector vector = tpLoc.toVector().subtract(player.getLocation().toVector()).normalize();
				
				tpLoc.setDirection(vector);
		    	if(targetBlock.getType() != Material.AIR && tpLoc.getBlock().getType() == Material.AIR && tpLoc.add(0, 1, 0).getBlock().getType() == Material.AIR) {
					timer.put(player.getUniqueId(), System.currentTimeMillis() + 10*1000);
					
					player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
		    		player.getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					player.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
					player.getWorld().spawnParticle(Particle.PORTAL, loc, 1);
					player.teleport(tpLoc);
					player.getLocation().setDirection(vector);
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);
					player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 1);
		    	}
		    }
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack sealofspace = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta sealofspaceItemMeta = sealofspace.getItemMeta();
		List<String> sealofspaceLore = new ArrayList<String>();
		sealofspaceLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealofspaceLore.add(ChatColor.BLUE + "Right Click: Teleport to the block you are looking at");
		sealofspaceLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
		sealofspaceItemMeta.setLore(sealofspaceLore);
		sealofspaceItemMeta.setDisplayName(this.name);
		sealofspace.setItemMeta(sealofspaceItemMeta);
		return sealofspace;
	}
}
