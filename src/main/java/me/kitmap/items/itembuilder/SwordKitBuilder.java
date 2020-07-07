package me.kitmap.items.itembuilder;

import me.kitmap.Main;
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

public class SwordKitBuilder extends ItemBuilder{

    private Inventory ironKit;

    public void build(){
        ironKit = Bukkit.createInventory(null, 36, "ironkit");

        ironKit.setItem(0, new ItemStack(Material.IRON_SWORD));
        ItemStack powerBow = new ItemStack(Material.BOW);
        powerBow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        ironKit.setItem(1, powerBow);

        ItemStack splash = new ItemStack(Material.SPLASH_POTION);
        PotionMeta splashMeta = (PotionMeta) splash.getItemMeta();
        splashMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
        splash.setItemMeta(splashMeta);
        ironKit.setItem(2, splash);
        ironKit.setItem(3, splash);

        ItemStack drink = new ItemStack(Material.POTION);
        PotionMeta drinkMeta = (PotionMeta) drink.getItemMeta();
        drinkMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
        drink.setItemMeta(drinkMeta);

        ironKit.setItem(4, drink);
        ironKit.setItem(5, drink);
        ironKit.setItem(6, new ItemStack(Material.GOLDEN_APPLE));
        ironKit.setItem(7, new ItemStack(Material.SUGAR));

        ItemStack grapple = new ItemStack(Material.FISHING_ROD);
        ItemMeta grappleMeta  = grapple.getItemMeta();
        grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
        grapple.setItemMeta(grappleMeta);
        ironKit.setItem(8, grapple);

        ironKit.setItem(9, new ItemStack(Material.ARROW, 15));

        ItemStack grenade = new ItemStack(Material.ENDER_PEARL);
        ItemMeta grenadeMeta  =grenade.getItemMeta();
        grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
        grenade.setItemMeta(grenadeMeta);

        ironKit.setItem(10, grenade);
        ironKit.setItem(11, new ItemStack(Material.GOLDEN_APPLE));
        ironKit.setItem(12, new ItemStack(Material.GOLDEN_APPLE));


        ironKit.setItem(13, new ItemStack(Material.COOKIE, 8));
        ironKit.setItem(14, new ItemStack(Material.COOKIE, 8));
        ironKit.setItem(15, new ItemStack(Material.MUSHROOM_SOUP));
        ironKit.setItem(16, new ItemStack(Material.MUSHROOM_SOUP));
        ironKit.setItem(17, new ItemStack(Material.MUSHROOM_SOUP));
        ironKit.setItem(18, new ItemStack(Material.ARROW, 15));
        ironKit.setItem(19, grenade);
        ironKit.setItem(20, splash);
        ironKit.setItem(21, splash);
        ironKit.setItem(22, drink);
        ironKit.setItem(23, drink);
        ironKit.setItem(24, drink);
        ironKit.setItem(25, drink);
        ironKit.setItem(26, drink);
        ironKit.setItem(27, new ItemStack(Material.ARROW, 15));
        ironKit.setItem(28, grenade);
        ironKit.setItem(29, splash);
        ironKit.setItem(30, splash);
        ironKit.setItem(31, drink);
        ironKit.setItem(32, drink);
        ironKit.setItem(33, drink);
        ironKit.setItem(34, drink);
        ironKit.setItem(35, new ItemStack(Material.SUGAR));
    }

    public Inventory getIronKit(){
        return this.ironKit;
    }
}
