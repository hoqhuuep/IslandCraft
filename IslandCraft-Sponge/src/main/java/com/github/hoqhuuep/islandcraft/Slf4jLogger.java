package com.github.hoqhuuep.islandcraft;

import org.slf4j.Logger;

import com.github.hoqhuuep.islandcraft.core.ICLogger;

public class Slf4jLogger extends ICLogger {
	private final Logger logger;

	public Slf4jLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void warning(String message) {
		logger.warn(message);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}
}
