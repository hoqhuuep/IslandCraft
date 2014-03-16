package com.github.hoqhuuep.islandcraft.realestate;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.CommandSender;

public enum Message {
	ABANDON,
	ADVERTISE,
	ALREADY_OWN,
	CANNOT_ABANDON,
	CANNOT_ADVERTISE,
	CANNOT_PAY_TAX,
	CANNOT_PURCHASE,
	CANNOT_RECLAIM,
	CANNOT_RENAME,
	CANNOT_REVOKE,
	DO_NOT_OWN,
	ENTER,
	EXAMINE,
	LEAVE,
	LONG_DESCRIPTION_ABANDONED,
	LONG_DESCRIPTION_FOR_SALE,
	LONG_DESCRIPTION_NEW,
	LONG_DESCRIPTION_PRIVATE,
	LONG_DESCRIPTION_REPOSSESSED,
	LONG_DESCRIPTION_RESERVED,
	LONG_DESCRIPTION_RESOURCE,
	NAME_ABANDONED,
	NAME_FOR_SALE,
	NAME_NEW,
	NAME_PRIVATE,
	NAME_REPOSSESSED,
	NAME_RESERVED,
	NAME_RESOURCE,
	NAME_SPAWN,
	NOT_ISLAND,
	NOT_ISLAND_CRAFT_WORLD,
	NOT_PLAYER,
	PAY_TAX,
	PAY_TAX_FUNDS_ERROR,
	PAY_TAX_MAX_ERROR,
	PURCHASE,
	PURCHASE_FUNDS_ERROR,
	PURCHASE_MAX_ERROR,
	RECLAIM,
	RENAME,
	REVOKE,
	SHORT_DESCRIPTION_ABANDONED,
	SHORT_DESCRIPTION_FOR_SALE,
	SHORT_DESCRIPTION_NEW,
	SHORT_DESCRIPTION_PRIVATE,
	SHORT_DESCRIPTION_REPOSSESSED,
	SHORT_DESCRIPTION_RESERVED,
	SHORT_DESCRIPTION_RESOURCE,
	UPDATE;

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
