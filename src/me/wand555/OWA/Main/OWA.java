package me.wand555.OWA.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.ScoreboardManager;

import me.wand555.OWA.Commands.CE;
import me.wand555.OWA.Listener.AdminAreaListener;
import me.wand555.OWA.Listener.AdminChangeWorldWhileSettingAreaListener;
import me.wand555.OWA.Listener.AdminDropSettingItemOrDeathListener;
import me.wand555.OWA.Listener.AdminLootChestListener;
import me.wand555.OWA.Listener.CampfireListener;
import me.wand555.OWA.Listener.CauldronBottleRefillListener;
import me.wand555.OWA.Listener.DisableZombieBurningListener;
import me.wand555.OWA.Listener.FenceJumpListener;
import me.wand555.OWA.Listener.LeashRightClickListener;
import me.wand555.OWA.Listener.PlayerChangeStatsListener;
import me.wand555.OWA.Listener.PlayerFindLootChestListener;
import me.wand555.OWA.Listener.PlayerLoginListener;
import me.wand555.OWA.Listener.PlayerRespawnListener;
import me.wand555.OWA.Listener.ThirstLevelChangeListener;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.PlayerProfile;
import me.wand555.OWA.Timer.DaylightZombieSpawning;
import me.wand555.OWA.Timer.DealThirstDamage;
import me.wand555.OWA.Timer.DetectTemperature;
import me.wand555.OWA.Timer.ThirstTimer;

public class OWA extends JavaPlugin {

	public static final String PREFIX = "[OWA]";
	
	private CE myCE;
	private OWA plugin;
	public static boolean extremeMobSpawn;
	public static long thirstTickrate;
	public static int thirstLow;
	public static int thirstVeryLow;
	public static int thirstRefillRange;
	public static long temperatureDayBegin;
	public static long temperatureDayEnd;
	public static int temperatureRangeCheck;
	public static long temperatureTickrate;
	public static int temperatureLow;
	public static int temperatureVeryLow;
	public static int temperatureHigh;
	public static int temperatureVeryHigh;
	public static int campfireSetwarpRadius;
	public static int inWaterLightLevel;
	public static int thirstDamageAmount;
	public static int thirstDamageTickrate;
	public static int zombieSpawnInnerBound;
	public static int zombieSpawnOuterBound;
	public static int zombieSpawnAmount;
	public static int zombieSpawnTickrate;
	public static int experienceZombieAmount;
	public static int experiencePlayerAmount;
	
	public static final Random random = new Random();
	public static ItemStack hoeItem;
	public static ItemStack shovelItem;
	
