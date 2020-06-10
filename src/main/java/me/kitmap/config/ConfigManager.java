package me.kitmap.config;

import me.kitmap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private Main plugin = Main.getPlugin(Main.class);
    public FileConfiguration damageConfig;
    public File damageFile;

    public void setup(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        damageFile = new File(plugin.getDataFolder(), "damage.yml");

        if(!damageFile.exists()){
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file doesn't exists");
            try {
                damageFile.createNewFile();

            } catch(IOException e){
                e.printStackTrace();
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "file exists");
        reloadDamage();
    }

    public FileConfiguration getDamage(){
        return damageConfig;
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
}
