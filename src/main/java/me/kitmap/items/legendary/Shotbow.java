package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Shotbow extends Legendary implements Listener{

	private Main plugin;
	private static final String NAME = ChatColor.RESET + "Shotbow";

	public Shotbow(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent ev) {
		if(!ev.isCancelled() && ev.getEntity() instanceof Player) {
			Player player = (Player) ev.getEntity();
			Vector velocity = ev.getProjectile().getVelocity();
			if(hasName(player.getInventory().getItemInMainHand(), NAME)){
				double angleController = 4;
				for (int i=0;i<8;i++) {
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.setPickupStatus(PickupStatus.DISALLOWED);
					arrow.setVelocity(new Vector(velocity.getX() + (Math.random() - 0.5) / angleController, velocity.getY(),
							velocity.getZ() + (Math.random() - 0.5) / angleController) );
				}
			}
		}
	}

	@Override
	public ItemStack getItem() {
		ItemStack shotbow = new ItemStack(Material.BOW);
		ItemMeta shotbowItemMeta = shotbow.getItemMeta();
		List<String> shotbowLore = new ArrayList<String>();
		shotbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		shotbowLore.add(ChatColor.BLUE + "Shoots a volley of 8 arrows");
		shotbowItemMeta.setLore(shotbowLore);
		shotbowItemMeta.setDisplayName(NAME);
		shotbow.setItemMeta(shotbowItemMeta);
		return shotbow;
	}
}
