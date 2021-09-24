package net.pgf.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class ReadyListener implements EventListener {
	
	public void onEvent(GenericEvent e) {
		if (!(e instanceof ReadyEvent)) { return; }
		
		System.out.println("Ready, active");
		
		for (Guild guild : Main.JDA.getGuilds())
		{
			System.out.print(guild.getName());
			System.out.print(", ");
		}
		
		System.out.print("(" + ((ReadyEvent) e).getGuildTotalCount() + ")");
		
	}

}
