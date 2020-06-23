package me.kitmap.items.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ZombieBow extends Legendary implements Listener {

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Zombie Bow";

	public ZombieBow(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onShot(EntityShootBowEvent ev) {
		if (!ev.isCancelled() && ev.getEntity() instanceof Player && hasName(ev.getBow(), NAME)) {
			ev.setCancelled(true);
			Player player = (Player) ev.getEntity();
			if(!player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 1)) {
				player.sendMessage(ChatColor.RED + "You have no arrows!");
				return;
			}
			for(ItemStack item: player.getInventory()) {
				if(item != null && item.getType() == Material.ARROW) {
					item.setAmount(item.getAmount() - 1);
					break;
				}
			}
			player.playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, 1, 1);
			Location fireLocation = new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY() + 0.2, player.getEyeLocation().getZ());
			Item zombieEgg = player.getWorld().dropItem(fireLocation, new ItemStack(Material.MONSTER_EGG, 1, (short)54));
			zombieEgg.setVelocity(ev.getProjectile().getVelocity().multiply(0.45));

			Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
				public void run() {
					zombieEgg.remove();
					zombieEgg.getWorld().playEffect(zombieEgg.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
					if(Math.random() > 0.33) {
						zombieEgg.getWorld().playSound(zombieEgg.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 1, 1);
					} else {
						zombieEgg.getWorld().playSound(zombieEgg.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);
						zombieEgg.getWorld().spawnEntity(zombieEgg.getLocation(), EntityType.ZOMBIE);
					}
				}
			}, 30L);
		}
	}

	@EventHandler
	public void noPickup(PlayerPickupItemEvent ev) {
		if(ev.getItem().getName().equals("item.item.monsterPlacer")) {
			ev.setCancelled(true);
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack zombiebow = new ItemStack(Material.BOW);
		ItemMeta zombiebowItemMeta = zombiebow.getItemMeta();
		List<String> zombiebowLore = new ArrayList<String>();
		zombiebowLore.add(ChatColor.BLUE + "Legendary Weapon");
		zombiebowLore.add(ChatColor.BLUE + "Fires a zombie egg");
		zombiebowLore.add(ChatColor.BLUE + "Has a chance to spawn a zombie");
		zombiebowItemMeta.setLore(zombiebowLore);
		zombiebowItemMeta.setDisplayName(NAME);
		zombiebow.setItemMeta(zombiebowItemMeta);
		return zombiebow;
	}
}