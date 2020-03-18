package me.wand555.OWA.Listener;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Config.HandleRestart;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.LootChest;

public class AdminLootChestListener implements Listener {

	private OWA plugin;
	
	public AdminLootChestListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onAdminMangeLootChestEvent(PlayerInteractEvent event) {
		if(event.hasBlock()) {
			if(event.getPlayer().hasPermission("owa.admin.lootchest.manage")) {
				if(event.hasItem()) {
					if(event.getItem().isSimilar(OWA.shovelItem)) {
						event.setCancelled(true);
						if(event.getClickedBlock().getType() == Material.CHEST) {
							Player p = event.getPlayer();
							AdminProfile profile = AdminProfile.getAdminProfileFromUUID(p.getUniqueId());
							LootChest lootChest = LootChest.getLootChestsFromLocation(event.getClickedBlock().getLocation());
							
							
							//register chest
							if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
								if(lootChest == null) {
									//to make sure he did /manageloot <name> <timer>
									if(profile.getLootChestName() != null) {
										Chest chest = (Chest) event.getClickedBlock().getState();
										profile.getLootChests().add(new LootChest(chest.getLocation(), chest.getInventory().getContents(),
												profile.getLootChestName(), profile.getLootChestReturnTickrate()));
										p.sendMessage("Sucessfully created a loot chest");
									}
									
								}
								else {
									p.sendMessage("The selected chest is already a lootchest");
								}
							}
							else if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
								if(lootChest != null) {
									if(lootChest.getReturnLootChest() != null) {
										lootChest.getReturnLootChest().getCountSecondsPassed().cancel();
										lootChest.getReturnLootChest().cancel();
									}
									LootChest.getLootChests().remove(lootChest);
									profile.getLootChests().remove(lootChest);
									
									
									//profile.setLootChestName(null);
									//profile.setLootChestReturnTickrate(0);
									event.getClickedBlock().setType(Material.AIR);
									p.sendMessage("Sucessfully deleted this loot chest");
									
								}
								else {
									p.sendMessage("The selected chest is not a lootchest");
								}
							}
						}			
					}
				}	
			}
		}	
	}
}
