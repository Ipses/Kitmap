package me.kitmap.items.legendary;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;

public class SpikeThrower extends Legendary implements Listener {
	@EventHandler
	public void onShoot(EntityShootBowEvent ev) {
		if(!ev.isCancelled() && isItem(ev.getBow())) {
			ev.getProjectile().setMetadata("spikethrower", new FixedMetadataValue(Main.getInstance(), true) );
		}
	}
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
				ev.getEntity() instanceof Player && ev.getDamager().hasMetadata("spikethrower")) {
			Player victim = (Player) ev.getEntity();
			Vector velocity = new Vector(0, 0, 0);
			for(int i=0;i<=10;i++) {
				Arrow arrow = victim.launchProjectile(Arrow.class);
				arrow.setPickupStatus(PickupStatus.DISALLOWED);
				arrow.setVelocity(new Vector(velocity.getX() + Math.random() - 0.5, velocity.getY(), 
						velocity.getZ() + Math.random() - 0.5).multiply(5));
			}
		}
	}
	private static final String name = ChatColor.RESET + "Spike Thrower";
	private static boolean isItem(ItemStack is) {
		
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
