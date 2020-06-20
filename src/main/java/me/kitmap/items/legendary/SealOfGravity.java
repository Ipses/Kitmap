package me.kitmap.items.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class SealOfGravity extends Legendary implements Listener {

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Seal of Gravity";
	private static final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, 20, 100);

	public SealOfGravity(Main plugin) {
		super(plugin);
	}


	@EventHandler
	public void onClick(PlayerInteractEvent ev) {
		if(hasName(ev.getPlayer().getInventory().getItemInMainHand(), NAME) &&
				(ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Player player = ev.getPlayer();
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, 1, 1);
			Location throwLocation = player.getLocation().add(0, 1.5, 0);
			Item seal = player.getWorld().dropItem(throwLocation, new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal())); // Not actually skull. It's a book
			seal.setVelocity(throwLocation.getDirection().multiply(1.3)); // I think 1.3 is balanced
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				public void run() {
					seal.getWorld().playEffect(seal.getLocation(), Effect.EXPLOSION_HUGE, 1);
					seal.getWorld().playSound(seal.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 1, 1);
					
					for(Player player: Bukkit.getOnlinePlayers()) {
						if(player.getLocation().distance(seal.getLocation()) < 10) {
							Vector pull = seal.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5);
							player.setVelocity(new Vector(pull.getX(), 0, pull.getZ())); // only in X and Z
							player.addPotionEffect(SLOW);
						}
					}
					seal.remove();
				}
			}, 30L);
		}
	}
	
	@EventHandler
	public void noPickup(PlayerPickupItemEvent ev) { // you need to change this for actual MineZ
		if(ev.getItem().getName().equals("item.item.skull.skeleton")) {
			ev.setCancelled(true);
		}
	}

	public ItemStack getItem() {
		ItemStack sealofgravity = new ItemStack(Material.ENCHANTED_BOOK, 1);
		ItemMeta sealofgravityItemMeta = sealofgravity.getItemMeta();
		List<String> sealofgravityLore = new ArrayList<String>();
		sealofgravityLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealofgravityLore.add(ChatColor.BLUE + "Right Click: Throw");
		sealofgravityLore.add(ChatColor.BLUE + "Launch nearby players towards the seal");
		sealofgravityItemMeta.setLore(sealofgravityLore);
		sealofgravityItemMeta.setDisplayName(NAME);
		sealofgravity.setItemMeta(sealofgravityItemMeta);
		return sealofgravity;
	}
}
