package me.kitmap.items.minezitems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Lasso implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent ev){
        Player player = ev.getPlayer();
        if(ev.getAction() == Action.RIGHT_CLICK_AIR){

        }

    }
}
