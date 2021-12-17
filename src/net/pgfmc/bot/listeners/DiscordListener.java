package net.pgfmc.bot.listeners;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.pgfmc.bot.Discord;
import net.pgfmc.core.ChatEvents;
import net.pgfmc.core.Main;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.permissions.Roles.Role;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class DiscordListener implements EventListener {

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(GenericEvent e) {
		
		
		// Message Event
		if (e instanceof MessageReceivedEvent) {
			MessageReceivedEvent m = (MessageReceivedEvent) e;
			
			
			// message sent in #server by a Member (not this bot)
			if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && !m.getAuthor().getId().equals("721949520728031232")) {
				
				String s = m.getMessage().getContentDisplay();
				if (s.length() == 0) {return;}
				
				Role r = Role.getDominantOf(m.getMember().getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
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
				}
				
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
		
		// Ready Event
		
		else if (e instanceof ReadyEvent) {
			
			// auto delete stuff down below :\
			FileConfiguration database = Mixins.getDatabase(Main.configPath);
			
			String s = database.getString("channel");
			
			// sets the channel from the channel stored on config.yml.
			if (s != null) {
				Discord.SERVER_CHANNEL = s;
			} else {
				Discord.SERVER_CHANNEL = "771247931005206579";
				database.set("channel", "771247931005206579");
			}
			Discord.setChannel(Discord.JDA.getTextChannelById(Discord.SERVER_CHANNEL));
			
			if (database.getString("delete") != null) { // deletes the "server stopping" message.
				AuditableRestAction<Void> EEEE = Discord.getChannel().deleteMessageById(database.getString("delete"));
				
				try {
					EEEE.complete();
				} finally {
					System.out.println("delete message failed!");
				}
			}
			database.set("delete", null);
			
			try {
				database.save(Mixins.getFile(Main.configPath));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Discord Bot Initialized!");
			Discord.sendMessage(Discord.START_MESSAGE);
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
