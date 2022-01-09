package net.pgfmc.bot.functions;

import net.dv8tion.jda.api.entities.User;
import net.pgfmc.bot.player.Roles;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class AccountLinking {
	
	/**
	 * Link a Discord account to a Minecraft account
	 * 
	 * @param code The sent code from the user
	 * @param user The user who sent the code
	 */
	public static boolean linkAsk(String code, User user)
	{
		// when a Direct Message is sent to the bot.
		code = code.strip().substring(0, 3);
		
		PlayerData codeMatch = null; // the playerdata that has the code match.
		
		boolean taken = false; // wether or not the discord account has been taken.
		
		for (PlayerData pd : PlayerData.getPlayerDataSet()) { // sets variables.
			
			if (pd.getData("linkCode") != null && pd.getData("linkCode").equals(code)) {
				pd.setData("linkCode", null);
				codeMatch = pd;
			}
			
			
			if (!taken) {
				taken = (pd.getData("Discord") != null && pd.getData("Discord").equals(user.getId()));
			}
		}
		
		// Link the account
		if (codeMatch != null && !taken) {
			link(codeMatch, user.getId());
			codeMatch.sendMessage("§aYour roles have been updated!");
			return true;
		}
		
		return false;
	}
	
	/**
	 * Manually link a player to a Discord
	 * 
	 * @param p Minecraft Player to link
	 * @param id Discord user ID to link
	 */
	public static void link(PlayerData pd, String id)
	{
		pd.setData("Discord", id).save();
		Roles.recalculateRoles(pd);
		Permissions.recalcPerms(pd);
		pd.sendMessage("§aYour roles have been updated!");
	}

}
