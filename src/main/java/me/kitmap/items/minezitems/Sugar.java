package me.kitmap.items.minezitems;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Sugar implements Listener {

    private HashMap<UUID, Long> timer = new HashMap<>();
    private static final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40*20, 1);
    private static final PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 10*20, 1);
    private static final PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 5*20, 2);

    @EventHandler
    public void onClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        if((ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                player.getInventory().getItemInMainHand().equals(new ItemStack(Material.SUGAR))){
            long cooldown = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
            if(cooldown > 0) {
                player.sendMessage(String.format("%s%s%d%s", ChatColor.RED,"You need to wait ",cooldown/1000," seconds to use sugar again!"));
                return;
            }
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);;
            player.addPotionEffect(speed);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1);
            timer.put(player.getUniqueId(), System.currentTimeMillis() + 40*1000);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                public void run() {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(slowness);
                    player.addPotionEffect(nausea);
                    //player.getWorld().playSound(player.getLocation(), Sound., 1, 1); FIND SUGAR SLOWNESS SOUND LATER
                }
            }, 30*20L);
        }
    }
}
