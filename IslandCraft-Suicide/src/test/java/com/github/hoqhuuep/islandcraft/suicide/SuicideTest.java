package com.github.hoqhuuep.islandcraft.suicide;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerInteractEvent.class)
public class SuicideTest {
	private SuicideCommandExecutor suicideCommandExecutor;
	@Mock
	private Player player;
	@Mock
	private ConsoleCommandSender consoleCommandSender;

	@Before
	public void setUp() {
		suicideCommandExecutor = new SuicideCommandExecutor(new SuicideManager());
	}

	@Test
	public void testSuicide() {
		final boolean result = suicideCommandExecutor.onCommand(player, null, "suicide", new String[] {});

		verify(player).setHealth(0.0);
		verify(player, never()).sendMessage(anyString());
		assertEquals(true, result);
	}

	@Test
	public void testArgs() {
		final boolean result = suicideCommandExecutor.onCommand(player, null, "suicide", new String[] { "arg" });

		verify(player, never()).setHealth(anyDouble());
		verify(player, never()).sendMessage(anyString());
		assertEquals(false, result);
	}

	@Test
	public void testConsole() {
		final boolean result = suicideCommandExecutor.onCommand(consoleCommandSender, null, "suicide", new String[] {});

		verify(consoleCommandSender).sendMessage(anyString());
		assertEquals(true, result);
	}
}
