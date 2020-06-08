package me.kitmap.items.legendary;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.BlockCactus;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class WebShot extends Legendary implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent ev) {
        if(!ev.isCancelled() && isItem(ev.getBow())) {
            Player player = (Player) ev.getEntity();
            if(ev.getForce() == 1) {
                ev.getProjectile().setMetadata("webshot", new FixedMetadataValue(Main.getInstance(), true) );
            } else {
                player.sendMessage(org.bukkit.ChatColor.RED + "The bow was not fully charged");
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent ev){
        if(ev.getEntity().getShooter() instanceof Player && ev.getEntity() instanceof Arrow && ev.getEntity().hasMetadata("webshot")){
            Player player = (Player) ev.getEntity().getShooter();
            Arrow proj = (Arrow) ev.getEntity();
            proj.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
            if(ev.getHitBlock() == null) return;
            Location location = ev.getHitBlock().getLocation();
            Block block = location.getBlock();

            Location north = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() - 1);
            Location south = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() + 1);
            Location east = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ());
            Location west = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ());
            Location center = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ());
            Location up = new Location(block.getWorld(), block.getX(), block.getY() + 2, block.getZ());

            ArrayList<Location> webLocations = new ArrayList<>();
            webLocations.add(north);
            webLocations.add(south);
            webLocations.add(west);
            webLocations.add(east);
            webLocations.add(center);
            webLocations.add(up);

            for(Location loc: webLocations){
                if(loc.getBlock().getType() == Material.AIR){
                    loc.getBlock().setType(Material.WEB);
                }
            }

            double distance = player.getLocation().distance(location);
            int duration;
            if(distance < 10) duration = 1;
            else if (10 <= distance && distance < 20) duration = 2;
            else if (20 <= distance && distance < 40) duration = 3;
            else duration = 5;

            Bukkit.broadcastMessage("distance: " + distance);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                public void run() {
                   for(Location loc: webLocations){
                       if(loc.getBlock().getType() == Material.WEB){
                           loc.getBlock().setType(Material.AIR);
                       }
                   }
                }
            }, duration*20L);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
                ev.getEntity() instanceof Player && ev.getDamager().hasMetadata("webshot")) {
            ev.setCancelled(true);
            Player player = (Player) ((Projectile) ev.getDamager()).getShooter();
            Player victim = (Player) ev.getEntity();

            if(isItem(player.getInventory().getItemInMainHand())){
                Block block = victim.getLocation().getBlock();
                Location north = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
                Location south = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
                Location east = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ());
                Location west = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ());
                Location center = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
                Location up = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ());
                ArrayList<Location> webLocations = new ArrayList<>();
                webLocations.add(north);
                webLocations.add(south);
                webLocations.add(west);
                webLocations.add(east);
                webLocations.add(center);
                webLocations.add(up);

                for(Location loc: webLocations){
                    if(loc.getBlock().getType() == Material.AIR){
                        loc.getBlock().setType(Material.WEB);
                    }
                }

                double distance = player.getLocation().distance(block.getLocation());
                int duration;
                if(distance < 10) duration = 1;
                else if (10 <= distance && distance < 20) duration = 2;
                else if (20 <= distance && distance < 40) duration = 3;
                else duration = 5;

                Bukkit.broadcastMessage("distance: " + distance);
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    public void run() {
                        for(Location loc: webLocations){
                            if(loc.getBlock().getType() == Material.WEB){
                                loc.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }, duration*20L);
            }
        }
    }
    
    private static final String name = ChatColor.RESET + "Web Shot";
    private static boolean isItem(ItemStack is) {
        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
            return true;
        }
        return false;
    }
}
