package me.wand555.OWA.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminProfile;

public class AdminChangeWorldWhileSettingAreaListener implements Listener {

	private OWA plugin;
	
	public AdminChangeWorldWhileSettingAreaListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onAdminChangeWolrdWhileSettingAreaEvent(PlayerChangedWorldEvent event) {
		if(event.getPlayer().hasPermission("owa.admin.setzone")) {
			AdminProfile profile = AdminProfile.getAdminProfileFromUUID(event.getPlayer().getUniqueId());
			if(profile.isAreaSetting()) {
				profile.setAreaSetting(false);
				profile.setFirstLocArea(null);
				profile.setSecondLocArea(null);
				profile.setType(null);
				profile.setName(null);
				event.getPlayer().sendMessage("Changed world while setting area. Please remove your hoe and do /setzone again");
			}
		}
	}
}
