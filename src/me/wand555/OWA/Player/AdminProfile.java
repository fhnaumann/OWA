package me.wand555.OWA.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Location;

import me.wand555.OWA.Main.AdminAreaType;

public class AdminProfile {

	private static HashMap<UUID, AdminProfile> adminProfiles = new HashMap<UUID, AdminProfile>();
	
	private UUID adminUUID;
	private boolean isAreaSetting;
	private boolean isLootChestSetting;
	private Location firstLocArea;
	private Location secondLocArea;
	//only valid if currently setting a type (e.g. firstLocArea != null)
	private AdminAreaType type = null;
	private String name = null;
	private int spawnAmount = 0;
	private long tickrate = 0;
	private ArrayList<LootChest> lootChests;
	
	public AdminProfile(UUID uuid) {
		this.adminUUID = uuid;
		this.isAreaSetting = false;
		this.isLootChestSetting = false;
		this.firstLocArea = null;
		this.secondLocArea = null;
		adminProfiles.put(uuid, this);
	}
	
	public static AdminProfile getAdminProfileFromUUID(UUID uuid) {
		return getAdminProfiles().get(uuid);
	}

	/**
	 * @return the adminUUID
	 */
	public UUID getAdminUUID() {
		return adminUUID;
	}

	/**
	 * @return the isAreaSetting
	 */
	public boolean isAreaSetting() {
		return isAreaSetting;
	}

	/**
	 * @param isAreaSetting the isAreaSetting to set
	 */
	public void setAreaSetting(boolean isAreaSetting) {
		this.isAreaSetting = isAreaSetting;
	}

	/**
	 * @return the isLootChestSetting
	 */
	public boolean isLootChestSetting() {
		return isLootChestSetting;
	}

	/**
	 * @param isLootChestSetting the isLootChestSetting to set
	 */
	public void setLootChestSetting(boolean isLootChestSetting) {
		this.isLootChestSetting = isLootChestSetting;
	}

	/**
	 * @return the firstLocArea
	 */
	public Location getFirstLocArea() {
		return firstLocArea;
	}

	/**
	 * @param firstLocArea the firstLocArea to set
	 */
	public void setFirstLocArea(Location firstLocArea) {
		this.firstLocArea = firstLocArea;
	}

	/**
	 * @return the secondLocArea
	 */
	public Location getSecondLocArea() {
		return secondLocArea;
	}

	/**
	 * @param secondLocArea the secondLocArea to set
	 */
	public void setSecondLocArea(Location secondLocArea) {
		this.secondLocArea = secondLocArea;
	}

	/**
	 * @return the adminProfiles
	 */
	public static HashMap<UUID, AdminProfile> getAdminProfiles() {
		return adminProfiles;
	}

	/**
	 * @return the type
	 */
	public AdminAreaType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AdminAreaType type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the spawnAmount
	 */
	public int getSpawnAmount() {
		return spawnAmount;
	}

	/**
	 * @param spawnAmount the spawnAmount to set
	 */
	public void setSpawnAmount(int spawnAmount) {
		this.spawnAmount = spawnAmount;
	}

	/**
	 * @return the tickrate
	 */
	public long getTickrate() {
		return tickrate;
	}

	/**
	 * @param tickrate the tickrate to set
	 */
	public void setTickrate(long tickrate) {
		this.tickrate = tickrate;
	}
}
