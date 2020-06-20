package me.kitmap.items.itembuilder;

import me.kitmap.Main;
import me.kitmap.items.legendary.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KothLootBuilder {

    private final Main plugin;
    private Inventory kothloot;

    public KothLootBuilder(Main plugin){
        this.plugin = plugin;
    }

    public void build(){
        this.kothloot = Bukkit.createInventory(null, 27, "Koth Loot");
        ItemStack overkill = this.plugin.getOverkill().getItem();
        ItemStack vampyr = this.plugin.getVampyr().getItem();
        ItemStack webshot = this.plugin.getWebShot().getItem();
        ItemStack truthBow = this.plugin.getTruthBow().getItem();
        ItemStack zombieBow = this.plugin.getZombieBow().getItem();
        ItemStack shotbow = this.plugin.getShotbow().getItem();

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 5);
        ItemMeta goldenAppleMeta = goldenApple.getItemMeta();
        goldenAppleMeta.setDisplayName(ChatColor.RESET + "Golden Apple");
        goldenApple.setItemMeta(goldenAppleMeta);

        ItemStack grapple = new ItemStack(Material.FISHING_ROD);
        ItemMeta grappleMeta = grapple.getItemMeta();
        grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
        grapple.setItemMeta(grappleMeta);

        this.kothloot.setItem(0, overkill);
        this.kothloot.setItem(1, vampyr);
        this.kothloot.setItem(2, webshot);
        this.kothloot.setItem(3, truthBow);
        this.kothloot.setItem(4, zombieBow);
        this.kothloot.setItem(5, shotbow);
        this.kothloot.setItem(6, goldenApple);
        this.kothloot.setItem(7, grapple);
    }

    public Inventory getKothloot(){
        return this.kothloot;
    }
}
