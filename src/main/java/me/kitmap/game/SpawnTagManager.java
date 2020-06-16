package me.kitmap.game;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.UUID;

public class SpawnTagManager implements Listener {

    private final Main plugin;
    private final SpawnTag spawnTag;

    public SpawnTagManager(Main plugin, SpawnTag spawnTag){
        this.plugin = plugin;
        this.spawnTag = spawnTag;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev) {
        if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getDamager();
            Player victim = (Player) ev.getEntity();
            if(this.spawnTag.isInPvPZone(player) && this.spawnTag.isInPvPZone(victim)) {
                this.spawnTag.getTimer().put(player.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                this.spawnTag.getTimer().put(victim.getUniqueId(), System.currentTimeMillis() + 15*1000L);
            } else { // TODO: better to keep players in hash, or remove on expire?
                if(this.spawnTag.getTimer().containsKey(player.getUniqueId()) && this.spawnTag.getTimer().containsKey(victim.getUniqueId())) {
                    this.spawnTag.getTimer().put(player.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                    this.spawnTag.getTimer().put(victim.getUniqueId(), System.currentTimeMillis() + 15*1000L);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot damage other players at spawn");
                    ev.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
                ev.getEntity() instanceof Player) {
            Player player = (Player) ((Projectile) ev.getDamager()).getShooter();
            Player victim = (Player) ev.getEntity();
            if(this.spawnTag.isInPvPZone(player) && this.spawnTag.isInPvPZone(victim)) {
                this.spawnTag.getTimer().put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                this.spawnTag.getTimer().put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
            } else {
                if(this.spawnTag.getTimer().containsKey(player.getUniqueId()) && this.spawnTag.getTimer().containsKey(victim.getUniqueId())) {
                    this.spawnTag.getTimer().put(player.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                    this.spawnTag.getTimer().put(victim.getUniqueId(), System.currentTimeMillis() + 10*1000L);
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot damage other players at spawn");
                    ev.setCancelled(true);
                }
            }
        }
    }

    public HashMap<UUID, Long> getTimer(){
        return this.spawnTag.getTimer();
    }
}
