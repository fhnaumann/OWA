package me.wand555.OWA.Timer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.PlayerProfile;

public class DealThirstDamage extends BukkitRunnable {

	private OWA plugin;
	private Player p;
	
	public DealThirstDamage(JavaPlugin plugin) {
		this.plugin = (OWA) plugin;
	}
	
	public DealThirstDamage(JavaPlugin plugin, Player p) {
		this.plugin = (OWA) plugin;
		this.p = p;
	}
	
	@Override
	public void run() {
		if(!p.isDead()) {
			PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
			if(profile.getThirst() <= 0) {
				if(p.getHealth() - OWA.thirstDamageAmount > 0) {
					p.damage(OWA.thirstDamageAmount);
				}
				else {
					profile.playerDeath(p);
				}	
			}
			else {
				this.cancel();
			}
			
		}
		else {
			this.cancel();
		}
	}
	
}
