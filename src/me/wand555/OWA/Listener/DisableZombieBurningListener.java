package me.wand555.OWA.Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;

public class DisableZombieBurningListener implements Listener {

	private OWA plugin;
	
	public DisableZombieBurningListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onZombieBurningEvent(EntityCombustEvent event) {
		if(event.getEntity() instanceof Zombie) {
			Zombie zombie = (Zombie) event.getEntity();
			Material matBottom = zombie.getLocation().getBlock().getType();
			Material matTop = zombie.getLocation().getBlock().getRelative(BlockFace.UP).getType();
			if((matBottom != Material.FIRE && matBottom != Material.LAVA) || matTop != Material.LAVA) {
				event.setCancelled(true);
			}		
		}
	}
}
