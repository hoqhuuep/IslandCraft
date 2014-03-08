package com.github.hoqhuuep.islandcraft.clock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
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
public class ClockTest {
	private ClockConfig config;
	private ClockListener clockListener;

	@Mock
	private World world;
	@Mock
	private Player player;
	@Mock
	private PlayerInteractEvent playerInteractEvent;

	@Before
	public void setUp() {
		config = new ClockConfig(YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml")));
		clockListener = new ClockListener(new ClockManager(config));

		when(world.getEnvironment()).thenReturn(Environment.NORMAL);
		when(world.getTime()).thenReturn(12345L);
		when(player.hasPermission("islandcraft.clock")).thenReturn(true);
		when(player.getWorld()).thenReturn(world);
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(playerInteractEvent.getMaterial()).thenReturn(Material.WATCH);
		when(playerInteractEvent.getPlayer()).thenReturn(player);
	}

	@Test
	public void testClock() {
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);

		verify(player, times(2)).sendMessage(String.format(config.M_CLOCK, 18, 20));
	}

	@Test
	public void testWrongItem() {
		when(playerInteractEvent.getMaterial()).thenReturn(Material.COMPASS);

		clockListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWrongAction() {
		when(playerInteractEvent.getAction()).thenReturn(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.PHYSICAL);

		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWithoutPermissions() {
		when(player.hasPermission("islandcraft.clock")).thenReturn(false);

		clockListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWrongWorld() {
		when(world.getEnvironment()).thenReturn(Environment.NETHER, Environment.THE_END);

		clockListener.onPlayerInteract(playerInteractEvent);
		clockListener.onPlayerInteract(playerInteractEvent);

		verify(player, times(2)).sendMessage(config.M_CLOCK_ERROR);
	}
}
