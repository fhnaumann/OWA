package me.wand555.OWA.Commands;

import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.Campfire;
import me.wand555.OWA.Player.PlayerProfile;

public class CE implements CommandExecutor {
	
	private OWA plugin;

	public CE(OWA plugin) {
		this.plugin = plugin;
	}
	
	private Location getNearestCampfire(Location loc) {
		int radius = OWA.campfireSetwarpRadius;
		Block middle = loc.getBlock();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					Block b = middle.getRelative(x, y, z);
					if(b.getType() == Material.CAMPFIRE) {
						return b.getLocation();
					}
				}
			}
		}				
		return null;			
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("setwarp")) {
				if(p.hasPermission("owa.setwarp")) {
					if(args.length == 1) {
						Location loc = getNearestCampfire(p.getLocation());
						if(loc != null) {
							PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
							if(!Campfire.hasAnyCampNameDuplicate(profile, args[0])) {
								if(Campfire.getAllCampfires().containsKey(loc)) {
									Campfire camp = Campfire.getCampfireByLocation(loc);
									
									//wenn der Spieler in diesem camp KEINEN namen hat
									if(!camp.getName().containsKey(p.getUniqueId())) {
										Campfire.getCampfireByLocation(loc).addNewIndividualToAllCampfires(profile, args[0], p.getLocation());
										p.sendMessage("Added warp to existing campfire");
									}
									else {
										p.sendMessage("You can only have one warp per campfire");
									}
									
								}
								else {
									new Campfire(loc, profile, args[0], p.getLocation());
									p.sendMessage("Added warp to new campfire");
								}
							}
							else {
								p.sendMessage("duplicate name entry");
							}
							
							
							
						}
						else {
							//no campfire nearby
						}
						
					}
					else {
						
					}
				}	
			}
			else if(cmd.getName().equalsIgnoreCase("warp")) {
				if(p.hasPermission("owa.warp")) {
					if(args.length == 0) {
						PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						System.out.println(profile.getCampfires().size());
						if(profile.getCampfires().size() == 1) {
							p.teleport(profile.getCampfires().get(0).getLoc().get(p.getUniqueId()), TeleportCause.PLUGIN);
						}
					}
					else if(args.length == 1) {
						PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						for(Campfire camp : profile.getCampfires()) {
							
							if(camp.getName().containsKey(p.getUniqueId())) {
								String name = camp.getName().get(p.getUniqueId());
								if(name.equalsIgnoreCase(args[0])) {
									p.teleport(camp.getLoc().get(p.getUniqueId()), TeleportCause.PLUGIN);
									return true;
								}
							}
						}
						p.sendMessage("no match found for given name");
					}
					else {
						
					}
				}		
			}
			else if(cmd.getName().equalsIgnoreCase("delwarp")) {
				if(p.hasPermission("owa.delwarp")) {
					if(args.length == 0) {
						PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						if(profile.getCampfires().size() == 1) {
							Campfire toRemove = Campfire.getAllCampfires().remove(profile.getCampfires().get(0).getCampfireLoc());
							profile.getCampfires().remove(toRemove);
							p.sendMessage("deleted warp");
						}
					}
					else if(args.length == 1) {
						PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						for(Campfire camp : profile.getCampfires()) {
							
							if(camp.getName().containsKey(p.getUniqueId())) {
								String name = camp.getName().get(p.getUniqueId());
								if(name.equalsIgnoreCase(args[0])) {
									Campfire toRemove = Campfire.getAllCampfires().remove(camp.getCampfireLoc());
									profile.getCampfires().remove(toRemove);
									p.sendMessage("deleted warp 2");
									return true;
								}
							}
						}
						p.sendMessage("no match found for given name");
					}
					else {
						
					}
				}	
			}
		}
		else {
			sender.sendMessage("Only players can use commands!");
		}
		
		return true;
	}
	
	

}
