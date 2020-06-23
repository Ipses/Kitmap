package me.kitmap.game;


import me.kitmap.Main;
import me.kitmap.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class DamageModifier implements Listener {

    private final Main plugin;
    private final ConfigManager configManager;
    private double wood, stone, iron, diamond, gold;

    public DamageModifier(Main plugin, ConfigManager configManager){
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void loadDamageValues(){ // this will be called onEnable in Main class.
        this.diamond = Double.parseDouble(configManager.getDamage().getString("diamond_sword"));
        this.iron = Double.parseDouble(configManager.getDamage().getString("iron_sword"));
        this.stone = Double.parseDouble(configManager.getDamage().getString("stone_sword"));
        this.wood = Double.parseDouble(configManager.getDamage().getString("wood_sword"));
        this.gold = Double.parseDouble(configManager.getDamage().getString("gold_sword"));
    }

    // TODO: add logic for critical hits, (in air and descending)
    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev){
        if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player){
            Player player = (Player) ev.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if(weapon.getType() == Material.DIAMOND_SWORD){
                ev.setDamage(this.diamond);
                Bukkit.broadcastMessage("dmg:" + this.diamond);
            } else if(weapon.getType() == Material.IRON_SWORD){
                ev.setDamage(this.iron);
            } else if(weapon.getType() == Material.STONE_SWORD){
                ev.setDamage(this.stone);
            } else if(weapon.getType() == Material.WOOD_SWORD){
                ev.setDamage(this.wood);
            } else if(weapon.getType() == Material.GOLD_SWORD){
                ev.setDamage(this.gold);
            }
        }
    }
}
