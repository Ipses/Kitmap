package me.kitmap.items.legendary;

import com.mojang.authlib.GameProfile;
import me.kitmap.Main;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SealOfEntropy extends Legendary implements Listener {

    private static final String NAME = ChatColor.RESET + "Seal of Entropy";
    private Main plugin;

    public SealOfEntropy(Main plugin) {
        super(plugin);
    }

    //@EventHandler
    public void onClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        if((ev.getAction() == Action.RIGHT_CLICK_AIR || ev.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                hasName(player.getInventory().getItemInMainHand(), NAME)) {
            MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
            WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
            EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(player.getUniqueId(), player.getName()), new PlayerInteractManager(nmsWorld));

            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            // npc should walk forward
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                public void run() {
                    // should despawn npc
                }
            }, 5*20L);
        }
    }

    public ItemStack getItem() {
        ItemStack sealofentropy = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.SKELETON.ordinal());
        ItemMeta sealofentropyItemMeta = sealofentropy.getItemMeta();
        List<String> sealofentropyLore = new ArrayList<String>();
        sealofentropyLore.add(net.md_5.bungee.api.ChatColor.DARK_PURPLE + "Lore here");
        sealofentropyLore.add(net.md_5.bungee.api.ChatColor.BLUE + ".");
        sealofentropyLore.add(net.md_5.bungee.api.ChatColor.BLUE + ".");
        sealofentropyItemMeta.setLore(sealofentropyLore);
        sealofentropyItemMeta.setDisplayName(NAME);
        sealofentropy.setItemMeta(sealofentropyItemMeta);
        return sealofentropy;
    }
}
