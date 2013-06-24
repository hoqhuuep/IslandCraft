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

    public final void onJoin(final ICPlayer player, final String party) {
        final String oldParty = database.loadParty(player.getName());
        if (oldParty != null) {
            player.info("You are no longer a member of " + oldParty);
        }
        database.saveParty(player.getName(), party);
        player.info("You are now a member of " + party);
    }

    public final void onLeave(final ICPlayer player) {
        final String oldParty = database.loadParty(player.getName());
        if (oldParty == null) {
            player.info("You are not a member of any party");
            return;
        }
        database.saveParty(player.getName(), null);
        player.info("You are no longer a member of " + oldParty);
    }

    public final void onMembers(final ICPlayer player) {
        final String party = database.loadParty(player.getName());
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> members = database.loadPartyPlayers(party);
        player.info("Members: [" + StringUtils.join(members, ", ") + "]");
    }

    public final void onPartyChat(final ICPlayer player, final String message) {
        final String party = database.loadParty(player.getName());
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> memberNames = database.loadPartyPlayers(party);
        for (final String memberName : memberNames) {
            final ICPlayer member = player.getServer().findOnlinePlayer(memberName);
            if (member != null) {
                member.party(player, party, message);
            }
        }
    }
}
