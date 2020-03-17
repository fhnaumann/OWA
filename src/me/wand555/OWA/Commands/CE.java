package me.wand555.OWA.Commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import me.wand555.OWA.Main.AdminAreaType;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Player.AdminArea;
import me.wand555.OWA.Player.AdminProfile;
import me.wand555.OWA.Player.Campfire;
import me.wand555.OWA.Player.LootChest;
import me.wand555.OWA.Player.PlayerProfile;
import me.wand555.OWA.Timer.AdminAreaTimer;

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
				if(p.hasPermission("owa.warp.set")) {
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
				if(p.hasPermission("owa.warp.use")) {
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
				if(p.hasPermission("owa.warp.delete")) {
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
			else if(cmd.getName().equalsIgnoreCase("warplist")) {
				if(p.hasPermission("owa.warp.list")) {
					if(args.length == 0) {
						PlayerProfile profile = PlayerProfile.getProfileFromPlayer(p.getUniqueId());
						if(!profile.getCampfires().isEmpty()) {
							p.sendMessage("------Campfires------");
							for(Campfire campfire : profile.getCampfires()) {
								Location campfireLoc = campfire.getCampfireLoc();
								String name = campfire.getName().get(p.getUniqueId());
								Location loc = campfire.getLoc().get(p.getUniqueId());
								p.sendMessage(name + " at " + loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ()
									+ " from the campfire at " + campfireLoc.getBlockX() + "/" + campfireLoc.getBlockY() + "/" + campfireLoc.getBlockZ() + "!");
							}
						}
						else {
							p.sendMessage("You dont have any warps set");
						}
					}
					else {
						
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("setzone")) {
				if(p.hasPermission("owa.admin.zone.set")) {
					//give item
					if(args.length == 2) {
						if(args[1].equalsIgnoreCase("s")) {
							AdminArea adminArea = AdminArea.getAdminAreaFromName(args[0]);
							if(adminArea == null) {
								AdminProfile profile = AdminProfile.getAdminProfileFromUUID(p.getUniqueId());
								if(profile.isAreaSetting()) {
									p.sendMessage("You're already setting an area!");
								}
								else {
									profile.setAreaSetting(true);
									profile.setType(AdminAreaType.SAFE_CAMP);
									profile.setName(args[0]);
									p.getInventory().addItem(OWA.hoeItem);
									p.sendMessage("Click the bounds with this item!");
								}
							}
							else {
								p.sendMessage("Area are with given name already exists");
							}
						}
					}
					
					//CHECK IF args[2] und args[3] nummbern sind!!!!!
					else if(args.length == 4) {		
						if(args[1].equalsIgnoreCase("zc")) {
							AdminArea adminArea = AdminArea.getAdminAreaFromName(args[0]);
							if(adminArea == null) {
								AdminProfile profile = AdminProfile.getAdminProfileFromUUID(p.getUniqueId());
								if(profile.isAreaSetting()) {
									p.sendMessage("You're already setting an area!");
								}
								else {
									if(StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[3])) {
										profile.setAreaSetting(true);
										profile.setType(AdminAreaType.ZOMBIE_CAMP);
										profile.setSpawnAmount(Integer.valueOf(args[2]));
										profile.setTickrate(Long.valueOf(args[3]));
										p.getInventory().addItem(OWA.hoeItem);
										p.sendMessage("Click the bounds with this item!");
									}
									else {
										p.sendMessage("last two parameters have to be numbers");
									}
									
								}
							}
							else {
								p.sendMessage("Area with given name already exists");
							}
						}
						else {
							p.sendMessage("Wrong format, only s or zc allowed as last parameter");
						}
					}
					else {
						p.sendMessage("Syntax: /setzone <uniqueName> <s OR zc> <spawnAmount> <tickrate>");
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("removezone")) {
				if(p.hasPermission("owa.admin.zone.remove")) {
					if(args.length == 1) {
						AdminArea adminArea = AdminArea.getAdminAreaFromName(args[0]);
						if(adminArea != null) {
							AdminProfile profile = AdminProfile.getAdminProfileFromUUID(adminArea.getCreator());
							if(profile != null) {
								if(adminArea.getRunningTask() != null) {
									adminArea.getRunningTask().cancel();
								}
								profile.setFirstLocArea(null);
								profile.setName(null);
								profile.setSecondLocArea(null);
								profile.setSpawnAmount(0);
								profile.setTickrate(0);
								profile.setType(null);
							}		
							AdminArea.getAdminAreas().remove(adminArea);
							p.sendMessage("Removed zone");
						}
						else {
							p.sendMessage("No match with given name");
						}
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("manageloot")) {
				if(p.hasPermission("owa.admin.lootchest.manage")) {
					AdminProfile profile = AdminProfile.getAdminProfileFromUUID(p.getUniqueId());
					if(profile.isLootChestSetting()) {
						//take away shovel, disable it
						p.getInventory().removeItem(OWA.shovelItem);
						profile.setLootChestSetting(false);
						profile.setLootChestName(null);
						profile.setLootChestReturnTickrate(0);
						p.sendMessage("Left manageloot-mode");
						return true;
					}
					if(args.length == 0) {
						
						//give player shovel
						// /manageloot <Optional: TimeInH>
						//each loot chest has a name
						//left click == destroy loot
						//right click == set loot
						p.sendMessage("Left Click chest to remove this lootchest.");
						p.getInventory().addItem(OWA.shovelItem);
						profile.setLootChestSetting(true);
						p.sendMessage("Run this command again to leave manageloot-mode");
						
					}
					else if(args.length == 2) {
						if(StringUtils.isNumeric(args[1])) {
							p.sendMessage("Left Click chest to remove this lootchest. Right click to make it a lootchest.");
							p.getInventory().addItem(OWA.shovelItem);
							profile.setLootChestSetting(true);
							profile.setLootChestName(args[0]);
							profile.setLootChestReturnTickrate(Integer.valueOf(args[1]));
							p.sendMessage("Run this command again to leave manageloot-mode");
							// /manageloot <Optional: TimeInH>
							//each loot chest has a name
							//left click == destroy loot
							//right click == set loot
							
						}
						else {
							p.sendMessage("Is no number");
						}
					}
					else {
						p.sendMessage("Syntax: ");
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
