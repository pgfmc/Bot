package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgf.bot.Main;

public class SetCount implements EventListener {
	
	public Counting counting;
	
	public SetCount(Counting counting)
	{
		this.counting = counting;
	}
	
	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		
		if (Counting.COUNTING_OPT_OUT.contains(msg.getGuild().getId())) { return; } // stops this code from running in opt-out servers
		if (msg.getAuthor().isBot()) { return; } // bots not allowed ;oo
		if (!Main.ADMINS.contains(msg.getAuthor().getId())) { return; } // Admins onlyyyy (hard coded in Main, doesn't use guild roles)
		if (!msg.getChannel().getName().equals("counting")) { return; } // #counting only (any guild not on the opt-out list)
		
		String raw = msg.getContentRaw();
		
		if (!raw.toLowerCase().substring(0, 10).equals(Main.PREFIX + "setcount ")) { return; } // If it doesn't start with "<prefix>setCount "
		
		String potentialNum = raw.toLowerCase().substring(10); // Removes "<prefix>setCount " and just leaves the argument
		
		if (!counting.isNumeric(potentialNum)) { return; } // not a number
		
		counting.count = Integer.valueOf(potentialNum);
		msg.getChannel().sendMessage(":pencil: Set the count to " + potentialNum + " (" + counting.mode.toString() + ")").queue();
		
	}

}
