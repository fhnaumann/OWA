package me.wand555.OWA.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.wand555.OWA.Main.OWA;

public class LeashRightClickListener implements Listener {

	private OWA plugin;
	
	public LeashRightClickListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onLeashRightClickZombieEvent(PlayerInteractEntityEvent event) {
		if(event.getHand() == EquipmentSlot.HAND) {
			if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEAD) {
				if(event.getRightClicked() instanceof Zombie) {
					Zombie zombie = (Zombie) event.getRightClicked();
					if(!zombie.isLeashed()) {
						event.getPlayer().getInventory().getItemInMainHand().setAmount(
								event.getPlayer().getInventory().getItemInMainHand().getAmount()-1);
						System.out.println("hdsgsdg");
						new BukkitRunnable() {		
							@Override
							public void run() {
								zombie.setLeashHolder(event.getPlayer());
							}
						}.runTaskLater(plugin, 1L);
					}	
				}
			}
		}	
	}
	
	//useless as it drops naturally already
	@EventHandler
	public void onLeashTest(PlayerUnleashEntityEvent event) {
		System.out.println("here");
	}
}
