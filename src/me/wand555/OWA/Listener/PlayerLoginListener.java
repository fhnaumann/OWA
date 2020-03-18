package me.wand555.OWA.Listener;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.PlayerProfile;

public class PlayerLoginListener implements Listener {

	private OWA plugin;
	
	public PlayerLoginListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		PlayerProfile profile = PlayerProfile.getProfileFromPlayer(event.getPlayer().getUniqueId());
		if(profile == null) {
			new PlayerProfile(event.getPlayer());
		}
		else {
			profile.setThirstBar(Bukkit.createBossBar("Thirst", BarColor.BLUE, BarStyle.SEGMENTED_20));
			profile.getThirstBar().setProgress(profile.getThirst()/20d);
			profile.getThirstBar().addPlayer(event.getPlayer());
			profile.setTemperatureBar(Bukkit.createBossBar("Temperature", profile.getFittingColor(), BarStyle.SEGMENTED_20));
			profile.getTemperatureBar().setProgress(profile.getTemperature()/20d);
			profile.getTemperatureBar().addPlayer(event.getPlayer());
			event.getPlayer().setScoreboard(profile.getBoard());
		}
		
		if(event.getPlayer().isOp() 
				|| event.getPlayer().hasPermission("owa.admin.zone.set") 
				|| event.getPlayer().hasPermission("owa.admin.zone.remove")
				|| event.getPlayer().hasPermission("owa.admin.lootchest.manage")) {
			new AdminProfile(event.getPlayer().getUniqueId());
			System.out.println("created new admin profile");
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		PlayerProfile profile = PlayerProfile.getProfileFromPlayer(event.getPlayer().getUniqueId());
		profile.getThirstBar().removeAll();
		profile.setThirstBar(null);
		profile.getTemperatureBar().removeAll();
		profile.setTemperatureBar(null);
		
		AdminProfile adminProfile = AdminProfile.getAdminProfileFromUUID(event.getPlayer().getUniqueId());
		if(adminProfile != null) {
			adminProfile.setAreaSetting(false);
			adminProfile.setFirstLocArea(null);
			adminProfile.setLootChestName(null);
			adminProfile.setLootChestReturnTickrate(0);
			adminProfile.setLootChestSetting(false);
			adminProfile.setName(null);
			adminProfile.setSecondLocArea(null);
			adminProfile.setSpawnAmount(0);
			adminProfile.setTickrate(0);
			adminProfile.setType(null);
			event.getPlayer().getInventory().removeItem(OWA.hoeItem);
			event.getPlayer().getInventory().removeItem(OWA.shovelItem);
		}
	}
}
