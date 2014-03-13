package com.github.hoqhuuep.islandcraft.privatemessage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MCommandExecutorTest {
	private MCommandExecutor mCommandExecutor;

	@Mock
	private PrivateMessageManager manager;
	@Mock
	private CommandSender sender;

	@Before
	public void setUp() {
		mCommandExecutor = new MCommandExecutor(manager);
	}

	@Test
	public void testPrivateMessage() {
		final boolean result = mCommandExecutor.onCommand(sender, null, null, new String[] { "hoqhuuep", "hello", "world" });

		verify(manager).sendMessage(sender, "hoqhuuep", "hello world");
		assertEquals(true, result);
	}

	@Test
	public void testNoMessage() {
		final boolean result = mCommandExecutor.onCommand(sender, null, null, new String[] { "hoqhuuep" });

		verify(manager, never()).sendMessage(any(CommandSender.class), anyString(), anyString());
		assertEquals(false, result);
	}

	@Test
	public void testNoPlayer() {
		final boolean result = mCommandExecutor.onCommand(sender, null, null, new String[] {});

		verify(manager, never()).sendMessage(any(CommandSender.class), anyString(), anyString());
		assertEquals(false, result);
	}

	@Test
	public void testShortMessage() {
		final boolean result = mCommandExecutor.onCommand(sender, null, null, new String[] { "hoqhuuep", " " });

		verify(manager).sendMessage(sender, "hoqhuuep", " ");
		assertEquals(true, result);
	}
}
