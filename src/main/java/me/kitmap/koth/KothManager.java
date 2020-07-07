package me.kitmap.koth;

import me.kitmap.Main;
import me.kitmap.scoreboard.PlayerBoards;
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
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KothManager {

    private final Main plugin;
    private final PlayerBoards playerBoards;
    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 40*20, 3);
    private static final PotionEffect REGEN = new PotionEffect(PotionEffectType.REGENERATION, 20*20, 1);
    private static final PotionEffect RESISTANCE = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 1);
    private static final String KOTHTEAMNAME = "kothTimer";

    private BukkitTask capCheckTask;
    private BukkitTask timeDisplayTask;
    private Koth koth;

    public KothManager(Main plugin, Koth koth, PlayerBoards playerBoards) {
        this.plugin = plugin;
        this.koth = koth;
        this.playerBoards = playerBoards;
    }

    public Koth getKoth(){
        return this.koth;
    }

    public void addKoth(Koth koth){
        this.koth = koth;
    }

    private boolean isInCap(@Nullable Player player) {
        if(player == null) return false;
        final Location location = player.getLocation();
        return this.koth.getMin().getBlockX() <= location.getBlockX() && location.getBlockX() <= this.koth.getMax().getBlockX() &&
                this.koth.getMin().getBlockZ() <= location.getBlockZ() && location.getBlockZ() <= this.koth.getMax().getBlockZ();
    }

    private ArrayList<Player> playersInCap(){
        ArrayList<Player> players = new ArrayList<>();
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

    public void tick(){ // every 10 ticks
        long remainingTime = this.koth.getRemainingTime();
        if(remainingTime <= 0L){ // CAPPED
            Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.GREEN + this.koth.getName() + ChatColor.YELLOW +
                    " was captured by " + ChatColor.GOLD + this.koth.getCapper().getName());
            giveKey(this.koth.getCapper());
            this.koth.getCapper().addPotionEffect(SPEED);
            this.koth.getCapper().addPotionEffect(REGEN);
            this.koth.getCapper().addPotionEffect(RESISTANCE);
            end();
        } else if(remainingTime == this.koth.getDefaultCaptureTime()){ // this.capper == null
            if(playersInCap().size() == 0){ // When cap is active but no one is capping
                return;
            } else {
                Player player = playersInCap().get(0);
                this.koth.setCapper(player); // pick a random capper if the previous capper is knocked
            }
            Bukkit.broadcastMessage("start time task"); // start time task when someone starts capping
            if(this.timeDisplayTask == null || this.timeDisplayTask.isCancelled()){
                this.timeDisplayTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        timeDisplayTick();
                    }
                }.runTaskTimer(plugin.getInstance(), 0L, 20L);
            }

            Bukkit.broadcastMessage(ChatColor.GREEN + this.koth.getCapper().getName() + ChatColor.WHITE +
                    " is controlling " + ChatColor.GREEN + this.koth.getName());
        } else { // remainingTime > 0
            if(!isInCap(this.koth.getCapper())){ // KNOCK
                this.koth.setCapper(null);
                Bukkit.broadcastMessage(ChatColor.GOLD + "[KOTH] " + ChatColor.YELLOW + "The control of "
                    + ChatColor.GREEN + koth.getName() + ChatColor.YELLOW + " was lost");
                resetCooldown();
                resetDisplayedTime();
                Bukkit.broadcastMessage("end time task");
                this.timeDisplayTask.cancel();
            }
//            if(this.koth.getCapper() != null){ // CAPPING
//                this.koth.getCapper().sendMessage("You are controlling " + this.koth.getName());
//            }
        }
    }

    private void timeDisplayTick(){ // every 20 ticks
        for(Player player: Bukkit.getOnlinePlayers()){
            Scoreboard playerBoard = this.playerBoards.getScoreboards().get(player.getUniqueId());
            Objective objective = playerBoard.getObjective("sb");
            int kothRemainingSeconds = Math.round(this.koth.getRemainingTime())/1000;
            String suffix = Integer.toString(kothRemainingSeconds);
            //String prefix = spawnTagSeconds.substring(0, spawnTagSeconds.length()/2);
            //String suffix = spawnTagSeconds.substring(spawnTagSeconds.length()/2);
            playerBoard.getTeam(KOTHTEAMNAME).setSuffix(suffix);
            objective.getScore(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ").setScore(5);
        }
    }

    private void resetDisplayedTime(){
        for(Player player: Bukkit.getOnlinePlayers()){
            Scoreboard playerBoard = this.playerBoards.getScoreboards().get(player.getUniqueId());
            Objective objective = playerBoard.getObjective("sb");
            int kothRemainingSeconds = Math.round(this.koth.getDefaultCaptureTime())/1000;
            String suffix = Integer.toString(kothRemainingSeconds);
            playerBoard.getTeam(KOTHTEAMNAME).setSuffix(suffix);
            objective.getScore(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ").setScore(5);
        }
    }

    private void removeDisplayedTime(){
        for(Player player: Bukkit.getOnlinePlayers()){
            Scoreboard playerBoard = this.playerBoards.getScoreboards().get(player.getUniqueId());
            if (playerBoard.getTeam(KOTHTEAMNAME) != null) {
                playerBoard.getTeam(KOTHTEAMNAME).unregister();
                playerBoard.resetScores(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ");
            }
        }
    }

    public void start(){
        if(this.koth == null) {
            return;
        }
        for(Player player: Bukkit.getOnlinePlayers()){
            Scoreboard playerBoard = this.playerBoards.getScoreboards().get(player.getUniqueId());
            Objective objective = playerBoard.getObjective("sb");
            int kothRemainingSeconds = Math.round(this.koth.getDefaultCaptureTime())/1000;
            String suffix = Integer.toString(kothRemainingSeconds);
            //String prefix = spawnTagSeconds.substring(0, spawnTagSeconds.length()/2);
            //String suffix = spawnTagSeconds.substring(spawnTagSeconds.length()/2);
            playerBoard.registerNewTeam(KOTHTEAMNAME);
            Team kothTimer = playerBoard.getTeam(KOTHTEAMNAME);
            kothTimer.addEntry(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ");
            kothTimer.setSuffix(suffix);
            //spawnTagCounter.setSuffix(suffix);
            objective.getScore(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ").setScore(5);
            playerBoard.getTeam(KOTHTEAMNAME).setSuffix(suffix);
        }

        this.capCheckTask = new BukkitRunnable() {
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
        removeDisplayedTime();
        Bukkit.broadcastMessage("end time task");
        this.timeDisplayTask.cancel();
        this.capCheckTask.cancel();
        this.koth = null;
    }

    private void giveKey(Player player){
        ItemStack kothkey = new ItemStack(Material.TRIPWIRE_HOOK);
        kothkey.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta kothkeyMeta  = kothkey.getItemMeta();
        kothkeyMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Koth Key");
        List<String> kothkeyLore = new ArrayList<>();
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