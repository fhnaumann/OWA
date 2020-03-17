package me.wand555.OWA.Listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import me.wand555.OWA.CustomEvents.ThirstLevelChangeEvent;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Main.ThirstLevelChangeReason;

public class ThirstLevelChangeListener implements Listener {

	private OWA plugin;
	private ArrayList<PotionEffect> onLowEffects;
	private ArrayList<PotionEffect> onVeryLowEffects;
	
	public ThirstLevelChangeListener(JavaPlugin plugin, ArrayList<PotionEffect> onLowEffects, ArrayList<PotionEffect> onVeryLowEffects) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.onLowEffects = onLowEffects;
		this.onVeryLowEffects = onVeryLowEffects;
		this.plugin = (OWA) plugin;
	}
	
	
	@EventHandler
	public void onRedirectFromWatterBottleEvent(PlayerItemConsumeEvent event) {
		if(event.getItem().getType() == Material.POTION) {
			plugin.getServer().getPluginManager().callEvent(new ThirstLevelChangeEvent(event.getPlayer(), 
					ThirstLevelChangeReason.WATER_BOTTLE, onLowEffects, onVeryLowEffects));
			return;
		}
	}
	
	@EventHandler
	public void onRedirectFromWaterRightClickEvent(PlayerInteractEvent event) {
		if(event.getAction() == Action.LEFT_CLICK_AIR) {
			Block b = event.getPlayer().getTargetBlock(null, OWA.thirstRefillRange);
			if(b.getType() == Material.WATER) {
				plugin.getServer().getPluginManager().callEvent(new ThirstLevelChangeEvent(event.getPlayer(), 
						ThirstLevelChangeReason.WATER_BOTTLE, onLowEffects, onVeryLowEffects));
				return;
			}
		}
		if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.WATER) {
				plugin.getServer().getPluginManager().callEvent(new ThirstLevelChangeEvent(event.getPlayer(), 
						ThirstLevelChangeReason.STILL_WATER, onLowEffects, onVeryLowEffects));
				return;
			}
		}
		
	}
}
