package me.kitmap.koth;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class KothManager {

    private BukkitTask task;
    public static Koth koth;

    public KothManager(Koth koth) {
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

    public void tick(){ // when KOTH is ACTIVE ( 15:00, no capper initially)
        long remainingTime = this.koth.getRemainingTime();
        Bukkit.broadcastMessage("remaining:" + remainingTime);
        if(remainingTime <= 0L){
            Bukkit.broadcastMessage(ChatColor.GREEN + this.koth.getName() + ChatColor.WHITE + " was captured by " +
                    ChatColor.GREEN + this.koth.getCapper().getName());
            Inventory capperInv = this.koth.getCapper().getInventory();
            capperInv.addItem(Main.kothkey);
            if(capperInv.firstEmpty() == -1){
                capperInv.setItem(17, Main.kothkey);
            }
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
                resetCooldown();
            }
            if(this.koth.getCapper() != null){
                this.koth.getCapper().sendMessage("You are controlling " + this.koth.getName());
                Bukkit.broadcastMessage("Someone is controlling " + this.koth.getName());
            }
        }
    }

    public void start(){
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(Main.getInstance(), 0L, 10L);
    }

    public void end(){
        this.koth = null;
        this.task.cancel();
        Bukkit.broadcastMessage("stop task");
    }
}