package me.kitmap.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class FoodHealing implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent ev) {
        Player player = ev.getPlayer();
        ItemStack item = ev.getItem();
        switch(item.getType()) {
            default:
                return;
            case BREAD:
                heal(player);
            case COOKIE:
                heal(player);
            case MUSHROOM_SOUP:
                heal(player);
        }
    }

    private void heal(Player player) {
        player.setHealth(Math.min(player.getHealth() + 1, 20));
    }
}
