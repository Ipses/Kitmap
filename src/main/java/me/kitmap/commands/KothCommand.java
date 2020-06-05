package me.kitmap.commands;

import me.kitmap.koth.Koth;
import me.kitmap.koth.KothManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class KothCommand implements CommandExecutor {

    public static String koth = "koth";

    public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] arg2 ) {
        if(cmd.getName().equalsIgnoreCase("koth") && sender instanceof Player) {
            Player player = (Player) sender;
            Koth koth = new Koth("Eillom", new Location(player.getWorld(), -90, 1, 404),
                    new Location(player.getWorld(), -83, 1, 411), TimeUnit.SECONDS.toMillis(10));
            KothManager kothManager = new KothManager(koth); // hardcoded temporarily
            kothManager.start();
            return true;
        }
        return false;
    }
}
