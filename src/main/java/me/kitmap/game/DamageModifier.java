package me.kitmap.game;


import me.kitmap.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageModifier implements Listener {

    private final Main plugin;

    public DamageModifier(Main plugin){
        this.plugin = plugin;
    }

    // TODO: add logic for critical hits, (in air and descending)
    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev){
        if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player){
            Player player = (Player) ev.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if(weapon.getType() == Material.DIAMOND_SWORD){
                ev.setDamage(plugin.getDiamondSwordDamage());
            } else if(weapon.getType() == Material.IRON_SWORD){
                ev.setDamage(plugin.getIronSwordDamage());
            } else if(weapon.getType() == Material.STONE_SWORD){
                ev.setDamage(plugin.getStoneSwordDamage());
            } else if(weapon.getType() == Material.WOOD_SWORD){
                ev.setDamage(plugin.getWoodSwordDamage());
            } else if(weapon.getType() == Material.GOLD_SWORD){
                ev.setDamage(plugin.getGoldSwordDamage());
            }
        }
    }
}
