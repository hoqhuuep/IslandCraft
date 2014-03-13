package com.github.hoqhuuep.islandcraft.suicide;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuicideManagerTest {
	private SuicideManager suicideManager;

	@Mock
	private Player player;

	@Before
	public void setUp() {
		suicideManager = new SuicideManager();
	}

	@Test
	public void testSuicide() {
		suicideManager.suicide(player);

		verify(player).setHealth(0.0);
		verify(player, never()).sendMessage(anyString());
	}
}
