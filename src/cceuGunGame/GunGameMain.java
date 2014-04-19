package cceuGunGame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GunGameMain extends JavaPlugin {
	
	public ArenaManager manager;

	@Override
	public void onEnable() {
		manager = new ArenaManager(this);
		
		FileConfiguration cfg = this.getConfig();
		cfg.options().copyDefaults(true);
		this.saveConfig();
		
		this.manager.readDataFromConfig();
		
		Bukkit.getPluginManager().registerEvents(new GunGameListener(this), this);
		
		System.out.println("The Plugin " + this.getDescription().getName() + " Version " + this.getDescription().getVersion() +  " loaded!");
	}

	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gg")) {
			
			sender.sendMessage("§6 §m------------ §bGunGame §6§m------------");
			sender.sendMessage("§aVersion: §b" + this.getDescription().getVersion());
			sender.sendMessage("§aAuthor: §b" + this.getDescription().getAuthors());
			sender.sendMessage("§6 §m------------ §bGunGame §6§m------------");
			
		} else if (cmd.getName().equalsIgnoreCase("gga")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				
				if (!p.hasPermission("gungame.admin")) {
					p.sendMessage("§cYou don't have the Permission to use that Command!");
					return true;
				}
				
				if (args.length == 0) {
					args = new String[] {
							"help",
						};
				}
				
				switch(args[0]) {
					case "addarena":
						
						this.manager.createArena(p);
						p.sendMessage("§aSuccessfully created a new Arena!");
						
						break;
					case "setlobby":
						
						if (args.length == 1) {
							p.sendMessage("§c/gga setlobby <ArenaID>");
						} else {
							int arenaID = Integer.valueOf(args[1]);
							Arena a = this.manager.getArena(arenaID);
							a.setLobby(p.getLocation());
							p.sendMessage("§aSuccessfully updated LobbyLocation!");
						}
						
						break;
					case "addspawn":
						
						if (args.length == 1) {
							p.sendMessage("§c/gga addspawn <ArenaID>");
						} else {
							int arenaID = Integer.valueOf(args[1]);
							Arena a = this.manager.getArena(arenaID);
							a.addSpawn(p.getLocation());
							p.sendMessage("§aSuccessfully added SpawnLocation!");
						}
						
						break;
					case "resetspawns":
						
						if (args.length == 1) {
							p.sendMessage("§c/gga resetspawns <ArenaID>");
						} else {
							int arenaID = Integer.valueOf(args[1]);
							Arena a = this.manager.getArena(arenaID);
							a.resetSpawns();
							p.sendMessage("§aSuccessfully reset SpawnLocations!");
						}
						
						break;
					default:
						p.sendMessage("§cGunGame-Help:");
						p.sendMessage("§6/gga setlobby §8to set the arenalobby");
						p.sendMessage("§6/gga addspawn §8to add an arenaspawn");
						p.sendMessage("§6/gga resetspawns §8to reset the arenaspawns");
						break;
				}
				
			} else {
				sender.sendMessage("§cOnly for Players!");
			}
		} else {
			sender.sendMessage("§cTry /gg");
		}
		
		return true;
	}
	
	public String locToString(Location l) {
        String ret;
        ret = l.getWorld().getName()+","+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ();
        return ret;
    }
	public Location stringToLoc(String s) {

        String[] a = s.split("\\,");

        World w = Bukkit.getServer().getWorld(a[0]);

        float x = Float.parseFloat(a[1]);
        float y = Float.parseFloat(a[2]);
        float z = Float.parseFloat(a[3]);

        return new Location(w,x,y,z);
    }
	
}
