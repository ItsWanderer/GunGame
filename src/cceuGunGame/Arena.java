package cceuGunGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {
	
	private List<Location> spawns = new ArrayList<Location>();
	
	private Location lobby;
	
	private ArenaManager manager;
	
	private int arenaID;

	private int minPlayers;
	private int maxPlayers;
	
	private boolean isEnabled;
	
	public ArenaPhase phase;
	
	public Arena(ArenaManager main, int arenaID, int minPlayers, int maxPlayers) {
		this.phase = ArenaPhase.LOBBY;
		
		this.isEnabled = true;
		
		this.manager = main;

		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		this.arenaID = arenaID;
	}
	
	public Arena(ArenaManager main, int arenaID, int minPlayers, int maxPlayers, boolean isEnabled, Location lobby, List<Location> spawns) {
		this.isEnabled = isEnabled;
		this.spawns = spawns;
		this.lobby = lobby;
		
		this.manager = main;

		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		this.arenaID = arenaID;
	}
	
	public Location getRandomSpawn() {
		Collections.shuffle(this.spawns);
		if (this.spawns.size() > 0) {
			return this.spawns.get(0);
		} else {
			return null;
		}
	}
	
	public int getID() {
		return this.arenaID;
	}
	
	public void setEnabled(Boolean b) {
		this.isEnabled = b;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public void startArena() {
		this.phase = ArenaPhase.STARTING;
		
		List<Location> temp_spawns = this.spawns;
		
		try {
			for (Player p : this.manager.getPlayerList(this.arenaID)) {
				try {
					if (temp_spawns.size() > 0) { 
						p.teleport(temp_spawns.get(0));
						temp_spawns.remove(0);
					} else {
						System.out.println("§cERROR: No spawns available!!");
					}
				} catch (Exception e1) {
				}
			}
		} catch (Exception e1) {
		}
		
		startCountdown();
	}
	
	public void joinLobby(Player p) {
		if (this.maxPlayers > this.manager.getPlayers(this.arenaID) && this.spawns.size() > this.manager.getPlayers(this.arenaID)) {
			
			this.manager.players.put(p.getName(), this.arenaID);
			
			p.teleport(this.lobby);
			
		} else {
			p.sendMessage("§cDiese Arena ist schon voll!");
		}
		
		if (this.minPlayers >= this.manager.getPlayers(this.arenaID)) {
			
			for (Player pl : this.manager.getPlayerList(this.arenaID)) {
				pl.sendMessage("§6§lThe game will start in 15 seconds!");
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(this.manager.plugin, new Runnable() {
				
				@Override
				public void run() {
					for (Player pl : manager.getPlayerList(arenaID)) {
						pl.sendMessage("§6§lThe game will start in 5 seconds!");
					}
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
						
						@Override
						public void run() {
							for (Player pl : manager.getPlayerList(arenaID)) {
								pl.sendMessage("§6§lThe game is starting...");
							}
							
							startArena();
						}
					}, 100);
				}
			}, 200);
			
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
	
	public void startCountdown() {
		this.phase = ArenaPhase.COUNTDOWN;
		
		for (Player pl : manager.getPlayerList(arenaID)) {
			pl.sendMessage("§6§lThe game starts in 30sec");
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
			
			@Override
			public void run() {
				for (Player pl : manager.getPlayerList(arenaID)) {
					pl.sendMessage("§6§lThe game starts in 20sec");
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
					
					@Override
					public void run() {
						for (Player pl : manager.getPlayerList(arenaID)) {
							pl.sendMessage("§6§lThe game starts in 10sec");
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
							
							@Override
							public void run() {
								for (Player pl : manager.getPlayerList(arenaID)) {
									pl.sendMessage("§6§lThe game starts in 5sec");
								}
								Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
									
									@Override
									public void run() {
										for (Player pl : manager.getPlayerList(arenaID)) {
											pl.sendMessage("§6§lThe game starts in 3sec");
										}
										Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
											
											@Override
											public void run() {
												for (Player pl : manager.getPlayerList(arenaID)) {
													pl.sendMessage("§6§lThe game starts in 2sec");
												}
												Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
													
													@Override
													public void run() {
														for (Player pl : manager.getPlayerList(arenaID)) {
															pl.sendMessage("§6§lThe game starts in 1sec");
														}
														Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, new Runnable() {
															
															@Override
															public void run() {
																for (Player pl : manager.getPlayerList(arenaID)) {
																	pl.sendMessage("§6§lYou can now attack eachother!");
																}
																
																phase = ArenaPhase.RUNNING;
															}
														}, 20);
													}
												}, 20);
											}
										}, 20);
									}
								}, 40);
							}
						}, 100);
					}
				}, 200);
			}
		}, 200);
	}

}
