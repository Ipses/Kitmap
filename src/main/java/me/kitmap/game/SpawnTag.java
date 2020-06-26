package me.kitmap.game;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.kitmap.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;

public class SpawnTag implements Listener {

    private final Main plugin;
    private HashMap<UUID, Long> timer = new HashMap<>();

    public SpawnTag(Main plugin){
        this.plugin = plugin;
    }

    public boolean isInPvPZone(Player player) {
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        return !(plugin.spawnMinX < x && x < plugin.spawnMaxX && plugin.spawnMinZ < z && z < plugin.spawnMaxZ);
    }

    public HashMap<UUID, Long> getTimer(){
        return timer;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev) {
        if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getDamager();
            Player victim = (Player) ev.getEntity();
            if(isInPvPZone(player) && isInPvPZone(victim)) {
                this.timer.put(player.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                this.timer.put(victim.getUniqueId(), System.currentTimeMillis() + 15*1000L);
            } else { // TODO: better to keep players in hash, or remove on expire?
                if(this.timer.containsKey(player.getUniqueId()) && this.timer.containsKey(victim.getUniqueId())) {
                    this.timer.put(player.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                    this.timer.put(victim.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot damage other players at spawn");
                    ev.setCancelled(true);
                } // TODO: after figuring out whether keeping players in hash on expire is better, fix the if condition.
            }
        }
    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
                ev.getEntity() instanceof Player) {
            Player player = (Player) ((Projectile) ev.getDamager()).getShooter();
            Player victim = (Player) ev.getEntity();
            if(isInPvPZone(player) && isInPvPZone(victim)) {
                this.timer.put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                this.timer.put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
            } else {
                if(this.timer.containsKey(player.getUniqueId()) && this.timer.containsKey(victim.getUniqueId())) {
                    this.timer.put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                    this.timer.put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot damage other players at spawn");
                    ev.setCancelled(true);
                }
            }
        }
    }
}
