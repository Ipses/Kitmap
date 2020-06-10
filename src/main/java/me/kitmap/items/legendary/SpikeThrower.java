package me.kitmap.items.legendary;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class SpikeThrower extends Legendary implements Listener {

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Spike Thrower";

	@EventHandler
	public void onShoot(EntityShootBowEvent ev) {
		if(!ev.isCancelled() && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getEntity();
			if(hasName(player.getInventory().getItemInMainHand(), NAME)){
				ev.getProjectile().setMetadata("spikethrower", new FixedMetadataValue(plugin.getInstance(), true) );
			}
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

	@Override
	public ItemStack getItem() {
		ItemStack spikethrower = new ItemStack(Material.BOW);
		ItemMeta spikethrowerItemMeta = spikethrower.getItemMeta();
		List<String> spikethrowerLore = new ArrayList<String>();
		spikethrowerLore.add(ChatColor.BLUE + "Legendary Weapon");
		spikethrowerLore.add(ChatColor.BLUE + "10 arrows randomly scatter in a circle around the player shot");
		spikethrowerItemMeta.setLore(spikethrowerLore);
		spikethrowerItemMeta.setDisplayName(ChatColor.RESET + "Spike Thrower");
		spikethrower.setItemMeta(spikethrowerItemMeta);
		return spikethrower;
	}
}
