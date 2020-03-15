package me.wand555.OWA.Timer;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.wand555.OWA.CustomEvents.ThirstLevelChangeEvent;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Main.ThirstLevelChangeReason;
import me.wand555.OWA.Player.PlayerProfile;

public class ThirstTimer extends BukkitRunnable {

	private ArrayList<PotionEffect> onLowEffects;
	private ArrayList<PotionEffect> onVeryLowEffects;
	private OWA plugin;
	
	public ThirstTimer(JavaPlugin plugin, ArrayList<PotionEffect> onLowEffects, ArrayList<PotionEffect> onVeryLowEffects) {
		this.onLowEffects = onLowEffects;
		this.onVeryLowEffects = onVeryLowEffects;
		this.plugin = (OWA) plugin;
	}
	
	@Override
	public void run() {
		for(Entry<UUID, PlayerProfile> entry : PlayerProfile.getProfiles().entrySet()) {
			//checks if player is online
			if(Bukkit.getPlayer(entry.getKey()) != null) {
				Player p = Bukkit.getPlayer(entry.getKey());
				
				if(p.hasPermission("owa.thirst.pass")) { //NOT NICHT VERGESSEN (!)
					plugin.getServer().getPluginManager().callEvent(new ThirstLevelChangeEvent(p, ThirstLevelChangeReason.TIME, 
							onLowEffects, onVeryLowEffects));
					
				}
			
			}
		}
	}
}
