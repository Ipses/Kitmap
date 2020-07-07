package me.kitmap.items.legendary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kitmap.Main;

public class Overkill extends Legendary implements Listener {
	private HashMap<UUID, Long> timer = new HashMap<>();
	private HashMap<UUID,Boolean> charged = new HashMap<>();
	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Overkill";
	private static final PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOW, 7*20, 1);
	private static final PotionEffect WEAKNESS = new PotionEffect(PotionEffectType.WEAKNESS, 7*20, 1);

	public Overkill(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		if( (ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) &&
				hasName(player.getInventory().getItemInMainHand(), NAME)) {
			long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
			
			if (cooldown > 0) { // If Overkill is on cooldown
				player.sendMessage(String.format("%s%s%d%s",ChatColor.RED,"You need to wait ",cooldown/1000," seconds to use it again!")); // +1
				return;
			}
			timer.put(player.getUniqueId(), System.currentTimeMillis() + 15*1000);
			reduceDura(player, player.getInventory().getItemInMainHand(), 50);
			
			if(charged.containsKey(player.getUniqueId())) charged.replace(player.getUniqueId(), true);
			else charged.put(player.getUniqueId(), true);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Overkill: " + ChatColor.WHITE + "Overkill empowers your next attack in 3 seconds");
			
			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () -> charged.replace(player.getUniqueId(), false), 3*20L);
			
			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () -> timer.remove(player.getUniqueId()), 20*20L);
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent ev) {
		if (ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
			Player player = (Player)ev.getDamager();
			Player victim = (Player)ev.getEntity();
			
			if (!ev.isCancelled() && hasName(player.getInventory().getItemInMainHand(), NAME) && isHittable(player) &&
					charged.containsKey(player.getUniqueId()) && charged.get(player.getUniqueId())){
				ev.setDamage(Math.max((20 - victim.getHealth()) * 0.4 + 1, 2));
				ev.setDamage(DamageModifier.ARMOR,0); // true dmg
				player.sendMessage("Enemy missing health: " + (20 - victim.getHealth())); // Damage = 1 + (40% of missing health);
				player.sendMessage("True damage dealth: " + ev.getDamage());
				charged.replace(player.getUniqueId(), false);
				player.addPotionEffect(SLOWNESS);
				player.addPotionEffect(WEAKNESS);
			}
		} 
	}

	@Override
	public ItemStack getItem() {
		ItemStack overkill = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta overkillItemMeta = overkill.getItemMeta();
		List<String> overkillLore = new ArrayList<>();
		overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Legendary Weapon");
		overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Right Click: Charge at the cost of 50 durability");
		overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "After charging, your next attack in 5 seconds");
		overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "does" + net.md_5.bungee.api.ChatColor.YELLOW + " true damage " + net.md_5.bungee.api.ChatColor.BLUE + "based on a player's missing health");
		overkillItemMeta.setLore(overkillLore);
		overkillItemMeta.setDisplayName(NAME);
		overkill.setItemMeta(overkillItemMeta);
		return overkill;
	}
}
