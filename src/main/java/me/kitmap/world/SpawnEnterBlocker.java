package me.kitmap.world;

import me.kitmap.timer.CombatTagTimer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnEnterBlocker implements Listener {

    private ConcurrentHashMap<UUID, Long> pvptimer = CombatTagTimer.timer;

    @EventHandler
    public void onMove(PlayerMoveEvent ev){
        Player player = ev.getPlayer();
        if(pvptimer.contains(player.getUniqueId())){

            Location barrierLoc = new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY(),
                    player.getEyeLocation().getZ() + 3);
            player.sendBlockChange(barrierLoc, Material.STAINED_GLASS, (byte) 1);
        }
    }
//    MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
//    WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
//    EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager(nmsWorld));
//	        npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
//
//    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
//			connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
//			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
}
