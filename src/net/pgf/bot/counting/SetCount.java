package net.pgf.bot.counting;

import net.dv8tion.jda.api.entities.Message;
import net.pgf.bot.Main;

public class SetCount extends CountingCommand {
	
	public SetCount()
	{
		super("setcount", true);
	}
	
	@Override
	public void run(Message msg, String[] args) {
		
		if (!Counting.isNumeric(args[0])) // not a number
		{
			msg.getChannel().sendMessage(":no_entry_sign: Failed to resolve to a type, try again").queue();
			msg.addReaction("U+1F6AB").queue();
			
			return;
		}
		
		int newCount = Integer.valueOf(args[0]);
		
		getCounter().setCount(newCount);
		getCounter().setLastUser(Main.JDA.getSelfUser());
		
		msg.getChannel().sendMessage(":pencil: Set the count to " + newCount + " (" + getCounter().getMode().toString() + ")").queue();
		msg.addReaction("U+1F4DD").queue();
	}

}
