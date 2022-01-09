package net.pgfmc.bot.cmd;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class LinkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§Only players can execute this command.");
			return true;
		}
		
		PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null)
		{
			pd.sendMessage("§cYour Discord has already been linked.");
			pd.sendMessage("§cUse §r/unlink §cto unlink your account.");
			return true;
		}
		
		pd.sendMessage("§aGenerating code...");
		Thread thread = new Thread() {
			public void run() {
				String code = generateCode();
				pd.setData("linkCode", code);
				sender.sendMessage("§6Message the code §f[ " + code + " ] §ato PGF.bot in dms");
				sender.sendMessage("§ato link your account.");
			}
		};
		thread.start();
		
		return true;
	}
	
	/**
	 * Get a randomly generated 4 digit code that isn't taken
	 * This should only be accessed through a thread!
	 * @return The 4 digit unique code
	 */
	private static String generateCode()
	{
		String code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		// range is 1000 to 9999
		LinkedList<String> takenCodes = new LinkedList<>();
		
		for (PlayerData pd : PlayerData.getPlayerDataSet())
		{
			String tempCode = pd.getData("linkCode");
			if (tempCode == null || tempCode == "") continue;
			takenCodes.add(tempCode);
		}
		
		while (!takenCodes.contains(code))
		{
			code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		}
		
		return code;
	}
}