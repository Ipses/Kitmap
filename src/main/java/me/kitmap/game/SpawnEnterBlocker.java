package me.kitmap.game;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpawnEnterBlocker implements Listener {

    private final Main plugin;
    private final SpawnTag spawnTag;

    public SpawnEnterBlocker(Main plugin, SpawnTag spawnTag){
        this.plugin = plugin;
        this.spawnTag = spawnTag;
    }

    // TODO: implement this
    @EventHandler
    public void updateBlock(EntityDamageByEntityEvent ev){
        Player player = (Player) ev.getEntity();
        if(this.spawnTag.getTimer().containsKey(player.getUniqueId())){
            for(int x=(int)this.plugin.spawnMinX;x<=(int)this.plugin.spawnMaxX;++x){
                for(int y=4;y<=30;y++){
                    Location barrierLoc1 = new Location(player.getWorld(), x, y, this.plugin.spawnMinZ);
                    player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 1);
                    Location barrierLoc2 = new Location(player.getWorld(), x, y, this.plugin.spawnMaxZ);
                    player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 1);
                }
            }
            Bukkit.broadcastMessage("first loop ran");

            for(int z=(int)this.plugin.spawnMinZ;z<=(int)this.plugin.spawnMaxZ;++z){
                for(int y=4;y<=30;y++){
                    Location barrierLoc1 = new Location(player.getWorld(), this.plugin.spawnMinX, y, z);
                    player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 1);
                    Location barrierLoc2 = new Location(player.getWorld(), this.plugin.spawnMaxX, y, z);
                    player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 1);
                }
            }
            Bukkit.broadcastMessage("2nd loop ran");

        }
    }
}
