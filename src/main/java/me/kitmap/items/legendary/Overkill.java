package me.kitmap.items.legendary;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kitmap.Main;

public class Overkill extends Legendary implements Listener {
	public static ConcurrentHashMap<UUID, Long> timer = new ConcurrentHashMap<>();
	private static HashMap<UUID,Boolean> charged = new HashMap<>(); 
	private static final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 7*20, 1);
	private static final PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 7*20, 1);
	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		if( (ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				isItem(player.getInventory().getItemInMainHand())) {
			long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
			
			if (cooldown > 0) { // If Overkill is on cooldown
				player.sendMessage(String.format("%s%s%d%s",ChatColor.RED,"You need to wait ",cooldown/1000," seconds to use it again!")); // +1
				return;
			}
			timer.put(player.getUniqueId(), System.currentTimeMillis() + 15*1000);
			reduceDura(player, player.getInventory().getItemInMainHand(), 50);
			
			if(charged.containsKey(player.getUniqueId())) charged.replace(player.getUniqueId(), true);
			else charged.put(player.getUniqueId(), true);
			player.sendMessage("Charged: " + charged.get(player.getUniqueId()));
			player.sendMessage("Overkill strengthens your next attack");
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				public void run() {
					charged.replace(player.getUniqueId(), false);
					player.sendMessage("Charged: " + charged.get(player.getUniqueId()));
					}
				}, 5*20L);
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				public void run() {
					timer.remove(player.getUniqueId());
					}
				}, 15*20L);
			
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if (ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player)ev.getDamager();
			Player victim = (Player)ev.getEntity();
			
			if (!ev.isCancelled() && isItem(player.getInventory().getItemInMainHand()) && victim.getNoDamageTicks() < victim.getMaximumNoDamageTicks() / 2 && 
					charged.containsKey(player.getUniqueId()) && charged.get(player.getUniqueId())){
				ev.setDamage(Math.max((20 - victim.getHealth()) * 0.4 + 1, 2));
				ev.setDamage(DamageModifier.ARMOR,0); // true dmg
				player.sendMessage("Enemy missing health: " + (20 - victim.getHealth())); // Damage = 1 + (40% of missing health);
				player.sendMessage("True damage dealth: " + ev.getDamage());
				charged.replace(player.getUniqueId(), false);
				player.addPotionEffect(slowness);
				player.addPotionEffect(weakness);
			}
		} 
	}
	
	// true damage = (20 - health)*0.4 + 1
	private static final String name = ChatColor.RESET + "Overkill";
	private boolean isItem(ItemStack is) {
		if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
			return true;
		}
		return false;
	}
}