	public void onEnable() {
		plugin = this;
		loadDefaultConfig();
		
		experienceZombieAmount = this.getConfig().getInt("Experience.Zombie.Amount");
		experiencePlayerAmount = this.getConfig().getInt("Experience.Player.Amount");
		campfireSetwarpRadius = this.getConfig().getInt("Campfire.setwarp_Radius");
		extremeMobSpawn = this.getConfig().getBoolean("ExtremeMobSpawn");
		thirstTickrate = this.getConfig().getLong("Thirst.Tickrate");
		thirstLow = this.getConfig().getInt("Thirst.Low.Value");
		thirstVeryLow = this.getConfig().getInt("Thirst.Very_Low.Value");
		thirstRefillRange = this.getConfig().getInt("Thirst.Refill.Range");
		temperatureDayBegin = this.getConfig().getLong("Temperature.Day.Begin");
		temperatureDayEnd = this.getConfig().getLong("Temperature.Day.End");
		temperatureRangeCheck = this.getConfig().getInt("Temperature.Range.Check");
		temperatureTickrate = this.getConfig().getLong("Temperature.Tickrate");
		temperatureLow = this.getConfig().getInt("Temperature.Low.Value");
		temperatureVeryLow = this.getConfig().getInt("Temperature.Very_Low.Value");
		temperatureHigh = this.getConfig().getInt("Temperature.High.Value");
		temperatureVeryHigh = this.getConfig().getInt("Temperature.Very_High.Value");
		inWaterLightLevel = this.getConfig().getInt("Temperature.In_Water_Light_Level");
		thirstDamageTickrate = this.getConfig().getInt("Thirst.Damage.Tickrate");
		thirstDamageAmount = this.getConfig().getInt("Thirst.Damage.Amount");
		zombieSpawnInnerBound = this.getConfig().getInt("Zombie.Spawn.InnerBound");
		zombieSpawnOuterBound = this.getConfig().getInt("Zombie.Spawn.OuterBound");
		zombieSpawnAmount = this.getConfig().getInt("Zombie.Spawn.Amount");
		zombieSpawnTickrate = this.getConfig().getInt("Zombie.Spawn.TickRate");
		
		ArrayList<PotionEffect> thirstLowEffect = this.deserializePotionEffects("Thirst.Low.Effects");
		ArrayList<PotionEffect> thirstVeryLowEffect = this.deserializePotionEffects("Thirst.Very_Low.Effects");
		
		ArrayList<PotionEffect> temperaturetLowEffect = this.deserializePotionEffects("Temperature.Low.Effects");
		ArrayList<PotionEffect> temperatureVeryLowEffect = this.deserializePotionEffects("Temperature.Very_Low.Effects");
		ArrayList<PotionEffect> temperatureHighEffect = this.deserializePotionEffects("Temperature.High.Effects");
		ArrayList<PotionEffect> temperatureVeryHighEffect = this.deserializePotionEffects("Temperature.Very_High.Effects");
		
		{
			hoeItem = new ItemStack(Material.DIAMOND_HOE);
			ItemMeta meta = hoeItem.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
			hoeItem.setItemMeta(meta);
		}
		{
			shovelItem = new ItemStack(Material.DIAMOND_SHOVEL);
			ItemMeta meta = shovelItem.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
			shovelItem.setItemMeta(meta);
		}
		
		new PlayerLoginListener(this);
		new DisableZombieBurningListener(this);
		new LeashRightClickListener(this);
		new CauldronBottleRefillListener(this);
		new ThirstLevelChangeListener(this, thirstLowEffect, thirstVeryLowEffect);
		new PlayerRespawnListener(this);
		new CampfireListener(this);
		new FenceJumpListener(this);
		new AdminAreaListener(this);
		new AdminChangeWorldWhileSettingAreaListener(this);
		new AdminDropSettingItemOrDeathListener(this);
		new AdminLootChestListener(this);
		new PlayerFindLootChestListener(this);
		new PlayerChangeStatsListener(this);
		
		//check if tickrate in config is the same for temperature and thirst, then put both in the same
		new ThirstTimer(this, thirstLowEffect, thirstVeryLowEffect).runTaskTimer(this, thirstTickrate, thirstTickrate);
		new DetectTemperature(this, temperatureVeryLowEffect, temperaturetLowEffect, temperatureHighEffect, temperatureVeryHighEffect).runTaskTimer(this, temperatureTickrate, temperatureTickrate);
		new DaylightZombieSpawning(this).runTaskTimer(this, zombieSpawnTickrate, zombieSpawnTickrate);
		
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			new PlayerProfile(p);
			if(p.hasPermission("owa.admin.setzone") || p.hasPermission("owa.admin.setchest")
					|| p.hasPermission("owa.admin.lootchest.manage")) {
				System.out.println("assigned admin profile");
				new AdminProfile(p.getUniqueId());
			}
		}
		
