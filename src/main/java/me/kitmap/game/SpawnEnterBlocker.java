package me.kitmap.game;

import com.mojang.authlib.GameProfile;
import me.kitmap.Main;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpawnEnterBlocker implements Listener {

    private final Main plugin;
    private final SpawnTag spawnTag;
    private Set<UUID> processing = new HashSet<UUID>();
    private static final Location SPAWNLOCATION = new Location(Bukkit.getWorld("world"), -90, 4, 385);
    private final ArrayList<Location> spawnBarrierBlocks;

    public SpawnEnterBlocker(Main plugin, SpawnTag spawnTag){
        this.plugin = plugin;
        this.spawnTag = spawnTag;
        this.spawnBarrierBlocks = this.plugin.getSpawnBarrierBlocks();
    }

//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent ev) {
//        BukkitTask task = new BukkitRunnable() {
//            public void run() {
//                spawnBarrierBlocks();
//            }
//        }.runTaskTimer(plugin.getInstance(), 0L, 5L);
//    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getDamager();
            Player victim = (Player) ev.getEntity();
            spawnBarrierBlocks(player);
            spawnBarrierBlocks(victim);
        }
    }

//    @EventHandler
//    public void onMove(PlayerMoveEvent ev) {
//
//        Player player = ev.getPlayer();
//        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
//        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager(nmsWorld));
//
//        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
//        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//
//        connection.sendPacket(new PacketPlayOutBlockChange(player.getTargetBlock(null, 5)));
//        // npc should walk forward
//        Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
//            public void run() {
//                // should despawn npc
//            }
//        }, 5*20L);
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
            Bukkit.broadcastMessage("cancel event");
            player.teleport(ev.getFrom()); // this responses faster than putting it in async task
        }
        if (this.processing.contains(ev.getPlayer().getUniqueId())) {
            return;
        }

        if (this.spawnTag.isTagged(player.getUniqueId())) {
//            if (isInSpawn(ev.getTo())) {
//                Bukkit.broadcastMessage("cancel event");
//                player.teleport(ev.getFrom()); // this responses faster than putting it in async task
//            }
            if (!this.processing.contains(player.getUniqueId()) && SPAWNLOCATION.distance(player.getLocation()) < 18) {
                this.processing.add(player.getUniqueId());
                Bukkit.broadcastMessage("process add");
                new BukkitRunnable() {
                    public void run() {
                        if (!isInSpawn(player.getLocation())) {
                            for (Location loc : spawnBarrierBlocks) {
                                if (loc.distance(player.getLocation()) < 18) {
                                    player.sendBlockChange(loc, Material.STAINED_GLASS, (byte) 14);
                                }
                            }
                            Bukkit.broadcastMessage("spawn block");
                        }
                    }
                }.runTaskAsynchronously(plugin.getInstance());

//                if (processing.contains(player.getUniqueId())) {
//                    processing.remove(player.getUniqueId());
//                    Bukkit.broadcastMessage("process remove");
//                }

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

//    @EventHandler
//    public void onShoot(EntityDamageByEntityEvent ev) {
//        if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
//                ev.getEntity() instanceof Player) {
//            Player player = (Player) ((Projectile) ev.getDamager()).getShooter();
//            Player victim = (Player) ev.getEntity();
//            spawnBarrierBlocks(player);
//            spawnBarrierBlocks(victim);
//        }
//    }

    @EventHandler
    public void onSpawnTagExpire(SpawnTagExpireEvent ev) {
        removeBarrierBlocks(ev.getPlayer());
    }

//    @EventHandler // TODO: PlayerInteractEvent doesn't detect right click with bare hand
//    public void onBarrierClick(PlayerInteractEvent ev) {
//        Bukkit.broadcastMessage(ev.getEventName());
//        Player player = ev.getPlayer();
//        Location playerLoc = player.getLocation();
//        Location spawnLoc = new Location(player.getWorld(), -90, 4, 385);
//        if (this.spawnTag.isTagged(player.getUniqueId()) && (ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) && playerLoc.distance(spawnLoc) < 20) {
//            Bukkit.getScheduler().runTaskLater(plugin.getInstance(), new Runnable() {
//                public void run() {
//                    spawnBarrierBlocks(player);
//                    Bukkit.broadcastMessage("run");
//                    Bukkit.broadcastMessage("actionL" + ev.getAction().name());
//                    Bukkit.broadcastMessage("distance: " + playerLoc.distance(spawnLoc));
//                }
//            }, 1L);
//        }
//    }

    @EventHandler
    public void onDeath(PlayerDeathEvent ev) {
        Player player = ev.getEntity().getPlayer();
        removeBarrierBlocks(player);
    }

    // TODO: implement this. Try GG's spawn tag system. but it has a bug that players can click blocks and despawn. This lags as well
    public void spawnBarrierBlocks(Player player){
        for (Location location: this.plugin.getSpawnBarrierBlocks()) {
            player.sendBlockChange(location, Material.STAINED_GLASS, (byte) 14);
        }
//        for(int x=(int)this.plugin.spawnMinX;x<=(int)this.plugin.spawnMaxX;++x){
//            for(int y=4;y<=15;++y){
//                Location barrierLoc1 = new Location(player.getWorld(), x, y, this.plugin.spawnMinZ);
//                player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 14);
//                Location barrierLoc2 = new Location(player.getWorld(), x, y, this.plugin.spawnMaxZ);
//                player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 14);
//            }
//        }
//        for(int z=(int)this.plugin.spawnMinZ;z<=(int)this.plugin.spawnMaxZ;++z){
//            for(int y=4;y<=15;++y){
//                Location barrierLoc1 = new Location(player.getWorld(), this.plugin.spawnMinX, y, z);
//                player.sendBlockChange(barrierLoc1, Material.STAINED_GLASS, (byte) 14);
//                Location barrierLoc2 = new Location(player.getWorld(), this.plugin.spawnMaxX, y, z);
//                player.sendBlockChange(barrierLoc2, Material.STAINED_GLASS, (byte) 14);
//            }
//        }
    }

    public void removeBarrierBlocks(Player player){
        for (Location location: this.plugin.getSpawnBarrierBlocks()) {
            player.sendBlockChange(location, Material.AIR, (byte) 0);
        }
//        for(int x=(int)this.plugin.spawnMinX;x<=(int)this.plugin.spawnMaxX;++x){
//            for(int y=4;y<=15;++y){
//                Location barrierLoc1 = new Location(player.getWorld(), x, y, this.plugin.spawnMinZ);
//                player.sendBlockChange(barrierLoc1, Material.AIR, (byte) 0);
//                Location barrierLoc2 = new Location(player.getWorld(), x, y, this.plugin.spawnMaxZ);
//                player.sendBlockChange(barrierLoc2, Material.AIR, (byte) 0);
//            }
//        }
//        for(int z=(int)this.plugin.spawnMinZ;z<=(int)this.plugin.spawnMaxZ;++z){
//            for(int y=4;y<=15;++y){
//                Location barrierLoc1 = new Location(player.getWorld(), this.plugin.spawnMinX, y, z);
//                player.sendBlockChange(barrierLoc1, Material.AIR, (byte) 0);
//                Location barrierLoc2 = new Location(player.getWorld(), this.plugin.spawnMaxX, y, z);
//                player.sendBlockChange(barrierLoc2, Material.AIR, (byte) 0);
//            }
//        }
    }
}
