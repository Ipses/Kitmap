package me.kitmap.items.legendary;

import java.util.HashMap;
import java.util.UUID;

import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class DepthStrider extends Legendary implements Listener{

	private Main plugin;
	private String name;
	private HashMap<UUID, Integer> movements = new HashMap<>();

	public DepthStrider(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onSwim(PlayerMoveEvent ev) {
		if(!ev.isCancelled() && ev.getPlayer().getInventory().getLeggings() != null && ev.getPlayer().getInventory().getLeggings().containsEnchantment(Enchantment.DEPTH_STRIDER) &&
				(ev.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER || ev.getPlayer().getLocation().getBlock().getType() == Material.WATER)) {
			Player player = ev.getPlayer();
			ItemStack depthPants = player.getInventory().getLeggings();
			if(!movements.containsKey(player.getUniqueId())) {
				movements.put(player.getUniqueId(), 1);
				player.sendMessage("no key found, added");
				return;
			}
			int count = movements.get(player.getUniqueId());	
			if(ev.getTo().getBlockX() != ev.getFrom().getBlockX() || ev.getTo().getBlockZ() != ev.getFrom().getBlockZ() || ev.getTo().getBlockY() != ev.getFrom().getBlockY()) {
				if(count >= 100) {
					movements.replace(player.getUniqueId(), 0);
					reduceDura(player, depthPants, 5);
					player.sendMessage("reduce dura");
				} else {
					if(player.isSprinting()) movements.replace(player.getUniqueId(), count += 2);
					else {
						movements.replace(player.getUniqueId(), count += 1);
					}
					player.sendMessage("move: " + movements.get(player.getUniqueId()));
				}
			}
		}	
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player victim = (Player) ev.getEntity();
			ItemStack depthPants = victim.getInventory().getLeggings();
		 if (victim.getInventory().getLeggings() != null && victim.getInventory().getLeggings().containsEnchantment(Enchantment.DEPTH_STRIDER)) {
			reduceDura(victim, depthPants, 2);
		 	}
		} 
	}

	@Override
	public ItemStack getItem(){
		ItemStack depth = new ItemStack(Material.IRON_LEGGINGS);
		depth.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
		return depth;
	}
}
