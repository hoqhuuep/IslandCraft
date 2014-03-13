package com.github.hoqhuuep.islandcraft.clock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClockManagerTest {
	private ClockConfig config;
	private ClockManager clockManager;

	@Mock
	private World world;
	@Mock
	private Player player;

	@Before
	public void setUp() {
		config = new ClockConfig(YamlConfiguration.loadConfiguration(new File("src/main/resources/config.yml")));
		clockManager = new ClockManager(config);

		when(world.getEnvironment()).thenReturn(Environment.NORMAL);
		when(world.getTime()).thenReturn(12345L);
		when(player.getWorld()).thenReturn(world);
	}

	@Test
	public void testClock() {
		clockManager.displayTime(player);

		verify(player).sendMessage(String.format(config.M_CLOCK, 18, 20));
	}

	@Test
	public void testWrongWorld() {
		when(world.getEnvironment()).thenReturn(Environment.NETHER, Environment.THE_END);

		clockManager.displayTime(player);
		clockManager.displayTime(player);

		verify(player, times(2)).sendMessage(String.format(config.M_CLOCK_ERROR));
	}
}
