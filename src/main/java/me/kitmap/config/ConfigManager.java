package me.kitmap.config;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final Main plugin = Main.getPlugin(Main.class);
    public FileConfiguration damageConfig, coordsConfig;
    public File damageFile, coordsFile;

    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        createFile("damage.yml");
        createFile("coordinates.yml");
        reloadDamage();
        reloadCoords();
    }

    public FileConfiguration getDamage(){
        return this.damageConfig;
    }

    public void saveDamage(){
        try{
            damageConfig.save(damageFile);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file saved");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reloadDamage(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file reloaded");
        damageConfig = YamlConfiguration.loadConfiguration(damageFile);
    }

    public void saveCoords(){
        try{
            coordsConfig.save(coordsFile);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file saved");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reloadCoords(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file reloaded");
        coordsConfig = YamlConfiguration.loadConfiguration(coordsFile);
    }

    public FileConfiguration getCoords() { return this.coordsConfig; }

    private void createFile(String fileName){
        coordsFile = new File(plugin.getDataFolder(), fileName);
        if(!coordsFile.exists()){
            try {
                coordsFile.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
