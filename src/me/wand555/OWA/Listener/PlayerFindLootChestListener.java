package me.wand555.OWA.Listener;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.LootChest;
import me.wand555.OWA.Timer.ReturnLootChest;

public class PlayerFindLootChestListener implements Listener {

	private OWA plugin;
	
	public PlayerFindLootChestListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerFindLootChestCloseEvent(InventoryCloseEvent event) {
		if(event.getView().getType() == InventoryType.CHEST) {
			LootChest lootChest = LootChest.getLootChestsFromLocation(event.getView().getTopInventory().getLocation());
			if(lootChest != null) {
				if(!lootChest.isLooted()) {
					lootChest.setLooted(true);
					System.out.println("here2");
					//not a one time loot
					if(lootChest.getTimer() != 0) {
						lootChest.setReturnLootChest(new ReturnLootChest(lootChest));
						lootChest.getReturnLootChest().runTaskLater(OWA.getPlugin(OWA.class), lootChest.getTimer());
					}
					else {
						//remove lootchest indexes
						for(Entry<UUID, AdminProfile> entry : AdminProfile.getAdminProfiles().entrySet()) {
							if(entry.getValue().getLootChests().contains(lootChest)) {
								entry.getValue().getLootChests().remove(lootChest);
							}
						}
						LootChest.getLootChests().remove(lootChest);
					}
					
				}		
			}
		}
	}
	
	@EventHandler
	public void onPlayerDestroyLootChestEvent(BlockBreakEvent event) {
		if(event.getBlock().getState() instanceof Chest) {
			LootChest lootChest = LootChest.getLootChestsFromLocation(event.getBlock().getLocation());
			if(lootChest != null) {
				if(!lootChest.isLooted()) {
					lootChest.setLooted(true);
					if(lootChest.getTimer() != 0) {
						lootChest.setReturnLootChest(new ReturnLootChest(lootChest));
						lootChest.getReturnLootChest().runTaskLater(OWA.getPlugin(OWA.class), lootChest.getTimer());
					}
					else {
						//remove lootchest indexes
						for(Entry<UUID, AdminProfile> entry : AdminProfile.getAdminProfiles().entrySet()) {
							if(entry.getValue().getLootChests().contains(lootChest)) {
								entry.getValue().getLootChests().remove(lootChest);
							}
						}
						LootChest.getLootChests().remove(lootChest);
					}
				}
			}
		}
	}
}
