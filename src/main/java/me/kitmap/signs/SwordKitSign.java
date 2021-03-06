package me.kitmap.signs;

import me.kitmap.game.KitChangeEvent;
import me.kitmap.items.itembuilder.SwordKitBuilder;
import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffect;

public class SwordKitSign implements Listener {

    private static final String LINE1 = "[Kit]";
    private static final String LINE2 = "Iron";
    private final SwordKitBuilder swordKitBuilder;

    public SwordKitSign(SwordKitBuilder swordKitBuilder){
        this.swordKitBuilder = swordKitBuilder;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent ev){
        Inventory ironKit = swordKitBuilder.getIronKit();
        Player player = ev.getPlayer();
        Block block = ev.getClickedBlock();
        if(ev.getAction() == Action.RIGHT_CLICK_BLOCK && block.getState() != null && block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            if(sign.getLine(1).equalsIgnoreCase(LINE1) && sign.getLine(2).equalsIgnoreCase(LINE2)){
                for(int i = 0; i < ironKit.getContents().length; ++i)
                    player.getInventory().setItem(i, ironKit.getContents()[i]);
                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                Bukkit.getServer().getPluginManager().callEvent(new KitChangeEvent(player, "sword"));
            }
        }
    }
}
