package net.robbytu.bukkit.NoTalk;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class chatHandler implements Listener {
	public static NoTalk parentInstance;
	
	public chatHandler(NoTalk instance) {
		parentInstance = instance;
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent event) {
		// Check if the player is allowed to bypass NoTalk regions
		if(!event.getPlayer().hasPermission("notalk.bypass")) {
			// This try statement will bypass a bug, in which the console gets full of Exceptions because
			// the user is trying to talk in a world where NoTalk has never been used before.
			try {
				Set<String> _regions = ((MemorySection)parentInstance.getConfig().get("region." + event.getPlayer().getWorld().getName())).getKeys(false);
				for(String region : _regions) {
					// Check if the player is in this region
					double player_x = event.getPlayer().getLocation().getX();
					double player_y = event.getPlayer().getLocation().getY();
					double player_z = event.getPlayer().getLocation().getZ();
	
					double max_x = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".max.x");
					double max_y = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".max.y");
					double max_z = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".max.z");
	
					double min_x = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".min.x");
					double min_y = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".min.y");
					double min_z = parentInstance.getConfig().getDouble("region." + event.getPlayer().getWorld().getName() + "." + region + ".min.z");
					
					if(player_x >= min_x && player_x < max_x
							&& player_y >= min_y && player_y < max_y
							&& player_z >= min_z && player_z < max_z) {
						event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to chat here.");
						event.setCancelled(true);
					}
				}
			}
			catch(Exception ex) {
				// Do nothing, world just does not exist
			}
		}
	}

	
	
}
