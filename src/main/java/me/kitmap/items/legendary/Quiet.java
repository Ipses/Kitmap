package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Random;

public class Quiet implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent ev) {
        if(!ev.isCancelled() && isItem(ev.getBow())) {
            ev.getProjectile().setMetadata("quiet", new FixedMetadataValue(Main.getInstance(), true));
        }
    }
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!ev.isCancelled() && ev.getDamager() instanceof Arrow && ((Projectile) ev.getDamager()).getShooter() instanceof Player &&
                ev.getEntity() instanceof Player && ev.getDamager().hasMetadata("quiet")) {
            Player victim = (Player) ev.getEntity();
            ArrayList<Sound> sounds = new ArrayList<>();
            sounds.add(Sound.ENTITY_GHAST_AMBIENT);
            sounds.add(Sound.ENTITY_GHAST_DEATH);
            sounds.add(Sound.ENTITY_GHAST_HURT);
            sounds.add(Sound.ENTITY_GHAST_SCREAM);
            sounds.add(Sound.ENTITY_GHAST_WARN);
            sounds.add(Sound.ENTITY_PARROT_IMITATE_GHAST);
            Sound randomSound = sounds.get(new Random().nextInt(sounds.size()));
            victim.playSound(victim.getLocation(), randomSound, 1, 1);
        }
    }
    private static final String name = ChatColor.RESET + "Quiet";
    private static boolean isItem(ItemStack is) {
        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals(name)) {
            return true;
        }
        return false;
    }
}
