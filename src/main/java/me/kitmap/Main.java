package me.kitmap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import me.kitmap.commands.KothCommand;
import me.kitmap.commands.RenameCommand;
import me.kitmap.config.ConfigManager;
import me.kitmap.game.*;
import me.kitmap.items.itembuilder.KitBuilder;
import me.kitmap.items.itembuilder.KothLootBuilder;
import me.kitmap.items.itembuilder.LegendaryBuilder;
import me.kitmap.koth.KothManager;
import me.kitmap.scoreboard.KillDeathUpdater;
import me.kitmap.items.legendary.*;
import me.kitmap.items.minezitems.Grenade;
import me.kitmap.items.minezitems.Sugar;
import me.kitmap.koth.KothCrate;
import me.kitmap.signs.KitSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.kitmap.commands.ItemCommand;
import me.kitmap.items.ItemMenu;
import me.kitmap.items.minezitems.WeakGrapple;
import me.kitmap.scoreboard.PlayerBoards;
import me.kitmap.mysql.MysqlData;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public Plugin plugin;
	private Connection connection;
	private ConfigManager configManager;
	private String host, database, username, password;
	public String table;
	private int port;
	private KothManager kothManager;
	private LegendaryBuilder legendaryBuilder;
	private KitBuilder kitBuilder;
	private KothLootBuilder kothLootBuilder;
	private PlayerBoards playerBoards;
	private DamageModifier damageModifier;

	public double spawnMinX, spawnMaxX,spawnMinZ, spawnMaxZ;
	private ArrayList<Location> spawnBarrierBlocks = new ArrayList<>();

	private Overkill overkill;
	private PluviasTempest pluviasTempest;
	private Quiet quiet;
	private RobbersBlade robbersBlade;
	private SealOfEntropy sealOfEntropy;
	private SealOfGravity sealOfGravity;
	private SealOfSpace sealOfSpace;
	private SealOfTime sealOfTime;
	private Shotbow shotbow;
	private SpikeThrower spikeThrower;
	private TruthBow truthBow;
	private Vampyr vampyr;
	private WebShot webShot;
	private ZombieBow zombieBow;

	public void onEnable() {
		plugin = this;
        loadConfig();
        loadConfigManager();
		registerEvents();
		buildItems();
		mysqlsetup();
		registerCommands();
		setCoords();
		setSpawnBarrierBlocks();
		loadDamageValues();
	}
	
	public Plugin getInstance() {
		return plugin;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private void registerCommands(){
		ItemCommand itemsCommand = new ItemCommand(this.legendaryBuilder);
		KothCommand kothCommand = new KothCommand(this);
		RenameCommand renameCommand = new RenameCommand();
		getCommand("items").setExecutor(itemsCommand);
		getCommand("koth").setExecutor(kothCommand);
		getCommand("rename").setExecutor(renameCommand);
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
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadConfigManager(){
	    this.configManager = new ConfigManager();
	    this.configManager.setup();
	    this.configManager.saveDamage();
	    this.configManager.reloadDamage();
	    this.configManager.saveCoords();
	    this.configManager.reloadCoords();
    }

	public void loadConfig() { // Mysql
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public ConfigManager getConfigManager(){
		return this.configManager;
	}

	public KothManager getKothManager(){
		return this.kothManager;
	}

	public void setCoords(){
		this.spawnMinX = Double.parseDouble(configManager.getCoords().getString("Spawn.minX"));
		this.spawnMaxX = Double.parseDouble(configManager.getCoords().getString("Spawn.maxX"));
		this.spawnMinZ = Double.parseDouble(configManager.getCoords().getString("Spawn.minZ"));
		this.spawnMaxZ = Double.parseDouble(configManager.getCoords().getString("Spawn.maxZ"));
	}

	public void setSpawnBarrierBlocks() {
		for(int x=(int)this.spawnMinX;x<=(int)this.spawnMaxX;++x){
			for(int y=4;y<=7;++y){
				Location barrierLoc1 = new Location(Bukkit.getWorld("world"), x, y, this.spawnMinZ);
				Location barrierLoc2 = new Location(Bukkit.getWorld("world"), x, y, this.spawnMaxZ);
				this.spawnBarrierBlocks.add(barrierLoc1);
				this.spawnBarrierBlocks.add(barrierLoc2);
			}
		}
		for(int z=(int)this.spawnMinZ;z<=(int)this.spawnMaxZ;++z){
			for(int y=4;y<=7;++y){
				Location barrierLoc1 = new Location(Bukkit.getWorld("world"), this.spawnMinX, y, z);
				Location barrierLoc2 = new Location(Bukkit.getWorld("world"), this.spawnMaxX, y, z);
				this.spawnBarrierBlocks.add(barrierLoc1);
				this.spawnBarrierBlocks.add(barrierLoc2);
			}
		}
	}

	public void loadDamageValues(){
	    this.damageModifier.loadDamageValues();
    }

    private void registerEvents() {

        PluginManager pluginManager = getServer().getPluginManager();
        SpawnTag spawnTag = new SpawnTag(this);
        this.playerBoards = new PlayerBoards(this, new MysqlData(this ), spawnTag);

        pluginManager.registerEvents(playerBoards, this);
        pluginManager.registerEvents(new KillDeathUpdater(this, this.playerBoards), this);

		this.kothManager = new KothManager(this, null, this.playerBoards);
		this.legendaryBuilder = new LegendaryBuilder(this);
		this.kitBuilder = new KitBuilder();
		this.kothLootBuilder = new KothLootBuilder(this);
		this.damageModifier = new DamageModifier(this, this.configManager);

		this.overkill = new Overkill(this);
		this.pluviasTempest = new PluviasTempest(this);
		this.quiet = new Quiet(this);
		this.robbersBlade = new RobbersBlade(this);
		this.sealOfGravity = new SealOfGravity(this);
		this.sealOfSpace = new SealOfSpace(this);
		this.sealOfTime = new SealOfTime(this);
		this.shotbow = new Shotbow(this);
		this.spikeThrower = new SpikeThrower(this);
		this.truthBow = new TruthBow(this);
		this.vampyr = new Vampyr(this);
		this.webShot = new WebShot(this);
		this.zombieBow = new ZombieBow(this);

		pluginManager.registerEvents(spawnTag, this);

		pluginManager.registerEvents(new ItemMenu(legendaryBuilder), this);
		pluginManager.registerEvents(new KitSign(kitBuilder), this);
		pluginManager.registerEvents(new KothCrate(this, kothLootBuilder), this);
		pluginManager.registerEvents(new SpawnEnterBlocker(this, spawnTag), this);
        pluginManager.registerEvents(this.damageModifier, this);
		pluginManager.registerEvents(new EmptyBottleRemover(this), this);
		pluginManager.registerEvents(new DeathMessage(), this);

		pluginManager.registerEvents(new Grenade(this), this);
		pluginManager.registerEvents(new Sugar(this), this);
		pluginManager.registerEvents(new WeakGrapple(), this);
		pluginManager.registerEvents(this.zombieBow, this);
		pluginManager.registerEvents(this.truthBow, this);
		pluginManager.registerEvents(this.pluviasTempest, this);
		pluginManager.registerEvents(this.shotbow, this);
		pluginManager.registerEvents(this.quiet, this);
		pluginManager.registerEvents(this.webShot, this);

		pluginManager.registerEvents(this.robbersBlade, this);
		pluginManager.registerEvents(this.spikeThrower, this);
		pluginManager.registerEvents(this.overkill, this);
		pluginManager.registerEvents(this.sealOfTime, this);
		pluginManager.registerEvents(this.sealOfSpace, this);
		pluginManager.registerEvents(this.sealOfGravity, this);

	}

	private void buildItems(){
		this.kitBuilder.build();
		this.legendaryBuilder.build();
		this.kothLootBuilder.build();
	}

	public Overkill getOverkill(){
		return this.overkill;
	}

	public PluviasTempest  getPluviasTempest(){
		return this.pluviasTempest;
	}

	public Quiet getQuiet(){
		return this.quiet;
	}

	public RobbersBlade getrobbersBlade(){
		return this.robbersBlade;
	}

	public SealOfEntropy getSealOfEntropy(){
		return this.sealOfEntropy;
	}

	public SealOfGravity getSealOfGravity(){
		return this.sealOfGravity;
	}

	public SealOfTime getSealOfTime(){
		return this.sealOfTime;
	}

	public SealOfSpace getSealOfSpace(){
		return this.sealOfSpace;
	}

	public Shotbow getShotbow(){
		return this.shotbow;
	}

	public SpikeThrower getSpikeThrower(){
		return this.spikeThrower;
	}

	public TruthBow getTruthBow(){
		return this.truthBow;
	}

	public Vampyr getVampyr(){
		return this.vampyr;
	}

	public WebShot getWebShot(){
		return this.webShot;
	}

	public ZombieBow getZombieBow(){
		return this.zombieBow;
	}

	public ArrayList<Location> getSpawnBarrierBlocks() {
		return this.spawnBarrierBlocks;
	}
//	private void buildItems() { // Will remove once I move the rest of legendaries.
//
//		ItemStack corsairsedge = new ItemStack(Material.IRON_SWORD);
//		ItemMeta corsairsedgeItemMeta = corsairsedge.getItemMeta();
//		List<String> corsairsedLelore = new ArrayList<String>();
//		corsairsedLelore.add(ChatColor.BLUE + "Legendary Weapon");
//		corsairsedLelore.add(ChatColor.BLUE + "Deal 33% more damage against players holding a diamond sword");
//		corsairsedgeItemMeta.setLore(corsairsedLelore);
//		corsairsedgeItemMeta.setDisplayName(ChatColor.RESET + "Corsair's Edge");
//		corsairsedge.setItemMeta(corsairsedgeItemMeta);
//
//		ItemStack endeavour = new ItemStack(Material.STONE_SWORD);
//		ItemMeta endeavourItemMeta = endeavour.getItemMeta();
//		List<String> endeavourLore = new ArrayList<String>();
//		endeavourLore.add(ChatColor.BLUE + "Legendary Weapon");
//		endeavourLore.add(ChatColor.BLUE + "Deal 60% more damage if a player has more health than you");
//		endeavourItemMeta.setLore(endeavourLore);
//		endeavourItemMeta.setDisplayName(ChatColor.RESET + "Endeavour");
//		endeavour.setItemMeta(endeavourItemMeta);
//
//		ItemStack flail = new ItemStack(Material.GOLD_SWORD);
//		ItemMeta flailItemMeta = flail.getItemMeta();
//		List<String> flailLore = new ArrayList<String>();
//		flailLore.add(ChatColor.BLUE + "Legendary Weapon");
//		flailLore.add(ChatColor.BLUE + "Deal more damage at less health and less damage at high health");
//		flailItemMeta.setLore(flailLore);
//		flailItemMeta.setDisplayName(ChatColor.RESET + "Flail");
//		flail.setItemMeta(flailItemMeta);
//
//		ItemStack gamble = new ItemStack(Material.STONE_SWORD);
//		ItemMeta gambleItemMeta = gamble.getItemMeta();
//		List<String> gambleLore = new ArrayList<String>();
//		gambleLore.add(ChatColor.BLUE + "Legendary Weapon");
//		gambleLore.add(ChatColor.BLUE + "50% chance to deal 2.5 hearts of " + ChatColor.YELLOW + "true damage" + ChatColor.BLUE + " to a player");
//		gambleLore.add(ChatColor.BLUE + "50% chance to deal 3 hearts of " + ChatColor.YELLOW + "true damage" + ChatColor.BLUE + " to you");
//		gambleItemMeta.setLore(gambleLore);
//		gambleItemMeta.setDisplayName(ChatColor.RESET + "Gamble");
//		gamble.setItemMeta(gambleItemMeta);
//
//		ItemStack ipsesfolly = new ItemStack(Material.GOLD_SWORD);
//		ItemMeta ipsesfollyItemMeta = ipsesfolly.getItemMeta();
//		List<String> ipsesfollyLore = new ArrayList<String>();
//		ipsesfollyLore.add(ChatColor.BLUE + "Legendary Weapon");
//		ipsesfollyLore.add(ChatColor.BLUE + "Right Click: Give players around Regen II for 15 seconds");
//		ipsesfollyLore.add(ChatColor.BLUE + "Takes 10 durability and 5 hearts of damage ");
//		ipsesfollyItemMeta.setLore(ipsesfollyLore);
//		ipsesfollyItemMeta.setDisplayName(ChatColor.RESET + "Ipse's Folly");
//		ipsesfolly.setItemMeta(ipsesfollyItemMeta);
//
//		ItemStack kikuichimonji = new ItemStack(Material.WOOD_SWORD);
//		ItemMeta kikuichimonjiItemMeta = kikuichimonji.getItemMeta();
//		List<String> kikuichimonjiLore = new ArrayList<String>();
//		kikuichimonjiLore.add(ChatColor.BLUE + "Legendary Weapon");
//		kikuichimonjiLore.add(ChatColor.BLUE + "Has a 33% chance to give a player Poison I for 5 seconds.");
//		kikuichimonjiLore.add(ChatColor.BLUE + "Has a 15% chance to give you Poison I for 5 seconds.");
//		kikuichimonjiLore.add(ChatColor.BLUE + "Deals the damage of an iron sword");
//		kikuichimonjiItemMeta.setLore(kikuichimonjiLore);
//		kikuichimonjiItemMeta.setDisplayName(ChatColor.RESET + "Kikuichimonji");
//		kikuichimonji.setItemMeta(kikuichimonjiItemMeta);
//
//		ItemStack masamune = new ItemStack(Material.IRON_SWORD);
//		ItemMeta masamuneItemMeta = masamune.getItemMeta();
//		List<String> masamuneLore = new ArrayList<String>();
//		masamuneLore.add(ChatColor.BLUE + "Legendary Weapon");
//		masamuneLore.add(ChatColor.BLUE + "Right Click: Heal 1.5 hearts");
//		masamuneLore.add(ChatColor.BLUE + "Gives slowness and weakness after use");
//		masamuneItemMeta.setLore(masamuneLore);
//		masamuneItemMeta.setDisplayName(ChatColor.RESET + "Masamune");
//		masamune.setItemMeta(masamuneItemMeta);
//
//		ItemStack muramasa = new ItemStack(Material.IRON_SWORD);
//		ItemMeta muramasaItemMeta = muramasa.getItemMeta();
//		List<String> muramasaLore = new ArrayList<String>();
//		muramasaLore.add(ChatColor.BLUE + "Legendary Weapon");
//		muramasaLore.add(ChatColor.BLUE + "Has a 15% chance to take away 1 hunger bar from a player");
//		muramasaLore.add(ChatColor.BLUE + "Has a 9% chance to take away 1 hunger bar from you");
//		muramasaItemMeta.setLore(muramasaLore);
//		muramasaItemMeta.setDisplayName(ChatColor.RESET + "Muramasa");
//		muramasa.setItemMeta(muramasaItemMeta);
//
//		ItemStack nightsshadow = new ItemStack(Material.IRON_SWORD);
//		ItemMeta nightsshadowItemMeta = nightsshadow.getItemMeta();
//		List<String> nightsshadowLore = new ArrayList<String>();
//		nightsshadowLore.add(ChatColor.BLUE + "Legendary Weapon");
//		nightsshadowLore.add(ChatColor.BLUE + "Has a 10% chance to give the player hit Nausea IV for 8 seconds");
//		nightsshadowLore.add(ChatColor.BLUE + "This effect only works at night");
//		nightsshadowItemMeta.setLore(nightsshadowLore);
//		nightsshadowItemMeta.setDisplayName(ChatColor.RESET + "Night's Shadow");
//		nightsshadow.setItemMeta(nightsshadowItemMeta);
//
//		ItemStack peace = new ItemStack(Material.DIAMOND_SWORD);
//		ItemMeta peaceItemMeta = peace.getItemMeta();
//		List<String> peaceLore = new ArrayList<String>();
//		peaceLore.add(ChatColor.BLUE + "Legendary Weapon");
//		peaceLore.add(ChatColor.BLUE + "Has a 25% chance to give a player Weakness II for 4 seconds");
//		peaceLore.add(ChatColor.BLUE + "Has a 15% chance to give you Weakness II for 4 seconds");
//		peaceItemMeta.setLore(peaceLore);
//		peaceItemMeta.setDisplayName(ChatColor.RESET + "Peace");
//		peace.setItemMeta(peaceItemMeta);
//
//
//		// BOWS
//
//		ItemStack slowbow = new ItemStack(Material.BOW);
//		ItemMeta slowbowItemMeta = vampyr.getItemMeta();
//		List<String> slowbowLore = new ArrayList<String>();
//		slowbowLore.add(ChatColor.BLUE + "Legendary Weapon");
//		slowbowLore.add(ChatColor.BLUE + "Gives player shot Slowness II\n");
//		slowbowLore.add(ChatColor.BLUE + "based on distance traveled");
//		slowbowLore.add(ChatColor.BLUE + "Consumes 2 arrows per shot");
//		slowbowItemMeta.setLore(slowbowLore);
//		slowbowItemMeta.setDisplayName(ChatColor.RESET + "Slow Bow");
//		slowbow.setItemMeta(slowbowItemMeta);
//
//		ItemStack healbow = new ItemStack(Material.BOW);
//		ItemMeta healbowItemMeta = healbow.getItemMeta();
//		List<String> healbowLore = new ArrayList<String>();
//		healbowLore.add(ChatColor.BLUE + "Legendary Weapon");
//		healbowLore.add(ChatColor.BLUE + "Heal 2.5 hearts to the player shot");
//		healbowLore.add(ChatColor.BLUE + "Consumes 2 arrows per shot");
//		healbowItemMeta.setLore(healbowLore);
//		healbowItemMeta.setDisplayName(ChatColor.RESET + "Heal Bow");
//		healbow.setItemMeta(healbowItemMeta);
//
//
//		ItemStack rocksteady = new ItemStack(Material.BOW);
//		ItemMeta rocksteadyItemMeta = rocksteady.getItemMeta();
//		List<String> rocksteadyLore = new ArrayList<String>();
//		rocksteadyLore.add(ChatColor.BLUE + "Legendary Weapon");
//		rocksteadyLore.add(ChatColor.BLUE + "Plays a ghast sound to the player shot");
//		rocksteadyItemMeta.setLore(rocksteadyLore);
//		rocksteadyItemMeta.setDisplayName(ChatColor.RESET + "Rocksteady");
//		rocksteady.setItemMeta(rocksteadyItemMeta);
//
//		ItemStack venombow = new ItemStack(Material.BOW);
//		ItemMeta venombowItemMeta = venombow.getItemMeta();
//		List<String> venombowLore = new ArrayList<String>();
//		venombowLore.add(ChatColor.BLUE + "Legendary Weapon");
//		venombowLore.add(ChatColor.BLUE + " a");
//		venombowItemMeta.setLore(venombowLore);
//		venombowItemMeta.setDisplayName(ChatColor.RESET + "Venom Bow");
//		venombow.setItemMeta(venombowItemMeta);
//
//		ItemStack voidbow = new ItemStack(Material.BOW);
//		ItemMeta voidbowItemMeta = voidbow.getItemMeta();
//		List<String> voidbowLore = new ArrayList<String>();
//		voidbowLore.add(ChatColor.BLUE + "Legendary Weapon");
//		voidbowLore.add(ChatColor.BLUE + "Removes all status effects from the player shot at full charge");
//		voidbowLore.add(ChatColor.BLUE + "Consumes 3 arrows. Arrows travels");
//		voidbowItemMeta.setLore(voidbowLore);
//		voidbowItemMeta.setDisplayName(ChatColor.RESET + "Void Bow");
//		voidbow.setItemMeta(voidbowItemMeta);
//
//
//
//
//
//		//LEGENDARY ARMORS
//
//		ItemStack gorgonsgaze = new ItemStack(Material.IRON_HELMET);
//		ItemMeta gorgonsgazeItemMeta = gorgonsgaze.getItemMeta();
//		List<String> gorgonsgazeLore = new ArrayList<String>();
//		gorgonsgazeLore.add(ChatColor.BLUE + "Legendary Armor");
//		gorgonsgazeLore.add(ChatColor.BLUE + "Has a 25% chance to give the attacker slowness I for 3 seconds");
//		gorgonsgazeLore.add(ChatColor.BLUE + "Has a 15% chance to give the wearer slowness I for 3 seconds");
//		gorgonsgazeLore.add(ChatColor.BLUE + "Only works at full charge");
//		gorgonsgazeItemMeta.setLore(gorgonsgazeLore);
//		gorgonsgazeItemMeta.setDisplayName(ChatColor.RESET + "Gorgon's Gaze");
//		gorgonsgaze.setItemMeta(gorgonsgazeItemMeta);
//
//		ItemStack helmetofvision = new ItemStack(Material.IRON_HELMET);
//		ItemMeta helmetofvisionItemMeta = helmetofvision.getItemMeta();
//		List<String> helmetofvisionLore = new ArrayList<String>();
//		helmetofvisionLore.add(ChatColor.BLUE + "Legendary Armor");
//		helmetofvisionLore.add(ChatColor.BLUE + "Gives the wearer Night Vision IV while equipped.");
//		helmetofvisionItemMeta.setLore(helmetofvisionLore);
//		helmetofvisionItemMeta.setDisplayName(ChatColor.RESET + "Helmet of Vision");
//		helmetofvision.setItemMeta(helmetofvisionItemMeta);
//
//		ItemStack ninjasandals = new ItemStack(Material.LEATHER_BOOTS);
//		ItemMeta ninjasandalsItemMeta = ninjasandals.getItemMeta();
//		List<String> ninjasandalsLore = new ArrayList<String>();
//		ninjasandalsLore.add(ChatColor.BLUE + "Legendary Armor");
//		ninjasandalsLore.add(ChatColor.BLUE + "Has a 25% chance to give the wearer");
//		ninjasandalsLore.add(ChatColor.BLUE + "Speed I for 3 seconds");
//		ninjasandalsItemMeta.setLore(ninjasandalsLore);
//		ninjasandalsItemMeta.setDisplayName(ChatColor.RESET + "Ninja Sandals");
//		ninjasandals.setItemMeta(ninjasandalsItemMeta);
//
//		ItemStack rabbitfeet = new ItemStack(Material.IRON_BOOTS);
//		ItemMeta rabbitfeetItemMeta = rabbitfeet.getItemMeta();
//		List<String> rabbitfeetLore = new ArrayList<String>();
//		rabbitfeetLore.add(ChatColor.BLUE + "Legendary Armor");
//		rabbitfeetLore.add(ChatColor.BLUE + "Has a 25% chance to give the wearer");
//		rabbitfeetLore.add(ChatColor.BLUE + "Speed I for 3 seconds");
//		rabbitfeetItemMeta.setLore(rabbitfeetLore);
//		rabbitfeetItemMeta.setDisplayName(ChatColor.RESET + "Rabbit Feet");
//		rabbitfeet.setItemMeta(rabbitfeetItemMeta);
//
//		ItemStack rubbershield = new ItemStack(Material.IRON_CHESTPLATE);
//		ItemMeta rubbershieldItemMeta = rubbershield.getItemMeta();
//		List<String> rubbershieldLore = new ArrayList<String>();
//		rubbershieldLore.add(ChatColor.BLUE + "Legendary Armor");
//		rubbershieldLore.add(ChatColor.BLUE + "Takes 20% less damage from arrows");
//		rubbershieldLore.add(ChatColor.BLUE + "Takes 20% more damage from swords or axes");
//		rubbershieldItemMeta.setLore(rubbershieldLore);
//		rubbershieldItemMeta.setDisplayName(ChatColor.RESET + "Rubber Shield");
//		rubbershield.setItemMeta(rubbershieldItemMeta);
//
//
//		//ELITE LEGENDARIES
//
//		ItemStack simoonssong = new ItemStack(Material.IRON_SWORD);
//		ItemMeta simoonssongItemMeta = simoonssong.getItemMeta();
//		List<String> simoonssongLore = new ArrayList<String>();
//		simoonssongLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
//		simoonssongLore.add(ChatColor.AQUA + "Legendary Weapon");
//		simoonssongLore.add(ChatColor.BLUE + "Inflicts a powerful knockback on the player hit");
//		simoonssongLore.add(ChatColor.BLUE + "Gives you slowness II for 4 seconds");
//		simoonssongItemMeta.setLore(simoonssongLore);
//		simoonssongItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Song");
//		simoonssong.setItemMeta(simoonssongItemMeta);
//
//		ItemStack simoonsmelody = new ItemStack(Material.BOW);
//		ItemMeta simoonsmelodyItemMeta = simoonsmelody.getItemMeta();
//		List<String> simoonsmelodyLore = new ArrayList<String>();
//		simoonsmelodyLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
//		simoonsmelodyLore.add(ChatColor.AQUA + "Legendary Weapon");
//		simoonsmelodyLore.add(ChatColor.BLUE + "Gives the player shot speed I for 10 seconds");
//		simoonsmelodyLore.add(ChatColor.BLUE + "Consumes 3 arrows per shot");
//		simoonsmelodyItemMeta.setLore(simoonsmelodyLore);
//		simoonsmelodyItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Melody");
//		simoonsmelody.setItemMeta(simoonsmelodyItemMeta);
//
//		ItemStack simoonssonata = new ItemStack(Material.IRON_CHESTPLATE);
//		ItemMeta simoonssonataItemMeta = simoonssonata.getItemMeta();
//		List<String> simoonssonataLore = new ArrayList<String>();
//		simoonssonataLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
//		simoonssonataLore.add(ChatColor.AQUA + "Legendary Armor");
//		simoonssonataLore.add(ChatColor.BLUE + "Has a 25% chance to give you speed I for 3 seconds");
//		simoonssonataLore.add(ChatColor.BLUE + "when you attack a player, at the cost of 3 durability");
//		simoonssonataItemMeta.setLore(simoonssonataLore);
//		simoonssonataItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Sonata");
//		simoonssonata.setItemMeta(simoonssonataItemMeta);
//
//		ItemStack simoonstune = new ItemStack(Material.POTION, 1, (byte)0);
//		ItemMeta simoonstuneItemMeta = simoonstune.getItemMeta();
//		List<String> simoonstuneLore = new ArrayList<String>();
//		simoonstuneLore.add(ChatColor.DARK_PURPLE + "Simoon's Set");
//		simoonstuneLore.add(ChatColor.AQUA + "Legendary Potion");
//		simoonstuneLore.add(ChatColor.BLUE + "Gives 30 seconds of speed III and jump boost III");
//		simoonstuneLore.add(ChatColor.BLUE + "After the effects run out, gives 15 seconds of");
//		simoonstuneLore.add(ChatColor.BLUE + "slowness and weakness");
//		simoonstuneItemMeta.setLore(simoonstuneLore);
//		simoonstuneItemMeta.setDisplayName(ChatColor.RESET + "Simoon's Tune");
//		simoonstune.setItemMeta(simoonstuneItemMeta);
//
//		ItemStack therumsstrength = new ItemStack(Material.IRON_SWORD);
//		ItemMeta therumsstrengthItemMeta = therumsstrength.getItemMeta();
//		List<String> therumsstrengthLore = new ArrayList<String>();
//		therumsstrengthLore.add(ChatColor.GOLD + "Therum's Set");
//		therumsstrengthLore.add(ChatColor.AQUA + "Legendary Weapon");
//		therumsstrengthLore.add(ChatColor.BLUE + "Has a 20% chance to give you resistance I");
//		therumsstrengthLore.add(ChatColor.BLUE + "and slowness I for 4 seconds when you attack a player");
//		therumsstrengthItemMeta.setLore(therumsstrengthLore);
//		therumsstrengthItemMeta.setDisplayName(ChatColor.RESET + "Therum's Strength");
//		therumsstrength.setItemMeta(therumsstrengthItemMeta);
//
//		ItemStack therumspower = new ItemStack(Material.BOW);
//		ItemMeta therumspowerItemMeta = therumspower.getItemMeta();
//		List<String> therumspowerLore = new ArrayList<String>();
//		therumspowerLore.add(ChatColor.GOLD + "Therum's Set");
//		therumspowerLore.add(ChatColor.AQUA + "Legendary Weapon");
//		therumspowerLore.add(ChatColor.BLUE + "Gives the player shot resistance I for 10 seconds");
//		therumspowerLore.add(ChatColor.BLUE + "Consumes 3 arrows per shot");
//		therumspowerItemMeta.setLore(therumspowerLore);
//		therumspowerItemMeta.setDisplayName(ChatColor.RESET + "Therum's Power");
//		therumspower.setItemMeta(therumspowerItemMeta);
//
//		ItemStack therumsforce = new ItemStack(Material.IRON_CHESTPLATE);
//		ItemMeta therumsforceItemMeta = therumsforce.getItemMeta();
//		List<String> therumsforceLore = new ArrayList<String>();
//		therumsforceLore.add(ChatColor.GOLD + "Therum's Set");
//		therumsforceLore.add(ChatColor.AQUA + "Legendary Armor");
//		therumsforceLore.add(ChatColor.BLUE + "Has a 25% chance to give you resistance I and");
//		therumsforceLore.add(ChatColor.BLUE + "slowness I for 3 seconds when hit");
//		therumsforceLore.add(ChatColor.BLUE + "at the cost of 3 durability");
//		therumsforceItemMeta.setLore(therumsforceLore);
//		therumsforceItemMeta.setDisplayName(ChatColor.RESET + "Therum's Force");
//		therumsforce.setItemMeta(therumsforceItemMeta);
//
//		ItemStack therumsmight = new ItemStack(Material.POTION, 1, (byte)0);
//		ItemMeta therumsmightItemMeta = therumsmight.getItemMeta();
//		List<String> therumsmightLore = new ArrayList<String>();
//		therumsmightLore.add(ChatColor.GOLD + "Therum's Set");
//		therumsmightLore.add(ChatColor.AQUA + "Legendary Potion");
//		therumsmightLore.add(ChatColor.BLUE + "Gives 30 seconds of resistance, slowness, weakness III.");
//		therumsmightLore.add(ChatColor.BLUE + "Increase your max health to 14 hearts and heal 4 hearts");
//		therumsmightLore.add(ChatColor.BLUE + "for 30 seconds");
//		therumsmightItemMeta.setLore(therumsmightLore);
//		therumsmightItemMeta.setDisplayName(ChatColor.RESET + "Therum's Might");
//		therumsmight.setItemMeta(therumsmightItemMeta);
//
//
//		ItemStack agnisrage = new ItemStack(Material.IRON_SWORD);
//		ItemMeta agnisrageItemMeta = agnisrage.getItemMeta();
//		List<String> agnisrageLore = new ArrayList<String>();
//		agnisrageLore.add(ChatColor.RED + "Agni's Set");
//		agnisrageLore.add(ChatColor.AQUA + "Legendary Sword");
//		agnisrageLore.add(ChatColor.BLUE + "Has a 25% chance to set the player hit on fire");
//		agnisrageLore.add(ChatColor.BLUE + "Has a 15% chance to set you on fire");
//		agnisrageItemMeta.setLore(agnisrageLore);
//		agnisrageItemMeta.setDisplayName(ChatColor.RESET + "Agni's Rage");
//		agnisrage.setItemMeta(agnisrageItemMeta);
//
//		ItemStack agnisfury = new ItemStack(Material.BOW);
//		ItemMeta agnisfuryItemMeta = agnisfury.getItemMeta();
//		List<String> agnisfuryLore = new ArrayList<String>();
//		agnisfuryLore.add(ChatColor.RED + "Agni's Set");
//		agnisfuryLore.add(ChatColor.AQUA + "Legendary Bow");
//		agnisfuryLore.add(ChatColor.BLUE + "Summons a bolt of lightning and set the players damaged on fire");
//		agnisfuryLore.add(ChatColor.BLUE + "at the cost of 3 arrows and 5 durability.");
//		agnisfuryItemMeta.setLore(agnisfuryLore);
//		agnisfuryItemMeta.setDisplayName(ChatColor.RESET + "Agni's Fury");
//		agnisfury.setItemMeta(agnisfuryItemMeta);
//
//
//		ItemStack sealoftime = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
//		ItemMeta sealoftimeItemMeta = sealoftime.getItemMeta();
//		List<String> sealoftimeLore = new ArrayList<String>();
//		sealoftimeLore.add(ChatColor.DARK_PURPLE + "Lore here");
//		sealoftimeLore.add(ChatColor.BLUE + "Right Click: Sets a recall point. After 5 seconds,");
//		sealoftimeLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
//		sealoftimeItemMeta.setLore(sealoftimeLore);
//		sealoftimeItemMeta.setDisplayName(ChatColor.RESET + "Seal of Time");
//		sealoftime.setItemMeta(sealoftimeItemMeta);
//
//		ItemStack sealofspace = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
//		ItemMeta sealofspaceItemMeta = sealofspace.getItemMeta();
//		List<String> sealofspaceLore = new ArrayList<String>();
//		sealofspaceLore.add(ChatColor.DARK_PURPLE + "Lore here");
//		sealofspaceLore.add(ChatColor.BLUE + "Right Click: Teleport to the block you are looking at");
//		sealofspaceLore.add(ChatColor.BLUE + "you are teleported back to the recall point and restore health");
//		sealofspaceItemMeta.setLore(sealofspaceLore);
//		sealofspaceItemMeta.setDisplayName(ChatColor.RESET + "Seal of Space");
//		sealofspace.setItemMeta(sealofspaceItemMeta);
//
//		ItemStack sealofgravity = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
//		ItemMeta sealofgravityItemMeta = sealofgravity.getItemMeta();
//		List<String> sealofgravityLore = new ArrayList<String>();
//		sealofgravityLore.add(ChatColor.DARK_PURPLE + "Lore here");
//		sealofgravityLore.add(ChatColor.BLUE + "Right Click: Throw");
//		sealofgravityLore.add(ChatColor.BLUE + "Launch nearby players towards the seal");
//		sealofgravityItemMeta.setLore(sealofgravityLore);
//		sealofgravityItemMeta.setDisplayName(ChatColor.RESET + "Seal of Gravity");
//		sealofgravity.setItemMeta(sealofgravityItemMeta);
//
//		ItemStack sealofentropy = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.SKELETON.ordinal());
//		ItemMeta sealofentropyItemMeta = sealofentropy.getItemMeta();
//		List<String> sealofentropyLore = new ArrayList<String>();
//		sealofentropyLore.add(ChatColor.DARK_PURPLE + "Lore here");
//		sealofentropyLore.add(ChatColor.BLUE + ".");
//		sealofentropyLore.add(ChatColor.BLUE + ".");
//		sealofentropyItemMeta.setLore(sealofentropyLore);
//		sealofentropyItemMeta.setDisplayName(ChatColor.RESET + "Seal of Entropy");
//		sealofentropy.setItemMeta(sealofentropyItemMeta);
//
//
//
//		// Swords
//		itemPage1.setItem(0, corsairsedge);
//		itemPage1.setItem(1, endeavour);
//		itemPage1.setItem(2, flail);
//		itemPage1.setItem(3, gamble);
//		itemPage1.setItem(4, ipsesfolly);
//		itemPage1.setItem(5, kikuichimonji);
//		itemPage1.setItem(6, masamune);
//		itemPage1.setItem(7, muramasa);
//		itemPage1.setItem(8, nightsshadow);
//		itemPage1.setItem(10, peace);
//
//		// Bows
//		itemPage1.setItem(18, slowbow);
//		itemPage1.setItem(19, healbow);
//		itemPage1.setItem(21, rocksteady);
//
//		itemPage1.setItem(25, venombow);
//		itemPage1.setItem(26, voidbow);
//
//		itemPage1.setItem(30, gorgonsgaze);
//		itemPage1.setItem(31, helmetofvision);
//		itemPage1.setItem(32, ninjasandals);
//		itemPage1.setItem(33, rabbitfeet);
//		itemPage1.setItem(34, rubbershield);
//
//		itemPage2.setItem(0, simoonssong);
//		itemPage2.setItem(1, simoonsmelody);
//		itemPage2.setItem(2, simoonssonata);
//		itemPage2.setItem(3, simoonstune);
//		itemPage2.setItem(9, therumsstrength);
//		itemPage2.setItem(10, therumspower);
//		itemPage2.setItem(11, therumsforce);
//		itemPage2.setItem(12, therumsmight);
//
//		itemPage2.setItem(18, agnisrage);
//		itemPage2.setItem(19, agnisfury);
//
//		itemPage2.setItem(8, sealoftime);
//		itemPage2.setItem(17, sealofspace);
//		itemPage2.setItem(26, sealofgravity);
//		itemPage2.setItem(35, sealofentropy);
//	}
}
	