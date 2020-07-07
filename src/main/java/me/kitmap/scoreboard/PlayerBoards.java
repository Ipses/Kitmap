package me.kitmap.scoreboard;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.kitmap.game.SpawnTag;
import me.kitmap.koth.Koth;
import me.kitmap.koth.KothManager;
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
	private final SpawnTag spawnTag;
	private HashMap<UUID, Integer> kills = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> deaths = new HashMap<UUID, Integer>();
	private static final String KOTHTEAMNAME = "kothTimer";

	public PlayerBoards(Main plugin, MysqlData mysqlData, SpawnTag spawnTag){
		this.plugin = plugin;
		this.mysqlData = mysqlData;
		this.spawnTag = spawnTag;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent ev) {
		final Player player = ev.getPlayer();
		mysqlData.createPlayer(player.getUniqueId(), 0, 0);
		this.kills.put(player.getUniqueId(), mysqlData.getDBKills(player.getUniqueId()));
		this.deaths.put(player.getUniqueId(), mysqlData.getDBDeaths(player.getUniqueId()));
		createScoreboard(player);
		this.task = new BukkitRunnable() {
		    public void run() {
		    	updateScoreboard();
		    }
		}.runTaskTimer(plugin.getInstance(), 0L, 20L);
	}

	public void createScoreboard(Player player) { // KILLS = BLUE DEATHS = GREEN
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("sb", "dummy");
		objective.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "This is a title     Test123");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		// TODO: get a more accurate way to get player ping
//		Score ping = board.getObjective("sb").getScore("Ping:");
//		ping.setScore(((CraftPlayer)player).getHandle().ping);

		String killSuffix = ChatColor.GRAY.toString() + this.kills.get(player.getUniqueId());
		board.registerNewTeam("kills");
		Team killCounter = board.getTeam("kills");
		killCounter.addEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Kills: ");
		killCounter.setSuffix(killSuffix);
		objective.getScore(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Kills: ").setScore(3);

		String deathSuffix = ChatColor.GRAY.toString() + this.deaths.get(player.getUniqueId());
		board.registerNewTeam("deaths");
		Team deathCounter = board.getTeam("deaths");
		deathCounter.addEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths: ");
		deathCounter.setSuffix(deathSuffix);
		objective.getScore(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths: ").setScore(2);

		objective.getScore(ChatColor.BLACK.toString()).setScore(9); // empty space
		objective.getScore(ChatColor.WHITE.toString()).setScore(1); // empty space

		player.setScoreboard(board);
		scoreboards.put(player.getUniqueId(), board);

		if (this.plugin.getKothManager().isRunning()) {
			Koth koth = this.plugin.getKothManager().getKoth();
			Scoreboard playerBoard = this.scoreboards.get(player.getUniqueId());
			int kothRemainingSeconds = Math.round(koth.getDefaultCaptureTime())/1000;
			String suffix = Integer.toString(kothRemainingSeconds);
			//String prefix = spawnTagSeconds.substring(0, spawnTagSeconds.length()/2);
			//String suffix = spawnTagSeconds.substring(spawnTagSeconds.length()/2);
			playerBoard.registerNewTeam(KOTHTEAMNAME);
			Team kothTimer = playerBoard.getTeam(KOTHTEAMNAME);
			kothTimer.addEntry(ChatColor.BLUE + koth.getName() +ChatColor.GRAY + ": ");
			kothTimer.setSuffix(suffix);
			//spawnTagCounter.setSuffix(suffix);
			objective.getScore(ChatColor.BLUE + koth.getName() + ChatColor.GRAY + ": ").setScore(5);
			playerBoard.getTeam(KOTHTEAMNAME).setSuffix(suffix);
		}
	}


	public void updateScoreboard() {
		for(Player player: Bukkit.getOnlinePlayers()) {
			Scoreboard playerBoard = this.scoreboards.get(player.getUniqueId());
			if(!(spawnTag.isTagged(player.getUniqueId()))) { // NOT Spawn Tagged
				if(playerBoard.getTeam("spawnTagCounter") != null){
					playerBoard.getTeam("spawnTagCounter").unregister();
					playerBoard.resetScores(ChatColor.RED + "Spawn Tag: ");
				}
			} else { // Spawn Tagged
				Objective objective = playerBoard.getObjective("sb");
				String spawnTagSeconds = ChatColor.RED.toString() +
						((this.spawnTag.getTimer().get(player.getUniqueId()) - System.currentTimeMillis()) / 1000L) + "s";
				String prefix = spawnTagSeconds.substring(0, spawnTagSeconds.length()/2);
				String suffix = spawnTagSeconds.substring(spawnTagSeconds.length()/2);

				if(playerBoard.getTeam("spawnTagCounter") == null) {
					playerBoard.registerNewTeam("spawnTagCounter");
					Team spawnTagCounter = playerBoard.getTeam("spawnTagCounter");
					spawnTagCounter.addEntry(ChatColor.RED + "Spawn Tag: ");
					spawnTagCounter.setPrefix(prefix);
					spawnTagCounter.setSuffix(suffix);
					objective.getScore(ChatColor.RED + "Spawn Tag: ").setScore(4);
					return;
				}
				playerBoard.getTeam("spawnTagCounter").setPrefix(prefix);
				playerBoard.getTeam("spawnTagCounter").setSuffix(suffix);
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
