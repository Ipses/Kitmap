package me.kitmap.koth;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KothManager {

    private final Main plugin;
    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 40*20, 3);
    private static final PotionEffect REGEN = new PotionEffect(PotionEffectType.REGENERATION, 20*20, 1);
    private static final PotionEffect RESISTANCE = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 1);

    private BukkitTask task;
    public Koth koth;

    public KothManager(Main plugin, Koth koth) {
        this.plugin = plugin;
        this.koth = koth;
    }

    public Koth getKoth(){
        return this.koth;
    }

    public void addKoth(Koth koth){
        this.koth = koth;
    }
    private boolean isInCap(@Nullable Player player) {
        final Location location = player.getLocation();
        if(this.koth.getMin().getBlockX() <= location.getBlockX() && location.getBlockX() <= this.koth.getMax().getBlockX() &&
                this.koth.getMin().getBlockZ() <= location.getBlockZ() && location.getBlockZ() <= this.koth.getMax().getBlockZ()) {
            return true;
        }
        return false;
    }

    private ArrayList<Player> playersInCap(){
        ArrayList<Player> players = new ArrayList<Player>();
        for(Player player: Bukkit.getOnlinePlayers()){
            if (isInCap(player)){
                players.add(player);
            }
        }
        return players;
    }

    private void resetCooldown(){
        this.koth.remainingTime = this.koth.getDefaultCaptureTime() + System.currentTimeMillis();
    }

    public void tick(){
        long remainingTime = this.koth.getRemainingTime();
        Bukkit.broadcastMessage("remaining:" + remainingTime);
        if(remainingTime <= 0L){
            Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.GREEN + this.koth.getName() + ChatColor.YELLOW +
                    " was captured by " + ChatColor.GOLD + this.koth.getCapper().getName());
            giveKey(this.koth.getCapper());
            this.koth.getCapper().addPotionEffect(SPEED);
            this.koth.getCapper().addPotionEffect(REGEN);
            this.koth.getCapper().addPotionEffect(RESISTANCE);
            end();
        } else if(remainingTime == this.koth.getDefaultCaptureTime()){ // this.capper == null
            if(playersInCap().size() == 0){
                return;
            }
            if(playersInCap().size() == 1){
                this.koth.setCapper(playersInCap().get(0));
            }
            if(playersInCap().size() > 1){
                Player randomPlayer = playersInCap().get(new Random().nextInt(playersInCap().size()));
                this.koth.setCapper(randomPlayer); // pick a random capper if the previous capper is knocked
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + this.koth.getCapper().getName() + ChatColor.WHITE +
                    " is controlling " + ChatColor.GREEN + this.koth.getName());
        } else { // remainingTime > 0
            if(!isInCap(this.koth.getCapper())){
                this.koth.setCapper(null);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW + "The control of "
                    + ChatColor.GREEN + koth.getName() + ChatColor.YELLOW + " was lost");
                resetCooldown();
            }
            if(this.koth.getCapper() != null){
                this.koth.getCapper().sendMessage("You are controlling " + this.koth.getName());
                Bukkit.broadcastMessage("Someone is controlling " + this.koth.getName());
            }
        }
    }

    public void start(){
        if(this.koth == null) {
            return;
        }
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(plugin.getInstance(), 0L, 10L);
        Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.GREEN + koth.getName() + ChatColor.YELLOW +
                " KOTH is opened");
    }

    public boolean isRunning(){
        return this.koth != null;
    }

    public void end(){
        this.koth = null;
        this.task.cancel();
    }

    private void giveKey(Player player){
        ItemStack kothkey = new ItemStack(Material.TRIPWIRE_HOOK);
        kothkey.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta kothkeyMeta  = kothkey.getItemMeta();
        kothkeyMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Koth Key");
        List<String> kothkeyLore = new ArrayList<String>();
        kothkeyLore.add(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + "Click an ender chest at spawn to claim your reward!");
        kothkeyMeta.setLore(kothkeyLore);
        kothkey.setItemMeta(kothkeyMeta);

        Inventory capperInv = this.koth.getCapper().getInventory();
        if(capperInv.firstEmpty() == -1){
            capperInv.setItem(17, kothkey);
        } else {
            capperInv.addItem(kothkey);
        }
    }
}