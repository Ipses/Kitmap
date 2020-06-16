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

public class MysqlData{
	
	private final Main plugin;

	public MysqlData(Main plugin){
		this.plugin = plugin;
	}
	
	public boolean playerExists(UUID uuid) {
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
	
	public void updateDBKills(UUID uuid, int kills) {
		try {
			PreparedStatement update = plugin.getConnection().prepareStatement(
					"UPDATE " + plugin.table + " SET KILLS=? WHERE UUID=?");
			
			update.setInt(1, kills);
			update.setString(2, uuid.toString());
			update.executeUpdate();
			Bukkit.broadcastMessage("kills saved in db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDBDeaths(UUID uuid, int deaths) {
		try {
			PreparedStatement update = plugin.getConnection().prepareStatement(
					"UPDATE " + plugin.table + " SET DEATHS=? WHERE UUID=?");
			
			update.setInt(1, deaths);
			update.setString(2, uuid.toString());
			update.executeUpdate();
			Bukkit.broadcastMessage("death saved in db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getDBKills(UUID uuid) {
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
	
	public int getDBDeaths(UUID uuid) {
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
	}
}
