package me.kitmap.items.minezitems;

import me.kitmap.items.legendary.Legendary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HealKit implements Listener {

    private static final String HELMETNAME = ChatColor.RESET + "Healer Helmet";
    private static final String CHESTPLATENAMET = ChatColor.RESET + "Healer ChestPlate";
    private static final String LEGGINGSNAME = ChatColor.RESET + "Healer Leggings";
    private static final String BOOTSNAME = ChatColor.RESET + "Healer Boots";

    private static final String REGENITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.RED + " (Regeneration)";
    private static final String SPEEDITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.AQUA + " (Speed)";
    private static final String ABSORPTIONITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.YELLOW + " (Absorption)";
    private static final String RESISTANCEITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.GRAY + " (Resistance)";

    @EventHandler
    public void onLeftClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        ItemStack helmet = ev.getPlayer().getInventory().getHelmet();
        ItemStack chestplate = ev.getPlayer().getInventory().getChestplate();
        ItemStack legs = ev.getPlayer().getInventory().getLeggings();
        ItemStack boots = ev.getPlayer().getInventory().getBoots();
        ItemStack handItem = ev.getPlayer().getInventory().getItemInMainHand();
        if (isLeatherKit(helmet, chestplate, legs, boots) && handItem.getType() == Material.SHEARS) {
            if (ev.getAction() == Action.LEFT_CLICK_AIR) {
                String displayName = handItem.getItemMeta().getDisplayName();
                ItemMeta healKitItemMeta = handItem.getItemMeta();
                if (displayName.equals(REGENITEMNAME)) {
                   healKitItemMeta.setDisplayName(SPEEDITEMNAME);
                } else if (displayName.equals(SPEEDITEMNAME)) {
                   healKitItemMeta.setDisplayName(ABSORPTIONITEMNAME);
                } else if (displayName.equals(ABSORPTIONITEMNAME)) {
                    healKitItemMeta.setDisplayName(RESISTANCEITEMNAME);
                } else {
                    healKitItemMeta.setDisplayName(REGENITEMNAME);
                }
                handItem.setItemMeta(healKitItemMeta);
                player.updateInventory();
            }
        }
    }

    private boolean isLeatherChestplate(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_CHESTPLATE;
    }

    private boolean isLeatherHLeggings(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_LEGGINGS;
    }

    private boolean isLeatherBoots(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_BOOTS;
    }

    private boolean isLeatherHelmet(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_HELMET;
    }

    private boolean isLeatherKit(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        return isLeatherBoots(boots) && isLeatherChestplate(chestplate) && isLeatherHelmet(helmet) &&
                isLeatherHLeggings(leggings);
    }
}
