package me.kitmap.items.minezitems;

import me.kitmap.Main;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Grenade implements Listener {

    private static final int range = 8;
    private static final String name = ChatColor.RESET + "Grenade";
    private final Main plugin;
    public Grenade(Main plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onHit(PlayerTeleportEvent ev) {
        if(ev.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            ev.setCancelled(true);
            Location location = ev.getTo();
            location.getWorld().playEffect(location, Effect.EXPLOSION_LARGE, 1);
            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);

            for(LivingEntity e: location.getWorld().getLivingEntities()) {
                double distance = e.getLocation().distance(location);
                if(distance <= range) {
                    double damage = (range-distance)/1.5;
                    Bukkit.broadcastMessage("Damage:" + damage);
                    if(damage >= 5) damage = 5;
                    e.damage(0);
                    e.setHealth(Math.max(e.getHealth() - damage, 0));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileThrownEvent(ProjectileLaunchEvent ev) {
        if(ev.getEntity() instanceof EnderPearl) {
            //ev.getEntity().getWorld().playSound(ev.getEntity().getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1, 1);
        }
    }
}
