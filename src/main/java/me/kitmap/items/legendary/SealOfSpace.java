package me.kitmap.items.legendary;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class SealOfSpace extends Legendary implements Listener {
	
	private ConcurrentHashMap<UUID, Long> timer = new ConcurrentHashMap<>();
	private static final String name = ChatColor.RESET + "Seal of Space";
	
	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		if((ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) && 
				hasName(ev.getPlayer().getInventory().getItemInMainHand(), name)) {
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
	
	@EventHandler
	public void onPlace(BlockPlaceEvent ev) {
		if(hasName(ev.getItemInHand(), name)) {
			ev.setCancelled(true);
		}
	}
}
