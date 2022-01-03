package net.pgfmc.bot.listeners;

import java.io.IOException;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.player.ChatEvents;
import net.pgfmc.bot.player.Roles;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.permissions.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnMessageReceived implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		MessageReceivedEvent m = (MessageReceivedEvent) e;
		
		// message sent in #server by a Member (not this bot)
		if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && !m.getAuthor().getId().equals("721949520728031232")) {
			
			String s = m.getMessage().getContentDisplay();
			if (s.length() == 0) {return;}
			
			Role r = Role.MEMBER;
			// If member of PGF (mainly for BTS/outside PGF server)
			if (Discord.PGF_GUILD.isMember(m.getAuthor()))
			{
				r = Role.getDominantOf(Discord.PGF_GUILD.getMember(m.getAuthor()).getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
			}
			
			
			
			
			/*
			Guild g = Discord.JDA.getGuildById("579055447437475851");
			if (g.isMember(m.getAuthor())
					&& Role.getDominantOf(g.getMember(m.getAuthor())
							.getRoles()
							.stream()
							.map(x -> x.getId())
							.collect(Collectors.toList()))
					.getDominance() >= Role.TRAINEE.getDominance()
					&& s.startsWith("!!"))
			{
				PluginCommand cmd = Bukkit.getPluginCommand(s.replace("!!", "").split(" ")[0]);
				PlayerData pd = PlayerData.getPlayerDataById(m.getAuthor().getId());
				
				// DEBUG
				if (pd == null) { System.out.println("pd is null"); }
				if (cmd == null) { System.out.println("cmd is null"); }
				if (!(Role.getPermissions((List<Role>) pd.getData("Roles")).contains(cmd.getPermission()))) { System.out.println("no permission"); }
				
				if (cmd != null && pd != null && Role.getPermissions((List<Role>) pd.getData("Roles")).contains(cmd.getPermission()))
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("!!", ""));
					return;
				}
			}*/
			
			s.replace("%", ""); // removes all "%"s from the message.
			
			// attempts to bring over formatting from discord.
			s = format(s, "\\*\\*\\*", "§l§o"); 
			s = format(s, "\\*\\*", "§l");
			s = format(s, "\\*", "§o");
			s = format(s, "__", "§n");
			
			Bukkit.getServer().broadcastMessage(r.getColorCode() + m.getMember().getEffectiveName() + " §r§8-|| " + ChatEvents.getMessageColor(m.getMember().getId()) + s);
			return;
			
		} else 
		
		
		// message sent to the bot in DMs.	
			
		if (m.getChannelType() == ChannelType.PRIVATE && !m.getAuthor().getId().equals("721949520728031232")) {
			
			// when a Direct Message is sent to the bot.
			
			String c = m.getMessage().getContentRaw();
			c.strip().substring(0, 3);
			
			int code = Integer.parseInt(c); // the code sent in the message.
			
			PlayerData codeMatch = null; // the playerdata that has the code match.
			
			boolean taken = false; // wether or not the discord account has been taken.

			for (PlayerData Pd : PlayerData.getPlayerDataSet()) { // sets variables.
				
				if (Pd.getData("linkCode") != null && (int) Pd.getData("linkCode") == code) {
					Pd.setData("linkCode", null);
					codeMatch = Pd;
				}
				
				
				if (!taken) {
					taken = (Pd.getData("Discord") != null && Pd.getData("Discord").equals(m.getAuthor().getId()));
				}
			}
			
			if (codeMatch != null && !taken) {
				codeMatch.setData("Discord", m.getAuthor().getId()).save();
				m.getChannel().sendMessage("Your Discord account has been linked to " + codeMatch.getName() + ".").queue();
				
				Roles.recalculateRoles(codeMatch);
				Permissions.recalcPerms(codeMatch);
				codeMatch.sendMessage("§aYour roles have been updated!");
				return;
			}
			
			
		// if the bot sent the message.
		} else if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && m.getAuthor().getId().equals("721949520728031232")) {
				
			if (m.getMessage().getContentRaw().contains(Discord.START_MESSAGE)) {
				
				Main.action = x -> {
					m.getChannel().deleteMessageById(m.getMessageId()).queue();
				};
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					
					@Override
					public void run()
					{
						Main.action.accept(null);
						Main.action = null;
					}
					
				}, 20 * 60);
			} else if (m.getMessage().getContentRaw().contains(Discord.STOP_MESSAGE)) {
				
				
				FileConfiguration database = Mixins.getDatabase(Main.configPath);
				database.set("delete", m.getMessageId());
				
				try {
					database.save(Mixins.getFile(Main.configPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private String format(String s, String ds, String mc) {
		
		String[] sa = s.split(ds);
		
		boolean mark = false;
		s = "";
		
		for (String S : sa) {
			
			if (mark) {
				s = s + mc + S + "§r";
				mark = false;
			} else {
				s = s + S;
				mark = true;
			}
		}
		return s;
	}

}
