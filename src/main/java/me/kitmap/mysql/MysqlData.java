package me.kitmap.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;

import me.kitmap.Main;
import net.md_5.bungee.api.ChatColor;

public class MysqlData{
	
	private final Main plugin;

	public MysqlData(Main plugin){
		this.plugin = plugin;
	}

//  		DB CREATE CODE
//	DROP DATABASE IF EXISTS `my_server`;
//	CREATE DATABASE `my_server`;
//	USE `my_server`;
//
//	DROP TABLE IF EXISTS `player_data`;
//	CREATE TABLE `player_data` (
//			`uuid` VARCHAR(36) NOT NULL,
//  `kills` SMALLINT(4) NOT NULL,
//  `deaths` SMALLINT(4) NOT NULL,
//	PRIMARY KEY (`uuid`)
//)

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
