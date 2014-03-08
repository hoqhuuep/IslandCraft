package com.github.hoqhuuep.islandcraft.clock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerInteractEvent.class)
public class ClockListenerTest {
	private ClockListener clockListener;

	@Mock
	private ClockManager manager;
	@Mock
	private Player player;
	@Mock
	private PlayerInteractEvent playerInteractEvent;

	@Before
	public void setUp() {
		clockListener = new ClockListener(manager);

		when(player.hasPermission("islandcraft.clock")).thenReturn(true);
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(playerInteractEvent.getMaterial()).thenReturn(Material.WATCH);
		when(playerInteractEvent.getPlayer()).thenReturn(player);
	}

	@Test
	public void testClock() {
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);

		verify(manager, times(2)).displayTime(player);
	}

	@Test
	public void testWrongItem() {
		when(playerInteractEvent.getMaterial()).thenReturn(Material.COMPASS);

		clockListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).displayTime(any(Player.class));
	}

	@Test
	public void testWrongAction() {
		when(playerInteractEvent.getAction()).thenReturn(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.PHYSICAL);

		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).displayTime(any(Player.class));
	}

	@Test
	public void testWithoutPermissions() {
		when(player.hasPermission("islandcraft.clock")).thenReturn(false);

		clockListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).displayTime(any(Player.class));
	}
}
