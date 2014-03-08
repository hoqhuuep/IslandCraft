package com.github.hoqhuuep.islandcraft.compass;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.World;
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
public class CompassListenerTest {
	private CompassListener compassListener;

	@Mock
	private CompassManager manager;
	@Mock
	private World world;
	@Mock
	private Player player;
	@Mock
	private PlayerInteractEvent playerInteractEvent;

	@Before
	public void setUp() {
		compassListener = new CompassListener(manager);

		when(player.hasPermission("islandcraft.compass")).thenReturn(true);
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(playerInteractEvent.getMaterial()).thenReturn(Material.COMPASS);
		when(playerInteractEvent.getPlayer()).thenReturn(player);
	}

	@Test
	public void testCompass() {
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);

		verify(manager, times(2)).onNextWaypoint(player);
	}

	@Test
	public void testWrongItem() {
		when(playerInteractEvent.getMaterial()).thenReturn(Material.WATCH);

		compassListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).onNextWaypoint(any(Player.class));
	}

	@Test
	public void testWrongAction() {
		when(playerInteractEvent.getAction()).thenReturn(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.PHYSICAL);

		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).onNextWaypoint(any(Player.class));
	}

	@Test
	public void testWithoutPermissions() {
		when(player.hasPermission("islandcraft.compass")).thenReturn(false);

		compassListener.onPlayerInteract(playerInteractEvent);

		verify(manager, never()).onNextWaypoint(any(Player.class));
	}
}
