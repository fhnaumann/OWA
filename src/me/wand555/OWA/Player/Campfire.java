package me.wand555.OWA.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Location;

public class Campfire {

	private static HashMap<Location, Campfire> allCampfires = new HashMap<Location, Campfire>();
	
	private Location campfireLoc;
	private HashMap<UUID, String> name = new HashMap<UUID, String>();
	private HashMap<UUID, Location> loc = new HashMap<UUID, Location>();
	
	public Campfire(Location campfireLoc, PlayerProfile profile, String pIndividualName, Location pIndividualLoc) {
		this.campfireLoc = campfireLoc;
		this.name.put(profile.getPlayerUUID(), pIndividualName);
		this.loc.put(profile.getPlayerUUID(), pIndividualLoc);
		allCampfires.put(campfireLoc, this);
		profile.addNewCampfire(this);
		
	}
	
	/**
	 * Used for loading from config. Due to way storing campfires is built, everything else will set after creating the instance.
	 * Order is: Call this constructor -> call addNewIndividualToAllCampfires method.
	 * 
	 * @param campfireLoc
	 */
	public Campfire(Location campfireLoc) {
		this.campfireLoc = campfireLoc;
		allCampfires.put(campfireLoc, this);
	}
	
	public static boolean hasAnyCampNameDuplicate(PlayerProfile profile, String name) {
		return allCampfires.entrySet().stream()
				.anyMatch(entry -> entry.getValue().getName().get(profile.getPlayerUUID()).equalsIgnoreCase(name));
	}
	
	public static Campfire getCampfireByLocation(Location l) {
		return allCampfires.get(l);
	}
	
	/**
	 * Adds player specific campfire to existing campfire.
	 * Also creates an entry in the given player profile.
	 * 
	 * @param profile
	 * @param name
	 * @param locToAdd
	 */
	public void addNewIndividualToAllCampfires(PlayerProfile profile, String name, Location locToAdd) {
		this.getName().put(profile.getPlayerUUID(), name);
		this.getLoc().put(profile.getPlayerUUID(), locToAdd);
		profile.addNewCampfire(this);
	}

	/**
	 * @return the name
	 */
	public HashMap<UUID, String> getName() {
		return name;
	}

	/**
	 * @return the loc
	 */
	public HashMap<UUID, Location> getLoc() {
		return loc;
	}

	/**
	 * @return the campfires
	 */
	public static HashMap<Location, Campfire> getAllCampfires() {
		return allCampfires;
	}

	/**
	 * @return the campfireLoc
	 */
	public Location getCampfireLoc() {
		return campfireLoc;
	}

	/**
	 * @param campfireLoc the campfireLoc to set
	 */
	public void setCampfireLoc(Location campfireLoc) {
		this.campfireLoc = campfireLoc;
	}

}
