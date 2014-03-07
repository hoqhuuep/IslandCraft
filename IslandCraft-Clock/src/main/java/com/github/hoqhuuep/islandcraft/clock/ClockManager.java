package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class ClockManager {
	private static final long ONE_DAY = 24000;
	private static final long HOURS_PER_DAY = 24;
	private static final long MINUTES_PER_HOUR = 60;
	private static final long ONE_HOUR = ONE_DAY / HOURS_PER_DAY;
	private static final long OFFSET = 6 * ONE_HOUR;
	private final ClockConfig config;

	/**
	 * Creates a <code>ClockManager</code> object.
	 * 
	 * @param config
	 */
	public ClockManager(final ClockConfig config) {
		this.config = config;
	}

	/**
	 * Sends an appropriate message to a player who asks for the time. If they
	 * are in the end or nether they will recieve a warning instead.
	 * 
	 * @param to
	 */
	public void displayTime(final Player to) {
		final World world = to.getWorld();
		final Environment environment = world.getEnvironment();
		if (environment == Environment.NORMAL) {
			final long time = world.getTime();
			final int hour = (int) (((time + OFFSET) % ONE_DAY) / ONE_HOUR);
			final int minute = (int) ((((time + OFFSET) % ONE_HOUR) * MINUTES_PER_HOUR) / ONE_HOUR);
			to.sendMessage(String.format(config.M_CLOCK, hour, minute));
		} else {
			to.sendMessage(config.M_CLOCK_ERROR);
		}
	}
}
