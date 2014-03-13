package com.github.hoqhuuep.islandcraft.privatemessage;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrivateMessageManagerTest {
	private PrivateMessageManager manager;
	private PrivateMessageConfig config;

	@Mock
	private Player player1;
	@Mock
	private Player player2;
	@Mock
	private Server server;
	@Mock
	private ConsoleCommandSender console;

	@Before
	public void setUp() {
		config = new PrivateMessageConfig(YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml")));
		manager = new PrivateMessageManager(config);

		when(player1.getName()).thenReturn("hoqhuuep");
		when(player2.getName()).thenReturn("hoqhuuep_prime");
		when(console.getName()).thenReturn("CONSOLE");
		when(server.getConsoleSender()).thenReturn(console);
		when(server.getPlayerExact(player1.getName())).thenReturn(player1);
		when(server.getPlayerExact(player2.getName())).thenReturn(player2);
		when(player1.getServer()).thenReturn(server);
		when(player2.getServer()).thenReturn(server);
		when(console.getServer()).thenReturn(server);
	}

	@Test
	public void testSendMessage() {
		manager.sendMessage(player1, player2.getName(), "hello world");

		verify(player1).sendMessage(String.format(config.M_M, "hoqhuuep", "hoqhuuep_prime", "hello world"));
		verify(player2).sendMessage(String.format(config.M_M, "hoqhuuep", "hoqhuuep_prime", "hello world"));
	}

	@Test
	public void testUnknownPlayer() {
		manager.sendMessage(player1, "unknown_player", "hello world");

		verify(player1).sendMessage(config.M_M_ERROR);
		verify(player2, never()).sendMessage(anyString());
	}

	@Test
	public void testConsole() {
		manager.sendMessage(console, "hoqhuuep", "hello world");

		verify(console).sendMessage(String.format(config.M_M, "CONSOLE", "hoqhuuep", "hello world"));
		verify(player1).sendMessage(String.format(config.M_M, "CONSOLE", "hoqhuuep", "hello world"));
	}
}
