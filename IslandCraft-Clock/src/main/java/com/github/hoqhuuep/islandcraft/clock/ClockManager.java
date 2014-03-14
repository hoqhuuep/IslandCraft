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
	private static final double ONE_DAY = 24000.0;
	private static final double HOURS_PER_DAY = 24.0;
	private static final double MINUTES_PER_HOUR = 60.0;
	private static final double SECONDS_PER_MINUTE = 60.0;
	private static final double MINUTES_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR;
	private static final double ONE_HOUR = ONE_DAY / HOURS_PER_DAY;
	private static final double ONE_MINUTE = ONE_DAY / MINUTES_PER_DAY;
	private static final double OFFSET = 6.0 * ONE_HOUR;

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
			final int hour = (int) Math.floor(((time + OFFSET) % ONE_DAY) / ONE_HOUR);
			final int minute = (int) Math.floor((((time + OFFSET) % ONE_HOUR) * MINUTES_PER_HOUR) / ONE_HOUR);
			final int second = (int) Math.floor((((time + OFFSET) % ONE_MINUTE) * SECONDS_PER_MINUTE) / ONE_MINUTE);
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
