package me.kitmap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.kitmap.commands.KothCommand;
import me.kitmap.items.legendary.*;
import me.kitmap.items.minezitems.Grenade;
import me.kitmap.items.minezitems.Sugar;
import me.kitmap.loot.KothCrate;
import me.kitmap.signs.KitSIgn;
import me.kitmap.world.SpawnEnterBlocker;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import me.kitmap.commands.ItemCommand;
import me.kitmap.commands.KitCommand;
import me.kitmap.items.ItemMenu;
import me.kitmap.items.minezitems.WeakGrapple;
import me.kitmap.scoreboard.ScoreboardHandler;
import me.kitmap.stats.MysqlData;
import me.kitmap.timer.CombatTagTimer;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public static Plugin plugin;
	private Connection connection;
	public String host, database, username, password, table;
	public int port;
	
	public static Inventory ironKit = Bukkit.createInventory(null, 36, "ironkit");
	private KitCommand kitCommand = new KitCommand();
	private ItemCommand itemsCommand = new ItemCommand();
	private KothCommand kothCommand = new KothCommand();

	private static Inventory itemPage1 = ItemCommand.itemPage1;	
	private static Inventory itemPage2 = ItemCommand.itemPage2;	
	private static Inventory itemPage3 = ItemCommand.itemPage3;

	public void onEnable() {
		System.out.println(ChatColor.GREEN + "on");
		plugin = this;
		registerEvents();
		loadConfig();
		mysqlsetup();
		buildKit();
		buildItems();
		registerCommands();
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
	
	public Connection getConnection() {
		return connection;
	}

	private void registerCommands(){
		getCommand(KitCommand.kit).setExecutor(kitCommand);
		getCommand(ItemCommand.items).setExecutor(itemsCommand);
		getCommand(KothCommand.koth).setExecutor(kothCommand);
	}
	public void mysqlsetup() {
		host = this.getConfig().getString("host");
		port = this.getConfig().getInt("port");
		database = this.getConfig().getString("database");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");
		table = this.getConfig().getString("table");
		
		try {
			synchronized (this) {
				if(getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				
				Class.forName("com.mysql.jdbc.Driver");
				setConnection( DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + 
				this.port + "/" + this.database, this.username, this.password));
			
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL connected");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new ScoreboardHandler(), this);
		getServer().getPluginManager().registerEvents(new CombatTagTimer(), this);
		getServer().getPluginManager().registerEvents(new MysqlData(), this);
		getServer().getPluginManager().registerEvents(new ItemMenu(), this);
		getServer().getPluginManager().registerEvents(new KitSIgn(), this);
		getServer().getPluginManager().registerEvents(new KothCrate(), this);
		getServer().getPluginManager().registerEvents(new SpawnEnterBlocker(), this);

		getServer().getPluginManager().registerEvents(new Grenade(), this);
		getServer().getPluginManager().registerEvents(new Sugar(), this);
		getServer().getPluginManager().registerEvents(new WeakGrapple(), this);
		getServer().getPluginManager().registerEvents(new ZombieBow(), this);
		getServer().getPluginManager().registerEvents(new TruthBow(), this);
		getServer().getPluginManager().registerEvents(new PluviasTempest(), this);
		getServer().getPluginManager().registerEvents(new Shotbow(), this);
		getServer().getPluginManager().registerEvents(new Quiet(), this);
		getServer().getPluginManager().registerEvents(new WebShot(), this);

		getServer().getPluginManager().registerEvents(new RobbersBlade(), this);
		getServer().getPluginManager().registerEvents(new DepthStrider(), this);
		getServer().getPluginManager().registerEvents(new SpikeThrower(), this);
		getServer().getPluginManager().registerEvents(new Overkill(), this);
		getServer().getPluginManager().registerEvents(new SealOfTime(), this);
		getServer().getPluginManager().registerEvents(new SealOfSpace(), this);
		getServer().getPluginManager().registerEvents(new SealOfGravity(), this);

	}
	
	private void buildKit() {
		ironKit.setItem(0, new ItemStack(Material.IRON_SWORD));		
		ItemStack powerBow = new ItemStack(Material.BOW);
		powerBow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		ironKit.setItem(1, powerBow);		
		
		ItemStack splash = new ItemStack(Material.SPLASH_POTION);
		PotionMeta splashMeta = (PotionMeta) splash.getItemMeta();
		splashMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
		splash.setItemMeta(splashMeta);
		ironKit.setItem(2, splash);
		ironKit.setItem(3, splash);

		ItemStack drink = new ItemStack(Material.POTION);
		PotionMeta drinkMeta = (PotionMeta) drink.getItemMeta();
		drinkMeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true)); // extend, upgrade
		drink.setItemMeta(drinkMeta);
		
		ironKit.setItem(4, drink);
		ironKit.setItem(5, drink);
		ironKit.setItem(6, new ItemStack(Material.GOLDEN_APPLE));
		ironKit.setItem(7, new ItemStack(Material.SUGAR));
		
		ItemStack grapple = new ItemStack(Material.FISHING_ROD);
		ItemMeta grappleMeta  = grapple.getItemMeta();
		grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
		grapple.setItemMeta(grappleMeta);
		ironKit.setItem(8, grapple);
		
		ironKit.setItem(9, new ItemStack(Material.ARROW, 15));
		
		ItemStack grenade = new ItemStack(Material.ENDER_PEARL);
		ItemMeta grenadeMeta  =grenade.getItemMeta();
		grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
		grenade.setItemMeta(grenadeMeta);
		
		ironKit.setItem(10, grenade);
		ironKit.setItem(11, new ItemStack(Material.GOLDEN_APPLE));
		ironKit.setItem(12, new ItemStack(Material.GOLDEN_APPLE));

	
		ironKit.setItem(13, new ItemStack(Material.COOKIE, 8));
		ironKit.setItem(14, new ItemStack(Material.COOKIE, 8));
		ironKit.setItem(15, new ItemStack(Material.MUSHROOM_SOUP));
		ironKit.setItem(16, new ItemStack(Material.MUSHROOM_SOUP));
		ironKit.setItem(17, new ItemStack(Material.MUSHROOM_SOUP));
		ironKit.setItem(18, new ItemStack(Material.ARROW, 15));
		ironKit.setItem(19, grenade);
		ironKit.setItem(20, splash);
		ironKit.setItem(21, splash);
		ironKit.setItem(22, drink);
		ironKit.setItem(23, drink);
		ironKit.setItem(24, drink);
		ironKit.setItem(25, drink);
		ironKit.setItem(26, drink);
		ironKit.setItem(27, new ItemStack(Material.ARROW, 15));
		ironKit.setItem(28, grenade);
		ironKit.setItem(29, splash);
		ironKit.setItem(30, splash);
		ironKit.setItem(31, drink);
		ironKit.setItem(32, drink);
		ironKit.setItem(33, drink);
		ironKit.setItem(34, drink);
		ironKit.setItem(35, new ItemStack(Material.SUGAR));
	}
	
	private void buildItems() {

		ItemStack corsairsedge = new ItemStack(Material.IRON_SWORD);
		ItemMeta corsairsedgeItemMeta = corsairsedge.getItemMeta();
		List<String> corsairsedLelore = new ArrayList<String>();
		corsairsedLelore.add(ChatColor.BLUE + "Legendary Weapon");
		corsairsedLelore.add(ChatColor.BLUE + "Deal 33% more damage against players holding a diamond sword");
		corsairsedgeItemMeta.setLore(corsairsedLelore);
		corsairsedgeItemMeta.setDisplayName(ChatColor.RESET + "Corsair's Edge");
		corsairsedge.setItemMeta(corsairsedgeItemMeta);
		
		ItemStack endeavour = new ItemStack(Material.STONE_SWORD);
		ItemMeta endeavourItemMeta = endeavour.getItemMeta();
		List<String> endeavourLore = new ArrayList<String>();
		endeavourLore.add(ChatColor.BLUE + "Legendary Weapon");
		endeavourLore.add(ChatColor.BLUE + "Deal 60% more damage if a player has more health than you");
		endeavourItemMeta.setLore(endeavourLore);
		endeavourItemMeta.setDisplayName(ChatColor.RESET + "Endeavour");
		endeavour.setItemMeta(endeavourItemMeta);		
		
		ItemStack flail = new ItemStack(Material.GOLD_SWORD);
		ItemMeta flailItemMeta = flail.getItemMeta();
		List<String> flailLore = new ArrayList<String>();
		flailLore.add(ChatColor.BLUE + "Legendary Weapon");
		flailLore.add(ChatColor.BLUE + "Deal more damage at less health and less damage at high health");
		flailItemMeta.setLore(flailLore);
		flailItemMeta.setDisplayName(ChatColor.RESET + "Flail");
		flail.setItemMeta(flailItemMeta);	
		
		ItemStack gamble = new ItemStack(Material.STONE_SWORD);
		ItemMeta gambleItemMeta = gamble.getItemMeta();
		List<String> gambleLore = new ArrayList<String>();
		gambleLore.add(ChatColor.BLUE + "Legendary Weapon");
		gambleLore.add(ChatColor.BLUE + "50% chance to deal 2.5 hearts of " + ChatColor.YELLOW + "true damage" + ChatColor.BLUE + " to a player");
		gambleLore.add(ChatColor.BLUE + "50% chance to deal 3 hearts of " + ChatColor.YELLOW + "true damage" + ChatColor.BLUE + " to you");
		gambleItemMeta.setLore(gambleLore);
		gambleItemMeta.setDisplayName(ChatColor.RESET + "Gamble");
		gamble.setItemMeta(gambleItemMeta);	
		
		ItemStack ipsesfolly = new ItemStack(Material.GOLD_SWORD);
		ItemMeta ipsesfollyItemMeta = ipsesfolly.getItemMeta();
		List<String> ipsesfollyLore = new ArrayList<String>();
		ipsesfollyLore.add(ChatColor.BLUE + "Legendary Weapon");
		ipsesfollyLore.add(ChatColor.BLUE + "Right Click: Give players around Regen II for 15 seconds");
		ipsesfollyLore.add(ChatColor.BLUE + "Takes 10 durability and 5 hearts of damage ");
		ipsesfollyItemMeta.setLore(ipsesfollyLore);
		ipsesfollyItemMeta.setDisplayName(ChatColor.RESET + "Ipse's Folly");
		ipsesfolly.setItemMeta(ipsesfollyItemMeta);	
		
		ItemStack kikuichimonji = new ItemStack(Material.WOOD_SWORD);
		ItemMeta kikuichimonjiItemMeta = kikuichimonji.getItemMeta();
		List<String> kikuichimonjiLore = new ArrayList<String>();
		kikuichimonjiLore.add(ChatColor.BLUE + "Legendary Weapon");
		kikuichimonjiLore.add(ChatColor.BLUE + "Has a 33% chance to give a player Poison I for 5 seconds.");
		kikuichimonjiLore.add(ChatColor.BLUE + "Has a 15% chance to give you Poison I for 5 seconds.");
		kikuichimonjiLore.add(ChatColor.BLUE + "Deals the damage of an iron sword");
		kikuichimonjiItemMeta.setLore(kikuichimonjiLore);
		kikuichimonjiItemMeta.setDisplayName(ChatColor.RESET + "Kikuichimonji");
		kikuichimonji.setItemMeta(kikuichimonjiItemMeta);
		
		ItemStack masamune = new ItemStack(Material.IRON_SWORD);
		ItemMeta masamuneItemMeta = masamune.getItemMeta();
		List<String> masamuneLore = new ArrayList<String>();
		masamuneLore.add(ChatColor.BLUE + "Legendary Weapon");
		masamuneLore.add(ChatColor.BLUE + "Right Click: Heal 1.5 hearts");
		masamuneLore.add(ChatColor.BLUE + "Gives slowness and weakness after use");
		masamuneItemMeta.setLore(masamuneLore);
		masamuneItemMeta.setDisplayName(ChatColor.RESET + "Masamune");
		masamune.setItemMeta(masamuneItemMeta);
		
		ItemStack muramasa = new ItemStack(Material.IRON_SWORD);
		ItemMeta muramasaItemMeta = muramasa.getItemMeta();
		List<String> muramasaLore = new ArrayList<String>();
		muramasaLore.add(ChatColor.BLUE + "Legendary Weapon");
		muramasaLore.add(ChatColor.BLUE + "Has a 15% chance to take away 1 hunger bar from a player");
		muramasaLore.add(ChatColor.BLUE + "Has a 9% chance to take away 1 hunger bar from you");
		muramasaItemMeta.setLore(muramasaLore);
		muramasaItemMeta.setDisplayName(ChatColor.RESET + "Muramasa");
		muramasa.setItemMeta(muramasaItemMeta);
		
		ItemStack nightsshadow = new ItemStack(Material.IRON_SWORD);
		ItemMeta nightsshadowItemMeta = nightsshadow.getItemMeta();
		List<String> nightsshadowLore = new ArrayList<String>();
		nightsshadowLore.add(ChatColor.BLUE + "Legendary Weapon");
		nightsshadowLore.add(ChatColor.BLUE + "Has a 10% chance to give the player hit Nausea IV for 8 seconds");
		nightsshadowLore.add(ChatColor.BLUE + "This effect only works at night");
		nightsshadowItemMeta.setLore(nightsshadowLore);
		nightsshadowItemMeta.setDisplayName(ChatColor.RESET + "Night's Shadow");
		nightsshadow.setItemMeta(nightsshadowItemMeta);
		
		ItemStack overkill = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta overkillItemMeta = overkill.getItemMeta();
		List<String> overkillLore = new ArrayList<String>();
		overkillLore.add(ChatColor.BLUE + "Legendary Weapon");
		overkillLore.add(ChatColor.BLUE + "Right Click: Charge at the cost of 50 durability");
		overkillLore.add(ChatColor.BLUE + "After charging, your next attack in 5 seconds");
		overkillLore.add(ChatColor.BLUE + "does" + ChatColor.YELLOW + " true damage " + ChatColor.BLUE + "based on a player's missing health");
		overkillItemMeta.setLore(overkillLore);
		overkillItemMeta.setDisplayName(ChatColor.RESET + "Overkill");
		overkill.setItemMeta(overkillItemMeta);
		
		ItemStack peace = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta peaceItemMeta = peace.getItemMeta();
		List<String> peaceLore = new ArrayList<String>();
		peaceLore.add(ChatColor.BLUE + "Legendary Weapon");
		peaceLore.add(ChatColor.BLUE + "Has a 25% chance to give a player Weakness II for 4 seconds");
		peaceLore.add(ChatColor.BLUE + "Has a 15% chance to give you Weakness II for 4 seconds");
		peaceItemMeta.setLore(peaceLore);
		peaceItemMeta.setDisplayName(ChatColor.RESET + "Peace");
		peace.setItemMeta(peaceItemMeta);
		
		ItemStack robbersblade = new ItemStack(Material.WOOD_SWORD);
		ItemMeta robbersbladeItemMeta =robbersblade.getItemMeta();
		List<String> robbersbladeLore = new ArrayList<String>();
		robbersbladeLore.add(ChatColor.BLUE + "Legendary Weapon");
		robbersbladeLore.add(ChatColor.BLUE + "Steals a random item from a player");
		robbersbladeLore.add(ChatColor.BLUE + "Breaks after use");
		robbersbladeItemMeta.setLore(robbersbladeLore);
		robbersbladeItemMeta.setDisplayName(ChatColor.RESET + "Robber's Blade");
		robbersblade.setItemMeta(robbersbladeItemMeta);

		ItemStack vampyr = new ItemStack(Material.IRON_SWORD);
		ItemMeta vampyrItemMeta = vampyr.getItemMeta();
		List<String> vampyrLore = new ArrayList<String>();
		vampyrLore.add(ChatColor.BLUE + "Legendary Weapon");
		vampyrLore.add(ChatColor.BLUE + "Has a 40% chance to heal 0.5 heart");
		vampyrLore.add(ChatColor.BLUE + "Does 25% less damage");
		vampyrItemMeta.setLore(vampyrLore);
		vampyrItemMeta.setDisplayName(ChatColor.RESET + "Vampyr");
		vampyr.setItemMeta(vampyrItemMeta);
		
		// BOWS
		
		ItemStack slowbow = new ItemStack(Material.BOW);
		ItemMeta slowbowItemMeta = vampyr.getItemMeta();
		List<String> slowbowLore = new ArrayList<String>();
		slowbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		slowbowLore.add(ChatColor.BLUE + "Gives player shot Slowness II\n");
		slowbowLore.add(ChatColor.BLUE + "based on distance traveled");
		slowbowLore.add(ChatColor.BLUE + "Consumes 2 arrows per shot");
		slowbowItemMeta.setLore(slowbowLore);
		slowbowItemMeta.setDisplayName(ChatColor.RESET + "Slow Bow");
		slowbow.setItemMeta(slowbowItemMeta);
		
		ItemStack healbow = new ItemStack(Material.BOW);
		ItemMeta healbowItemMeta = healbow.getItemMeta();
		List<String> healbowLore = new ArrayList<String>();
		healbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		healbowLore.add(ChatColor.BLUE + "Heal 2.5 hearts to the player shot");
		healbowLore.add(ChatColor.BLUE + "Consumes 2 arrows per shot");
		healbowItemMeta.setLore(healbowLore);
		healbowItemMeta.setDisplayName(ChatColor.RESET + "Heal Bow");
		healbow.setItemMeta(healbowItemMeta);
		
		ItemStack quiet = new ItemStack(Material.BOW);
		ItemMeta quietItemMeta = quiet.getItemMeta();
		List<String> quietLore = new ArrayList<String>();
		quietLore.add(ChatColor.BLUE + "Legendary Weapon");
		quietLore.add(ChatColor.BLUE + "Plays a ghast sound to the player shot");
		quietItemMeta.setLore(quietLore);
		quietItemMeta.setDisplayName(ChatColor.RESET + "Quiet");
		quiet.setItemMeta(quietItemMeta);
		
		ItemStack rocksteady = new ItemStack(Material.BOW);
		ItemMeta rocksteadyItemMeta = rocksteady.getItemMeta();
		List<String> rocksteadyLore = new ArrayList<String>();
		rocksteadyLore.add(ChatColor.BLUE + "Legendary Weapon");
		rocksteadyLore.add(ChatColor.BLUE + "Plays a ghast sound to the player shot");
		rocksteadyItemMeta.setLore(rocksteadyLore);
		rocksteadyItemMeta.setDisplayName(ChatColor.RESET + "Rocksteady");
		rocksteady.setItemMeta(rocksteadyItemMeta);
		
		ItemStack shotbow = new ItemStack(Material.BOW);
		ItemMeta shotbowItemMeta = shotbow.getItemMeta();
		List<String> shotbowLore = new ArrayList<String>();
		shotbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		shotbowLore.add(ChatColor.BLUE + "Shoots a volley of 8 arrows");
		shotbowItemMeta.setLore(shotbowLore);
		shotbowItemMeta.setDisplayName(ChatColor.RESET + "Shotbow");
		shotbow.setItemMeta(shotbowItemMeta);
		
		ItemStack spikethrower = new ItemStack(Material.BOW);
		ItemMeta spikethrowerItemMeta = spikethrower.getItemMeta();
		List<String> spikethrowerLore = new ArrayList<String>();
		spikethrowerLore.add(ChatColor.BLUE + "Legendary Weapon");
		spikethrowerLore.add(ChatColor.BLUE + "10 arrows randomly scatter in a circle around the player shot");
		spikethrowerItemMeta.setLore(spikethrowerLore);
		spikethrowerItemMeta.setDisplayName(ChatColor.RESET + "Spike Thrower");
		spikethrower.setItemMeta(spikethrowerItemMeta);
		
		ItemStack venombow = new ItemStack(Material.BOW);
		ItemMeta venombowItemMeta = venombow.getItemMeta();
		List<String> venombowLore = new ArrayList<String>();
		venombowLore.add(ChatColor.BLUE + "Legendary Weapon");
		venombowLore.add(ChatColor.BLUE + " a");
		venombowItemMeta.setLore(venombowLore);
		venombowItemMeta.setDisplayName(ChatColor.RESET + "Venom Bow");
		venombow.setItemMeta(venombowItemMeta);
		
		ItemStack voidbow = new ItemStack(Material.BOW);
		ItemMeta voidbowItemMeta = voidbow.getItemMeta();
		List<String> voidbowLore = new ArrayList<String>();
		voidbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		voidbowLore.add(ChatColor.BLUE + "Removes all status effects from the player shot at full charge");
		voidbowLore.add(ChatColor.BLUE + "Consumes 3 arrows. Arrows travels");
		voidbowItemMeta.setLore(voidbowLore);
		voidbowItemMeta.setDisplayName(ChatColor.RESET + "Void Bow");
		voidbow.setItemMeta(voidbowItemMeta);
		
		ItemStack zombiebow = new ItemStack(Material.BOW);
		ItemMeta zombiebowItemMeta = zombiebow.getItemMeta();
		List<String> zombiebowLore = new ArrayList<String>();
		zombiebowLore.add(ChatColor.BLUE + "Legendary Weapon");
		zombiebowLore.add(ChatColor.BLUE + "Fires a zombie egg");
		zombiebowLore.add(ChatColor.BLUE + "Has a chance to spawn a zombie");
		zombiebowItemMeta.setLore(zombiebowLore);
		zombiebowItemMeta.setDisplayName(ChatColor.RESET + "Zombie Bow");
		zombiebow.setItemMeta(zombiebowItemMeta);
		
		ItemStack truthbow = new ItemStack(Material.BOW);
		ItemMeta truthbowItemMeta = zombiebow.getItemMeta();
		List<String> truthbowLore = new ArrayList<String>();
		truthbowLore.add(ChatColor.BLUE + "Legendary Weapon");
		truthbowLore.add(ChatColor.BLUE + "Marks player for 10 seconds");
		truthbowLore.add(ChatColor.BLUE + "Marked player takes 10% more damage");
		truthbowLore.add(ChatColor.BLUE + "Only works at full charge");
		truthbowItemMeta.setLore(truthbowLore);
		truthbowItemMeta.setDisplayName(ChatColor.RESET + "Truth Bow");
		truthbow.setItemMeta(truthbowItemMeta);
		
		
		//LEGENDARY ARMORS
		
		ItemStack gorgonsgaze = new ItemStack(Material.IRON_HELMET);
		ItemMeta gorgonsgazeItemMeta = gorgonsgaze.getItemMeta();
		List<String> gorgonsgazeLore = new ArrayList<String>();
		gorgonsgazeLore.add(ChatColor.BLUE + "Legendary Armor");
		gorgonsgazeLore.add(ChatColor.BLUE + "Has a 25% chance to give the attacker slowness I for 3 seconds");
		gorgonsgazeLore.add(ChatColor.BLUE + "Has a 15% chance to give the wearer slowness I for 3 seconds");
		gorgonsgazeLore.add(ChatColor.BLUE + "Only works at full charge");
		gorgonsgazeItemMeta.setLore(gorgonsgazeLore);
		gorgonsgazeItemMeta.setDisplayName(ChatColor.RESET + "Gorgon's Gaze");
		gorgonsgaze.setItemMeta(gorgonsgazeItemMeta);
		
		ItemStack helmetofvision = new ItemStack(Material.IRON_HELMET);
		ItemMeta helmetofvisionItemMeta = helmetofvision.getItemMeta();
		List<String> helmetofvisionLore = new ArrayList<String>();
		helmetofvisionLore.add(ChatColor.BLUE + "Legendary Armor");
		helmetofvisionLore.add(ChatColor.BLUE + "Gives the wearer Night Vision IV while equipped.");
		helmetofvisionItemMeta.setLore(helmetofvisionLore);
		helmetofvisionItemMeta.setDisplayName(ChatColor.RESET + "Helmet of Vision");
		helmetofvision.setItemMeta(helmetofvisionItemMeta);
		
		ItemStack ninjasandals = new ItemStack(Material.LEATHER_BOOTS);
		ItemMeta ninjasandalsItemMeta = ninjasandals.getItemMeta();
		List<String> ninjasandalsLore = new ArrayList<String>();
		ninjasandalsLore.add(ChatColor.BLUE + "Legendary Armor");
		ninjasandalsLore.add(ChatColor.BLUE + "Has a 25% chance to give the wearer");
		ninjasandalsLore.add(ChatColor.BLUE + "Speed I for 3 seconds");
		ninjasandalsItemMeta.setLore(ninjasandalsLore);
		ninjasandalsItemMeta.setDisplayName(ChatColor.RESET + "Ninja Sandals");
		ninjasandals.setItemMeta(ninjasandalsItemMeta);
		
		ItemStack rabbitfeet = new ItemStack(Material.IRON_BOOTS);
		ItemMeta rabbitfeetItemMeta = rabbitfeet.getItemMeta();
		List<String> rabbitfeetLore = new ArrayList<String>();
		rabbitfeetLore.add(ChatColor.BLUE + "Legendary Armor");
		rabbitfeetLore.add(ChatColor.BLUE + "Has a 25% chance to give the wearer");
		rabbitfeetLore.add(ChatColor.BLUE + "Speed I for 3 seconds");
		rabbitfeetItemMeta.setLore(rabbitfeetLore);
		rabbitfeetItemMeta.setDisplayName(ChatColor.RESET + "Rabbit Feet");
		rabbitfeet.setItemMeta(rabbitfeetItemMeta);
		
		ItemStack rubbershield = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta rubbershieldItemMeta = rubbershield.getItemMeta();
		List<String> rubbershieldLore = new ArrayList<String>();
		rubbershieldLore.add(ChatColor.BLUE + "Legendary Armor");
		rubbershieldLore.add(ChatColor.BLUE + "Takes 20% less damage from arrows");
		rubbershieldLore.add(ChatColor.BLUE + "Takes 20% more damage from swords or axes");
		rubbershieldItemMeta.setLore(rubbershieldLore);
		rubbershieldItemMeta.setDisplayName(ChatColor.RESET + "Rubber Shield");
		rubbershield.setItemMeta(rubbershieldItemMeta);

		ItemStack webshot = new ItemStack(Material.BOW);
		ItemMeta webshotItemMeta = webshot.getItemMeta();
		List<String> webshotLore = new ArrayList<String>();
		webshotLore.add(ChatColor.BLUE + "Legendary Bow");
		webshotItemMeta.setLore(webshotLore);
		webshotItemMeta.setDisplayName(ChatColor.RESET + "Web Shot");
		webshot.setItemMeta(webshotItemMeta);


		//ELITE LEGENDARIES
		
		ItemStack simoonssong = new ItemStack(Material.IRON_SWORD);
		ItemMeta simoonssongItemMeta = simoonssong.getItemMeta();
		List<String> simoonssongLore = new ArrayList<String>();
		simoonssongLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
		simoonssongLore.add(ChatColor.AQUA + "Legendary Weapon");
		simoonssongLore.add(ChatColor.BLUE + "Inflicts a powerful knockback on the player hit");
		simoonssongLore.add(ChatColor.BLUE + "Gives you slowness II for 4 seconds");
		simoonssongItemMeta.setLore(simoonssongLore);
		simoonssongItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Song");
		simoonssong.setItemMeta(simoonssongItemMeta);
		
		ItemStack simoonsmelody = new ItemStack(Material.BOW);
		ItemMeta simoonsmelodyItemMeta = simoonsmelody.getItemMeta();
		List<String> simoonsmelodyLore = new ArrayList<String>();
		simoonsmelodyLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
		simoonsmelodyLore.add(ChatColor.AQUA + "Legendary Weapon");
		simoonsmelodyLore.add(ChatColor.BLUE + "Gives the player shot speed I for 10 seconds");
		simoonsmelodyLore.add(ChatColor.BLUE + "Consumes 3 arrows per shot");
		simoonsmelodyItemMeta.setLore(simoonsmelodyLore);
		simoonsmelodyItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Melody");
		simoonsmelody.setItemMeta(simoonsmelodyItemMeta);
		
		ItemStack simoonssonata = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta simoonssonataItemMeta = simoonssonata.getItemMeta();
		List<String> simoonssonataLore = new ArrayList<String>();
		simoonssonataLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
		simoonssonataLore.add(ChatColor.AQUA + "Legendary Armor");
		simoonssonataLore.add(ChatColor.BLUE + "Has a 25% chance to give you speed I for 3 seconds");
		simoonssonataLore.add(ChatColor.BLUE + "when you attack a player, at the cost of 3 durability");
		simoonssonataItemMeta.setLore(simoonssonataLore);
		simoonssonataItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Sonata");
		simoonssonata.setItemMeta(simoonssonataItemMeta);
		
		ItemStack simoonstune = new ItemStack(Material.POTION, 1, (byte)0);
		ItemMeta simoonstuneItemMeta = simoonstune.getItemMeta();
		List<String> simoonstuneLore = new ArrayList<String>();
		simoonstuneLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
		simoonstuneLore.add(ChatColor.AQUA + "Legendary Potion");
		simoonstuneLore.add(ChatColor.BLUE + "Gives 30 seconds of speed III and jump boost III");
		simoonstuneLore.add(ChatColor.BLUE + "After the effects run out, gives 15 seconds of");
		simoonstuneLore.add(ChatColor.BLUE + "slowness and weakness");
		simoonstuneItemMeta.setLore(simoonstuneLore);
		simoonstuneItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Tune");
		simoonstune.setItemMeta(simoonstuneItemMeta);
		
		ItemStack therumsstrength = new ItemStack(Material.IRON_SWORD);
		ItemMeta therumsstrengthItemMeta = therumsstrength.getItemMeta();
		List<String> therumsstrengthLore = new ArrayList<String>();
		therumsstrengthLore.add(ChatColor.GOLD + "Therum's Set");
		therumsstrengthLore.add(ChatColor.AQUA + "Legendary Weapon");
		therumsstrengthLore.add(ChatColor.BLUE + "Has a 20% chance to give you resistance I");
		therumsstrengthLore.add(ChatColor.BLUE + "and slowness I for 4 seconds when you attack a player");
		therumsstrengthItemMeta.setLore(therumsstrengthLore);
		therumsstrengthItemMeta.setDisplayName(ChatColor.RESET + "Therum's Strength");
		therumsstrength.setItemMeta(therumsstrengthItemMeta);
		
		ItemStack therumspower = new ItemStack(Material.BOW);
		ItemMeta therumspowerItemMeta = therumspower.getItemMeta();
		List<String> therumspowerLore = new ArrayList<String>();
		therumspowerLore.add(ChatColor.GOLD + "Therum's Set");
		therumspowerLore.add(ChatColor.AQUA + "Legendary Weapon");
		therumspowerLore.add(ChatColor.BLUE + "Gives the player shot resistance I for 10 seconds");
		therumspowerLore.add(ChatColor.BLUE + "Consumes 3 arrows per shot");
		therumspowerItemMeta.setLore(therumspowerLore);
		therumspowerItemMeta.setDisplayName(ChatColor.RESET + "Therum's Power");
		therumspower.setItemMeta(therumspowerItemMeta);
		
		ItemStack therumsforce = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta therumsforceItemMeta = therumsforce.getItemMeta();
		List<String> therumsforceLore = new ArrayList<String>();
		therumsforceLore.add(ChatColor.GOLD + "Therum's Set");
		therumsforceLore.add(ChatColor.AQUA + "Legendary Armor");
		therumsforceLore.add(ChatColor.BLUE + "Has a 25% chance to give you resistance I and");
		therumsforceLore.add(ChatColor.BLUE + "slowness I for 3 seconds when hit");
		therumsforceLore.add(ChatColor.BLUE + "at the cost of 3 durability");
		therumsforceItemMeta.setLore(therumsforceLore);
		therumsforceItemMeta.setDisplayName(ChatColor.RESET + "Therum's Force");
		therumsforce.setItemMeta(therumsforceItemMeta);
		
		ItemStack therumsmight = new ItemStack(Material.POTION, 1, (byte)0);
		ItemMeta therumsmightItemMeta = therumsmight.getItemMeta();
		List<String> therumsmightLore = new ArrayList<String>();
		therumsmightLore.add(ChatColor.GOLD + "Therum's Set");
		therumsmightLore.add(ChatColor.AQUA + "Legendary Potion");
		therumsmightLore.add(ChatColor.BLUE + "Gives 30 seconds of resistance, slowness, weakness III.");
		therumsmightLore.add(ChatColor.BLUE + "Increase your max health to 14 hearts and heal 4 hearts");
		therumsmightLore.add(ChatColor.BLUE + "for 30 seconds");
		therumsmightItemMeta.setLore(therumsmightLore);
		therumsmightItemMeta.setDisplayName(ChatColor.RESET + "Therum's Might");
		therumsmight.setItemMeta(therumsmightItemMeta);
		
		
		ItemStack agnisrage = new ItemStack(Material.IRON_SWORD);
		ItemMeta agnisrageItemMeta = agnisrage.getItemMeta();
		List<String> agnisrageLore = new ArrayList<String>();
		agnisrageLore.add(ChatColor.RED + "Agni's Set");
		agnisrageLore.add(ChatColor.AQUA + "Legendary Sword");
		agnisrageLore.add(ChatColor.BLUE + "Has a 25% chance to set the player hit on fire");
		agnisrageLore.add(ChatColor.BLUE + "Has a 15% chance to set you on fire");
		agnisrageItemMeta.setLore(agnisrageLore);
		agnisrageItemMeta.setDisplayName(ChatColor.RESET + "Agni's Rage");
		agnisrage.setItemMeta(agnisrageItemMeta);
		
		ItemStack agnisfury = new ItemStack(Material.BOW);
		ItemMeta agnisfuryItemMeta = agnisfury.getItemMeta();
		List<String> agnisfuryLore = new ArrayList<String>();
		agnisfuryLore.add(ChatColor.RED + "Agni's Set");
		agnisfuryLore.add(ChatColor.AQUA + "Legendary Bow");
		agnisfuryLore.add(ChatColor.BLUE + "Summons a bolt of lightning and set the players damaged on fire");
		agnisfuryLore.add(ChatColor.BLUE + "at the cost of 3 arrows and 5 durability.");
		agnisfuryItemMeta.setLore(agnisfuryLore);
		agnisfuryItemMeta.setDisplayName(ChatColor.RESET + "Agni's Fury");
		agnisfury.setItemMeta(agnisfuryItemMeta);
		
		
		
		ItemStack pluviastempest = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta pluviastempestItemMeta = pluviastempest.getItemMeta();
		List<String> pluviastempestLore = new ArrayList<String>();
		pluviastempestLore.add(ChatColor.AQUA + "Pluvia's Set");
		pluviastempestLore.add(ChatColor.AQUA + "Legendary Armor");
		pluviastempestLore.add(ChatColor.BLUE + "Has a 25% chance to heal you when you get hit.");
		pluviastempestLore.add(ChatColor.BLUE + "Has a 15% chance to heal the player who attacked you.");
		pluviastempestItemMeta.setLore(pluviastempestLore);
		pluviastempestItemMeta.setDisplayName(ChatColor.RESET + "Pluvia's Tempest");
		pluviastempest.setItemMeta(pluviastempestItemMeta);
		
		
		
		ItemStack sealoftime = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
		ItemMeta sealoftimeItemMeta = sealoftime.getItemMeta();
		List<String> sealoftimeLore = new ArrayList<String>();
		sealoftimeLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealoftimeLore.add(ChatColor.BLUE + "Right Click: Sets a recall point. After 5 seconds,");
		sealoftimeLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
		sealoftimeItemMeta.setLore(sealoftimeLore);
		sealoftimeItemMeta.setDisplayName(ChatColor.RESET + "Seal of Time");
		sealoftime.setItemMeta(sealoftimeItemMeta);
		
		ItemStack sealofspace = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
		ItemMeta sealofspaceItemMeta = sealofspace.getItemMeta();
		List<String> sealofspaceLore = new ArrayList<String>();
		sealofspaceLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealofspaceLore.add(ChatColor.BLUE + "Right Click: Teleport to the block you are looking at");
		sealofspaceLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
		sealofspaceItemMeta.setLore(sealofspaceLore);
		sealofspaceItemMeta.setDisplayName(ChatColor.RESET + "Seal of Space");
		sealofspace.setItemMeta(sealofspaceItemMeta);
		
		ItemStack sealofgravity = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
		ItemMeta sealofgravityItemMeta = sealofgravity.getItemMeta();
		List<String> sealofgravityLore = new ArrayList<String>();
		sealofgravityLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealofgravityLore.add(ChatColor.BLUE + "Right Click: Throw");
		sealofgravityLore.add(ChatColor.BLUE + "Launch nearby players towards the seal");
		sealofgravityItemMeta.setLore(sealofgravityLore);
		sealofgravityItemMeta.setDisplayName(ChatColor.RESET + "Seal of Gravity");
		sealofgravity.setItemMeta(sealofgravityItemMeta);
		
		ItemStack sealofentropy = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
		ItemMeta sealofentropyItemMeta = sealofentropy.getItemMeta();
		List<String> sealofentropyLore = new ArrayList<String>();
		sealofentropyLore.add(ChatColor.DARK_PURPLE + "Lore here");
		sealofentropyLore.add(ChatColor.BLUE + ".");
		sealofentropyLore.add(ChatColor.BLUE + ".");
		sealofentropyItemMeta.setLore(sealofentropyLore);
		sealofentropyItemMeta.setDisplayName(ChatColor.RESET + "Seal of Entropy");
		sealofentropy.setItemMeta(sealofentropyItemMeta);
		
		
		
		
		
		ItemStack infectionStick = new ItemStack(Material.STICK);
		ItemMeta infectionStickMeta  = infectionStick.getItemMeta();
		infectionStickMeta.setDisplayName(ChatColor.RESET + "Infection Stick");
		infectionStick.setItemMeta(infectionStickMeta);
		
		ItemStack bleedingStick = new ItemStack(Material.STICK);
		ItemMeta bleedingStickMeta  = bleedingStick.getItemMeta();
		bleedingStickMeta.setDisplayName(ChatColor.RESET + "Bleeding Stick");
		bleedingStick.setItemMeta(bleedingStickMeta);
		
		ItemStack grenade = new ItemStack(Material.ENDER_PEARL);
		ItemMeta grenadeMeta  =grenade.getItemMeta();
		grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
		grenade.setItemMeta(grenadeMeta);
		
		ItemStack sugar = new ItemStack(Material.SUGAR);
		
		ItemStack grapple = new ItemStack(Material.FISHING_ROD);
		ItemMeta grappleMeta  = grapple.getItemMeta();
		grappleMeta.setDisplayName(ChatColor.RESET + "Weak Grapple");
		grapple.setItemMeta(grappleMeta);
		
		ItemStack bandage = new ItemStack(Material.PAPER);
		ItemMeta bandageMeta  = bandage.getItemMeta();
		bandageMeta.setDisplayName(ChatColor.RESET + "Bandage");
		bandage.setItemMeta(bandageMeta);
		
		ItemStack depth = new ItemStack(Material.IRON_LEGGINGS);
		depth.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
		
		ItemStack shears = new ItemStack(Material.SHEARS);
		
		ItemStack reddye = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
		ItemMeta reddyeMeta = reddye.getItemMeta();
		reddyeMeta.setDisplayName(ChatColor.RESET + "Healing Ointment");
		reddye.setItemMeta(reddyeMeta);
		
		ItemStack yellowdye = new ItemStack(Material.INK_SACK, 1, DyeColor.YELLOW.getDyeData());
		ItemMeta yellowMeta = yellowdye.getItemMeta();
		yellowMeta.setDisplayName(ChatColor.RESET + "Yellow Dye");
		yellowdye.setItemMeta(yellowMeta);
		
		ItemStack DARK_GREENdye = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
		ItemMeta DARK_GREENdyeMeta = yellowdye.getItemMeta();
		DARK_GREENdyeMeta.setDisplayName(ChatColor.RESET + "Antibiotics");
		DARK_GREENdye.setItemMeta(DARK_GREENdyeMeta);
		
		ItemStack bluedye = new ItemStack(Material.INK_SACK, 1, DyeColor.LIGHT_BLUE.getDyeData());
		ItemMeta bluedyeMeta = bluedye.getItemMeta();
		bluedyeMeta.setDisplayName(ChatColor.RESET + "Blue Dye");
		bluedye.setItemMeta(bluedyeMeta);
		
		ItemStack nextpage1 = new ItemStack(Material.EMPTY_MAP);
		ItemMeta nextpage1Meta  = nextpage1.getItemMeta();
		nextpage1Meta.setDisplayName(ChatColor.RESET + "Next Page");
		nextpage1.setItemMeta(nextpage1Meta);
		
		ItemStack prevpage2 = new ItemStack(Material.EMPTY_MAP);
		ItemMeta prevpage2Meta  = prevpage2.getItemMeta();
		prevpage2Meta.setDisplayName(ChatColor.RESET + "Previous Page");
		prevpage2.setItemMeta(prevpage2Meta);
		
		ItemStack nextpage2 = new ItemStack(Material.EMPTY_MAP);
		ItemMeta nextpage2Meta  = nextpage2.getItemMeta();
		nextpage2Meta.setDisplayName(ChatColor.RESET + "Next Page");
		nextpage2.setItemMeta(nextpage2Meta);
		
		ItemStack prevpage3 = new ItemStack(Material.EMPTY_MAP);
		ItemMeta prevpage3Meta  = prevpage3.getItemMeta();
		prevpage3Meta.setDisplayName(ChatColor.RESET + "Previous Page");
		prevpage3.setItemMeta(prevpage3Meta);
		
		ItemStack nextpage3 = new ItemStack(Material.EMPTY_MAP);
		ItemMeta nextpage3Meta  = nextpage3.getItemMeta();
		nextpage3Meta.setDisplayName(ChatColor.RESET + "Next Page");
		nextpage3.setItemMeta(nextpage3Meta);
		
		// Swords
		itemPage1.setItem(0, corsairsedge);
		itemPage1.setItem(1, endeavour);
		itemPage1.setItem(2, flail);
		itemPage1.setItem(3, gamble);
		itemPage1.setItem(4, ipsesfolly);
		itemPage1.setItem(5, kikuichimonji);
		itemPage1.setItem(6, masamune);
		itemPage1.setItem(7, muramasa);
		itemPage1.setItem(8, nightsshadow);
		itemPage1.setItem(9, overkill);
		itemPage1.setItem(10, peace);
		itemPage1.setItem(11, robbersblade);
		itemPage1.setItem(12, vampyr);
		// Bows
		itemPage1.setItem(18, slowbow);
		itemPage1.setItem(19, healbow);
		itemPage1.setItem(20, quiet);
		itemPage1.setItem(21, rocksteady);
		itemPage1.setItem(22, shotbow);
		itemPage1.setItem(23, spikethrower);
		itemPage1.setItem(24, truthbow);
		itemPage1.setItem(25, venombow);
		itemPage1.setItem(26, voidbow);
		itemPage1.setItem(27, webshot);
		itemPage1.setItem(28, zombiebow);

					
		
		itemPage1.setItem(30, gorgonsgaze);
		itemPage1.setItem(31, helmetofvision);
		itemPage1.setItem(32, ninjasandals);
		itemPage1.setItem(33, rabbitfeet);
		itemPage1.setItem(34, rubbershield);

		itemPage1.setItem(51, bleedingStick);
		itemPage1.setItem(52, infectionStick);

		itemPage1.setItem(53, nextpage1);
	
	
		
		itemPage2.setItem(0, simoonssong);
		itemPage2.setItem(1, simoonsmelody);
		itemPage2.setItem(2, simoonssonata);
		itemPage2.setItem(3, simoonstune);
		itemPage2.setItem(9, therumsstrength);
		itemPage2.setItem(10, therumspower);
		itemPage2.setItem(11, therumsforce);
		itemPage2.setItem(12, therumsmight);

		itemPage2.setItem(18, agnisrage);
		itemPage2.setItem(19, agnisfury);

		itemPage2.setItem(29, pluviastempest);

		itemPage2.setItem(8, sealoftime);
		itemPage2.setItem(17, sealofspace);
		itemPage2.setItem(26, sealofgravity);
		itemPage2.setItem(35, sealofentropy);

		
		
		itemPage2.setItem(45, prevpage2);
		itemPage2.setItem(53, nextpage2);


		itemPage3.setItem(0, grenade);
		itemPage3.setItem(1, sugar);
		itemPage3.setItem(2, grapple);
		itemPage3.setItem(3, bandage);
		itemPage3.setItem(4, shears);
		itemPage3.setItem(5, reddye);
		itemPage3.setItem(6, yellowdye);
		itemPage3.setItem(7, DARK_GREENdye);
		itemPage3.setItem(8, bluedye);
		itemPage3.setItem(9, depth);	

		itemPage3.setItem(45, prevpage3);
		itemPage3.setItem(53, nextpage3);

	}
}
	