package me.kitmap.game;


import me.kitmap.Main;
import me.kitmap.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageModifier implements Listener {

    private final ConfigManager configManager;
    private double wood, stone, iron, diamond, gold;

    public DamageModifier(ConfigManager configManager){
        this.configManager = configManager;
    }

    public void loadDamageValues(){ // this will be called onEnable in Main class.
        this.diamond = Double.parseDouble(configManager.getDamage().getString("diamond_sword"));
        this.iron = Double.parseDouble(configManager.getDamage().getString("iron_sword"));
        this.stone = Double.parseDouble(configManager.getDamage().getString("stone_sword"));
        this.wood = Double.parseDouble(configManager.getDamage().getString("wood_sword"));
        this.gold = Double.parseDouble(configManager.getDamage().getString("gold_sword"));
    }

    private boolean isCriticalHit(Player player) {
        return !player.isOnGround() && player.getFallDistance() > 0;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent ev){
        if(!ev.isCancelled() && ev.getDamager() instanceof Player && ev.getEntity() instanceof Player){
            Player player = (Player) ev.getDamager();
            ItemStack weapon = player.getInventory().getItemInMainHand();
            double multiplier = isCriticalHit(player) ? 1.5 : 1;
            switch(weapon.getType()){
                default:
                    return;
                case DIAMOND_SWORD:
                    ev.setDamage(this.diamond * multiplier);
                    break;
                case IRON_SWORD:
                    ev.setDamage(this.iron * multiplier);
                    break;
                case STONE_SWORD:
                    ev.setDamage(this.stone * multiplier);
                    break;
                case WOOD_SWORD:
                    ev.setDamage(this.wood * multiplier);
                    break;
                case GOLD_SWORD:
                    ev.setDamage(this.gold * multiplier);
                    break;
            }
        }
    }
}
