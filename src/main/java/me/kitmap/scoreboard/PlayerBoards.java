package me.kitmap.scoreboard;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.kitmap.game.SpawnTagManager;
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
import org.bukkit.scoreboard.Scoreboard;

import me.kitmap.Main;
import me.kitmap.mysql.MysqlData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.scoreboard.Team;

public class PlayerBoards implements Listener {
	
	BukkitTask task;
	public ConcurrentHashMap<UUID, Scoreboard> scoreboards = new ConcurrentHashMap<>();
	private final Main plugin;
	private final MysqlData mysqlData;
	private final SpawnTagManager spawnTagManager;
	private HashMap<UUID, Integer> kills = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> deaths = new HashMap<UUID, Integer>();

	public PlayerBoards(Main plugin, MysqlData mysqlData, SpawnTagManager spawnTagManager){
		this.plugin = plugin;
		this.mysqlData = mysqlData;
		this.spawnTagManager = spawnTagManager;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		final Player player = ev.getPlayer();
		mysqlData.createPlayer(player.getUniqueId(), 0, 0);
		this.kills.put(player.getUniqueId(), mysqlData.getDBKills(player.getUniqueId()));
		this.deaths.put(player.getUniqueId(), mysqlData.getDBDeaths(player.getUniqueId()));
		createScoreboard(player);
		this.task = new BukkitRunnable() {
		    @Override
		    public void run() {
		    	updateScoreboard();
		    }
		}.runTaskTimer(plugin.getInstance(), 20L, 20L);
	}

	public void createScoreboard(Player player) { // KILLS = BLUE DEATHS = GREEN
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("sb", "dummy");
		objective.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "This is a title     Test123");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//		Score ping = board.getObjective("sb").getScore("Ping:");
//		ping.setScore(((CraftPlayer)player).getHandle().ping);

		String killPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Kills: " + ChatColor.GRAY +
				this.kills.get(player.getUniqueId());
		board.registerNewTeam("kills");
		Team killCounter = board.getTeam("kills");
		killCounter.addEntry(ChatColor.BLUE.toString());
		killCounter.setPrefix(killPrefix);
		objective.getScore(ChatColor.BLUE.toString()).setScore(3);

		String deathPrefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths: " + ChatColor.GRAY +
				this.deaths.get(player.getUniqueId());
		board.registerNewTeam("deaths");
		Team deathCounter = board.getTeam("deaths");
		deathCounter.addEntry(ChatColor.GREEN.toString());
		deathCounter.setPrefix(deathPrefix);
		objective.getScore(ChatColor.GREEN.toString()).setScore(2);

		objective.getScore(ChatColor.BLACK.toString()).setScore(9); // empty space
		objective.getScore(ChatColor.WHITE.toString()).setScore(1); // empty space

		player.setScoreboard(board);
		scoreboards.put(player.getUniqueId(), board);
	}

	public void updateScoreboard() {
		for(Player player: Bukkit.getOnlinePlayers()) {
			if(spawnTagManager.getTimer().containsKey(player.getUniqueId())) {
				Scoreboard playerBoard = this.scoreboards.get(player.getUniqueId());
				if((spawnTagManager.getTimer().get(player.getUniqueId()) - System.currentTimeMillis() ) / 1000 <= 0) {
					// NOT Spawn Tagged
					if(playerBoard.getTeam("spawnTagCounter") != null){
						playerBoard.getTeam("spawnTagCounter").unregister();
						playerBoard.resetScores(ChatColor.RED.toString());
					} else{
						// do nothing
					}
				} else { // Spawn Tagged
					Objective objective = playerBoard.getObjective("sb");
					String spawnTagSeconds = ChatColor.RED + "Spawn Tag: "
							+ (Math.round(spawnTagManager.getTimer().get(player.getUniqueId())
							- System.currentTimeMillis()) / 1000) + "s";
					String prefix = spawnTagSeconds.substring(0, spawnTagSeconds.length()/2);
					String suffix = spawnTagSeconds.substring(spawnTagSeconds.length()/2);

					if(playerBoard.getTeam("spawnTagCounter") == null) {
						playerBoard.registerNewTeam("spawnTagCounter");
						Team spawnTagCounter = playerBoard.getTeam("spawnTagCounter");
						spawnTagCounter.addEntry(ChatColor.RED.toString());
						spawnTagCounter.setPrefix(prefix);
						spawnTagCounter.setSuffix(suffix);
						objective.getScore(ChatColor.RED.toString()).setScore(4);
						return;
					}
					playerBoard.getTeam("spawnTagCounter").setPrefix(prefix);
					playerBoard.getTeam("spawnTagCounter").setSuffix(suffix);
				}
			}
			player.setScoreboard(scoreboards.get(player.getUniqueId()));
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		//this.task.cancel();
		Player player = ev.getPlayer();
		mysqlData.updateDBKills(player.getUniqueId(), this.kills.get(player.getUniqueId()));
		mysqlData.updateDBDeaths(player.getUniqueId(), this.deaths.get(player.getUniqueId()));
		this.kills.remove(player.getUniqueId());
		this.deaths.remove(player.getUniqueId());

		//updateScoreboard();
	}

	public ConcurrentHashMap<UUID, Scoreboard> getScoreboards(){
		return this.scoreboards;
	}

	public HashMap<UUID, Integer> getPlayerKills(){
		return this.kills;
	}

	public HashMap<UUID, Integer> getPlayerDeaths(){
		return this.deaths;
	}
}
