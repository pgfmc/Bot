package net.pgfmc.bot;

import java.io.File;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.bot.cmd.LinkCommand;
import net.pgfmc.bot.cmd.UnlinkCommand;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static String configPath;
	
	public static Consumer<Random> action;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		configPath = plugin.getDataFolder() + File.separator + "config.yml";
		
		
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
	}
	
	@Override
	public void onDisable()
	{
		String msg = "";
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			
			msg += "<:LEAVE:905682349239463957> " + p.getName() + "\n";
		}
		
		Discord.sendMessage(msg);
		Discord.sendMessage(Discord.STOP_MESSAGE);
		
		if (action != null) {
			action.accept(null);
		}
	}

}
