package me.kitmap.scoreboard;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Scoreboard;

public class KillDeathUpdater implements Listener {

    private final Main plugin;
    private PlayerBoards playerBoards;

    public KillDeathUpdater(Main plugin, PlayerBoards playerBoards){
        this.plugin = plugin;
        this.playerBoards = playerBoards;
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

        Integer killCount = playerBoards.getPlayerKills().get(killer.getUniqueId());
        Integer deathCount = playerBoards.getPlayerDeaths().get(player.getUniqueId());
        playerBoards.getPlayerKills().replace(killer.getUniqueId(), killCount + 1);
        playerBoards.getPlayerDeaths().replace(player.getUniqueId(), deathCount + 1);

        Scoreboard killerBoard = playerBoards.getScoreboards().get(killer.getUniqueId());
//        killerBoard.getTeam("kills").unregister();
//        killerBoard.resetScores(ChatColor.BLUE.toString());
        String killPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Kills: " + ChatColor.GRAY +
                this.playerBoards.getPlayerKills().get(killer.getUniqueId());
        killerBoard.getTeam("kills").setPrefix(killPrefix);
        killerBoard.getObjective("sb").getScore(ChatColor.BLUE.toString()).setScore(3);

        Scoreboard playerBoard = playerBoards.getScoreboards().get(player.getUniqueId());
//        killerBoard.getTeam("kills").unregister();
//        killerBoard.resetScores(ChatColor.BLUE.toString());
        String deathPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths: " + ChatColor.GRAY +
                this.playerBoards.getPlayerDeaths().get(player.getUniqueId());
        playerBoard.getTeam("deaths").setPrefix(deathPrefix);
        playerBoard.getObjective("sb").getScore(ChatColor.GREEN.toString()).setScore(2);
    }
}
