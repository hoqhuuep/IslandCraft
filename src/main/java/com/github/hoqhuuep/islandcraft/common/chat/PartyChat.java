package com.github.hoqhuuep.islandcraft.common.chat;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.hoqhuuep.islandcraft.common.api.ICDatabase;
import com.github.hoqhuuep.islandcraft.common.api.ICPlayer;

/**
 * @author Daniel (hoqhuuep) Simmons
 * @see <a
 *      href="https://github.com/hoqhuuep/IslandCraft/wiki/Chat#party-chat">IslandCraft
 *      wiki</a>
 */
public class PartyChat {
    private final ICDatabase database;

    public PartyChat(final ICDatabase database) {
        this.database = database;
    }

    public void onJoin(final ICPlayer player, final String party) {
        final String oldParty = this.database.loadParty(player.getName());
        if (oldParty != null) {
            player.info("You are no longer a member of " + oldParty);
        }
        this.database.saveParty(player.getName(), party);
        player.info("You are now a member of " + party);
    }

    public void onLeave(final ICPlayer player) {
        final String oldParty = this.database.loadParty(player.getName());
        if (oldParty == null) {
            player.info("You are not a member of any party");
            return;
        }
        this.database.saveParty(player.getName(), null);
        player.info("You are no longer a member of " + oldParty);
    }

    public void onMembers(final ICPlayer player) {
        final String party = this.database.loadParty(player.getName());
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> members = this.database.loadPartyMembers(party);
        player.info("Members: [" + StringUtils.join(members, ", ") + "]");
    }

    public void onPartyChat(final ICPlayer player, final String message) {
        final String party = this.database.loadParty(player.getName());
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> members = this.database.loadPartyMembers(party);
        for (final String member : members) {
            final ICPlayer m = player.getServer().findOnlinePlayer(member);
            if (m != null) {
                m.party(player, party, message);
            }
        }
    }
}
