package cceuGunGame;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GunGameListener implements Listener {
	
	public GunGameMain plugin;
	
	public GunGameListener(GunGameMain main) {
		this.plugin = main;
	}
	
	@EventHandler
	public void onSignWrite(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[GunGame]") && e.getPlayer().hasPermission("gungame.sign.create")) {
			int arenaID = -1;
			try {
				arenaID = Integer.valueOf(e.getLine(1));
			} catch (NumberFormatException e1) {
			}
			if (arenaID != -1) {
				Location l = e.getBlock().getLocation();
				Arena a = this.plugin.manager.getArena(arenaID);
				
				String[] lines = a.updateSign(l);
				
				int lineNumber = 0;
				for (String s : lines) {
					e.setLine(lineNumber, s);
					lineNumber++;
				}
				
				a.signs.add(l);
				
				List<String> temp = this.plugin.getConfig().getStringList("arenas." + a.getID() + ".signs");
				temp.add(this.plugin.locToString(l));
				this.plugin.getConfig().set("arenas." + a.getID() + ".signs", temp);
				
				this.plugin.saveConfig();
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			if (this.plugin.manager.getArena(p) != null) {
				if (e.getDamager().getType() != EntityType.PLAYER) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		if (this.plugin.manager.getArena(p) != null) {
			p.setHealth(20.0D);
			
			Location l = this.plugin.manager.getArena(p).getRandomSpawn();
			
			if (l != null) {
				p.teleport(l);
			}
			
			if (p.getKiller() != null) {
				Player killer = p.getKiller();
				
				int level = this.plugin.manager.players_level.get(killer.getName())+1;
				
				this.plugin.manager.players_level.put(killer.getName(), level);
				killer.setLevel(level);
				
				killer.sendMessage("§2[§cGunGame§2] §6You killed " + p.getName() + "! §2Level UP!");
				
				killer.getInventory().clear();
				
				giveKit(p, level);
			}
			
		}
	}
	
	public void giveKit(Player p, Integer level) {
		
		switch (level) {
		case 0:
			
			ItemStack stick = new ItemStack(Material.STICK);
			stick.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
			
			ItemMeta stick_meta = stick.getItemMeta();
			stick_meta.setDisplayName("§6§lStick");
			stick.setItemMeta(stick_meta);
			
			p.getInventory().addItem(stick);
			
			break;
		case 1:
			
			p.getInventory().addItem(new ItemStack(Material.WOOD_SWORD));
			p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			
			break;
		case 2:
			
			p.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
			p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
			
			break;
		case 3:
			
			this.plugin.manager.getArena(p).win(p);
			
			break;
		}
		
	}
}
