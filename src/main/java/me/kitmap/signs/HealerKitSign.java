package me.kitmap.signs;

import me.kitmap.game.KitChangeEvent;
import me.kitmap.items.itembuilder.HealerKitBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class HealerKitSign implements Listener {

    private static final String LINE1 = "[Kit]";
    private static final String LINE2 = "Healer";
    private final HealerKitBuilder healerKitBuilder;

    public HealerKitSign(HealerKitBuilder healerKitBuilder){
        this.healerKitBuilder = healerKitBuilder;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent ev){
        Inventory healerKit = healerKitBuilder.getHealerKit();
        Player player = ev.getPlayer();
        Block block = ev.getClickedBlock();
        if(ev.getAction() == Action.RIGHT_CLICK_BLOCK && block.getState() != null && block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            if(sign.getLine(1).equalsIgnoreCase(LINE1) && sign.getLine(2).equalsIgnoreCase(LINE2)){
                for(int i = 0; i < healerKit.getContents().length; ++i)
                    player.getInventory().setItem(i, healerKit.getContents()[i]);

                ItemStack healerHelmet = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta healerHelmetMeta = (LeatherArmorMeta) healerHelmet.getItemMeta();
                healerHelmetMeta.setDisplayName(ChatColor.RESET + "Healer Helmet");
                healerHelmetMeta.setColor(Color.LIME);
                healerHelmet.setItemMeta(healerHelmetMeta);

                ItemStack healerChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta healerChestplateItemMeta = (LeatherArmorMeta) healerChestplate.getItemMeta();
                healerChestplateItemMeta.setDisplayName(ChatColor.RESET + "Healer Chestplate");
                healerChestplateItemMeta.setColor(Color.LIME);
                healerChestplate.setItemMeta(healerChestplateItemMeta);

                ItemStack healerLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
                LeatherArmorMeta healerLeggingsItemMeta = (LeatherArmorMeta) healerLeggings.getItemMeta();
                healerLeggingsItemMeta.setDisplayName(ChatColor.RESET + "Healer Leggings");
                healerLeggingsItemMeta.setColor(Color.LIME);
                healerLeggings.setItemMeta(healerLeggingsItemMeta);

                ItemStack healerBoots = new ItemStack(Material.LEATHER_BOOTS);
                LeatherArmorMeta healerBootsItemMeta = (LeatherArmorMeta) healerBoots.getItemMeta();
                healerBootsItemMeta.setDisplayName(ChatColor.RESET + "Healer Boots");
                healerBootsItemMeta.setColor(Color.LIME);
                healerBoots.setItemMeta(healerBootsItemMeta);

                player.getInventory().setHelmet(healerHelmet);
                player.getInventory().setChestplate(healerChestplate);
                player.getInventory().setLeggings(healerLeggings);
                player.getInventory().setBoots(healerBoots);
                Bukkit.getServer().getPluginManager().callEvent(new KitChangeEvent(player, "healer"));
            }
        }
    }
}
