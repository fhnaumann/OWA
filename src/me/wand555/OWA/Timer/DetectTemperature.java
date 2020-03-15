package me.wand555.OWA.Timer;

import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.CustomEvents.TemperatureChangeEvent;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Main.TemperatureChangeReason;
import me.wand555.OWA.Player.PlayerProfile;

public class DetectTemperature extends BukkitRunnable {

	private OWA plugin;
	private List<World> worlds;
	private ArrayList<PotionEffect> veryLowEffect = new ArrayList<>();
	private ArrayList<PotionEffect> lowEffect = new ArrayList<>();
	private ArrayList<PotionEffect> highEffect = new ArrayList<>();
	private ArrayList<PotionEffect> veryHighEffect = new ArrayList<>();

	public DetectTemperature(JavaPlugin plugin, ArrayList<PotionEffect> veryLowEffect, ArrayList<PotionEffect> lowEffect,
			ArrayList<PotionEffect> highEffect, ArrayList<PotionEffect> veryHighEffect) {

		this.plugin = (OWA) plugin;
		this.worlds = Bukkit.getWorlds().stream().filter(w -> w.getEnvironment() == Environment.NORMAL)
				.collect(Collectors.toList());
		this.veryLowEffect = veryLowEffect;
		this.lowEffect = lowEffect;
		this.highEffect = highEffect;
		this.veryHighEffect = veryHighEffect;
	}
	
	private void checkNearBlocksAndCallUpon(Player p) {
		int radius = OWA.temperatureRangeCheck;
		Block middle = p.getLocation().getBlock();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					Material m = middle.getRelative(x, y, z).getType();
					if (m == Material.LAVA || m == Material.FIRE) {
						plugin.getServer().getPluginManager()
								.callEvent(new TemperatureChangeEvent(p,
										TemperatureChangeReason.LAVA, veryLowEffect, lowEffect,
										highEffect, veryHighEffect));
						System.out.println("found hot source");
						return;
					} 
					else if (m == Material.ICE
							|| m == Material.BLUE_ICE
							|| m == Material.PACKED_ICE
							|| m == Material.FROSTED_ICE) {
						plugin.getServer().getPluginManager()
								.callEvent(new TemperatureChangeEvent(p,
										TemperatureChangeReason.ICE, veryLowEffect, lowEffect,
										highEffect, veryHighEffect));
						return;
					}
				}
			}
		}
	}
	
	public int getHighestNotCountingWaterBlock(World w, int x, int z) {
		int i = 255;
		while(i > 0) {
			Location l = new Location(w, x, i, z);
			if(l.getBlock().getType() != Material.AIR && l.getBlock().getType() != Material.WATER) {
				return i;
			}
			i--;
		}
		return Integer.MAX_VALUE;
	}

	
	
	@Override
	public void run() {
		for (World w : worlds) {

			for (Entry<UUID, PlayerProfile> entry : PlayerProfile.getProfiles().entrySet()) {
				if (Bukkit.getPlayer(entry.getKey()) != null) {
					Player p = Bukkit.getPlayer(entry.getKey());
					if (p.hasPermission("owa.temp.pass")) { // NOT NICHT VERGESSEN (!)

						// wenn es Tag ist und nicht stürmt
						if (w.getTime() > OWA.temperatureDayBegin && w.getTime() < OWA.temperatureDayEnd
								&& !w.hasStorm()) {
							if (p.getLocation().getBlock().getLightFromSky() >= 15) {
								plugin.getServer().getPluginManager()
										.callEvent(new TemperatureChangeEvent(p, TemperatureChangeReason.SUN,
												veryLowEffect, lowEffect, highEffect, veryHighEffect));
								return;
							} 
							else {
								if(p.getLocation().getBlock().getType() == Material.WATER) {
									//wenn mit unterer Hälfte in Wasser
									if(p.getLocation().getBlock().getRelative(BlockFace.UP).getLightLevel() >= 15) {
										plugin.getServer().getPluginManager()
										.callEvent(new TemperatureChangeEvent(p, TemperatureChangeReason.SUN,
												veryLowEffect, lowEffect, highEffect, veryHighEffect));
										return;
									}
									else {
										System.out.println(getHighestNotCountingWaterBlock(
												p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockZ()));
										//check if player has block above head
										if(p.getLocation().getBlockY()+1 >= getHighestNotCountingWaterBlock(
												p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockZ())) {
											if(p.getLocation().getBlock().getLightLevel() >= OWA.inWaterLightLevel) {
												System.out.println("here");
												plugin.getServer().getPluginManager()
												.callEvent(new TemperatureChangeEvent(p, TemperatureChangeReason.SUN,
														veryLowEffect, lowEffect, highEffect, veryHighEffect));
												return;
											}
										}		
									}
								}
								checkNearBlocksAndCallUpon(p);
							}
						} 
						else {
							// bei nacht oder Sturm hier
							
							//wenn er kein Dach hat
							if(p.getLocation().getBlockY() + 1 > p.getWorld().getHighestBlockYAt(p.getLocation())) {
								plugin.getServer().getPluginManager()
										.callEvent(new TemperatureChangeEvent(p, TemperatureChangeReason.MOON,
												veryLowEffect, lowEffect, highEffect, veryHighEffect));
								return;
							}
							else {
								checkNearBlocksAndCallUpon(p);
							}
						}
					}
				}
			}

		}
	}

}
