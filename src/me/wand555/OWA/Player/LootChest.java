package me.wand555.OWA.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.block.data.Directional;

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
	private Location chestLoc;
	private BlockFace facing;
	private String name;
	private boolean isLooted;
	private long timer;
	private BukkitTask runningTask;
	private ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
	//timer is in hours, have to match to ticks now
	public LootChest(Location chestLoc, ItemStack[] contents, String name, int timer) {
		this.chestLoc = chestLoc;
		this.facing = ((Directional) chestLoc.getBlock().getBlockData()).getFacing();
		this.name = name;
		this.isLooted = false;
		//this.timer = timer*60*60*20;
		this.timer = timer; //CHANGE LATER BACK TO NORMAL AGAIN
		Arrays.stream(contents).forEachOrdered(i -> {
			if(i == null) this.contents.add(i);
			else this.contents.add(i.clone());
			;
		});
		for(ItemStack i : this.contents) {
			if(i != null) System.out.println(i);
		}
		this.setRunningTask(null);
		Chest chest = (Chest) chestLoc.getBlock().getState();
		chest.setCustomName(name);
		
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
	public Location getChestLoc() {
		return chestLoc;
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
			if(c.getChestLoc().equals(loc)) {
				return c;
			}
		}
		return null;
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
	 * @return the contents
	 */
	public ArrayList<ItemStack> getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(ArrayList<ItemStack> contents) {
		this.contents = contents;
	}

	/**
	 * @return the facing
	 */
	public BlockFace getFacing() {
		return facing;
	}

	/**
	 * @param facing the facing to set
	 */
	public void setFacing(BlockFace facing) {
		this.facing = facing;
	}
}
