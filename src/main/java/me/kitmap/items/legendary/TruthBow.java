package me.kitmap.items.legendary;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kitmap.Main;

public class TruthBow extends Legendary implements Listener {
	private static PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 10*20, 1);
	private static HashMap<UUID,Long> marked = new HashMap<>(); 

	@EventHandler
	public void onShoot(EntityShootBowEvent ev) {
		if(!ev.isCancelled() && isItem(ev.getBow())) {
			Player player = (Player) ev.getEntity();
			if(ev.getForce() == 1) {
				ev.getProjectile().setMetadata("truthbow", new FixedMetadataValue(Main.getInstance(), true) );

			} else {
				player.sendMessage(ChatColor.RED + "The bow was not fully charged");
				ev.setCancelled(true);
			}
		} 
	}
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() &&  ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getDamager();
			Player victim = (Player) ev.getEntity();
			if(marked.containsKey(victim.getUniqueId()) && marked.get(victim.getUniqueId()) - System.currentTimeMillis() > 0) {
				player.sendMessage("duration: " + (marked.get(victim.getUniqueId()) - System.currentTimeMillis()));
				player.sendMessage("old dmg: " + ev.getDamage());
				ev.setDamage(ev.getDamage() * 1.1);
				player.sendMessage("new dmg: " + ev.getDamage());
			}
		}
	}
	@EventHandler
	public void onShot(EntityDamageByEntityEvent ev) {
		if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
				ev.getEntity() instanceof Player && ev.getDamager().hasMetadata("truthbow")) {
			Player player = (Player) ((Projectile) ev.getDamager()).getShooter();
			Player victim = (Player) ev.getEntity();
			
			if(marked.containsKey(victim.getUniqueId())) marked.replace(victim.getUniqueId(), System.currentTimeMillis() + 10*1000);
			else marked.put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
			if (isItem(player.getInventory().getItemInMainHand())) {
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Truth Bow: " + ChatColor.WHITE + "You've marked " + ChatColor.YELLOW + victim.getName() + ChatColor.WHITE + " for " + 
						ChatColor.YELLOW + "10 " + ChatColor.WHITE + "seconds!");
				if(victim.hasPotionEffect(PotionEffectType.GLOWING)) victim.removePotionEffect(PotionEffectType.GLOWING);
				victim.addPotionEffect(glowing);
				victim.sendMessage(ChatColor.LIGHT_PURPLE + "Truth Bow: " + ChatColor.WHITE + "Marked you (+10% damage taken) for 10 seconds");
			}
		}
	}
	private static final String name = ChatColor.RESET + "Truth Bow";
	private static boolean isItem(ItemStack is) {
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
