package me.wand555.OWA.CustomEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Main.TemperatureChangeReason;
import me.wand555.OWA.Player.PlayerProfile;

public class TemperatureChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private TemperatureChangeReason reason;
	private PlayerProfile profile;
	
	
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadVeryLowPotionsBefore = new HashMap<>();
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadLowPotionsBefore = new HashMap<>();
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadHighPotionsBefore = new HashMap<>();
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadVeryHighPotionsBefore = new HashMap<>();
	
	public TemperatureChangeEvent(Player p, TemperatureChangeReason reason, ArrayList<PotionEffect> veryLowEffect, 
			ArrayList<PotionEffect> lowEffect, ArrayList<PotionEffect> highEffect, ArrayList<PotionEffect> veryHighEffect) {		
		if(p.isDead()) {
			return;
		}
		this.player = p;
		this.reason = reason;
		this.profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
		//increase temperature
		if(reason == TemperatureChangeReason.FIRE || reason == TemperatureChangeReason.LAVA || reason == TemperatureChangeReason.SUN) {
			this.profile.setTemperature((this.profile.getTemperature() + reason.getAmount()) > 0 ?
					this.profile.getTemperature() + reason.getAmount() : 0);
			this.profile.getTemperatureBar().setProgress(this.profile.getTemperature()/20d);
			
		}
		//decrease temperature
		else {
			this.profile.setTemperature((this.profile.getTemperature() - reason.getAmount()) > 0 ?
					this.profile.getTemperature() - reason.getAmount() : 0);
			this.profile.getTemperatureBar().setProgress(this.profile.getTemperature()/20d);
		}
		
		this.profile.updateTempScoreboard();
		
		
		if(this.profile.getTemperature() <= OWA.temperatureLow) {
			if(this.profile.getTemperature() <= OWA.temperatureVeryLow) {
				this.profile.getTemperatureBar().setColor(BarColor.PURPLE);
				//player hat effect nicht?
				//	player bekommt effect
				//	player
				//player hat effect gehabt?
			}
			else {
				this.profile.getTemperatureBar().setColor(BarColor.BLUE);
			}
		} 
		else if(this.profile.getTemperature() >= OWA.temperatureHigh) {
			if(this.profile.getTemperature() >= OWA.temperatureVeryHigh) {
				this.profile.getTemperatureBar().setColor(BarColor.RED);
			}
			else {
				this.profile.getTemperatureBar().setColor(BarColor.YELLOW);
			}
		}
		else {
			//make white
			this.profile.getTemperatureBar().setColor(BarColor.WHITE);
		}
		
		if(this.profile.getTemperature() <= 0) this.profile.playerDeath(p);
		else if(this.profile.getTemperature() >= 20) this.profile.playerDeath(p);
		
		
		//low
		if(!storeHadLowPotionsBefore.containsKey(p.getUniqueId())) {
			if(profile.getTemperature() <= OWA.temperatureLow) {
				storeHadLowPotionsBefore.put(p.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : lowEffect) {
					if(!storeHadLowPotionsBefore.get(p.getUniqueId()).contains(effect.getType())) {
						storeHadLowPotionsBefore.get(p.getUniqueId()).add(effect.getType());
						p.addPotionEffect(effect);
					}
				}
			}
		}
		else {
			if(profile.getTemperature() > OWA.temperatureLow) {
				for(PotionEffectType effect : storeHadLowPotionsBefore.get(p.getUniqueId())) {
					p.removePotionEffect(effect);
				}
				storeHadLowPotionsBefore.remove(p.getUniqueId());
			}
		}
		
		//very low
		if(!storeHadVeryLowPotionsBefore.containsKey(p.getUniqueId())) {
			if(profile.getTemperature() <= OWA.temperatureVeryLow) {
				storeHadVeryLowPotionsBefore.put(p.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : veryLowEffect) {
					if(!storeHadVeryLowPotionsBefore.get(p.getUniqueId()).contains(effect.getType())) {
						storeHadVeryLowPotionsBefore.get(p.getUniqueId()).add(effect.getType());
						p.addPotionEffect(effect);
					}
				}
			}
		}
		else {
			if(profile.getTemperature() > OWA.temperatureVeryLow) {
				for(PotionEffectType effect : storeHadVeryLowPotionsBefore.get(p.getUniqueId())) {
					p.removePotionEffect(effect);
				}
				storeHadVeryLowPotionsBefore.remove(p.getUniqueId());
			}
		}
		
		//high
		if(!storeHadHighPotionsBefore.containsKey(p.getUniqueId())) {
			
			if(profile.getTemperature() >= OWA.temperatureHigh) {
				storeHadHighPotionsBefore.put(p.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : highEffect) {
					if(!storeHadHighPotionsBefore.get(p.getUniqueId()).contains(effect.getType())) {
						storeHadHighPotionsBefore.get(p.getUniqueId()).add(effect.getType());
						p.addPotionEffect(effect);
						System.out.println("added effect");
					}
				}
			}
		}
		else {
			if(profile.getTemperature() < OWA.temperatureHigh) {
				for(PotionEffectType effect : storeHadHighPotionsBefore.get(p.getUniqueId())) {
					p.removePotionEffect(effect);
				}
				storeHadHighPotionsBefore.remove(p.getUniqueId());
			}
		}
		
		//veryHigh
		if(!storeHadVeryHighPotionsBefore.containsKey(p.getUniqueId())) {
			if(profile.getTemperature() >= OWA.temperatureVeryHigh) {
				storeHadVeryHighPotionsBefore.put(p.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : veryHighEffect) {
					if(!storeHadVeryHighPotionsBefore.get(p.getUniqueId()).contains(effect.getType())) {
						storeHadVeryHighPotionsBefore.get(p.getUniqueId()).add(effect.getType());
						p.addPotionEffect(effect);
					}
				}
			}
		}
		else {
			if(profile.getTemperature() < OWA.temperatureVeryHigh) {
				for(PotionEffectType effect : storeHadVeryHighPotionsBefore.get(p.getUniqueId())) {
					p.removePotionEffect(effect);
				}
				storeHadVeryHighPotionsBefore.remove(p.getUniqueId());
			}
		}
		
	}
	
	
	@Override
	public HandlerList getHandlers() {
		
		return handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}


	/**
	 * @return the reason
	 */
	public TemperatureChangeReason getReason() {
		return reason;
	}

}
