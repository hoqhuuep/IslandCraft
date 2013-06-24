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

    /**
     * To be called when a player tries to join a party.
     * 
     * @param player
     * @param party
     */
    public final void onJoin(final ICPlayer player, final String party) {
        final String oldParty = database.loadParty(player.getName());
        if (oldParty != null) {
            player.info("You are no longer a member of " + oldParty);
        }
        database.saveParty(player.getName(), party);
        player.info("You are now a member of " + party);
        // TODO Notify members that player has joined
    }

    /**
     * To be called when a player tries to leave a party.
     * 
     * @param player
     */
    public final void onLeave(final ICPlayer player) {
        final String oldParty = database.loadParty(player.getName());
        if (oldParty == null) {
            player.info("You are not a member of any party");
            return;
        }
        database.saveParty(player.getName(), null);
        player.info("You are no longer a member of " + oldParty);
        // TODO Notify members that player has left
    }

    /**
     * To be called when a player requests a list of members of their party.
     * 
     * @param player
     */
    public final void onMembers(final ICPlayer player) {
        final String party = database.loadParty(player.getName());
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> members = database.loadPartyPlayers(party);
        player.info("Members: [" + StringUtils.join(members, ", ") + "]");
    }

    /**
     * To be called when a player tries to send a party chat message.
     * 
     * @param player
     * @param message
     */
    public final void onPartyChat(final ICPlayer player, final String message) {
        final String name = player.getName();
        final String party = database.loadParty(name);
        if (party == null) {
            player.info("You are not a member of any party");
            return;
        }
        final List<String> memberNames = database.loadPartyPlayers(party);
        for (final String memberName : memberNames) {
            final ICPlayer member = player.getServer().findOnlinePlayer(memberName);
            if (member != null) {
                member.party(name, party, message);
            }
        }
    }
}
