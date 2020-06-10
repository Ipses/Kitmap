package me.kitmap.loot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class KothCrate implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent ev){
        Player player = ev.getPlayer();
        Block block = ev.getClickedBlock();
        if(!ev.isCancelled() && ev.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST){
            if(isKey(player.getInventory().getItemInMainHand())){
                ev.setCancelled(true);
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + player.getName() + ChatColor.YELLOW +
                        " is obtaining loot for a " + ChatColor.GREEN + "KOTH Key");

                // give loot
            }
        }
    }

    private static final String NAME = ChatColor.BOLD + "" + ChatColor.AQUA + "Koth Key";
    private static boolean isKey(ItemStack is) {
        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(NAME)) {
            return true;
        }
        return false;
    }
}
