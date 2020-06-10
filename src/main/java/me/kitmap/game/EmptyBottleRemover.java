package me.kitmap.game;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class EmptyBottleRemover implements Listener {

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent ev){
        ItemStack item = ev.getItem();
        if(!ev.isCancelled() && item.getType() == Material.POTION){
            Player player = ev.getPlayer();
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                public void run() {
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    if(itemInHand.getType() == Material.GLASS_BOTTLE){
                        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                        player.updateInventory();
                    }
                }
            }, 1L);
        }
    }
}
