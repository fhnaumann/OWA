package me.wand555.OWA.Config;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.wand555.OWA.Main.AdminAreaType;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminArea;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.Campfire;
import me.wand555.OWA.Player.LootChest;
import me.wand555.OWA.Player.PlayerProfile;
import me.wand555.OWA.Timer.ReturnLootChest;

public class HandleRestart {

	public static OWA plugin = OWA.getPlugin(OWA.class);
	
	public static void saveToCustomConfig() {
		savePlayerProfileToCustomConfig();
		saveLootChestsToCustomConfig();
		saveCampfireToCustomConfig();
		saveAdminAreaToCustomConfig();
	}
	
	public static void loadFromConfig() {
		loadPlayerProfileFromConfig();
		loadLootChestsFromConfig();
		loadCampfireFromConfig();
		loadAdminAreaFromConfig();
	}
	
	private static String serializeLocation(Location loc) {
		return loc.getWorld().getName()+"/"+loc.getBlockX()+"/"+loc.getBlockY()+"/"+loc.getBlockZ();
	}
	
	private static Location deserializeLocation(String path) {
		String[] args = path.split("/");
		return new Location(Bukkit.getWorld(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}
	
	private static List<String> serializeLocationList(ArrayList<Location> list) {
		return list.stream().map(l -> serializeLocation(l)).collect(Collectors.toList());
	}
	
	private static ArrayList<Location> deserializeLocationList(List<String> list) {
		return list.stream().map(s -> deserializeLocation(s)).collect(Collectors.toCollection(ArrayList::new));
	}
	
	/*
	public static void removeGivenLootChest(LootChest lootChest) {
		checkOrdnerlootChests();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"LootChests", "lootChests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String s = serializeLocation(lootChest.getChestLoc());
		cfg.set(s, null);
		saveCustomYml(cfg, file);
	}
	
	public static void removeGivenAdminArea(AdminArea area) {
		checkOrdnerCampfire();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"AdminAreas", "adminAreas.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set(area.getName(), null);
		saveCustomYml(cfg, file);
	}
	
	public static void removeGivenCampLoc(Campfire camp) {
		checkOrdnerCampfire();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Campfires", "campfires.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
	}
	*/
	
	private static void saveAdminAreaToCustomConfig() {
		checkOrdnerCampfire();
		File del = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"AdminAreas", "adminAreas.yml");
		del.delete();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"AdminAreas", "adminAreas.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(AdminArea adminArea : AdminArea.getAdminAreas()) {
			String key = adminArea.getName();
			cfg.set(key+".Creator", adminArea.getCreator().toString());
			cfg.set(key+".Type", adminArea.getType().toString());
			if(adminArea.getType() == AdminAreaType.ZOMBIE_CAMP) {
				cfg.set(key+".spawnAmount", adminArea.getSpawnAmount());
				cfg.set(key+".Tickrate", adminArea.getTickrate());
			}
			cfg.set(key+".Locations", serializeLocationList(adminArea.getArea()));
		}
		saveCustomYml(cfg, file);
	}
	
