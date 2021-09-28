package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgf.bot.Main;
import net.pgf.bot.counting.Counting.Mode;

public class SetMode implements EventListener {
	
	public Counting counting;
	
	public String cmd = "setmode";
	
	public SetMode(Counting counting)
	{
		this.counting = counting;
	}
	
	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		if (!Main.checkCountingConditions(cmd, msg, true)) { return; } // Checks for conditions
		
		String potentialMode = msg.getContentRaw().toLowerCase().substring(cmd.length() + 2); // Removes "<prefix>setMode " and just leaves the argument
		
		for (Mode mode : Mode.values()) // Checks to see if potentialMode is a type Mode
		{
			if (potentialMode.equals(mode.toString().toLowerCase())) // I did it this way because I didn't want to make the Mode types lowercase...
			{
				counting.mode = mode;
				msg.getChannel().sendMessage(":pencil: Set the mode to " + mode.toString()).queue();
				msg.addReaction("U+1F4DD").queue();
				
				return;
			}
		}
		
		// If it fails to resolve to a type
		msg.getChannel().sendMessage(":no_entry_sign: Failed to resolve to a type, try again").queue();
		msg.addReaction("U+1F6AB").queue();
		
		return;
		
		// The way I WAS going to do it
		/*
		try {
			Mode mode = Mode.valueOf(potentialMode);
			counting.mode = mode;
			msg.getChannel().sendMessage(":pencil: Set the mode to " + potentialMode).queue();
			msg.addReaction("U+1F4DD").queue();
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			msg.getChannel().sendMessage(":no_entry_sign: Could not resolve to a type of mode").queue();
			msg.addReaction("U+1F6AB").queue();
		}
		*/
		
		
		
	}

}
