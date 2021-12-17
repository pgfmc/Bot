package net.pgfmc.bot;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.bot.cmd.LinkCommand;
import net.pgfmc.bot.cmd.UnlinkCommand;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable()
	{
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
