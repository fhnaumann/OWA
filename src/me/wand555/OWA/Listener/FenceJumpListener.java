package me.wand555.OWA.Listener;

import org.bukkit.Location;
import org.bukkit.block.data.type.Fence;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.wand555.OWA.Main.OWA;

public class FenceJumpListener implements Listener {

	private OWA plugin;
	
	public FenceJumpListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onNearFenceAndClickEvent(PlayerInteractEvent event) {
		if(event.getClickedBlock() != null) {
			if(event.getClickedBlock().getBlockData() instanceof Fence) {
				if(event.getPlayer().isOnGround()) {
					Location clicked = event.getClickedBlock().getLocation();
					Location loc = event.getPlayer().getLocation();
					if(loc.getBlockX() == clicked.getBlockX() 
							&& loc.getBlockY() == clicked.getBlockY() 
							&& loc.getBlockZ() == clicked.getBlockZ()) {
						event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(0, 0.3d, 0)));
					}
				}	
			}
		}	
	}
}
