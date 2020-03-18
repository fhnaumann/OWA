package me.wand555.OWA.Timer;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.LootChest;

/**
 * An instance is created after a player has looted this chest.
 * More formally after closing his inventory and if its set to be
 * a returning loot chest.
 * 
 * @author Felix
 *
 */
public class ReturnLootChest extends BukkitRunnable {

	private LootChest lootChest;
	private CountSecondsPassedTimer countSecondsPassed;
	
	public ReturnLootChest(LootChest lootChest) {
		this.lootChest = lootChest;
		if(lootChest.isLooted()) {
			countSecondsPassed = new CountSecondsPassedTimer(0);
			countSecondsPassed.runTaskTimerAsynchronously(OWA.getPlugin(OWA.class), 0L, 20L);
		}
	}
	
	@Override
	public void run() {	
		countSecondsPassed.cancel();
		lootChest.setLooted(false);
		Location loc = lootChest.getChestLoc();
		loc.getBlock().setType(Material.CHEST);
		Chest c = (Chest) loc.getBlock().getState();
		c.setCustomName(lootChest.getName());
		((Directional) c.getBlockData()).setFacing(lootChest.getFacing());
		ArrayList<ItemStack> contents = lootChest.getContents();
		//er packt die Items nicht wirklich in chest
		for(int i=0; i<contents.size(); i++) {
			c.getInventory().setItem(i, contents.get(i));
		}
		for(ItemStack i : c.getInventory()) System.out.println(i);
		System.out.println("placed items in chest again");
	}

	/**
	 * @return the countSecondsPassed
	 */
	public CountSecondsPassedTimer getCountSecondsPassed() {
		return countSecondsPassed;
	}

}
