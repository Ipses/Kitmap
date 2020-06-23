package me.kitmap.items.minezitems;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class WeakGrapple implements Listener {
	
	private HashMap<UUID,Long> timer = new HashMap<>();

	@EventHandler
	public void onHook(PlayerFishEvent ev) {
		Player player = ev.getPlayer();
		Location location = ev.getHook().getLocation();
		if (ev.getState() != State.FISHING && isItem(player.getInventory().getItemInMainHand())) {
			Block b = location.getBlock().getRelative(BlockFace.DOWN);
			switch(b.getType()) {
				default:
					if(player.getLocation().getY() > location.getY() - 1.5) { // horizontal grapple
						long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
						if (cooldown > 0) {
							player.sendMessage(String.format("%s%s%d%s",ChatColor.RED,"Wait ",cooldown/1000," seconds to use horizontal grapple again!"));
							return;
						}
						timer.put(player.getUniqueId(), System.currentTimeMillis() + 15*1000);

					}
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
					player.setVelocity(location.toVector().subtract(player.getLocation().toVector()).multiply(0.2));
					player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability() + 8));
					return;
				case AIR:
				case WATER:
				case STATIONARY_WATER:
				case LAVA:
				case STATIONARY_LAVA:
			}
			player.sendMessage(ChatColor.RED + "Unable to Grapple");
		}
	}
	private static final String name = ChatColor.RESET + "Weak Grapple";
	private static boolean isItem(ItemStack is) {
		return is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name);
	}
}
