package net.pgf.bot.counting;

import java.util.Random;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class Counting implements EventListener {
	
	public int count = 0;
	
	public enum Mode {
		Normal,
		Doubles,
		Reverse
	};
	
	public Mode mode = Mode.Normal;
	
	public Random r = new Random();
	
	public int nextCount()
	{
		if (mode == Mode.Normal)
		{
			return count + 1;
		}
		
		if (mode == Mode.Doubles)
		{
			return count + 2;
		}
		
		if (mode == Mode.Reverse)
		{
			return count - 1;
		}
		
		return count;
	}
	
	public boolean isNumeric(String strNum)
	{
		if (strNum == null)
		{
			return false;
		}
		
		try {
			Integer num = Integer.valueOf(strNum);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
		
	}
	
	public void onEvent(GenericEvent e)
	{
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		
		if (msg.getAuthor().isBot()) { return; } // bots not allowed ;oo
		if (!msg.getChannel().getName().equals("counting")) { return; } // #counting only (any guild)
		
		String raw = msg.getContentRaw();
		
		if (!isNumeric(raw)) { return; } // Is it not a number?
		
		Integer num = Integer.valueOf(raw);
		
		if (nextCount() == num)
		{
			count = nextCount();
			
			
			// Determines if there is a new mode
			if (r.nextInt(2) == 0)
			{
				int newMode = r.nextInt(3);
				
				if (newMode == 0)
				{
					if (mode != Mode.Normal)
					{
						msg.getChannel().sendMessage("Mode is now Normal").queue();
					}
					mode = Mode.Normal;
				}
				if (newMode == 1)
				{
					if (mode != Mode.Doubles)
					{
						msg.getChannel().sendMessage("Mode is now Doubles").queue();
					}
					mode = Mode.Doubles;
				}
				if (newMode == 2)
				{
					if (mode != Mode.Reverse)
					{
						msg.getChannel().sendMessage("Mode is now Reverse").queue();
					}
					mode = Mode.Reverse;
				}
			}
			
			// Reaction based on determined mode (above)
			if (mode == Mode.Normal)
			{
				msg.addReaction("U+1F522").queue();
			}
			if (mode == Mode.Doubles)
			{
				msg.addReaction("U+0032 U+FE0F U+20E3").queue();
			}
			if (mode == Mode.Reverse)
			{
				msg.addReaction("U+1F53D").queue();
			}
			
			
			
		} else
		{
			msg.getChannel().sendMessage(":no-entry-sign: **" + msg.getAuthor().getName() + "** ruined the count! :no-entry-sign:\n:robot: Expected: " + nextCount() + " (" + mode.toString() + ")").queue();
		}
		
		
		
	}

}
