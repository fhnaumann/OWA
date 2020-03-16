package me.wand555.OWA.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.LootChest;
import me.wand555.OWA.Timer.ReturnLootChest;

public class PlayerFindLootChestListener implements Listener {

	private OWA plugin;
	
	public PlayerFindLootChestListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerFindLootChestEvent(InventoryOpenEvent event) {
		if(event.getView().getType() == InventoryType.CHEST) {
			LootChest lootChest = LootChest.getLootChestsFromLocation(event.getView().getTopInventory().getLocation());
			if(lootChest != null) {
				lootChest.setLooted(true);
				lootChest.setRunningTask(new ReturnLootChest(lootChest).runTaskLater(OWA.getPlugin(OWA.class), lootChest.getTimer()));
			}
		}
	}
}
