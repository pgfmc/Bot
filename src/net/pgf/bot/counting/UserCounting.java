package net.pgf.bot.counting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.User;

public class UserCounting {
	
	public static Map<User, UserCounting> USERCOUNTERS = new HashMap<User, UserCounting>();
	
	public User user;
	
	private List<HashMap<Integer, Boolean>> userCount = new ArrayList<HashMap<Integer, Boolean>>();
	
	public UserCounting(User user)
	{
		this.user = user;
	}

	public static UserCounting getCoutner(User user)
	{
		if (USERCOUNTERS.containsKey(user))
		{
			return USERCOUNTERS.get(user);
		} else
		{
			return new UserCounting(user);
		}
	}
	
	public void count(int count, boolean correct)
	{
		HashMap<Integer, Boolean> thisCount = new HashMap<Integer, Boolean>();
		thisCount.put(count, correct);
		userCount.add(thisCount);
	}
	
	public int getCorrect()
	{
		// TODO
		return 0;
	}
	
	public int getIncorrect()
	{
		// TODO
		return 0;
	}
	
	public int getTotal()
	{
		return getCorrect() + getIncorrect();
	}
	
	public double getRatio()
	{
		return getCorrect() / getTotal();
	}

}
