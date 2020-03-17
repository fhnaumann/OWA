package me.wand555.OWA.Listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.PlayerProfile;

public class PlayerChangeStatsListener implements Listener {

	private OWA plugin;
	
	public PlayerChangeStatsListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onPlayerKillZombieEvent(EntityDeathEvent event) {
		if(event.getEntity() instanceof Zombie) {
			Player p = ((LivingEntity) event.getEntity()).getKiller();
			if(p != null) {
				PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
				profile.setZombieKills(profile.getZombieKills() + 1);
				profile.setExperience(profile.getExperience() + OWA.experienceZombieAmount);
				profile.updateZombieKillScoreboard();
				profile.updateExperienceScoreboard();
				System.out.println(profile.getZombieKills());
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerKillOtherPlayerEvent(PlayerDeathEvent event) {
		PlayerProfile deathProfile = PlayerProfile.getProfileFromPlayer(event.getEntity().getUniqueId());
		deathProfile.setDeaths(deathProfile.getDeaths() + 1);
		deathProfile.updatePlayerDeathScoreboard();
		System.out.println(deathProfile.getDeaths());
		
		PlayerProfile profile = PlayerProfile.getProfileFromPlayer(event.getEntity().getKiller().getUniqueId());
		profile.setPlayerKills(profile.getPlayerKills() + 1);
		profile.setExperience(profile.getExperience() + OWA.experiencePlayerAmount);
		profile.updatePlayerKillScoreboard();
		profile.updateExperienceScoreboard();
		System.out.println(profile.getPlayerKills());
		
		
	}
}
