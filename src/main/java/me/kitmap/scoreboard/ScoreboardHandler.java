package me.kitmap.scoreboard;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.kitmap.Main;
import me.kitmap.stats.MysqlData;
import me.kitmap.timer.CombatTagTimer;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardHandler implements Listener {
	
	BukkitTask task;
	public static ConcurrentHashMap<UUID, Scoreboard> scoreboards = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<UUID, Long> pvptagTimer = CombatTagTimer.timer;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		final Player player = ev.getPlayer();
		createScoreboard(player);
		this.task = new BukkitRunnable() {
		    @Override
		    public void run() {
		    	updateScoreboard();
		    }
		}.runTaskTimer(Main.getInstance(), 20L, 20L);
	}
	
	
	
	
	public void createScoreboard(Player player) {
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("sb", "dummy");
		objective.setDisplayName(ChatColor.GOLD + "test");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//		Score ping = board.getObjective("sb").getScore("Ping:");
//		ping.setScore(((CraftPlayer)player).getHandle().ping);

		Score kills = board.getObjective("sb").getScore("Kills:");
		Score deaths = board.getObjective("sb").getScore("Deaths:");

		if(!MysqlData.playerExists(player.getUniqueId()) && !MysqlData.playerExists(player.getUniqueId())) {
			kills.setScore(0);
			deaths.setScore(0);
		} else {
			kills.setScore(MysqlData.getKills(player.getUniqueId()));
			deaths.setScore(MysqlData.getDeaths(player.getUniqueId()));
		}
		
		player.setScoreboard(board);
		scoreboards.put(player.getUniqueId(), board);
		player.sendMessage("Board built");
	}
	
	public static void updateScoreboard() {
		for(Player player: Bukkit.getOnlinePlayers()) {
			
			if(pvptagTimer.containsKey(player.getUniqueId())) {
				Score pvp = scoreboards.get(player.getUniqueId()).getObjective(DisplaySlot.SIDEBAR).getScore("Spawn Tag:");
				if((pvptagTimer.get(player.getUniqueId()) - System.currentTimeMillis() + 1) / 1000 <= 0) {
					//player.sendMessage("reset");
					scoreboards.get(player.getUniqueId()).resetScores("Spawn Tag:");
				} else {
					//player.sendMessage("setscore:");
					pvp.setScore(Math.round(pvptagTimer.get(player.getUniqueId()) - System.currentTimeMillis() + 1) / 1000);
				}
			}
			player.setScoreboard(scoreboards.get(player.getUniqueId()));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		//this.task.cancel();
		updateScoreboard();
	}
}