	private static void loadAdminAreaFromConfig() {
		checkOrdnerCampfire();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"AdminAreas", "adminAreas.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(String name : cfg.getKeys(false)) {
			UUID uuid = UUID.fromString(cfg.getString(name+".Creator").trim());
			@SuppressWarnings("unchecked")
			ArrayList<Location> area = new ArrayList<Location>(deserializeLocationList((List<String>) cfg.getList(name+".Locations")));
			AdminAreaType type = AdminAreaType.valueOf(cfg.getString(name+".Type"));
			if(type == AdminAreaType.ZOMBIE_CAMP) {
				new AdminArea(name, 
						uuid, 
						area, 
						type, 
						cfg.getInt(name+".spawnAmount"), 
						cfg.getLong(name+".Tickrate"));
			}
			else {
				new AdminArea(name, uuid, area, type);
			}
		}
	}
	
	private static void saveCampfireToCustomConfig() {
		checkOrdnerCampfire();
		File del = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Campfires", "campfires.yml");
		del.delete();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Campfires", "campfires.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(Entry<Location, Campfire> entry : Campfire.getAllCampfires().entrySet()) {
			String key = serializeLocation(entry.getKey());
			
			for(Entry<UUID, String> nameEntry : entry.getValue().getName().entrySet()) {
				
				cfg.set(key+".Players."+nameEntry.getKey().toString()+".Name", nameEntry.getValue());
				cfg.set(key+".Players."+nameEntry.getKey().toString()+".warpLocation", serializeLocation(entry.getValue().getLoc().get(nameEntry.getKey())));
				
			}
		}
		saveCustomYml(cfg, file);
	}
	
	/**
	 * Call this after creating the playerProfile, because it will try to set each players existing camp.
	 */
	private static void loadCampfireFromConfig() {
		checkOrdnerCampfire();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Campfires", "campfires.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		for(String sLoc : cfg.getKeys(false)) {
			Location loc = deserializeLocation(sLoc);
			Campfire campfire = new Campfire(loc);
			for(String sUUID : cfg.getConfigurationSection(sLoc+".Players.").getKeys(false)) {
				UUID uuid = UUID.fromString(sUUID.trim());
				String name = cfg.getString(sLoc+".Players."+sUUID+".Name");
				Location playerWarpLoc = deserializeLocation(cfg.getString(sLoc+".Players."+sUUID+".warpLocation"));
				PlayerProfile profile = PlayerProfile.getProfileFromPlayer(uuid);
				campfire.addNewIndividualToAllCampfires(profile, name, playerWarpLoc);
			}
			
		}
	}
	
	private static void saveLootChestsToCustomConfig() {
		checkOrdnerlootChests();
		File del = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"LootChests", "lootChests.yml");
		del.delete();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"LootChests", "lootChests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(LootChest lootChest : LootChest.getLootChests()) {
			String key = serializeLocation(lootChest.getChestLoc());
			cfg.set(key+".Facing", lootChest.getFacing().toString());
			cfg.set(key+".Name", lootChest.getName());
			cfg.set(key+".isLooted", lootChest.isLooted());		
			cfg.set(key+".Timer", lootChest.getTimer());
			if(lootChest.getReturnLootChest() != null) {
				if(lootChest.getReturnLootChest().getCountSecondsPassed() != null) {
					//beim laden: getTimer - getCountSecondsPassed
					cfg.set(key+".LootChest", lootChest.getReturnLootChest().getCountSecondsPassed().getTimePassed());
				}
				else {
					cfg.set(key+".LootChest", null);
				}
			}
			else {
				cfg.set(key+".LootChest", null);
			}
			cfg.set(key+".Content", lootChest.getContents());
		}
		saveCustomYml(cfg, file);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadLootChestsFromConfig() {
		checkOrdnerlootChests();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"LootChests", "lootChests.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(String s : cfg.getKeys(false)) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(ItemStack item : (ArrayList<ItemStack>) cfg.getList(s+".Content")) {
				items.add(item);
			}
			
			System.out.println(cfg.getString(s+".Name"));
			
			LootChest lootChest = new LootChest(deserializeLocation(s), 
					items.toArray(new ItemStack[items.size()]),		
					cfg.getString(s+".Name"), 
					cfg.getInt(s+".Timer"));
			lootChest.setFacing(BlockFace.valueOf(cfg.getString(s+".Facing")));
			lootChest.setLooted(cfg.getBoolean(s+".isLooted"));
			lootChest.setReturnLootChest(new ReturnLootChest(lootChest));
			if(lootChest.isLooted()) {
				//set timer to stored value
				long time = lootChest.getTimer() - cfg.getLong(s+".LootChest")*20 < 0 ? 
						0 : lootChest.getTimer() - cfg.getLong(s+".LootChest")*20;
				lootChest.getReturnLootChest().runTaskLater(OWA.getPlugin(OWA.class), time);
				System.out.println("TIME: " + time);
				lootChest.getReturnLootChest().getCountSecondsPassed().setTimePassed(cfg.getLong(s+".LootChest"));
			}
		}
		//file.delete();
	}
	
	private static void savePlayerProfileToCustomConfig() {
		checkOrdnerPlayerProfile();
		File del = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Profiles"+"//"+"PlayerProfiles", "playerProfile.yml");
		del.delete();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Profiles"+"//"+"PlayerProfiles", "playerProfile.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(Entry<UUID, PlayerProfile> entry : PlayerProfile.getProfiles().entrySet()) {
			String stringUUID = entry.getKey().toString();
			PlayerProfile profile = entry.getValue();
			if(profile.getThirstBar() != null) {
				profile.getThirstBar().removeAll();
				profile.setThirstBar(null);
			}
			if(profile.getTemperatureBar() != null) {
				profile.getTemperatureBar().removeAll();
				profile.setTemperatureBar(null);
			}	
			
			cfg.set(stringUUID+".Thirst", profile.getThirst());
			cfg.set(stringUUID+".Temperature", profile.getTemperature());
			cfg.set(stringUUID+".ZombieKills", profile.getZombieKills());
			cfg.set(stringUUID+".PlayerKills", profile.getPlayerKills());
			cfg.set(stringUUID+".PlayerDeaths", profile.getDeaths());
			cfg.set(stringUUID+".Experience", profile.getExperience());
			//PERKS LATER
			
			//Campfires über Campfire class speichern und später dem Spieler zuordnen
			/*
			for(Campfire campfire : profile.getCampfires()) {
				cfg.set(stringUUID+".campfires." + serzializeLocation(campfire.getCampfireLoc()), );
			}
			*/
			saveCustomYml(cfg, file);
		}
	}
	
	private static void loadPlayerProfileFromConfig() {
		checkOrdnerPlayerProfile();
		File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Profiles"+"//"+"PlayerProfiles", "playerProfile.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		for(String s : cfg.getKeys(false)) {
			new PlayerProfile(UUID.fromString(s.trim()),
					cfg.getInt(s+".Thirst"), 
					cfg.getInt(s+".Temperature"), 
					cfg.getInt(s+".ZombieKills"), 
					cfg.getInt(s+".PlayerKills"), 
					cfg.getInt(s+".PlayerDeaths"), 
					cfg.getLong(s+".Experience"));
		}
	}
	
	   private static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
		   try {
			   ymlConfig.save(ymlFile);
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
	   
	   private static void checkOrdnerPlayerProfile() {
		   File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Profiles"+"//"+"PlayerProfiles");
		   if(!file.exists()) {
			   file.mkdir();
		   }
	   }
	   
	   private static void checkOrdnerlootChests() {
		   File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"LootChests");
		   if(!file.exists()) {
			   file.mkdir();
		   }
	   }
	   
	   private static void checkOrdnerCampfire() {
		   File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"Campfires");
		   if(!file.exists()) {
			   file.mkdir();
		   }
	   }
	   
	   private static void checkOrdnerAdminArea() {
		   File file = new File(OWA.getPlugin(OWA.class).getDataFolder()+"//"+"AdminAreas");
		   if(!file.exists()) {
			   file.mkdir();
		   }
	   }
}
