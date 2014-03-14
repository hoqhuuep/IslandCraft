package com.github.hoqhuuep.islandcraft.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.bukkit.command.CommandSender;

public class Message {
	private final MessageFormat messageFormat;

	public Message(final String pattern, final Locale locale) {
		messageFormat = new MessageFormat(pattern, locale);
	}

	public void send(final CommandSender to, final Object... args) {
		to.sendMessage(messageFormat.format(args));
	}

	public static Message CLOCK_USE;
	public static Message CLOCK_USE_WORLD_ERROR;
	public static Message ISLAND_ABANDON;
	public static Message ISLAND_ABANDON_OCEAN_ERROR;
	public static Message ISLAND_ABANDON_OWNER_ERROR;
	public static Message ISLAND_ABANDON_WORLD_ERROR;
	public static Message ISLAND_ENTER_ABANDONED;
	public static Message ISLAND_ENTER_NEW;
	public static Message ISLAND_ENTER_PRIVATE;
	public static Message ISLAND_ENTER_REPOSSESSED;
	public static Message ISLAND_ENTER_RESERVED;
	public static Message ISLAND_ENTER_RESOURCE;
	public static Message ISLAND_EXAMINE_OCEAN_ERROR;
	public static Message ISLAND_EXAMINE_WORLD_ERROR;
	public static Message ISLAND_LEAVE_ABANDONED;
	public static Message ISLAND_LEAVE_NEW;
	public static Message ISLAND_LEAVE_PRIVATE;
	public static Message ISLAND_LEAVE_REPOSSESSED;
	public static Message ISLAND_LEAVE_RESERVED;
	public static Message ISLAND_LEAVE_RESOURCE;
	public static Message ISLAND_PURCHASE;
	public static Message ISLAND_PURCHASE_FUNDS_ERROR;
	public static Message ISLAND_PURCHASE_MAX_ERROR;
	public static Message ISLAND_PURCHASE_OCEAN_ERROR;
	public static Message ISLAND_PURCHASE_OTHER_ERROR;
	public static Message ISLAND_PURCHASE_RESERVED_ERROR;
	public static Message ISLAND_PURCHASE_RESOURCE_ERROR;
	public static Message ISLAND_PURCHASE_SELF_ERROR;
	public static Message ISLAND_PURCHASE_WORLD_ERROR;
	public static Message ISLAND_RENAME;
	public static Message ISLAND_RENAME_OCEAN_ERROR;
	public static Message ISLAND_RENAME_OWNER_ERROR;
	public static Message ISLAND_RENAME_WORLD_ERROR;
	public static Message ISLAND_TAX;
	public static Message ISLAND_TAX_FUNDS_ERROR;
	public static Message ISLAND_TAX_MAX_ERROR;
	public static Message ISLAND_TAX_OCEAN_ERROR;
	public static Message ISLAND_TAX_OWNER_ERROR;
	public static Message ISLAND_TAX_WORLD_ERROR;
	public static Message ISLAND_UPDATE;
	public static Message ISLAND_UPDATE_OCEAN_ERROR;
	public static Message ISLAND_UPDATE_WORLD_ERROR;
	public static Message LOCAL_MESSAGE;
	public static Message PARTY_JOIN;
	public static Message PARTY_JOIN_NOTIFY;
	public static Message PARTY_LEAVE;
	public static Message PARTY_LEAVE_NOTIFY;
	public static Message PARTY_MEMBERS;
	public static Message PARTY_MESSAGE;
	public static Message PARTY_NONE;
	public static Message PLAYER_NOT_ONLINE;
	public static Message PRIVATE_MESSAGE;
	public static Message WAYPOINT_ADD;
	public static Message WAYPOINT_ADD_RESERVED_ERROR;
	public static Message WAYPOINT_ADD_WORLD_ERROR;
	public static Message WAYPOINT_LIST;
	public static Message WAYPOINT_NOT_DEFINED;
	public static Message WAYPOINT_REMOVE;
	public static Message WAYPOINT_REMOVE_RESERVED_ERROR;
	public static Message WAYPOINT_SET;
	public static Message WAYPOINT_SET_WORLD_ERROR;

