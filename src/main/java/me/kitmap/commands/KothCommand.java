package me.kitmap.commands;

import me.kitmap.Main;
import me.kitmap.config.ConfigManager;
import me.kitmap.koth.Koth;
import me.kitmap.koth.KothManager;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class KothCommand implements CommandExecutor {

    private final Main plugin;
    private ConfigManager configManager;

    public KothCommand(Main plugin){
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] args ) {
        if(cmd.getName().equalsIgnoreCase("koth") && sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0){
                player.sendMessage("/koth list: view all koths");
                player.sendMessage("/koth start <name>: start koth");
                player.sendMessage("/koth end <name> end koth");
                return true;
            }
            if(args[0].equalsIgnoreCase("list")){
                player.sendMessage("Koth List");
                player.sendMessage("Eillom");
                return true;
            }
            if(args[0].equalsIgnoreCase("start")){
                if(args.length == 1){
                    player.sendMessage("enter a valid koth name in /koth list");
                    return true;
                }
                if(plugin.getKothManager().isRunning()){
                    player.sendMessage("There's already a KOTH running");
                    return true;
                }
                if(args[1].equalsIgnoreCase("Eillom")){
                    double eillomMinX = Double.parseDouble(configManager.getCoords().getString("Eillom.minX"));
                    double eillomMaxX = Double.parseDouble(configManager.getCoords().getString("Eillom.maxX"));
                    double eillomMinZ = Double.parseDouble(configManager.getCoords().getString("Eillom.minZ"));
                    double eillomMaxZ = Double.parseDouble(configManager.getCoords().getString("Eillom.maxZ"));
                    Koth eillom = new Koth("Eillom", new Location(player.getWorld(),
                            eillomMinX, 1, eillomMinZ),
                            new Location(player.getWorld(), eillomMaxX, 1, 411), TimeUnit.SECONDS.toMillis(10));
                    this.plugin.getKothManager().addKoth(eillom);
                    this.plugin.getKothManager().start();
                    return true;
                }
                // add more koth here
                player.sendMessage("enter a valid koth name in /koth list");
                return true;
            }

            if(args[0].equalsIgnoreCase("end")){
                if(!this.plugin.getKothManager().isRunning()){
                    player.sendMessage("There's no koth running");
                    return false;
                }
                plugin.getKothManager().end();
                return true;
            }
        }
        return false;
    }
}
