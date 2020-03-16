package me.wand555.OWA.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.AdminAreaType;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminArea;
import me.wand555.OWA.Player.AdminProfile;

public class AdminAreaListener implements Listener {

	private OWA plugin;
	
	public AdminAreaListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onAdminAreaClickEvent(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(event.getItem().isSimilar(OWA.hoeItem)) {
				event.setCancelled(true);
				Player p = event.getPlayer();
				AdminProfile profile = AdminProfile.getAdminProfileFromUUID(p.getUniqueId());
				//first loc
				if(profile.getFirstLocArea() == null && profile.getSecondLocArea() == null) {
					profile.setFirstLocArea(event.getClickedBlock().getLocation());
					p.sendMessage("Sucessfully added first point!");
				}
				//second loc
				else if(profile.getFirstLocArea() != null && profile.getSecondLocArea() == null) {
					profile.setSecondLocArea(event.getClickedBlock().getLocation());
					profile.setAreaSetting(false);	
					p.sendMessage("Sucessfully added second point!");
					p.sendMessage("Storing area in memory...");
					if(profile.getType() == AdminAreaType.ZOMBIE_CAMP) {
						new AdminArea(profile.getName(), p, profile.getFirstLocArea(), profile.getSecondLocArea(), profile.getType(), profile.getSpawnAmount(), profile.getTickrate());
					}
					else {
						new AdminArea(profile.getName(), p, profile.getFirstLocArea(), profile.getSecondLocArea(), profile.getType());
					}
					profile.setFirstLocArea(null);
					profile.setName(null);
					profile.setSecondLocArea(null);
					profile.setSpawnAmount(0);
					profile.setTickrate(0);
					profile.setType(null);
					
					
				}
				else {
					p.sendMessage("Already set both locs. This message should have never been sent...");
				}
				
			}		
		}
	}
}