	static {
		setLocale(Locale.ENGLISH);
	}

	public static void setLocale(final Locale locale) {
		final ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
		CLOCK_USE = new Message(messages.getString("CLOCK_USE"), locale);
		CLOCK_USE_WORLD_ERROR = new Message(messages.getString("CLOCK_USE_WORLD_ERROR"), locale);
		ISLAND_ABANDON = new Message(messages.getString("ISLAND_ABANDON"), locale);
		ISLAND_ABANDON_OCEAN_ERROR = new Message(messages.getString("ISLAND_ABANDON_OCEAN_ERROR"), locale);
		ISLAND_ABANDON_OWNER_ERROR = new Message(messages.getString("ISLAND_ABANDON_OWNER_ERROR"), locale);
		ISLAND_ABANDON_WORLD_ERROR = new Message(messages.getString("ISLAND_ABANDON_WORLD_ERROR"), locale);
		ISLAND_ENTER_ABANDONED = new Message(messages.getString("ISLAND_ENTER_ABANDONED"), locale);
		ISLAND_ENTER_NEW = new Message(messages.getString("ISLAND_ENTER_NEW"), locale);
		ISLAND_ENTER_PRIVATE = new Message(messages.getString("ISLAND_ENTER_PRIVATE"), locale);
		ISLAND_ENTER_REPOSSESSED = new Message(messages.getString("ISLAND_ENTER_REPOSSESSED"), locale);
		ISLAND_ENTER_RESERVED = new Message(messages.getString("ISLAND_ENTER_RESERVED"), locale);
		ISLAND_ENTER_RESOURCE = new Message(messages.getString("ISLAND_ENTER_RESOURCE"), locale);
		ISLAND_EXAMINE_OCEAN_ERROR = new Message(messages.getString("ISLAND_EXAMINE_OCEAN_ERROR"), locale);
		ISLAND_EXAMINE_WORLD_ERROR = new Message(messages.getString("ISLAND_EXAMINE_WORLD_ERROR"), locale);
		ISLAND_LEAVE_ABANDONED = new Message(messages.getString("ISLAND_LEAVE_ABANDONED"), locale);
		ISLAND_LEAVE_NEW = new Message(messages.getString("ISLAND_LEAVE_NEW"), locale);
		ISLAND_LEAVE_PRIVATE = new Message(messages.getString("ISLAND_LEAVE_PRIVATE"), locale);
		ISLAND_LEAVE_REPOSSESSED = new Message(messages.getString("ISLAND_LEAVE_REPOSSESSED"), locale);
		ISLAND_LEAVE_RESERVED = new Message(messages.getString("ISLAND_LEAVE_RESERVED"), locale);
		ISLAND_LEAVE_RESOURCE = new Message(messages.getString("ISLAND_LEAVE_RESOURCE"), locale);
		ISLAND_PURCHASE = new Message(messages.getString("ISLAND_PURCHASE"), locale);
		ISLAND_PURCHASE_FUNDS_ERROR = new Message(messages.getString("ISLAND_PURCHASE_FUNDS_ERROR"), locale);
		ISLAND_PURCHASE_MAX_ERROR = new Message(messages.getString("ISLAND_PURCHASE_MAX_ERROR"), locale);
		ISLAND_PURCHASE_OCEAN_ERROR = new Message(messages.getString("ISLAND_PURCHASE_OCEAN_ERROR"), locale);
		ISLAND_PURCHASE_OTHER_ERROR = new Message(messages.getString("ISLAND_PURCHASE_OTHER_ERROR"), locale);
		ISLAND_PURCHASE_RESERVED_ERROR = new Message(messages.getString("ISLAND_PURCHASE_RESERVED_ERROR"), locale);
		ISLAND_PURCHASE_RESOURCE_ERROR = new Message(messages.getString("ISLAND_PURCHASE_RESOURCE_ERROR"), locale);
		ISLAND_PURCHASE_SELF_ERROR = new Message(messages.getString("ISLAND_PURCHASE_SELF_ERROR"), locale);
		ISLAND_PURCHASE_WORLD_ERROR = new Message(messages.getString("ISLAND_PURCHASE_WORLD_ERROR"), locale);
		ISLAND_RENAME = new Message(messages.getString("ISLAND_RENAME"), locale);
		ISLAND_RENAME_OCEAN_ERROR = new Message(messages.getString("ISLAND_RENAME_OCEAN_ERROR"), locale);
		ISLAND_RENAME_OWNER_ERROR = new Message(messages.getString("ISLAND_RENAME_OWNER_ERROR"), locale);
		ISLAND_RENAME_WORLD_ERROR = new Message(messages.getString("ISLAND_RENAME_WORLD_ERROR"), locale);
		ISLAND_TAX = new Message(messages.getString("ISLAND_TAX"), locale);
		ISLAND_TAX_FUNDS_ERROR = new Message(messages.getString("ISLAND_TAX_FUNDS_ERROR"), locale);
		ISLAND_TAX_MAX_ERROR = new Message(messages.getString("ISLAND_TAX_MAX_ERROR"), locale);
		ISLAND_TAX_OCEAN_ERROR = new Message(messages.getString("ISLAND_TAX_OCEAN_ERROR"), locale);
		ISLAND_TAX_OWNER_ERROR = new Message(messages.getString("ISLAND_TAX_OWNER_ERROR"), locale);
		ISLAND_TAX_WORLD_ERROR = new Message(messages.getString("ISLAND_TAX_WORLD_ERROR"), locale);
		ISLAND_UPDATE = new Message(messages.getString("ISLAND_UPDATE"), locale);
		ISLAND_UPDATE_OCEAN_ERROR = new Message(messages.getString("ISLAND_UPDATE_OCEAN_ERROR"), locale);
		ISLAND_UPDATE_WORLD_ERROR = new Message(messages.getString("ISLAND_UPDATE_WORLD_ERROR"), locale);
		LOCAL_MESSAGE = new Message(messages.getString("LOCAL_MESSAGE"), locale);
		PARTY_JOIN = new Message(messages.getString("PARTY_JOIN"), locale);
		PARTY_JOIN_NOTIFY = new Message(messages.getString("PARTY_JOIN_NOTIFY"), locale);
		PARTY_LEAVE = new Message(messages.getString("PARTY_LEAVE"), locale);
		PARTY_LEAVE_NOTIFY = new Message(messages.getString("PARTY_LEAVE_NOTIFY"), locale);
		PARTY_MEMBERS = new Message(messages.getString("PARTY_MEMBERS"), locale);
		PARTY_MESSAGE = new Message(messages.getString("PARTY_MESSAGE"), locale);
		PARTY_NONE = new Message(messages.getString("PARTY_NONE"), locale);
		PLAYER_NOT_ONLINE = new Message(messages.getString("PLAYER_NOT_ONLINE"), locale);
		PRIVATE_MESSAGE = new Message(messages.getString("PRIVATE_MESSAGE"), locale);
		WAYPOINT_ADD = new Message(messages.getString("WAYPOINT_ADD"), locale);
		WAYPOINT_ADD_RESERVED_ERROR = new Message(messages.getString("WAYPOINT_ADD_RESERVED_ERROR"), locale);
		WAYPOINT_ADD_WORLD_ERROR = new Message(messages.getString("WAYPOINT_ADD_WORLD_ERROR"), locale);
		WAYPOINT_LIST = new Message(messages.getString("WAYPOINT_LIST"), locale);
		WAYPOINT_NOT_DEFINED = new Message(messages.getString("WAYPOINT_NOT_DEFINED"), locale);
		WAYPOINT_REMOVE = new Message(messages.getString("WAYPOINT_REMOVE"), locale);
		WAYPOINT_REMOVE_RESERVED_ERROR = new Message(messages.getString("WAYPOINT_REMOVE_RESERVED_ERROR"), locale);
		WAYPOINT_SET = new Message(messages.getString("WAYPOINT_SET"), locale);
		WAYPOINT_SET_WORLD_ERROR = new Message(messages.getString("WAYPOINT_SET_WORLD_ERROR"), locale);
	}
}
