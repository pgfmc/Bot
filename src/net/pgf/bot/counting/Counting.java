package net.pgf.bot.counting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.pgf.bot.Main;

public class Counting {
	
	public enum Mode {
		Normal,
		Twos,
		Reverse
	};
	
	private Mode mode = Mode.Normal;
	
	private int count = 0;
	
	private Random r = new Random();
	
	private Guild guild;
	private User lastUser;
	
	public static List<String> COUNTING_OPT_OUT = new ArrayList<String>(Arrays.asList("869608974562000917", "689296182387933208")); // The American Pantheon, Lobster Fest
	
	private List<HashMap<Integer, Boolean>> guildCount = new ArrayList<HashMap<Integer, Boolean>>();
	
	private static Map<Guild, Counting> COUNTERS = new HashMap<Guild, Counting>();
	
	//public Map<User, Map<Integer, Boolean>> userCounts = new HashMap<User, Map<Integer, Boolean>>();
	//public Map<Integer, Boolean> guildCounts = new HashMap<Integer, Boolean>();
	
	public Counting(Guild guild)
	{
		this.guild = guild;
	}
	
	public static Counting getCounter(Guild guild)
	{
		if (COUNTERS.containsKey(guild))
		{
			return COUNTERS.get(guild);
		} else
		{
			return new Counting(guild);
		}
	}
	
	public Mode getMode()
	{
		return mode;
	}
	
	public void setMode(Mode mode)
	{
		this.mode = mode;
	}
	
	public Guild getGuild()
	{
		return guild;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setCount(int newCount)
	{
		count = newCount;
	}
	
	public void setLastUser(User lastUser)
	{
		this.lastUser = lastUser;
	}
	
	public void fail(Message msg)
	{
		lastUser = Main.JDA.getSelfUser();
		count = 0;
		mode = Mode.Normal;
		
		String response = ":no_entry_sign: **" + msg.getAuthor().getName() + "** ruined the count! :no_entry_sign:\n:robot: Expected: " + nextCount() + " (" + mode.toString() + ")";
		if (msg.getAuthor().equals(lastUser)) // Cannot count twice in a row
		{
			response += (" (You cannot count twice in a row)");
		}
		response += ("\n\n*Start the count with 1 to begin adventure*");
		
		msg.getChannel().sendMessage(response).queue();
	}
	
	public void win(int nowCount, User user, Message msg)
	{
		lastUser = user;
		count = nowCount;
		
		next(msg);
	}
	
	public static boolean isNumeric(String strNum)
	{
		if (strNum == null)
		{
			return false;
		}
		
		try {
			int testNum = Integer.valueOf(strNum);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
		
	}
	
	public void count(String userCount, User user, Message msg)
	{
		if (COUNTING_OPT_OUT.contains(msg.getGuild().getId())) { return; } // stops this code from running in opt-out servers
		
		if (!isNumeric(userCount))
		{
			return;
		}
		
		if (user.equals(lastUser))
		{
			fail(msg);
			
			return;
		}
		
		
		
		
		
		
		int nowCount = Integer.valueOf(userCount);
		
		if (nowCount == nextCount())
		{
			win(nowCount, user, msg);
			
			return;
		} else
		{
			fail(msg);
			
			return;
		}
	}
	
	public int nextCount()
	{
		if (mode == Mode.Normal)
		{
			return count + 1;
		}
		
		if (mode == Mode.Twos)
		{
			return count + 2;
		}
		
		if (mode == Mode.Reverse)
		{
			return count - 1;
		}
		
		return count;
	}
	
	public void next(Message msg)
	{
		if (r.nextInt(10) == 0)
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
				if (mode != Mode.Twos)
				{
					msg.getChannel().sendMessage("Mode is now Doubles").queue();
				}
				mode = Mode.Twos;
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
		if (mode == Mode.Twos)
		{
			msg.addReaction("U+0032 U+FE0F U+20E3").queue();
		}
		if (mode == Mode.Reverse)
		{
			msg.addReaction("U+1F53D").queue();
		}
	}
	
	

}
