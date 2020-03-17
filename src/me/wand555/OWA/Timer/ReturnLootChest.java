package me.wand555.OWA.Timer;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.LootChest;

public class ReturnLootChest extends BukkitRunnable {

	private LootChest lootChest;
	
	public ReturnLootChest(LootChest lootChest) {
		this.lootChest = lootChest;
	}
	
	@Override
	public void run() {
		lootChest.setLooted(false);
		Location loc = lootChest.getChestLoc();
		loc.getBlock().setType(Material.CHEST);
		Chest c = (Chest) loc.getBlock().getState();
		c.setCustomName(lootChest.getName());
		((Directional) c.getBlockData()).setFacing(lootChest.getFacing());
		ArrayList<ItemStack> contents = lootChest.getContents();
		//er packt die Items nicht wirklich in chest
		for(int i=0; i<contents.size(); i++) {
			if(contents.get(i) != null) System.out.println(contents.get(i));
			c.getInventory().setItem(i, contents.get(i));
		}
		for(ItemStack i : c.getInventory()) System.out.println(i);
		System.out.println("placed items in chest again");
	}

}
