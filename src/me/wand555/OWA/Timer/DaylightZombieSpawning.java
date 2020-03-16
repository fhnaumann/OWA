package me.wand555.OWA.Timer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminArea;
import me.wand555.OWA.Player.PlayerProfile;

public class DaylightZombieSpawning extends BukkitRunnable {

	private OWA plugin;
	private List<World> worlds;
	
	public DaylightZombieSpawning(JavaPlugin plugin) {
		this.plugin = (OWA) plugin;
		this.worlds = Bukkit.getWorlds().stream().filter(w -> w.getEnvironment() == Environment.NORMAL)
				.collect(Collectors.toList());
	}

	@Override
	public void run() {
		for(World w : worlds) {
			for(Player p : w.getPlayers()) {
				if(!p.isDead()) {
					if(p.hasPermission("owa.spawn")) {
						//PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						int firstInnerBound = OWA.zombieSpawnInnerBound;
						int firstOuterBound = OWA.zombieSpawnOuterBound;
						
						int x = p.getLocation().getBlockX();
						int y = p.getLocation().getBlockY();
						int z = p.getLocation().getBlockZ();
						
						//ab hier eigentlich schleife
						outerloop:
						for(int j=0; j<OWA.zombieSpawnAmount; j++) {
							
							
							int randomNumberX = ThreadLocalRandom.current().nextInt(firstInnerBound, firstOuterBound+1);
							int randomNumberZ = ThreadLocalRandom.current().nextInt(firstInnerBound, firstOuterBound+1);
							
							Location rLoc = new Location(w, 0, 0, 0);
							
							double r = OWA.random.nextDouble();
							if(r < 0.25) {
								rLoc = new Location(w, x+randomNumberX, y, z+randomNumberZ);
							}
							else if(r >= 0.25 && r < 0.5){
								rLoc = new Location(w, x-randomNumberX, y, z+randomNumberZ);
							}
							else if(r >= 0.5 && r < 0.75){
								rLoc = new Location(w, x+randomNumberX, y, z-randomNumberZ);
							}
							else if(r >= 0.75){
								rLoc = new Location(w, x-randomNumberX, y, z-randomNumberZ);
							}
											
							if(rLoc.getBlock().getType() == Material.AIR) {
								
								int i = rLoc.getBlockY()+1;
								while(i > 0) {
									Location l = new Location(w, rLoc.getBlockX(), i, rLoc.getBlockZ());
									if(l.getBlock().getType() != Material.AIR) {
										
										for(AdminArea adminArea : AdminArea.getAdminAreas()) {
											if(adminArea.getArea().contains(l)) {
												System.out.println("avoided spawn due to safe camp");
												continue outerloop;
											}
										}
										//spawn mob here (at 'l')
										Zombie zombie = (Zombie) w.spawnEntity(l.getBlock().getRelative(BlockFace.UP).getLocation(), EntityType.ZOMBIE);
										zombie.setRemoveWhenFarAway(true);
										//System.out.println(l.getBlockX() + " ABC  " + l.getBlockZ());
										continue outerloop;
									}
									i--;
								}
							}
							else {
								//try and move it up
								int i = rLoc.getBlockY()+1;
								while(i < 255) {
									Location l = new Location(w, rLoc.getBlockX(), i, rLoc.getBlockZ());
									//when mob has space to spawn
									if(l.getBlock().getType() == Material.AIR && l.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
										
										for(AdminArea adminArea : AdminArea.getAdminAreas()) {
											if(adminArea.getArea().contains(l)) {
												System.out.println("avoided spawn due to safe camp");
												continue outerloop;
											}
										}
										//spawn mob
										Zombie zombie = (Zombie) w.spawnEntity(l, EntityType.ZOMBIE);
										zombie.setRemoveWhenFarAway(true);
										//System.out.println(l.getBlockX() + " DEF  " + l.getBlockZ());
										continue outerloop;
									}
									i++;
								}
							}
						}
					}
				}					
			}
		}		
	}
	
	
}
