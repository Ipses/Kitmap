package me.kitmap.items.legendary;

import me.kitmap.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quiet extends Legendary implements Listener {

    private Main plugin;
    private String name;
    private static final String NAME = ChatColor.RESET + "Quiet";

    public Quiet(Main plugin) {
        super(plugin);
        this.name = NAME;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent ev) {
        if(!ev.isCancelled() && hasName(ev.getBow())) {
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

    @Override
    public ItemStack getItem() {
        ItemStack quiet = new ItemStack(Material.BOW);
        ItemMeta quietItemMeta = quiet.getItemMeta();
        List<String> quietLore = new ArrayList<String>();
        quietLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Legendary Weapon");
        quietLore.add(net.md_5.bungee.api.ChatColor.BLUE + "Plays a ghast sound to the player shot");
        quietItemMeta.setLore(quietLore);
        quietItemMeta.setDisplayName(this.name);
        quiet.setItemMeta(quietItemMeta);
        return quiet;
    }
}
