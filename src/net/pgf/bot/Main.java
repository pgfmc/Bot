package net.pgf.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgf.bot.counting.Counting;
import net.pgf.bot.counting.SetCount;
import net.pgf.bot.counting.SetMode;
/*
 * Version: 1.0.0
 */
public class Main extends ListenerAdapter {
	
	public static JDA JDA;
	
	public static String PREFIX = "!";
	
	public static List<String> ADMINS = new ArrayList<>(Arrays.asList("243499063838769152", "440726027405361152")); // bk, Crimson
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(new Secret().getKey()); // bot token, don't share.
		Counting counting = new Counting();
		builder.addEventListeners(new ReadyListener(), counting, new SetCount(counting), new SetMode(counting));
		
		JDA = builder.build();
		JDA.awaitReady();
	}
	
	
	
	public static boolean checkCountingConditions(String cmd, Message msg, boolean admin)
	{
		if (Counting.COUNTING_OPT_OUT.contains(msg.getGuild().getId())) { return false; } // stops this code from running in opt-out servers
		if (msg.getAuthor().isBot()) { return false; } // bots not allowed ;oo
		if (admin) { if (!Main.ADMINS.contains(msg.getAuthor().getId())) { return false; } }// Admins onlyyyy (hard coded in Main, doesn't use guild roles)
		if (!msg.getChannel().getName().equals("counting")) { return false; } // #counting only (any guild not on the opt-out list)
		
		String raw = msg.getContentRaw();
		
		if (!raw.toLowerCase().substring(0, cmd.length() + 2).equals(PREFIX + cmd + " ")) { return false; } // If it doesn't start with "<prefix>setMode "
		
		return true;
	}
}