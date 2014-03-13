package com.github.hoqhuuep.islandcraft.localchat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LCommandExecutorTest {
	private LCommandExecutor lCommandExecutor;

	@Mock
	private LocalChatManager manager;
	@Mock
	private Player player;
	@Mock
	private CommandSender otherCommandSender;

	@Before
	public void setUp() {
		lCommandExecutor = new LCommandExecutor(manager);
	}

	@Test
	public void testPrivateMessage() {
		final boolean result = lCommandExecutor.onCommand(player, null, null, new String[] { "hello", "world" });

		verify(manager).sendLocalMessage(player, "hello world");
		assertEquals(true, result);
	}

	@Test
	public void testNoMessage() {
		final boolean result = lCommandExecutor.onCommand(player, null, null, new String[] {});

		verify(manager, never()).sendLocalMessage(any(Player.class), anyString());
		assertEquals(false, result);
	}

	@Test
	public void testShortMessage() {
		final boolean result = lCommandExecutor.onCommand(player, null, null, new String[] { " " });

		verify(manager).sendLocalMessage(player, " ");
		assertEquals(true, result);
	}

	@Test
	public void testOtherCommandSender() {
		final boolean result = lCommandExecutor.onCommand(otherCommandSender, null, null, new String[] { " " });

		verify(manager, never()).sendLocalMessage(any(Player.class), anyString());
		assertEquals(true, result);
	}
}
