package me.wand555.OWA.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent.ChangeReason;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;

public class CauldronBottleRefillListener implements Listener {

	private OWA plugin;
	
	public CauldronBottleRefillListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onCauldronBottleRefillEvent(CauldronLevelChangeEvent event) {
		if(event.getReason() == ChangeReason.BOTTLE_FILL) {
			event.setNewLevel(event.getOldLevel());
		}
	}
}
