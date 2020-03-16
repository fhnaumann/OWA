package me.wand555.OWA.Player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitTask;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Timer.ReturnLootChest;
/**
 * Represents a loot chest set by an admin via /setloot
 * The runningTask is null until a player has looted the chest
 * (as long as its a repeating event)
 * 
 * @author Felix
 *
 */
public class LootChest {

	private static ArrayList<LootChest> lootChests = new ArrayList<LootChest>();
	
	private UUID creator;
	private Chest chest;
	private boolean isLooted;
	private long timer;
	private BukkitTask runningTask;
	//timer is in hours, have to match to ticks now
	public LootChest(Chest chest, String name, int timer) {
		this.chest = chest;
		this.chest.setCustomName(name);
		this.isLooted = false;
		this.timer = timer*60*60*20;
		this.setRunningTask(null);
		lootChests.add(this);
	}

	/**
	 * @return the creator
	 */
	public UUID getCreator() {
		return creator;
	}

	/**
	 * @return the chest
	 */
	public Chest getChest() {
		return chest;
	}

	/**
	 * @return the isLooted
	 */
	public boolean isLooted() {
		return isLooted;
	}

	/**
	 * @param isLooted the isLooted to set
	 */
	public void setLooted(boolean isLooted) {
		this.isLooted = isLooted;
	}

	/**
	 * @return the timer
	 */
	public long getTimer() {
		return timer;
	}

	/**
	 * @return the runningTask
	 */
	public BukkitTask getRunningTask() {
		return runningTask;
	}

	/**
	 * @param runningTask the runningTask to set
	 */
	public void setRunningTask(BukkitTask runningTask) {
		this.runningTask = runningTask;
	}

	/**
	 * @return the lootChests
	 */
	public static ArrayList<LootChest> getLootChests() {
		return lootChests;
	}
	
	public static LootChest getLootChestsFromLocation(Location loc) {
		for(LootChest c : getLootChests()) {
			if(c.getChest().getLocation().equals(loc)) {
				return c;
			}
		}
		return null;
	}
}
