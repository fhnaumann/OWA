package me.wand555.OWA.Timer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Player.LootChest;

public class ReturnLootChest extends BukkitRunnable {

	private LootChest lootChest;
	
	public ReturnLootChest(LootChest lootChest) {
		this.lootChest = lootChest;
	}
	
	@Override
	public void run() {
		lootChest.setLooted(false);
		Location loc = lootChest.getChest().getLocation();
		loc.getBlock().setType(Material.CHEST);
		Chest c = (Chest) loc.getBlock();
		c.getInventory().setContents(lootChest.getChest().getInventory().getContents());
	}

}
