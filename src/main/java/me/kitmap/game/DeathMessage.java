package me.kitmap.game;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathMessage implements Listener {

    private CraftEntity getLastAttacker(PlayerDeathEvent ev) {
        EntityLiving lastAttacker = ((CraftPlayer)ev.getEntity()).getHandle().lastDamager;
        return lastAttacker == null ? null : lastAttacker.getBukkitEntity();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        Player player = ev.getEntity().getPlayer();
        Player killer = null;
        ItemStack weapon;

        if(ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent){ // Direct death by player
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) player.getLastDamageCause();
            if(event.getDamager() instanceof Player){ // Melee attack
                killer = (Player) event.getDamager();
                weapon = killer.getInventory().getItemInMainHand();

                ev.setDeathMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " was slain by " + ChatColor.YELLOW +
                        killer.getName() + ChatColor.RED + " using " + getWeaponName(weapon));

            } else if(event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) { // bow shot
                killer = (Player) ((Arrow) event.getDamager()).getShooter();
                weapon = killer.getInventory().getItemInMainHand();

                double distance = killer.getLocation().distance(player.getLocation());
                ev.setDeathMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " was shot by " +
                        ChatColor.YELLOW + killer.getName() + ChatColor.RED + " using " + ChatColor.YELLOW +
                        getWeaponName(weapon) + ChatColor.RED + " from " + ChatColor.BLUE + Math.round(distance) + " blocks ");
                return;
            } else {
                killer = null;
            }
        } else if(ev.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL){ // Fall damage
            if(getLastAttacker(ev) == null){ // If no one hit this player before fall death
                ev.setDeathMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " fell from a high place");
                return;
            }
            if(getLastAttacker(ev) instanceof Player){ // If hit by another player (expire in 3~4 seconds)
                killer = (Player) getLastAttacker(ev);
                weapon = killer.getInventory().getItemInMainHand();
                ev.setDeathMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " was knocked off from " +
                        "a high " + "place thanks to " + ChatColor.YELLOW +  killer.getName() + ChatColor.RED +
                        " using " + getWeaponName(weapon));
            } else{
                killer = null;
            }
        }
        if(killer == null){
            ev.setDeathMessage(ChatColor.YELLOW + player.getName() + ChatColor.RED + " died");
            Bukkit.broadcastMessage("killer == null");
        }
    }

    private String getWeaponName(ItemStack weapon){
        if(weapon == null || weapon.getType() == Material.AIR){
            return ChatColor.RED + "their" + ChatColor.WHITE + " bare hands";
        }
        if(weapon.getItemMeta().hasDisplayName()){ // custom named items
            return ChatColor.WHITE + weapon.getItemMeta().getDisplayName();
        } else { // vanilla items
            return ChatColor. YELLOW + getDefaultItemDisplayName(weapon);
        }
    }

    private String getDefaultItemDisplayName(ItemStack item) {
        switch (item.getType()) {
            default:
                return null;
            case DIAMOND_SWORD:
                return "diamond sword";
            case IRON_SWORD:
                return "iron sword";
            case STONE_SWORD:
                return "stone sword";
            case WOOD_SWORD:
                return "wooden sword";
            case GOLD_SWORD:
                return "golden sword";
            case POTION:
                return "potion";
            case SPLASH_POTION:
                return "splash potion";
            case SUGAR:
                return "sugar";
            case COOKIE:
                return "cookie";
            case BREAD:
                return "bread";
        }
    }
}
