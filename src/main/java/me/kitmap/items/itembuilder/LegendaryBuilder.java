package me.kitmap.items.itembuilder;

import me.kitmap.Main;
import me.kitmap.items.legendary.*;
import me.kitmap.items.minezitems.Sugar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LegendaryBuilder extends ItemBuilder {

    private final Main plugin;

    public LegendaryBuilder(Main plugin) {
        this.plugin = plugin;
    }
    private Inventory itemPage1, itemPage2, itemPage3;

    public void build(){
        this.itemPage1 = Bukkit.createInventory(null, 54, "Legendaries 1");
        this.itemPage2 = Bukkit.createInventory(null, 54, "Legendaries 2");
        this.itemPage3 = Bukkit.createInventory(null, 54, "MineZ Items");

       // Overkill overkill = this.plugin.getOverkill();
        PluviasTempest pluviasTempest = this.plugin.getPluviasTempest();
        Quiet quiet = this.plugin.getQuiet();
        RobbersBlade robbersBlade = this.plugin.getrobbersBlade();
        SealOfGravity sealOfGravity = this.plugin.getSealOfGravity();
        SealOfSpace sealOfSpace = this.plugin.getSealOfSpace();
        SealOfTime sealOfTime = this.plugin.getSealOfTime();
        Shotbow shotbow = this.plugin.getShotbow();
        SpikeThrower spikeThrower = this.plugin.getSpikeThrower();
        TruthBow truthBow = this.plugin.getTruthBow();
        Vampyr vampyr = this.plugin.getVampyr();
        WebShot webShot = this.plugin.getWebShot();
        ZombieBow zombieBow = this.plugin.getZombieBow();

        //ItemStack overkillItem = overkill.getItem();
        ItemStack pluviastempestItem = pluviasTempest.getItem();
        ItemStack quietItem = quiet.getItem();
        ItemStack robbersbladeItem = robbersBlade.getItem();
        ItemStack sealofgravityItem = sealOfGravity.getItem();
        ItemStack sealofspaceItem = sealOfSpace.getItem();
        ItemStack sealoftimeItem = sealOfTime.getItem();
        ItemStack shotbowItem = shotbow.getItem();
        ItemStack spikethrowerItem = spikeThrower.getItem();
        ItemStack truthbowItem = truthBow.getItem();
        ItemStack vampyrItem = vampyr.getItem();
        ItemStack webshotItem = webShot.getItem();
        ItemStack zombiebowItem = zombieBow.getItem();

        ItemStack grenade = new ItemStack(Material.ENDER_PEARL);
        ItemMeta grenadeMeta  =grenade.getItemMeta();
        grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
        grenade.setItemMeta(grenadeMeta);

        ItemStack sugar = new ItemStack(Material.SUGAR);

        ItemStack grapple = new ItemStack(Material.FISHING_ROD);
        ItemMeta grappleMeta  = grapple.getItemMeta();
        grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
        grapple.setItemMeta(grappleMeta);

        ItemStack nextpage1 = new ItemStack(Material.EMPTY_MAP);
        ItemMeta nextpage1Meta  = nextpage1.getItemMeta();
        nextpage1Meta.setDisplayName(ChatColor.RESET + "Next Page");
        nextpage1.setItemMeta(nextpage1Meta);

        ItemStack prevpage2 = new ItemStack(Material.EMPTY_MAP);
        ItemMeta prevpage2Meta  = prevpage2.getItemMeta();
        prevpage2Meta.setDisplayName(ChatColor.RESET + "Previous Page");
        prevpage2.setItemMeta(prevpage2Meta);

        ItemStack nextpage2 = new ItemStack(Material.EMPTY_MAP);
        ItemMeta nextpage2Meta  = nextpage2.getItemMeta();
        nextpage2Meta.setDisplayName(ChatColor.RESET + "Next Page");
        nextpage2.setItemMeta(nextpage2Meta);

        ItemStack prevpage3 = new ItemStack(Material.EMPTY_MAP);
        ItemMeta prevpage3Meta  = prevpage3.getItemMeta();
        prevpage3Meta.setDisplayName(ChatColor.RESET + "Previous Page");
        prevpage3.setItemMeta(prevpage3Meta);

        ItemStack nextpage3 = new ItemStack(Material.EMPTY_MAP);
        ItemMeta nextpage3Meta  = nextpage3.getItemMeta();
        nextpage3Meta.setDisplayName(ChatColor.RESET + "Next Page");
        nextpage3.setItemMeta(nextpage3Meta);

        ItemStack overkill = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta overkillItemMeta = overkill.getItemMeta();
        List<String> overkillLore = new ArrayList<String>();
        overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Legendary Weapon");
        overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Right Click: Charge at the cost of 50 durability");
        overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "After charging, your next attack in 5 seconds");
        overkillLore.add(net.md_5.bungee.api.ChatColor.BLUE + "does" + ChatColor.YELLOW + " true damage " + ChatColor.BLUE + "based on a player's missing health");
        overkillItemMeta.setLore(overkillLore);
        overkillItemMeta.setDisplayName(ChatColor.RESET + "Overkill");
        overkill.setItemMeta(overkillItemMeta);
        // Swords
//        itemPage1.setItem(0, corsairsedge);
//        itemPage1.setItem(1, endeavour);
//        itemPage1.setItem(2, flail);
//        itemPage1.setItem(3, gamble);
//        itemPage1.setItem(4, ipsesfolly);
//        itemPage1.setItem(5, kikuichimonji);
//        itemPage1.setItem(6, masamune);
//        itemPage1.setItem(7, muramasa);
//        itemPage1.setItem(8, nightsshadow);
        itemPage1.setItem(9, overkill);
//        itemPage1.setItem(10, peace);
        itemPage1.setItem(11, robbersbladeItem);
        itemPage1.setItem(12, vampyrItem);
        // Bows
//        itemPage1.setItem(18, slowbow);
//        itemPage1.setItem(19, healbow);
        itemPage1.setItem(20, quietItem);
        //itemPage1.setItem(21, rocksteady);
        itemPage1.setItem(22, shotbowItem);
        itemPage1.setItem(23, spikethrowerItem);
        itemPage1.setItem(24, truthbowItem);
//        itemPage1.setItem(25, venombow);
//        itemPage1.setItem(26, voidbow);
        itemPage1.setItem(27, webshotItem);
        itemPage1.setItem(28, zombiebowItem);



//        itemPage1.setItem(30, gorgonsgaze);
//        itemPage1.setItem(31, helmetofvision);
//        itemPage1.setItem(32, ninjasandals);
//        itemPage1.setItem(33, rabbitfeet);
//        itemPage1.setItem(34, rubbershield);
//
        itemPage1.setItem(53, nextpage1);

//        itemPage2.setItem(0, simoonssong);
//        itemPage2.setItem(1, simoonsmelody);
//        itemPage2.setItem(2, simoonssonata);
//        itemPage2.setItem(3, simoonstune);
//        itemPage2.setItem(9, therumsstrength);
//        itemPage2.setItem(10, therumspower);
//        itemPage2.setItem(11, therumsforce);
//        itemPage2.setItem(12, therumsmight);
//
//        itemPage2.setItem(18, agnisrage);
//        itemPage2.setItem(19, agnisfury);

        itemPage2.setItem(29, pluviastempestItem);

        itemPage2.setItem(8, sealoftimeItem);
        itemPage2.setItem(17, sealofspaceItem);
        itemPage2.setItem(26, sealofgravityItem);

        itemPage2.setItem(45, prevpage2);
        itemPage2.setItem(53, nextpage2);

        itemPage3.setItem(0, grenade);
        itemPage3.setItem(1, sugar);
        itemPage3.setItem(2, grapple);

        itemPage3.setItem(45, prevpage3);
        itemPage3.setItem(53, nextpage3);


    }

    public Inventory getItemPage1(){
        return this.itemPage1;
    }

    public Inventory getItemPage2(){
        return this.itemPage2;
    }

    public Inventory getItemPage3(){
        return this.itemPage3;
    }
}
