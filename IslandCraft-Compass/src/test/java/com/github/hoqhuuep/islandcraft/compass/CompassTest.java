package com.github.hoqhuuep.islandcraft.compass;

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
public class CompassTest {
	private CompassConfig config;
	private CompassListener compassListener;

	@Mock
	private World world;
	@Mock
	private Player player;
	@Mock
	private PlayerInteractEvent playerInteractEvent;
	@Mock
	private CompassDatabase database;

	@Before
	public void setUp() {
		config = new CompassConfig(YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml")));
		compassListener = new CompassListener(new CompassManager(database, config));

		when(world.getEnvironment()).thenReturn(Environment.NORMAL);
		when(player.getWorld()).thenReturn(world);
		when(player.isSneaking()).thenReturn(false);
		when(player.hasPermission("islandcraft.compass")).thenReturn(true);
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(playerInteractEvent.getMaterial()).thenReturn(Material.COMPASS);
		when(playerInteractEvent.getPlayer()).thenReturn(player);

		when(player.getName()).thenReturn("hoqhuuep");
		when(database.loadCompass("hoqhuuep")).thenReturn("Spawn");
	}

	@Test
	public void testCompass() {
		when(playerInteractEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);

		verify(player, times(2)).sendMessage(String.format(config.M_COMPASS, "Spawn"));
	}

	@Test
	public void testWrongItem() {
		when(playerInteractEvent.getMaterial()).thenReturn(Material.WATCH);

		compassListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWrongAction() {
		when(playerInteractEvent.getAction()).thenReturn(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK, Action.PHYSICAL);

		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWithoutPermissions() {
		when(player.hasPermission("islandcraft.compass")).thenReturn(false);

		compassListener.onPlayerInteract(playerInteractEvent);

		verify(player, never()).sendMessage(anyString());
	}

	@Test
	public void testWrongWorld() {
		when(world.getEnvironment()).thenReturn(Environment.NETHER, Environment.THE_END);

		compassListener.onPlayerInteract(playerInteractEvent);
		compassListener.onPlayerInteract(playerInteractEvent);

		verify(player, times(2)).sendMessage(config.M_COMPASS_ERROR);
	}
}