		myCE = new CE(plugin);
		this.getCommand("setwarp").setExecutor(myCE);
		this.getCommand("warp").setExecutor(myCE);
		this.getCommand("delwarp").setExecutor(myCE);
		this.getCommand("setzone").setExecutor(myCE);
		this.getCommand("removezone").setExecutor(myCE);
		this.getCommand("manageloot").setExecutor(myCE);
		
		
	}
	
	public void onDisable() {
		
	}
	
	private void loadDefaultConfig() {
		this.getConfig().options().copyDefaults(true);
		
		this.getConfig().addDefault("Experience.Zombie.Amount", 1);
		this.getConfig().addDefault("Experience.Player.Amount", 20);
		this.getConfig().addDefault("Campfire.setwarp_Radius", 5);
		this.getConfig().addDefault("Zombie.Spawn.InnerBound", 10);
		this.getConfig().addDefault("Zombie.Spawn.OuterBound", 100);
		this.getConfig().addDefault("Zombie.Spawn.Amount", 30);
		this.getConfig().addDefault("Zombie.Spawn.TickRate", 20*60);
		this.getConfig().addDefault("Thirst.Refill.Range", 3);
		this.getConfig().addDefault("Thirst.Damage.Tickrate", 20);
		this.getConfig().addDefault("Thirst.Damage.Amount", 2);
		this.getConfig().addDefault("Thirst.Tickrate", 20*60);
		this.getConfig().addDefault("Thirst.Low.Value", 7);
		this.getConfig().addDefault("Thirst.Low.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*20, 1, false, false, false), false, false, false),
				serializePotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*20, 1, false, false, false), false, false, false)));
		this.getConfig().addDefault("Thirst.Very_Low.Value", 3);
		this.getConfig().addDefault("Thirst.Very_Low.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*10, 2, false, false, false), false, false, false)));
		this.getConfig().addDefault("Temperature.Day.Begin", 167);
		this.getConfig().addDefault("Temperature.Day.End", 11834);
		this.getConfig().addDefault("Temperature.Range.Check", 2);
		this.getConfig().addDefault("Temperature.Tickrate", 20*60);
		this.getConfig().addDefault("Temperature.In_Water_Light_Level", 10);
		
		this.getConfig().addDefault("Temperature.Low.Value", 7);
		this.getConfig().addDefault("Temperature.Low.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*20, 1, false, false, false), false, false, false),
				serializePotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 1, false, false, false), false, false, false)));
		this.getConfig().addDefault("Temperature.Very_Low.Value", 3);
		this.getConfig().addDefault("Temperature.Very_Low.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*20, 2, false, false, false), false, false, false),
				serializePotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 2, false, false, false), false, false, false)));
		
		this.getConfig().addDefault("Temperature.High.Value", 14);
		this.getConfig().addDefault("Temperature.High.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 1, false, false, false), false, false, false),
				serializePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*20, 1, false, false, false), false, false, false)));
		this.getConfig().addDefault("Temperature.Very_High.Value", 17);
		this.getConfig().addDefault("Temperature.Very_High.Effects", Arrays.asList(
				serializePotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 2, false, false, false), false, false, false),
				serializePotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*20, 2, false, false, false), false, false, false)));
		
		this.saveConfig();
	}
	
	private String serializePotionEffect(PotionEffect effect, boolean ambient, boolean particles, boolean icon) {
		String s = "";
		s = effect.getType().getName() + "/" + effect.getDuration() + "/" + effect.getAmplifier() + "/"
				+ ambient + "/" + particles + "/" + icon;
		return s;
	}
	
	private ArrayList<PotionEffect> deserializePotionEffects(String s) {
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		this.getConfig().getList(s).forEach(effect -> 
		{
			String[] args = ((String) effect).split("/");
			effects.add(new PotionEffect(
					PotionEffectType.getByName(
					args[0]), 
					Integer.valueOf(args[1]), 
					Integer.valueOf(args[2])-1, 
					Boolean.valueOf(args[3]), 
					Boolean.valueOf(args[4]), 
					Boolean.valueOf(args[5])));
		});
	
		//Lösung darüber finden: this.getConfig().getList("Thirst.Low.Effects").stream().map(blablabla)
		return effects;
	}
}
