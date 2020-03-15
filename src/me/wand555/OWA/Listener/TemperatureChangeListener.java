package me.wand555.OWA.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;

public class TemperatureChangeListener implements Listener {
	
	private OWA plugin;
	
	public TemperatureChangeListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerNearHotBlocksEvent() {
		
	}

}
