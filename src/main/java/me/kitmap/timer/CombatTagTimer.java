package me.kitmap.timer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;

public class CombatTagTimer implements Listener {
	
	public static ConcurrentHashMap<UUID, Long> timer = new ConcurrentHashMap<>();

	private static boolean isInPvPZone(Player player) {
		double x = player.getLocation().getX();
		double z = player.getLocation().getZ();
		return !(-99 < x && x < -80 && 378 < z && z < 396);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getDamager();
			Player victim = (Player) ev.getEntity();
			if(isInPvPZone(player) && isInPvPZone(victim)) {
				timer.put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
				timer.put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
			} else {
				if(timer.containsKey(player.getUniqueId()) && timer.containsKey(victim.getUniqueId())) {
					timer.put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
					timer.put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
				} else {
					player.sendMessage(ChatColor.RED + "You cannot damage other players at spawn");
					ev.setCancelled(true);
				}
			}
		}
	}
}
