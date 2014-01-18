package com.github.hoqhuuep.islandcraft.partychat;

import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;

public class PartyChatPlugin extends JavaPlugin {
	private PartyChatManager partyChatManager;

	public PartyChatManager getPartyChatManager() {
		return partyChatManager;
	}

	@Override
	public void onEnable() {
		// Hack to ensure database exists
		try {
			getDatabase().find(PartyBean.class).findRowCount();
		} catch (PersistenceException e) {
			installDDL();
		}
		final EbeanServer ebean = getDatabase();
		final PartyDatabase database = new PartyDatabase(ebean);
		partyChatManager = new PartyChatManager(database);

		final CommandExecutor pCommandExecutor = new PCommandExecutor(
				partyChatManager);
		getCommand("p").setExecutor(pCommandExecutor);

		final PartyCommandExecutor partyCommandExecutor = new PartyCommandExecutor(
				partyChatManager);
		final PluginCommand partyCommand = getCommand("party");
		partyCommand.setExecutor(partyCommandExecutor);
		partyCommand.setTabCompleter(partyCommandExecutor);
	}

	@Override
	public List<Class<?>> getDatabaseClasses() {
		return PartyDatabase.getDatabaseClasses();
	}
}
