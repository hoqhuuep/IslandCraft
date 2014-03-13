package com.github.hoqhuuep.islandcraft.localchat;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocalChatManagerTest {
	private LocalChatManager manager;
	private LocalChatConfig config;

	@Mock
	private Player sender;
	@Mock
	private Player near;
	@Mock
	private Player far;
	@Mock
	private World world;

	@Before
	public void setUp() {
		config = new LocalChatConfig(YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml")));
		manager = new LocalChatManager(config);

		final Location senderLocation = new Location(world, 0, 0, 0);
		final Location nearLocation = new Location(world, config.LOCAL_CHAT_RADIUS, 0, 0);
		final Location farLocation = new Location(world, 0, 0, config.LOCAL_CHAT_RADIUS + 1);

		when(sender.getName()).thenReturn("sender");
		when(near.getName()).thenReturn("near");
		when(far.getName()).thenReturn("far");
		when(sender.getLocation()).thenReturn(senderLocation);
		when(near.getLocation()).thenReturn(nearLocation);
		when(far.getLocation()).thenReturn(farLocation);
		when(sender.getWorld()).thenReturn(world);
		when(near.getWorld()).thenReturn(world);
		when(far.getWorld()).thenReturn(world);
		when(world.getPlayers()).thenReturn(Arrays.asList(new Player[] { sender, near, far }));
	}

	@Test
	public void testLocalMessage() {
		manager.sendLocalMessage(sender, "hello world");

		verify(sender).sendMessage(String.format(config.M_L, "sender", "hello world"));
		verify(near).sendMessage(String.format(config.M_L, "sender", "hello world"));
		verify(far, never()).sendMessage(anyString());
	}

	@Test
	public void testNoRecieve() {
		manager.sendLocalMessage(far, "hello world");

		verify(sender, never()).sendMessage(anyString());
		verify(far).sendMessage(String.format(config.M_L, "far", "hello world"));
		verify(near, never()).sendMessage(anyString());
	}
}
