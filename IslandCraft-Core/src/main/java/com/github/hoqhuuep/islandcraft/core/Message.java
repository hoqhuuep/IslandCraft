package com.github.hoqhuuep.islandcraft.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.CommandSender;

public enum Message {
	CLOCK_USE,
	CLOCK_USE_WORLD_ERROR,
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
	ISLAND_UPDATE,
	ISLAND_UPDATE_OCEAN_ERROR,
	ISLAND_UPDATE_WORLD_ERROR,
	LOCAL_MESSAGE,
	PARTY_JOIN,
	PARTY_JOIN_NOTIFY,
	PARTY_LEAVE,
	PARTY_LEAVE_NOTIFY,
	PARTY_MEMBERS,
	PARTY_MESSAGE,
	PARTY_NONE,
	PLAYER_NOT_ONLINE,
	PRIVATE_MESSAGE,
	WAYPOINT_ADD,
	WAYPOINT_ADD_RESERVED_ERROR,
	WAYPOINT_ADD_WORLD_ERROR,
	WAYPOINT_LIST,
	WAYPOINT_NOT_DEFINED,
	WAYPOINT_REMOVE,
	WAYPOINT_REMOVE_RESERVED_ERROR,
	WAYPOINT_SET,
	WAYPOINT_SET_WORLD_ERROR;

	public void send(final CommandSender to, final Object... args) {
		to.sendMessage(messageFormat.format(args));
	}

	public static void setLocale(final Locale locale) {
		final ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
		for (Message message : values()) {
			final String pattern = StringEscapeUtils.unescapeJava(messages.getString(message.toString()));
			message.messageFormat = new MessageFormat(pattern, locale);
		}
	}

	private MessageFormat messageFormat;

	private Message() {
		final Locale locale = Locale.ENGLISH;
		final ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
		final String pattern = messages.getString(toString());
		messageFormat = new MessageFormat(pattern, locale);
	}
}
