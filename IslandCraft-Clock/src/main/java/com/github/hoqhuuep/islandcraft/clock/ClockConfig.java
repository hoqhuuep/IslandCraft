package com.github.hoqhuuep.islandcraft.clock;

import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Daniel Simmons
 * @version 2014-03-07
 */
public class ClockConfig {
	/**
	 * Message sent to player when they right click with a clock. Parameters:
	 * <code>hour</code>, <code>minute</code>.
	 */
	public final String M_CLOCK;

	/**
	 * Message sent to player when they right click with a clock in the nether
	 * or end. No parameters.
	 */
	public final String M_CLOCK_ERROR;

	/**
	 * Creates a <code>ClockConfig</code> object.
	 * 
	 * @param config
	 *            a bukkit configuration section. Usually obtained by calling
	 *            <code>plugin.getConfig()</code>. Must contain the right keys.
	 */
	public ClockConfig(final ConfigurationSection config) {
		final ConfigurationSection message = config.getConfigurationSection("message");
		M_CLOCK = message.getString("clock");
		M_CLOCK_ERROR = message.getString("clock-error");
	}
}
