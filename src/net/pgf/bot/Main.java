package net.pgf.bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {
	
	public static JDA JDA;
	
	public static void main(String[] args) throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(new Secret().getKey()); // bot token, don't share.
		builder.addEventListeners(new ReadyListener(), new Counting());
		
		JDA = builder.build();
		JDA.awaitReady();
	}
}