package me.kitmap.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitChangeEvent extends Event {

    private Player player;
    private String kit;
    private static final HandlerList handlers = new HandlerList();

    public KitChangeEvent(Player player, String kit) {
        this.player = player;
        this.kit = kit;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getKit() { return this.kit; }
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
