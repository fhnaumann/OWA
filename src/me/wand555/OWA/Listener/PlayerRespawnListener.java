package me.wand555.OWA.Listener;

import java.util.Random;

import org.bukkit.boss.BarColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.PlayerProfile;

public class PlayerRespawnListener implements Listener {

	private OWA plugin;
	
	public PlayerRespawnListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		PlayerProfile profile = PlayerProfile.getProfileFromPlayer(event.getPlayer().getUniqueId());
		profile.setThirst(profile.getMaxThirst());
		profile.getThirstBar().setProgress(profile.getMaxThirst()/20d);
		profile.setTemperature(profile.getMaxTemperature()/2);
		profile.getTemperatureBar().setColor(BarColor.WHITE);
		profile.getTemperatureBar().setProgress(0.5d);
		profile.updateThirstScoreboard();
		profile.updateTempScoreboard();
		if(!profile.getCampfires().isEmpty()) {
			
			event.setRespawnLocation(profile.getCampfires().get(OWA.random.nextInt(profile.getCampfires().size())).getLoc().get(profile.getPlayerUUID()));
		}
	}
}
