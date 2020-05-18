package me.kitmap.stats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.kitmap.Main;
import me.kitmap.scoreboard.ScoreboardHandler;
import net.md_5.bungee.api.ChatColor;

public class MysqlData implements Listener {
	
	static Main plugin = Main.getPlugin(Main.class);
	private ConcurrentHashMap<UUID, Scoreboard> scoreboards = ScoreboardHandler.scoreboards;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		createPlayer(player.getUniqueId(), 0, 0);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent ev) {
        Player player = ev.getEntity();
        Player killer;
        if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) player.getLastDamageCause();
            
            if ((event.getDamager() instanceof Player)) {
                killer = (Player) event.getDamager(); 
            } else if(event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
                killer = (Player) ((Arrow) event.getDamager()).getShooter();
            } else { // check for grenade, fall damage, legendary later
            	killer = null;
            }     
            updateKills(killer.getUniqueId());
            updateDeaths(player.getUniqueId());
            Score kills = scoreboards.get(killer.getUniqueId()).getObjective("sb").getScore("Kills:");
    		kills.setScore(MysqlData.getKills(killer.getUniqueId()));
    		Score deaths = scoreboards.get(killer.getUniqueId()).getObjective("sb").getScore("Deaths:");
     		deaths.setScore(MysqlData.getKills(killer.getUniqueId()));
        }
	 }
	
	public static boolean playerExists(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			
			ResultSet results = statement.executeQuery();
			if(results.next()) {
				plugin.getServer().broadcastMessage("Player Found");
				return true;
			}
			plugin.getServer().broadcastMessage("Player Not Found");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void createPlayer(final UUID uuid, int kills, int deaths) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement(
					"SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			if(!playerExists(uuid)) {
				PreparedStatement insert = plugin.getConnection().prepareStatement(
						"INSERT INTO " + plugin.table + "(UUID, KILLS, DEATHS) VALUE (?, ?, ?)");
				insert.setString(1, uuid.toString());
				insert.setInt(2, 0);
				insert.setInt(3, 0);
				insert.executeUpdate();
				plugin.getServer().broadcastMessage(ChatColor.GREEN  + "Player Inserted");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateKills(UUID uuid) {
		try {
			PreparedStatement update = plugin.getConnection().prepareStatement(
					"UPDATE " + plugin.table + " SET KILLS=? WHERE UUID=?");
			
			update.setInt(1, getKills(uuid) + 1);
			update.setString(2, uuid.toString());
			update.executeUpdate();
			Bukkit.broadcastMessage("increment");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDeaths(UUID uuid) {
		try {
			PreparedStatement update = plugin.getConnection().prepareStatement(
					"UPDATE " + plugin.table + " SET DEATHS=? WHERE UUID=?");
			
			update.setInt(1, getDeaths(uuid) + 1);
			update.setString(2, uuid.toString());
			update.executeUpdate();
			Bukkit.broadcastMessage("increment");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int getKills(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement(
					"SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			Bukkit.broadcastMessage("kills:" + results.getInt("KILLS"));
			return results.getInt("KILLS");
		} catch (SQLException e) {
			return 0;
			//e.printStackTrace();
		}
		//return 0;
	}
	
	public static int getDeaths(UUID uuid) {
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement(
					"SELECT * FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			Bukkit.broadcastMessage("deaths:" + results.getInt("DEATHS"));
			return results.getInt("DEATHS");
		} catch (SQLException e) {
			return 0;
			//e.printStackTrace();
		}
		//return 0;
	}
}
