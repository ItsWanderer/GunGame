package cceuGunGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class ArenaManager {
	
	public GunGameMain plugin;
	
	private List<Arena> arenas = new ArrayList<Arena>();
	
	public HashMap<String, Integer> players = new HashMap<String, Integer>();
	
	public ArenaManager(GunGameMain main) {
		this.plugin = main;
	}
	
	public void createArena(Player p) {
		int arenaCount = this.plugin.getConfig().getInt("arenaCount");
		
		this.arenas.add(new Arena(this, this.arenas.size(), 2, 10));
		
		this.plugin.getConfig().set("arenaCount", arenaCount+1);
		
		this.plugin.getConfig().set("arenas." + arenaCount + ".lobby", this.plugin.locToString(p.getLocation()));
		this.plugin.getConfig().set("arenas." + arenaCount + ".spawns", new ArrayList<String>());
		
		this.plugin.getConfig().set("enabled", true);
		
		this.plugin.saveConfig();
	}
	
	public void deleteArena(Arena a) {
		this.arenas.remove(a);
		
		this.plugin.getConfig().set("enabled", false);
		
		this.plugin.saveConfig();
	}
	
	public Arena getArena(int arenaID) {
		return this.arenas.get(arenaID);
	}
	
	public Arena getArena(Player p) {
		if (this.players.containsKey(p.getName())) {
			int arenaID = this.players.get(p.getName());
			return this.arenas.get(arenaID);
		} else {
			return null;
		}
	}
	
	public int getPlayers(int arenaID) {
		int temp = 0;
		
		for (Integer i : this.players.values()) {
			if (i == arenaID) {
				temp++;
			}
		}
		
		return temp;
	}

}
