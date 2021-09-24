package net.pgf.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgf.bot.counting.Counting;
import net.pgf.bot.counting.SetCount;
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
		builder.addEventListeners(new ReadyListener(), counting, new SetCount(counting));
		
		JDA = builder.build();
		JDA.awaitReady();
	}
}