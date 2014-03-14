package com.github.hoqhuuep.islandcraft.clock;

import java.util.Calendar;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.hoqhuuep.islandcraft.core.Message;

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
			final int second = 0; // TODO calculate seconds
			final Calendar date = Calendar.getInstance();
			date.set(Calendar.HOUR_OF_DAY, hour);
			date.set(Calendar.MINUTE, minute);
			date.set(Calendar.SECOND, second);
			Message.CLOCK_USE.send(to, date.getTime());
		} else {
			Message.CLOCK_USE_WORLD_ERROR.send(to);
		}
	}
}
