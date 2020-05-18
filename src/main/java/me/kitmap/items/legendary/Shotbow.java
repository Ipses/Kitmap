package me.kitmap.items.legendary;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Shotbow implements Listener{
	@EventHandler
	public void onShoot(EntityShootBowEvent ev) {
		if(!ev.isCancelled() && isItem(ev.getBow())) {
			Player player = (Player) ev.getEntity();
			Vector velocity = ev.getProjectile().getVelocity();
			double angleController = 4;
			for (int i=0;i<8;i++) {
				Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setPickupStatus(PickupStatus.DISALLOWED);
				arrow.setVelocity(new Vector(velocity.getX() + (Math.random() - 0.5) / angleController, velocity.getY(), 
						velocity.getZ() + (Math.random() - 0.5) / angleController) );
			}
		}
	}
	private static final String name = ChatColor.RESET + "Shotbow";
	private static boolean isItem(ItemStack is) {
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
