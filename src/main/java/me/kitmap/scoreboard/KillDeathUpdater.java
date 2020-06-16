package me.kitmap.scoreboard;

import me.kitmap.Main;
import me.kitmap.scoreboard.ScoreboardHandler;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class KillDeathUpdater implements Listener {

    private final Main plugin;
    private ScoreboardHandler scoreboardHandler;

    public KillDeathUpdater(Main plugin, ScoreboardHandler scoreboardHandler){
        this.plugin = plugin;
        this.scoreboardHandler = scoreboardHandler;
    }

    private CraftEntity getLastAttacker(PlayerDeathEvent ev) {
        EntityLiving lastAttacker = ((CraftPlayer)ev.getEntity()).getHandle().lastDamager;
        return lastAttacker == null ? null : lastAttacker.getBukkitEntity();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {

        Player player = ev.getEntity().getPlayer();
        Player killer = null;

        if(ev.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent){ // Direct death by player
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) player.getLastDamageCause();
            if(event.getDamager() instanceof Player){ // Melee attack
                killer = (Player) event.getDamager();
            } else if(event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) { // bow shot
                killer = (Player) ((Arrow) event.getDamager()).getShooter();
            } else {
                killer = null;
            }
        } else if(ev.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL){ // Fall damage
            if(getLastAttacker(ev) == null){ // If no one hit this player before fall death
                return;
            }
            if(getLastAttacker(ev) instanceof Player){ // If hit by another player (expire in 3~4 seconds)
                killer = (Player) getLastAttacker(ev);
            } else{
                killer = null;
            }
        }

        Integer killCount = scoreboardHandler.getPlayerKills().get(killer.getUniqueId());
        Integer deathCount = scoreboardHandler.getPlayerDeaths().get(player.getUniqueId());
        scoreboardHandler.getPlayerKills().replace(killer.getUniqueId(), killCount + 1);
        scoreboardHandler.getPlayerDeaths().replace(player.getUniqueId(), deathCount + 1);

        Scoreboard killerBoard = scoreboardHandler.getScoreboards().get(killer.getUniqueId());
//        killerBoard.getTeam("kills").unregister();
//        killerBoard.resetScores(ChatColor.BLUE.toString());
        String killPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Kills: " + ChatColor.GRAY +
                this.scoreboardHandler.getPlayerKills().get(killer.getUniqueId());
        killerBoard.getTeam("kills").setPrefix(killPrefix);
        killerBoard.getObjective("sb").getScore(ChatColor.BLUE.toString()).setScore(3);

        Scoreboard playerBoard = scoreboardHandler.getScoreboards().get(player.getUniqueId());
//        killerBoard.getTeam("kills").unregister();
//        killerBoard.resetScores(ChatColor.BLUE.toString());
        String deathPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths: " + ChatColor.GRAY +
                this.scoreboardHandler.getPlayerDeaths().get(player.getUniqueId());
        playerBoard.getTeam("deaths").setPrefix(deathPrefix);
        playerBoard.getObjective("sb").getScore(ChatColor.GREEN.toString()).setScore(2);
    }
}
