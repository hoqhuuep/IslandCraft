package com.github.hoqhuuep.islandcraft.compass;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompassManagerTest {
	private CompassManager manager;

	@Mock
	private World world;
	@Mock
	private Player player;
	@Mock
	private CompassDatabase database;

	@Before
	public void setUp() {
		manager = new CompassManager(database);

		when(world.getEnvironment()).thenReturn(Environment.NORMAL);
		when(world.getName()).thenReturn("world");
		when(player.getWorld()).thenReturn(world);
		when(player.isSneaking()).thenReturn(false);
		when(player.hasPermission("islandcraft.compass")).thenReturn(true);

		when(player.getName()).thenReturn("hoqhuuep");
		when(database.loadCompass("hoqhuuep")).thenReturn("Spawn");
		when(database.loadWaypoints("hoqhuuep")).thenReturn(new ArrayList<String>(Arrays.asList("a", "b")));
		when(database.loadWaypoint("hoqhuuep", "a")).thenReturn(new Location(world, 0.0, 0.0, 0.0));
		when(database.loadWaypoint("hoqhuuep", "b")).thenReturn(new Location(world, 0.0, 0.0, 0.0));
	}

	@Test
	public void testCompass() {
		when(database.loadCompass("hoqhuuep")).thenReturn("Spawn", "a", "b");

		manager.onNextWaypoint(player);
		manager.onNextWaypoint(player);
		manager.onNextWaypoint(player);

		final InOrder inOrder = Mockito.inOrder(player, database);
		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(database).loadWaypoint("hoqhuuep", "a");
		inOrder.verify(player).sendMessage(anyString());

		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(database).loadWaypoint("hoqhuuep", "b");
		inOrder.verify(player).sendMessage(anyString());

		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(player).sendMessage(anyString());
	}

	@Test
	public void testCompassSneak() {
		when(player.isSneaking()).thenReturn(true);
		when(database.loadCompass("hoqhuuep")).thenReturn("Spawn", "a", "b");

		manager.onNextWaypoint(player);
		manager.onNextWaypoint(player);
		manager.onNextWaypoint(player);

		final InOrder inOrder = Mockito.inOrder(player, database);
		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(database).loadWaypoint("hoqhuuep", "b");
		inOrder.verify(player).sendMessage(anyString());

		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(player).sendMessage(anyString());

		inOrder.verify(database).loadCompass("hoqhuuep");
		inOrder.verify(database).loadWaypoints("hoqhuuep");
		inOrder.verify(database).loadWaypoint("hoqhuuep", "a");
		inOrder.verify(player).sendMessage(anyString());
	}

	@Test
	public void testCompassWrongWorld() {
		when(world.getEnvironment()).thenReturn(Environment.NETHER, Environment.THE_END);

		manager.onNextWaypoint(player);
		manager.onNextWaypoint(player);

		verify(player, times(2)).sendMessage(anyString());
	}
}
