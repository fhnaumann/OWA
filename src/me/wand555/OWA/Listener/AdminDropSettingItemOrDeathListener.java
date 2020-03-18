package me.wand555.OWA.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.wand555.OWA.Main.OWA;

public class AdminDropSettingItemOrDeathListener implements Listener {

	private OWA plugin;
	
	public AdminDropSettingItemOrDeathListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = (OWA) plugin;
	}
	
	@EventHandler
	public void onAdminDropSettingItemEvent(PlayerDropItemEvent event) {
		if(event.getPlayer().hasPermission("owa.admin.zone.set") 
				|| event.getPlayer().hasPermission("owa.admin.zone.remove")
				|| event.getPlayer().hasPermission("owa.admin.lootchest.manage")) {
			if(event.getItemDrop().getItemStack().isSimilar(OWA.hoeItem)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("You cannot drop your setzone item!");
			}
			if(event.getItemDrop().getItemStack().isSimilar(OWA.shovelItem)) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("You cannot drop your lootchest item!");
			}
		}
	}
	
	@EventHandler
	public void onAdminWithSettingItemDeaethEvent(PlayerDeathEvent event) {
		if(event.getEntity().hasPermission("owa.admin.zone.set") 
				|| event.getEntity().hasPermission("owa.admin.zone.remove")
				|| event.getEntity().hasPermission("owa.admin.lootchest.manage")) {
			if(event.getDrops().contains(OWA.hoeItem)) {
				event.getDrops().remove(OWA.hoeItem);
				event.getEntity().sendMessage("You died with your setzone item. It got destroyed upon death");
			}
			if(event.getDrops().contains(OWA.shovelItem)) {
				event.getDrops().remove(OWA.shovelItem);
				event.getEntity().sendMessage("You died with your lootchest item. It got destroyed upon death");
			}
		}
	}
}
