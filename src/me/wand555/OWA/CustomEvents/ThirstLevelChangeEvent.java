package me.wand555.OWA.CustomEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Main.ThirstLevelChangeReason;
import me.wand555.OWA.Player.PlayerProfile;
import me.wand555.OWA.Timer.DealThirstDamage;

public class ThirstLevelChangeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private ThirstLevelChangeReason reason;
	private PlayerProfile profile;
	
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadVeryLowPotionsBefore = new HashMap<>();
	private static HashMap<UUID, ArrayList<PotionEffectType>> storeHadLowPotionsBefore = new HashMap<>();
	
	public ThirstLevelChangeEvent(Player player, ThirstLevelChangeReason reason, ArrayList<PotionEffect> lowEffect, 
			ArrayList<PotionEffect> veryLowEffect) {
		this.player = player;
		this.reason = reason;
		this.profile = PlayerProfile.getProfileFromPlayer(player.getUniqueId());
		if(reason == ThirstLevelChangeReason.TIME) {
			this.profile.setThirst((this.profile.getThirst() - reason.getAmount()) > 0 ? 
					this.profile.getThirst() - reason.getAmount() : 0);
			this.profile.getThirstBar().setProgress(this.profile.getThirst()/20d);
			if(this.profile.getThirst() <= 0) new DealThirstDamage(OWA.getPlugin(OWA.class), player)
				.runTaskTimer(OWA.getPlugin(OWA.class), OWA.thirstDamageTickrate, OWA.thirstDamageTickrate);
		}
		else {
			this.profile.setThirst((this.profile.getThirst() + reason.getAmount()) < this.profile.getMaxThirst() ? 
					this.profile.getThirst() + reason.getAmount() : this.profile.getMaxThirst());
			this.profile.getThirstBar().setProgress(this.profile.getThirst()/20d);
		}
		
		//low
		if(!storeHadLowPotionsBefore.containsKey(player.getUniqueId())) {
			if(profile.getThirst() <= OWA.thirstLow) {
				storeHadLowPotionsBefore.put(player.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : lowEffect) {
					if(!storeHadLowPotionsBefore.get(player.getUniqueId()).contains(effect.getType())) {
						storeHadLowPotionsBefore.get(player.getUniqueId()).add(effect.getType());
						player.addPotionEffect(effect);
						System.out.println(effect.getType());
					}
				}
			}
		}
		else {
			if(profile.getThirst() > OWA.thirstLow) {
				for(PotionEffectType effect : storeHadLowPotionsBefore.get(player.getUniqueId())) {
					player.removePotionEffect(effect);
				}
				storeHadLowPotionsBefore.remove(player.getUniqueId());
			}
		}
		
		//very low
		if(!storeHadVeryLowPotionsBefore.containsKey(player.getUniqueId())) {
			if(profile.getThirst() <= OWA.thirstVeryLow) {
				storeHadVeryLowPotionsBefore.put(player.getUniqueId(), new ArrayList<>());
				
				for(PotionEffect effect : veryLowEffect) {
					if(!storeHadVeryLowPotionsBefore.get(player.getUniqueId()).contains(effect.getType())) {
						storeHadVeryLowPotionsBefore.get(player.getUniqueId()).add(effect.getType());
						player.addPotionEffect(effect);
					}
				}
			}
		}
		else {
			if(profile.getThirst() > OWA.thirstVeryLow) {
				for(PotionEffectType effect : storeHadVeryLowPotionsBefore.get(player.getUniqueId())) {
					player.removePotionEffect(effect);
				}
				storeHadVeryLowPotionsBefore.remove(player.getUniqueId());
			}
		}
	}
	
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public int getNewThirst() {
		return this.profile.getThirst();
	}

	public Player getPlayer() {
		return player;
	}

	public ThirstLevelChangeReason getReason() {
		return reason;
	}

}
