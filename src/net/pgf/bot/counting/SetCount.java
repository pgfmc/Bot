package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgf.bot.Main;

public class SetCount implements EventListener {
	
	public Counting counting;
	
	public String cmd = "setcount";
	
	public SetCount(Counting counting)
	{
		this.counting = counting;
	}
	
	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		if (!Main.checkCountingConditions(cmd, msg, true)) { return; } // Checks for conditions
		
		String potentialNum = msg.getContentRaw().toLowerCase().substring(cmd.length() + 2); // Removes "<prefix>setCount " and just leaves the argument
		
		if (!counting.isNumeric(potentialNum)) // not a number
		{
			msg.getChannel().sendMessage(":no_entry_sign: Failed to resolve to a type, try again").queue();
			msg.addReaction("U+1F6AB").queue();
			
			return;
		}
		
		counting.count = Integer.valueOf(potentialNum);
		msg.getChannel().sendMessage(":pencil: Set the count to " + potentialNum + " (" + counting.mode.toString() + ")").queue();
		msg.addReaction("U+1F4DD").queue();
	}

}
