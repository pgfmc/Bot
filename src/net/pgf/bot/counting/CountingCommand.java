package net.pgf.bot.counting;

import java.util.LinkedList;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgf.bot.Main;

public abstract class CountingCommand implements EventListener {
	
	private Counting counter;
	private String cmd;
	private boolean admin;
	
	public static LinkedList<CountingCommand> COUNTING_COMMAND = new LinkedList<CountingCommand>();
	
	public CountingCommand(String cmd, boolean admin)
	{
		this.cmd = cmd;
		this.admin = admin;
		
		COUNTING_COMMAND.add(this);
	}
	
	public abstract void run(Message msg, String[] args);
	
	public String getCmd()
	{
		return cmd;
	}
	
	public boolean isAdmin()
	{
		return admin;
	}
	
	public Counting getCounter()
	{
		return counter;
	}
	
	@Override
	public void onEvent(GenericEvent e)
	{
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		Message msg = ((MessageReceivedEvent) e).getMessage();
		
		COUNTING_COMMAND.forEach((countCmd) -> {
			if (countCmd.checkCountingConditions(msg))
			{
				counter = Counting.getCounter(msg.getGuild());
				if (msg.getContentRaw().length() < getCmd().length() + 2)
				{
					countCmd.run(msg, null);
				} else
				{
					String args = msg.getContentRaw().toLowerCase().substring(getCmd().length() + 2);
					
					countCmd.run(msg, args.split(" "));
				}
				
			}
		});
	}
	
	public boolean checkCountingConditions(Message msg)
	{
		User user = msg.getAuthor();
		
		if (user.isBot()) { return false; } // bots not allowed ;oo
		if (Counting.COUNTING_OPT_OUT.contains(msg.getGuild().getId())) { return false; } // stops this code from running in opt-out servers
		if (admin) { if (!Main.ADMINS.contains(user.getId())) { return false; } }// Admins onlyyyy (hard coded in Main, doesn't use guild roles)
		if (!msg.getChannel().getName().equals("counting")) { return false; } // #counting only (any guild not on the opt-out list)
		
		String raw = msg.getContentRaw();
		if (msg.getContentRaw().length() < getCmd().length())
		{
			return false;
		}
		if (!raw.toLowerCase().substring(0, cmd.length() + 1).equals(Main.PREFIX + cmd)) { return false; } // If it doesn't start with "<prefix><cmd>"
		
		return true;
	}

}
