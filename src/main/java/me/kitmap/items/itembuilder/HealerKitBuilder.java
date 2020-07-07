package me.kitmap.items.itembuilder;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class HealerKitBuilder {

    private Inventory healerKit;

    public void build(){
        healerKit = Bukkit.createInventory(null, 36, "healerkit");

        healerKit.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack powerBow = new ItemStack(Material.BOW);
        powerBow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        healerKit.setItem(1, powerBow);

        ItemStack splash = new ItemStack(Material.SPLASH_POTION);
        PotionMeta splashMeta = (PotionMeta) splash.getItemMeta();
        splashMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
        splash.setItemMeta(splashMeta);
        healerKit.setItem(2, splash);
        healerKit.setItem(3, splash);

        ItemStack healKit = new ItemStack(Material.SHEARS);
        ItemMeta healKitItemMeta = healKit.getItemMeta();
        List<String> healKitLore = new ArrayList<>();
        healKitLore.add(ChatColor.BLUE + "Left Click: Choose healing effect");
        healKitLore.add(ChatColor.BLUE + "Right Click: Gives an effect to you and");
        healKitLore.add(ChatColor.BLUE + "the player you are looking at within 5 blocks");
        healKitLore.add(ChatColor.BLUE + "if you are not looking at any players,");
        healKitLore.add(ChatColor.BLUE + "effects only go to you.");
        healKitItemMeta.setLore(healKitLore);
        healKitItemMeta.setDisplayName(ChatColor.RESET + "Heal Kit" + ChatColor.RED + " (Regeneration)");
        healKit.setItemMeta(healKitItemMeta);

        healerKit.setItem(4, healKit);

        ItemStack drink = new ItemStack(Material.POTION);
        PotionMeta drinkMeta = (PotionMeta) drink.getItemMeta();
        drinkMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
        drink.setItemMeta(drinkMeta);

        healerKit.setItem(5, drink);
        healerKit.setItem(6, new ItemStack(Material.GOLDEN_APPLE));
        healerKit.setItem(7, new ItemStack(Material.SUGAR));

        ItemStack grapple = new ItemStack(Material.FISHING_ROD);
        ItemMeta grappleMeta  = grapple.getItemMeta();
        grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
        grapple.setItemMeta(grappleMeta);
        healerKit.setItem(8, grapple);

        healerKit.setItem(9, new ItemStack(Material.ARROW, 15));

        ItemStack grenade = new ItemStack(Material.ENDER_PEARL);
        ItemMeta grenadeMeta  =grenade.getItemMeta();
        grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
        grenade.setItemMeta(grenadeMeta);

        healerKit.setItem(10, grenade);
        healerKit.setItem(11, new ItemStack(Material.GOLDEN_APPLE));
        healerKit.setItem(12, new ItemStack(Material.GOLDEN_APPLE));


        healerKit.setItem(13, new ItemStack(Material.COOKIE, 8));
        healerKit.setItem(14, new ItemStack(Material.COOKIE, 8));
        healerKit.setItem(15, new ItemStack(Material.MUSHROOM_SOUP));
        healerKit.setItem(16, new ItemStack(Material.MUSHROOM_SOUP));
        healerKit.setItem(17, new ItemStack(Material.MUSHROOM_SOUP));
        healerKit.setItem(18, new ItemStack(Material.ARROW, 15));
        healerKit.setItem(19, grenade);
        healerKit.setItem(20, splash);
        healerKit.setItem(21, splash);
        healerKit.setItem(22, drink);
        healerKit.setItem(23, drink);
        healerKit.setItem(24, drink);
        healerKit.setItem(25, drink);
        healerKit.setItem(26, drink);
        healerKit.setItem(27, new ItemStack(Material.ARROW, 15));
        healerKit.setItem(28, grenade);
        healerKit.setItem(29, splash);
        healerKit.setItem(30, splash);
        healerKit.setItem(31, drink);
        healerKit.setItem(32, drink);
        healerKit.setItem(33, drink);
        healerKit.setItem(34, drink);
        healerKit.setItem(35, new ItemStack(Material.SUGAR));
    }

    public Inventory getHealerKit(){
        return this.healerKit;
    }
}

