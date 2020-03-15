package me.wand555.OWA.Listener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.Campfire;
import me.wand555.OWA.Player.PlayerProfile;

public class CampfireListener implements Listener {
	
	private OWA plugin;
	
	public CampfireListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onCampfirePlaceEvent(BlockPlaceEvent event) {
		Block b = event.getBlockPlaced();
		if(b.getType() == Material.CAMPFIRE) {
			
		}
	}
	
	@EventHandler
	public void onCampfireBreakEvent(BlockBreakEvent event) {
		Block b = event.getBlock();
		if(b.getType() == Material.CAMPFIRE) {
			if(Campfire.getAllCampfires().containsKey(b.getLocation())) {
				Campfire campfire = Campfire.getCampfireByLocation(b.getLocation());
				Predicate<PlayerProfile> predicate = camp -> camp.getCampfires().stream().anyMatch(c -> c.getCampfireLoc().equals(b.getLocation()));
					
				
				PlayerProfile.getProfiles().entrySet().stream()
					.map(Map.Entry::getValue)
					.filter(predicate)
					.forEach(profile -> {
						OfflinePlayer ofp = Bukkit.getOfflinePlayer(profile.getPlayerUUID());
						if(ofp.isOnline()) {
							profile.getCampfires().remove(campfire);
							campfire.getName().remove(profile.getPlayerUUID());
							campfire.getLoc().remove(profile.getPlayerUUID());
							Player p = (Player) ofp;
							if(!p.getUniqueId().equals(event.getPlayer().getUniqueId())) {
								p.sendMessage("Someone has destroyed a campfire of yours!");
							}
							else {
								p.sendMessage("Sucessfully destroyed your camp and warp!");
							}
							
						}
					});
			}		
		}
	}

}
