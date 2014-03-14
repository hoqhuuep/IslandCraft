package com.github.hoqhuuep.islandcraft.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.plugin.java.JavaPlugin;

public class IslandCraft extends JavaPlugin {
	@Override
	public void onEnable() {
		final String[] files = { "messages_en.properties" };
		for (String name : files) {
			final File file = new File(getDataFolder(), name);
			if (!file.exists()) {
				saveResource("messages_en.properties", false);
			}
		}
		try {
			final URL[] urls = { getDataFolder().toURI().toURL() };
			final ClassLoader loader = new URLClassLoader(urls);
			final ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault(), loader);
			Message.setBundle(messages);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
