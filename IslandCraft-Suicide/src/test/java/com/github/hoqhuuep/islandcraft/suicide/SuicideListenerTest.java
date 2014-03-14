package com.github.hoqhuuep.islandcraft.suicide;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuicideListenerTest {
	private SuicideCommandExecutor suicideCommandExecutor;

	@Mock
	private SuicideManager manager;
	@Mock
	private Player player;
	@Mock
	private ConsoleCommandSender consoleCommandSender;

	@Before
	public void setUp() {
		suicideCommandExecutor = new SuicideCommandExecutor(manager);
	}

	@Test
	public void testSuicide() {
		final boolean result = suicideCommandExecutor.onCommand(player, null, null, new String[] {});

		verify(manager).suicide(player);
		verify(player, never()).sendMessage(anyString());
		assertEquals(true, result);
	}

	@Test
	public void testArgs() {
		final boolean result = suicideCommandExecutor.onCommand(player, null, null, new String[] { "arg" });

		verify(manager, never()).suicide(any(Player.class));
		verify(player, never()).sendMessage(anyString());
		assertEquals(false, result);
	}

	@Test
	public void testConsole() {
		final boolean result = suicideCommandExecutor.onCommand(consoleCommandSender, null, null, new String[] {});

		verify(manager, never()).suicide(any(Player.class));
		verify(consoleCommandSender).sendMessage(anyString());
		assertEquals(true, result);
	}
}
