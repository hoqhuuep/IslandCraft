package com.github.hoqhuuep.islandcraft.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.CommandSender;

public enum Message {
	ISLAND_ABANDON,
	ISLAND_ABANDON_OCEAN_ERROR,
	ISLAND_ABANDON_OWNER_ERROR,
	ISLAND_ABANDON_WORLD_ERROR,
	ISLAND_ENTER_ABANDONED,
	ISLAND_ENTER_NEW,
	ISLAND_ENTER_PRIVATE,
	ISLAND_ENTER_REPOSSESSED,
	ISLAND_ENTER_RESERVED,
	ISLAND_ENTER_RESOURCE,
	ISLAND_EXAMINE_ABANDONED,
	ISLAND_EXAMINE_NEW,
	ISLAND_EXAMINE_OCEAN_ERROR,
	ISLAND_EXAMINE_PRIVATE,
	ISLAND_EXAMINE_REPOSSESSED,
	ISLAND_EXAMINE_RESERVED,
	ISLAND_EXAMINE_RESOURCE,
	ISLAND_EXAMINE_WORLD_ERROR,
	ISLAND_LEAVE_ABANDONED,
	ISLAND_LEAVE_NEW,
	ISLAND_LEAVE_PRIVATE,
	ISLAND_LEAVE_REPOSSESSED,
	ISLAND_LEAVE_RESERVED,
	ISLAND_LEAVE_RESOURCE,
	ISLAND_PURCHASE,
	ISLAND_PURCHASE_FUNDS_ERROR,
	ISLAND_PURCHASE_MAX_ERROR,
	ISLAND_PURCHASE_OCEAN_ERROR,
	ISLAND_PURCHASE_OTHER_ERROR,
	ISLAND_PURCHASE_RESERVED_ERROR,
	ISLAND_PURCHASE_RESOURCE_ERROR,
	ISLAND_PURCHASE_SELF_ERROR,
	ISLAND_PURCHASE_WORLD_ERROR,
	ISLAND_RENAME,
	ISLAND_RENAME_OCEAN_ERROR,
	ISLAND_RENAME_OWNER_ERROR,
	ISLAND_RENAME_WORLD_ERROR,
	ISLAND_TAX,
	ISLAND_TAX_FUNDS_ERROR,
	ISLAND_TAX_MAX_ERROR,
	ISLAND_TAX_OCEAN_ERROR,
	ISLAND_TAX_OWNER_ERROR,
	ISLAND_TAX_WORLD_ERROR,
	ISLAND_TITLE_ABANDONED,
	ISLAND_TITLE_NEW,
	ISLAND_TITLE_PRIVATE,
	ISLAND_TITLE_REPOSSESSED,
	ISLAND_TITLE_RESERVED,
	ISLAND_TITLE_RESOURCE,
	ISLAND_TITLE_SPAWN,
	ISLAND_UPDATE,
	ISLAND_UPDATE_OCEAN_ERROR,
	ISLAND_UPDATE_WORLD_ERROR,
	NOT_PLAYER_ERROR;

	public void send(final CommandSender to, final Object... args) {
		to.sendMessage(format(args));
	}

	public String format(final Object... args) {
		return messageFormat.format(args);
	}

	public static void setBundle(final ResourceBundle bundle) {
		for (Message message : values()) {
			final String pattern = StringEscapeUtils.unescapeJava(bundle.getString(message.toString()));
			message.messageFormat = new MessageFormat(pattern, bundle.getLocale());
		}
	}

	private MessageFormat messageFormat;

	private Message() {
		final Locale locale = Locale.getDefault();
		final ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
		final String pattern = messages.getString(toString());
		messageFormat = new MessageFormat(pattern, locale);
	}
}
