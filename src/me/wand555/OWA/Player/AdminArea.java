package me.wand555.OWA.Player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import me.wand555.OWA.Main.AdminAreaType;
import me.wand555.OWA.Main.OWA;
import me.wand555.OWA.Timer.AdminAreaTimer;

public class AdminArea {
	
	private static ArrayList<AdminArea> adminAreas = new ArrayList<AdminArea>();

	private String name;
	private UUID creator;
	private ArrayList<Location> area;
	private BoundingBox box;
	private int spawnAmount;
	private long tickrate;
	private AdminAreaType type;
	private BukkitTask runningTask;
	
	public AdminArea(String name, Player p, Location firstCorner, Location secondCorner, AdminAreaType type) {
		p.getInventory().removeItem(OWA.hoeItem);
		this.name = name;
		this.creator = p.getUniqueId();
		this.setType(type);
		
		this.area = new ArrayList<Location>();
		
		BoundingBox box = BoundingBox.of(firstCorner, secondCorner);
		this.box = box;
		for(double x = box.getMinX(); x <= box.getMaxX(); x++) {
			for(double y = box.getMinY(); y <= box.getMaxY(); y++) {
				for(double z = box.getMinZ(); z <= box.getMaxZ(); z++) {
					this.area.add(new Location(firstCorner.getWorld(), x, y, z));
				}
			}
		}
		
		p.sendMessage("Finished storing blocks");
		//neue Runnable
		runningTask = null;
		area.stream().forEachOrdered(l -> System.out.println(l.getBlockX() + "/" + l.getBlockY() + "/" + l.getBlockZ()));
		adminAreas.add(this);
	}
	
	/**
	 * Use this if type == ZOMBIE_CAMP
	 * 
	 * @param name
	 * @param p
	 * @param firstCorner
	 * @param secondCorner
	 * @param type
	 * @param amount
	 * @param tickrate
	 */
	public AdminArea(String name, Player p, Location firstCorner, Location secondCorner, AdminAreaType type, int amount, long tickrate) {
		p.getInventory().removeItem(OWA.hoeItem);
		this.name = name;
		this.creator = p.getUniqueId();
		this.area = new ArrayList<Location>();
		this.spawnAmount = amount;
		this.tickrate = tickrate;
		this.setType(type);
		BoundingBox box = BoundingBox.of(firstCorner, secondCorner);
		this.box = box;
		for(double x = box.getMinX(); x <= box.getMaxX(); x++) {
			for(double y = box.getMinY(); y <= box.getMaxY(); y++) {
				for(double z = box.getMinZ(); z <= box.getMaxZ(); z++) {
					this.area.add(new Location(firstCorner.getWorld(), x, y, z));
				}
			}
		}
		p.sendMessage("Finished storing blocks");
		//neue Runnable
		if(type == AdminAreaType.ZOMBIE_CAMP) {
			runningTask = new AdminAreaTimer(area, amount).runTaskTimer(OWA.getPlugin(OWA.class), tickrate, tickrate);
		}
		else {
			runningTask = null;
		}
		adminAreas.add(this);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the creator
	 */
	public UUID getCreator() {
		return creator;
	}

	/**
	 * @return the area
	 */
	public ArrayList<Location> getArea() {
		return area;
	}

	/**
	 * @return the type
	 */
	public AdminAreaType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AdminAreaType type) {
		this.type = type;
	}

	/**
	 * Always check if != null. This task is always null if getType == SAFE_CAMP
	 * 
	 * @return the runningTask
	 */
	public BukkitTask getRunningTask() {
		return runningTask;
	}

	/**
	 * @return the adminAreas
	 */
	public static ArrayList<AdminArea> getAdminAreas() {
		return adminAreas;
	}
	
	public static AdminArea getAdminAreaFromName(String name) {
		for(AdminArea adminArea : getAdminAreas()) {
			if(adminArea.getName().equalsIgnoreCase(name)) {
				return adminArea;
			}
		}
		return null;
	}

	/**
	 * @return the tickrate
	 */
	public long getTickrate() {
		return tickrate;
	}

	/**
	 * @param tickrate the tickrate to set
	 */
	public void setTickrate(long tickrate) {
		this.tickrate = tickrate;
	}

	/**
	 * @return the spawnAmount
	 */
	public int getSpawnAmount() {
		return spawnAmount;
	}

	/**
	 * @param spawnAmount the spawnAmount to set
	 */
	public void setSpawnAmount(int spawnAmount) {
		this.spawnAmount = spawnAmount;
	}

	/**
	 * @return the box
	 */
	public BoundingBox getBox() {
		return box;
	}
	
}
