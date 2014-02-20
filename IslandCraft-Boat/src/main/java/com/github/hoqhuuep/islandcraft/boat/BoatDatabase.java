package com.github.hoqhuuep.islandcraft.boat;

import com.avaje.ebean.EbeanServer;

public class BoatDatabase {
    private final EbeanServer ebean;

    public BoatDatabase(final EbeanServer ebean) {
        this.ebean = ebean;
    }

    public final boolean hadBoat(final String boat) {
        final BoatBean bean = ebean.find(BoatBean.class, boat);
        if (bean != null) {
            ebean.delete(bean);
            return true;
        }
        return false;
    }

    public final void saveBoat(final String boat) {
        BoatBean bean = ebean.find(BoatBean.class, boat);
        if (bean == null) {
            bean = new BoatBean();
            bean.setBoat(boat);
            ebean.save(bean);
        }
    }
}
