package me.kitmap.items.minezitems;

import me.kitmap.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HealKit implements Listener {

    private final Main plugin;
    private HashMap<UUID, Long> timer = new HashMap<>();

    private static final String REGENITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.RED + " (Regeneration)";
    private static final String SPEEDITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.AQUA + " (Speed)";
    private static final String ABSORPTIONITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.YELLOW + " (Absorption)";
    private static final String RESISTANCEITEMNAME = ChatColor.RESET + "Heal Kit" + ChatColor.GRAY + " (Resistance)";

    private static final PotionEffect PERMASPEED = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0);
    private static final PotionEffect REGEN = new PotionEffect(PotionEffectType.REGENERATION, 5*20, 0);
    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 5*20, 1);
    private static final PotionEffect ABSORPTION = new PotionEffect(PotionEffectType.ABSORPTION, 3*20, 0);
    private static final PotionEffect RESISTANCE = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 0);

    public HealKit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();
        ItemStack helmet = ev.getPlayer().getInventory().getHelmet();
        ItemStack chestplate = ev.getPlayer().getInventory().getChestplate();
        ItemStack legs = ev.getPlayer().getInventory().getLeggings();
        ItemStack boots = ev.getPlayer().getInventory().getBoots();
        ItemStack handItem = ev.getPlayer().getInventory().getItemInMainHand();
        if (handItem == null || handItem.getType() == Material.AIR) {
            return;
        }
        String displayName = handItem.getItemMeta().getDisplayName();
        ItemMeta healKitItemMeta = handItem.getItemMeta();

        if (isLeatherKit(helmet, chestplate, legs, boots) && handItem.getType() == Material.SHEARS) {
            if (ev.getAction() == Action.LEFT_CLICK_AIR || ev.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (displayName.equals(REGENITEMNAME)) {
                   healKitItemMeta.setDisplayName(SPEEDITEMNAME);
                } else if (displayName.equals(SPEEDITEMNAME)) {
                   healKitItemMeta.setDisplayName(ABSORPTIONITEMNAME);
                } else if (displayName.equals(ABSORPTIONITEMNAME)) {
                    healKitItemMeta.setDisplayName(RESISTANCEITEMNAME);
                } else {
                    healKitItemMeta.setDisplayName(REGENITEMNAME);
                } // TODO: get it working with switch
                handItem.setItemMeta(healKitItemMeta);
                player.updateInventory();
            } else if (ev.getAction() == Action.RIGHT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_AIR) {
                long cooldownLeft = timer.containsKey(player.getUniqueId()) ? timer.get(player.getUniqueId()) - System.currentTimeMillis() : 0;
                if (cooldownLeft > 0) {
                    player.sendMessage(ChatColor.RED + "You cannot use this for another " + cooldownLeft / 1000 + " seconds!");
                    return;
                }
                if (!isInPvPZone(player)) {
                    player.sendMessage(ChatColor.RED + "You cannot heal other players at spawn!");
                    return;
                }

                long cooldown;
                if (displayName.equals(REGENITEMNAME)) {
                    player.addPotionEffect(REGEN);
                    cooldown = System.currentTimeMillis() + 10*1000;
                    timer.put(player.getUniqueId(), cooldown);
                } else if (displayName.equals(SPEEDITEMNAME)) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(SPEED);
                    Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () -> {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(PERMASPEED);
                    }, 15*20);
                    cooldown = System.currentTimeMillis() + 15*1000;
                    timer.put(player.getUniqueId(), cooldown);
                } else if (displayName.equals(ABSORPTIONITEMNAME)) {
                    player.addPotionEffect(ABSORPTION);
                    cooldown = System.currentTimeMillis() + 8*1000;
                    timer.put(player.getUniqueId(), cooldown);
                } else {
                    player.addPotionEffect(RESISTANCE);
                    cooldown = System.currentTimeMillis() + 8*1000;
                    timer.put(player.getUniqueId(), cooldown);
                }

                ArrayList<Player> playersInSight = getPlayersInSight(player);
                if (playersInSight.size() == 0) {
                    player.sendMessage(ChatColor.RED + "You were not looking at any player and only healed yourself");
                    return;
                }
                Player closestPlayer = getClosestPlayer(player, playersInSight);
                if (isLooking(player, closestPlayer)) {
                    if (displayName.equals(REGENITEMNAME)) {
                            closestPlayer.addPotionEffect(REGEN);
                            player.addPotionEffect(REGEN);
                            player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.RED + "Regen I " +
                                    ChatColor.YELLOW + "to " + ChatColor.GOLD + closestPlayer.getName());
                        } else if (displayName.equals(SPEEDITEMNAME)) {
                            closestPlayer.addPotionEffect(SPEED);
                            player.addPotionEffect(SPEED);
                            player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.AQUA + "Speed II " +
                                    ChatColor.YELLOW + "to " + ChatColor.GOLD + closestPlayer.getName());
                        } else if (displayName.equals(ABSORPTIONITEMNAME)) {
                            closestPlayer.addPotionEffect(ABSORPTION);
                            player.addPotionEffect(ABSORPTION);
                            player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.GOLD + "2 hearts of shield " +
                                    ChatColor.YELLOW + "to " + ChatColor.GOLD + closestPlayer.getName());
                        } else {
                            closestPlayer.addPotionEffect(RESISTANCE);
                            player.addPotionEffect(RESISTANCE);
                            player.sendMessage(ChatColor.YELLOW + "You have given " + ChatColor.GRAY + "Resisstance I " +
                                    ChatColor.YELLOW + "to " + ChatColor.GOLD + closestPlayer.getName());
                        }
                    reduceDura(player, player.getInventory().getItemInMainHand());
                    Bukkit.getScheduler().runTaskLater(plugin.getInstance(), () -> timer.remove(player.getUniqueId()), cooldown*20);
                }
            }
        }
    }


    private ArrayList<Player> getPlayersInSight(Player player){
        ArrayList<Player> playersInSight = new ArrayList<>();
        List<Entity> nearbyEntities = player.getNearbyEntities(5, 3, 5);
        for (Entity entity : nearbyEntities) {
            if(entity instanceof Player){
                if(isLooking(player, (Player) entity)) {
                    playersInSight.add((Player) entity);
                }
            }
        }
        return playersInSight;
    }

    private Player getClosestPlayer(Player player, ArrayList<Player> playerList) {
        double minDistanceSquared = Double.MAX_VALUE;
        double currentDistanceSquared;
        Player closestPlayer = null;
        if (playerList == null) {
            return null;
        }
        for (Player playerInSight: playerList) {
            currentDistanceSquared = playerInSight.getLocation().distanceSquared(player.getLocation());
            if (currentDistanceSquared < minDistanceSquared) {
                minDistanceSquared = currentDistanceSquared;
                closestPlayer = playerInSight;
            }
        }
        return closestPlayer;
    }

    private boolean isLooking(Player player, Player playersInSight) {
        Location location = player.getEyeLocation();
        Vector vector = playersInSight.getEyeLocation().toVector().subtract(location.toVector());
        double dotProduct = vector.normalize().dot(location.getDirection());
        return dotProduct > 0.97;
    }

    private void reduceDura(Player player, ItemStack is) {
        if(is.getDurability() > is.getType().getMaxDurability() - 8){
            is.setAmount(0);
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        } else {
            is.setDurability((short) (is.getDurability() + 4));
        }
        player.updateInventory();
    }

    private boolean isInPvPZone(Player player) {
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        return !(plugin.spawnMinX < x && x < plugin.spawnMaxX && plugin.spawnMinZ < z && z < plugin.spawnMaxZ);
    }

    private boolean isLeatherChestplate(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_CHESTPLATE;
    }

    private boolean isLeatherHLeggings(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_LEGGINGS;
    }

    private boolean isLeatherBoots(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_BOOTS;
    }

    private boolean isLeatherHelmet(ItemStack item) {
        return item != null && item.getType() == Material.LEATHER_HELMET;
    }

    private boolean isLeatherKit(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        return isLeatherBoots(boots) && isLeatherChestplate(chestplate) && isLeatherHelmet(helmet) &&
                isLeatherHLeggings(leggings);
    }
}
