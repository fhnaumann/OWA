package me.wand555.OWA.Listener;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.LootChest;

public class PlayerPlaceChestNextToLootChestListener implements Listener {

	private OWA plugin;
	
	public PlayerPlaceChestNextToLootChestListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerPlaceChestNextToLootChestEvent(BlockPlaceEvent event) {
		if(event.getBlockPlaced().getState() instanceof Chest) {
			//if(!event.getPlayer().isSneaking()) {
				Block b = event.getBlockPlaced();
				if(LootChest.getLootChestsFromLocation(b.getRelative(BlockFace.NORTH).getLocation()) != null) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot build a chest next to a loot chest!");
					return;
				}
				if(LootChest.getLootChestsFromLocation(b.getRelative(BlockFace.EAST).getLocation()) != null) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot build a chest next to a loot chest!");
					return;
				}
				if(LootChest.getLootChestsFromLocation(b.getRelative(BlockFace.SOUTH).getLocation()) != null) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot build a chest next to a loot chest!");
					return;
				}
				if(LootChest.getLootChestsFromLocation(b.getRelative(BlockFace.WEST).getLocation()) != null) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("Cannot build a chest next to a loot chest!");
					return;
				}
			//}		
		}
	}
}
