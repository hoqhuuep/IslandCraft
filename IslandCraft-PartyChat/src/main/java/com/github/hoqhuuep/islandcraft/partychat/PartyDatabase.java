package com.github.hoqhuuep.islandcraft.partychat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.avaje.ebean.EbeanServer;

public class PartyDatabase {
	private final EbeanServer ebean;

	public PartyDatabase(final EbeanServer ebean) {
		this.ebean = ebean;
	}

	public static List<Class<?>> getDatabaseClasses() {
		final Class<?>[] classes = { PartyBean.class };
		return Arrays.asList(classes);
	}

	public final String loadParty(final String player) {
		final PartyBean bean = ebean.find(PartyBean.class, player);
		if (bean == null) {
			return null;
		}
		return bean.getParty();
	}

	public final List<String> loadMembers(final String party) {
		final List<PartyBean> beans = ebean.find(PartyBean.class).where()
				.ieq("party", party).findList();
		final List<String> members = new ArrayList<String>(beans.size());
		for (final PartyBean bean : beans) {
			members.add(bean.getPlayer());
		}
		return members;
	}

	public final void saveParty(final String player, final String party) {
		if (party == null) {
			ebean.delete(PartyBean.class, player);
			return;
		}
		final PartyBean bean = new PartyBean();
		bean.setPlayer(player);
		bean.setParty(party);
		ebean.save(bean);
	}
}
