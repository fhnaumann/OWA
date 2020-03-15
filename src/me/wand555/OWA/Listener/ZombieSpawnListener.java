package me.wand555.OWA.Listener;

import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;

public class ZombieSpawnListener implements Listener {

	private OWA plugin;
	
	public ZombieSpawnListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onZombieSpawnEvent(CreatureSpawnEvent event) {
		if(OWA.extremeMobSpawn) {
			if(event.getEntity() instanceof Zombie) {
				//maybe spawn in even more later;
			}
		}	
	}
}
