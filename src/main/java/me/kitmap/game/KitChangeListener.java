package me.kitmap.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitChangeListener implements Listener {

    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0);

    @EventHandler
    public void onKitChange(KitChangeEvent ev) {
        final Player player = ev.getPlayer();
        final String kit = ev.getKit();
        for (PotionEffect effect: player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        if (kit.equalsIgnoreCase("healer")) {
            player.addPotionEffect(SPEED);
        }
    }

    @EventHandler
    public void onArmorTakeoff(InventoryClickEvent ev) {
        if (ev.getWhoClicked() instanceof Player) {
            final Player player = (Player) ev.getWhoClicked();
            if (!ev.isCancelled() && ev.getSlotType() == InventoryType.SlotType.ARMOR) {
                for (PotionEffect effect: player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }
}
