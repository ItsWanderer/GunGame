package cceuGunGame;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GunGameListener implements Listener {
	
	public GunGameMain plugin;
	
	public GunGameListener(GunGameMain main) {
		this.plugin = main;
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			if (e.getDamager().getType() != EntityType.PLAYER) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		p.setHealth(20.0D);
		
		Location l = this.plugin.manager.getArena(p).getRandomSpawn();
		
		if (l != null) {
			p.teleport(l);
		}
	}
}
