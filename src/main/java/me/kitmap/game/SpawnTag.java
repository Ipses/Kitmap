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

    private Main plugin;
    public HashMap<UUID, Long> timer = new HashMap<>();

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
}
