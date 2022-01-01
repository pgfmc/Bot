package net.pgfmc.bot.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.configify.Configify;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles extends Configify {
	
	public static String defaultRoleId = "0";
	
	public static void recalculateRoles(PlayerData pd) {
		
		String discordo = (String) pd.loadFromFile("Discord");
		
		if (discordo != null) {
			pd.setData("Discord", discordo);
			
			List<String> roles = new ArrayList<String>();
			Discord.JDA.getGuildById("579055447437475851").getMemberById(discordo).getRoles().forEach(r -> roles.add(r.getName()));
			
			pd.setData("Roles", roles);
			
		} else {
			pd.setData("Roles", Arrays.asList(Discord.JDA.getRoleById(defaultRoleId)));
		}
	}

	@Override
	public void reload() {
		Optional.ofNullable(Mixins.getDatabase(Main.configPath).getString("defaultRoleId")).orElse("579062298526875648");
	}
}
