package me.kitmap.koth;

import me.kitmap.Main;
import me.kitmap.items.itembuilder.KothLootBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class KothCrate implements Listener {

    private final KothLootBuilder kothLootBuilder;
    private static final PotionEffect GLOW = new PotionEffect(PotionEffectType.GLOWING, 5*20, 1);

    public KothCrate(KothLootBuilder kothLootBuilder){
        this.kothLootBuilder = kothLootBuilder;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent ev){
        Inventory kothloot = this.kothLootBuilder.getKothloot();
        Player player = ev.getPlayer();
        Block block = ev.getClickedBlock();
        if(!ev.isCancelled() && ev.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST){
            if(isKey(player.getInventory().getItemInMainHand())){
                ev.setCancelled(true);
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + player.getName() + ChatColor.YELLOW +
                        " is obtaining loot for a " + ChatColor.GREEN + "KOTH Key");
                player.sendMessage(ChatColor.RED + "Make sure you have a few empty slots in your inventory, " +
                        "otherwise you might lose some items!");
                player.addPotionEffect(GLOW);
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                    public void run() {
                        ArrayList<Integer> itemSlots = new ArrayList<>();
                        for(int i=0; i<27; i++) {
                            if(kothloot.getItem(i) != null) {
                                itemSlots.add(i);
                            }
                        }
                        Inventory inv = player.getInventory();
                        int randInt1 = itemSlots.get(new Random().nextInt(itemSlots.size()));
                        ItemStack randomLoot1 = kothloot.getItem(randInt1);
                        int randInt2 = itemSlots.get(new Random().nextInt(itemSlots.size()));
                        ItemStack randomLoot2 = kothloot.getItem(randInt2);

                        if(inv.firstEmpty() == -1){
                            inv.setItem(16, randomLoot1);
                            inv.setItem(17, randomLoot2);
                        } else {
                            inv.addItem(randomLoot1);
                            inv.addItem(randomLoot2);
                        }
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + player.getName() + ChatColor.YELLOW +
                            " obtained " + ChatColor.YELLOW + randomLoot1.getItemMeta().getDisplayName() + ", " +
                            randomLoot2.getItemMeta().getDisplayName());
                    }
                }, 5*20L);
            }
        }
        if(!ev.isCancelled() && ev.getAction() == Action.LEFT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST){
            player.openInventory(this.kothLootBuilder.getKothloot());
        }
    }

    private static final String NAME = ChatColor.BOLD + "" + ChatColor.AQUA + "Koth Key";
    private static boolean isKey(ItemStack is) {
        return is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(NAME);
    }
}
