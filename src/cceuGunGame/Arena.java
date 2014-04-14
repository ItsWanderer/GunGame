package cceuGunGame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {
	
	private List<Location> spawns = new ArrayList<Location>();
	
	private Location lobby;
	
	private ArenaManager manager;
	
	private int arenaID;

	private int minPlayers;
	private int maxPlayers;
	
	public Arena(ArenaManager main, int arenaID, int minPlayers, int maxPlayers) {
		this.manager = main;

		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		this.arenaID = arenaID;
	}
	
	public void joinLobby(Player p) {
		if (this.maxPlayers > this.manager.getPlayers(this.arenaID)) {
			
			this.manager.players.put(p.getName(), this.arenaID);
			
			p.teleport(this.lobby);
			
		} else {
			p.sendMessage("§cDiese Arena ist schon voll!");
		}
	}
	
	public void setLobby(Location l) {
		this.lobby = l;
		
		this.manager.plugin.getConfig().set("arenas." + this.arenaID + ".lobby", this.manager.plugin.locToString(l));
		
		this.manager.plugin.saveConfig();
	}
	
	public void addSpawn(Location l) {
		spawns.add(l);
		
		List<String> temp = this.manager.plugin.getConfig().getStringList("arenas." + this.arenaID + ".spawns");
		temp.add(this.manager.plugin.locToString(l));
		this.manager.plugin.getConfig().set("arenas." + this.arenaID + ".spawns", temp);
		
		this.manager.plugin.saveConfig();
	}
	
	public void resetSpawns() {
		spawns.clear();
		
		this.manager.plugin.getConfig().set("arenas." + this.arenaID + ".spawns", new ArrayList<String>());
		
		this.manager.plugin.saveConfig();
	}

}
