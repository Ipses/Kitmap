package me.kitmap.game;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpawnEnterBlocker implements Listener {

    private final Main plugin;
    private final SpawnTag spawnTag;

    public SpawnEnterBlocker(Main plugin, SpawnTag spawnTag){
        this.plugin = plugin;
        this.spawnTag = spawnTag;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        BukkitTask task = new BukkitRunnable() {
            public void run() {
                spawnBarrierBlocks();
            }
        }.runTaskTimer(plugin.getInstance(), 0L, 5L);
    }

    @EventHandler
    public void onSpawnTagExpire(SpawnTagExpireEvent ev) {
        removeBarrierBlocks(ev.getPlayer());
    }

    // TODO: implement this. Try GG's spawn tag system. but it has a bug that players can click blocks and despawn. This lags as well
    public void spawnBarrierBlocks(){
        for (Player player: Bukkit.getOnlinePlayers()){
            if(this.spawnTag.isTagged(player.getUniqueId())){
                for(int x=(int)this.plugin.spawnMinX;x<=(int)this.plugin.spawnMaxX;++x){
                    for(int y=4;y<=15;++y){
                        Location barrierLoc1 = new Location(player.getWorld(), x, y, this.plugin.spawnMinZ);
                        player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 14);
                        Location barrierLoc2 = new Location(player.getWorld(), x, y, this.plugin.spawnMaxZ);
                        player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 14);
                    }
                }
                for(int z=(int)this.plugin.spawnMinZ;z<=(int)this.plugin.spawnMaxZ;++z){
                    for(int y=4;y<=15;++y){
                        Location barrierLoc1 = new Location(player.getWorld(), this.plugin.spawnMinX, y, z);
                        player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 14);
                        Location barrierLoc2 = new Location(player.getWorld(), this.plugin.spawnMaxX, y, z);
                        player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 14);
                    }
                }
            }
        }
    }

    public void removeBarrierBlocks(Player player){
        for(int x=(int)this.plugin.spawnMinX;x<=(int)this.plugin.spawnMaxX;++x){
            for(int y=4;y<=15;++y){
                Location barrierLoc1 = new Location(player.getWorld(), x, y, this.plugin.spawnMinZ);
                player.sendBlockChange(barrierLoc1, Material.AIR, (byte) 0);
                Location barrierLoc2 = new Location(player.getWorld(), x, y, this.plugin.spawnMaxZ);
                player.sendBlockChange(barrierLoc2, Material.AIR, (byte) 0);
            }
        }
        for(int z=(int)this.plugin.spawnMinZ;z<=(int)this.plugin.spawnMaxZ;++z){
            for(int y=4;y<=15;++y){
                Location barrierLoc1 = new Location(player.getWorld(), this.plugin.spawnMinX, y, z);
                player.sendBlockChange(barrierLoc1, Material.AIR, (byte) 0);
                Location barrierLoc2 = new Location(player.getWorld(), this.plugin.spawnMaxX, y, z);
                player.sendBlockChange(barrierLoc2, Material.AIR, (byte) 0);
            }
        }
    }
}
