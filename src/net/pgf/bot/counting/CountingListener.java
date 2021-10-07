package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class CountingListener implements EventListener {
	
	@Override
	public void onEvent(GenericEvent e)
	{
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		
		if (msg.getAuthor().isBot()) { return; } // bots not allowed ;oo
		if (!msg.getChannel().getName().equals("counting")) { return; } // #counting only (any guild not in the opt-out)
		
		String raw = msg.getContentRaw();
		
		Counting counter = Counting.getCounter(msg.getGuild());
		
		counter.count(raw, msg.getAuthor(), msg);
		
	}

}
