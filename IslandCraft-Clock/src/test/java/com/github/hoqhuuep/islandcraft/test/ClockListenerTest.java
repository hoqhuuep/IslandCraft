package com.github.hoqhuuep.islandcraft.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.hoqhuuep.islandcraft.clock.ClockConfig;
import com.github.hoqhuuep.islandcraft.clock.ClockListener;
import com.github.hoqhuuep.islandcraft.clock.ClockManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerInteractEvent.class)
public class ClockListenerTest {
	private final ConfigurationSection config = YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml"));
	private final ClockListener clockListener = new ClockListener(new ClockManager(new ClockConfig(config)));

	@Test
	public void testRightClick() {
		final World mockWorld = mock(World.class);
		when(mockWorld.getEnvironment()).thenReturn(Environment.NORMAL);

		final Player mockPlayer = mock(Player.class);
		when(mockPlayer.hasPermission("islandcraft.clock")).thenReturn(true);
		when(mockPlayer.getWorld()).thenReturn(mockWorld);

		final PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
		when(mockEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(mockEvent.getMaterial()).thenReturn(Material.WATCH);
		when(mockEvent.getPlayer()).thenReturn(mockPlayer);

		clockListener.onPlayerInteract(mockEvent);
		verify(mockPlayer).sendMessage(contains("§7[-]"));
	}

	@Test
	public void testRightClickWithoutClock() {
		final World mockWorld = mock(World.class);
		when(mockWorld.getEnvironment()).thenReturn(Environment.NORMAL);

		final Player mockPlayer = mock(Player.class);
		when(mockPlayer.hasPermission("islandcraft.clock")).thenReturn(true);
		when(mockPlayer.getWorld()).thenReturn(mockWorld);

		final PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
		when(mockEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(mockEvent.getMaterial()).thenReturn(Material.AIR);
		when(mockEvent.getPlayer()).thenReturn(mockPlayer);

		clockListener.onPlayerInteract(mockEvent);
		verify(mockPlayer, never()).sendMessage(anyString());
	}

	@Test
	public void testLeftClick() {
		final World mockWorld = mock(World.class);
		when(mockWorld.getEnvironment()).thenReturn(Environment.NORMAL);

		final Player mockPlayer = mock(Player.class);
		when(mockPlayer.hasPermission("islandcraft.clock")).thenReturn(true);
		when(mockPlayer.getWorld()).thenReturn(mockWorld);

		final PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
		when(mockEvent.getAction()).thenReturn(Action.LEFT_CLICK_AIR);
		when(mockEvent.getMaterial()).thenReturn(Material.WATCH);
		when(mockEvent.getPlayer()).thenReturn(mockPlayer);

		clockListener.onPlayerInteract(mockEvent);
		verify(mockPlayer, never()).sendMessage(anyString());
	}

	@Test
	public void testRightClickWithoutPermissions() {
		final World mockWorld = mock(World.class);
		when(mockWorld.getEnvironment()).thenReturn(Environment.NORMAL);

		final Player mockPlayer = mock(Player.class);
		when(mockPlayer.hasPermission("islandcraft.clock")).thenReturn(false);
		when(mockPlayer.getWorld()).thenReturn(mockWorld);

		final PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
		when(mockEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(mockEvent.getMaterial()).thenReturn(Material.WATCH);
		when(mockEvent.getPlayer()).thenReturn(mockPlayer);

		clockListener.onPlayerInteract(mockEvent);
		verify(mockPlayer, never()).sendMessage(anyString());
	}

	@Test
	public void testRightClickInNether() {
		final World mockWorld = mock(World.class);
		when(mockWorld.getEnvironment()).thenReturn(Environment.NETHER);

		final Player mockPlayer = mock(Player.class);
		when(mockPlayer.hasPermission("islandcraft.clock")).thenReturn(true);
		when(mockPlayer.getWorld()).thenReturn(mockWorld);

		final PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);
		when(mockEvent.getAction()).thenReturn(Action.RIGHT_CLICK_AIR);
		when(mockEvent.getMaterial()).thenReturn(Material.WATCH);
		when(mockEvent.getPlayer()).thenReturn(mockPlayer);

		clockListener.onPlayerInteract(mockEvent);
		verify(mockPlayer).sendMessage(contains("§c[-]"));
	}
}
