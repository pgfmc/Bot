package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.pgf.bot.counting.Counting.Mode;

public class SetMode extends CountingCommand {
	
	public SetMode()
	{
		super("setmode", true);
	}

	@Override
	public void run(Message msg, String[] args) {
		
		for (Mode mode : Mode.values()) // Checks to see if potentialMode is a type Mode
		{
			if (args[0].equals(mode.toString().toLowerCase())) // I did it this way because I didn't want to make the Mode types lowercase...
			{
				getCounter().setMode(mode);
				msg.getChannel().sendMessage(":pencil: Set the mode to " + mode.toString()).queue();
				msg.addReaction("U+1F4DD").queue();
				
				return;
			}
		}
		
		// If it fails to resolve to a type
		msg.getChannel().sendMessage(":no_entry_sign: Failed to resolve to a type, try again").queue();
		msg.addReaction("U+1F6AB").queue();
		
		return;
		
	}

}
