package net.pgfmc.bot.listeners;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.player.Roles;

public class OnDiscordJoin implements EventListener {

	@Override
	public void onEvent(GenericEvent ev) {
		if (!(ev instanceof GuildJoinEvent)) { return; }
		GuildJoinEvent e = (GuildJoinEvent) ev;
		Guild g = e.getGuild();
		
		// PGF
		if (!e.getGuild().getId().equals("579055447437475851")) { return; }
		List<Member> allMembers = g.getMembers();
		List<Member> allRoleMembers = g.getMembersWithRoles(Discord.JDA.getRoleById(Roles.defaultRoleId)); // Members who have Member role
		
		allMembers.removeAll(allRoleMembers);
		
		allMembers.forEach(m -> g.addRoleToMember(m, Discord.JDA.getRoleById(Roles.defaultRoleId)).queue());
		
	}

}
