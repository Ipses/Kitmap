package me.kitmap.game;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnEnterBlocker implements Listener {

    private final Main plugin;
    private final SpawnTag spawnTag;
    private Set<UUID> processing = new HashSet<UUID>();
    private static final Location SPAWNLOCATION = new Location(Bukkit.getWorld("world"), -90, 4, 385);
    private static final int BLOCKVIEWDISTANCE = 6;
    private static final int BLOCKUPDATEDISTANCE = 15;

    private final ArrayList<Location> spawnBarrierBlocks;

    public SpawnEnterBlocker(Main plugin, SpawnTag spawnTag){
        this.plugin = plugin;
        this.spawnTag = spawnTag;
        this.spawnBarrierBlocks = this.plugin.getSpawnBarrierBlocks();
    }

//    @EventHandler // Current GG's method.
//    public void onPlayerJoin(PlayerJoinEvent ev) {
//        BukkitTask task = new BukkitRunnable() {
//            public void run() {
//                spawnBarrierBlocks();
//            }
//        }.runTaskTimer(plugin.getInstance(), 0L, 5L);
//    }

//    @EventHandler // Currently not needed because knockback calls movement event
//    public void onTag(SpawnTagStartEvent ev) {
//        final Player player = ev.getPlayer();
//        if (player.getLocation().distance(SPAWNLOCATION) >= 15) {
//            return;
//        }
//        spawnNearbyBarrierBlocks(player);
//    }

    private boolean isInSpawn(Location location) {
        double x = location.getX();
        double z = location.getZ();
        return plugin.spawnMinX < x && x < plugin.spawnMaxX && plugin.spawnMinZ < z && z < plugin.spawnMaxZ + 1.3;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent ev) {
        if (ev.getFrom().getX() == ev.getTo().getX() && ev.getFrom().getZ() == ev.getTo().getZ()) {
            return;
        }
        final Player player = ev.getPlayer();
        if (this.spawnTag.isTagged(player.getUniqueId()) && isInSpawn(ev.getTo())) {
            Bukkit.broadcastMessage(ChatColor.RED + "You cannot enter spawn while spawn tagged!");
            player.teleport(ev.getFrom());
        }
        if (this.processing.contains(ev.getPlayer().getUniqueId())) {
            return;
        }
        if (this.spawnTag.isTagged(player.getUniqueId())) {
            if (SPAWNLOCATION.getBlock().getLocation().distanceSquared(player.getLocation()) <
                    Math.pow(BLOCKUPDATEDISTANCE + 3, 2)) {
                this.processing.add(player.getUniqueId());
                Bukkit.broadcastMessage("process add");
                new BukkitRunnable() {
                    public void run() {
                        if (!isInSpawn(player.getLocation())) {
                            for (Location loc : spawnBarrierBlocks) {
                                if (loc.getBlock().getLocation().distanceSquared(player.getLocation()) <
                                        Math.pow(BLOCKVIEWDISTANCE, 2)) {
                                    player.sendBlockChange(loc, Material.STAINED_GLASS, (byte) 14);
                                } else {
                                    player.sendBlockChange(loc, Material.AIR, (byte) 0);
                                }
                            }
                            Bukkit.broadcastMessage("spawn block");
                        }
                    }
                }.runTaskAsynchronously(plugin.getInstance());

                new BukkitRunnable() {
                    public void run() {
                        if (processing.contains(player.getUniqueId())) {
                            processing.remove(player.getUniqueId());
                            Bukkit.broadcastMessage("process remove");
                        }
                    }
                }.runTaskLaterAsynchronously(plugin.getInstance(), 10L);
            }
        }
    }

    @EventHandler
    public void onSpawnTagExpire(SpawnTagExpireEvent ev) {
        removeBarrierBlocks(ev.getPlayer());
    }

    @EventHandler // TODO: PlayerInteractEvent doesn't detect right click with bare hand
    public void onBarrierClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        Location playerLoc = player.getLocation();
        if (this.spawnTag.isTagged(player.getUniqueId()) && (ev.getAction() == Action.RIGHT_CLICK_AIR ||
                ev.getAction() == Action.RIGHT_CLICK_BLOCK) && playerLoc.distance(SPAWNLOCATION) < 15) {
            Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
                public void run() {
                    spawnNearbyBarrierBlocks(player);
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent ev) {
        final Player player = ev.getEntity().getPlayer();
        removeBarrierBlocks(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        this.processing.remove(player.getUniqueId());
        removeBarrierBlocks(player);
    }

    // TODO: GG's spawn tag system has a bug that players can click blocks and enter spawn while tagged.
    public void spawnBarrierBlocks(Player player){
        for (Location location: this.plugin.getSpawnBarrierBlocks()) {
            player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
        }
    }

    public void spawnNearbyBarrierBlocks(Player player){
        for (Location location: this.plugin.getSpawnBarrierBlocks()) {
            if (player.getLocation().distance(location) < BLOCKVIEWDISTANCE) {
                player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
            }
        }
    }

    public void removeBarrierBlocks(Player player){
        for (Location location: this.plugin.getSpawnBarrierBlocks()) {
            player.sendBlockChange(location, Material.AIR, (byte) 0);
        }
    }
}
