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
        final String name = player.getName();
        final String oldParty = database.loadParty(name);
        if (oldParty != null) {
            player.message("party-leave", oldParty);
        }
        database.saveParty(name, party);
        player.message("party-join", party);

        // Notify members that player has joined
        final List<String> memberNames = database.loadPartyPlayers(party);
        for (final String memberName : memberNames) {
            final ICPlayer member = player.getServer().findOnlinePlayer(memberName);
            if (member != null && !member.getName().equalsIgnoreCase(player.getName())) {
                member.message("party-join-notify", name);
            }
        }
    }

    /**
     * To be called when a player tries to leave a party.
     * 
     * @param player
     */
    public final void onLeave(final ICPlayer player) {
        final String name = player.getName();
        final String party = database.loadParty(name);
        if (party == null) {
            player.message("party-leave-error");
            return;
        }
        database.saveParty(name, null);
        player.message("party-leave", party);

        // Notify members that player has left
        final List<String> memberNames = database.loadPartyPlayers(party);
        for (final String memberName : memberNames) {
            final ICPlayer member = player.getServer().findOnlinePlayer(memberName);
            if (member != null && !member.getName().equalsIgnoreCase(player.getName())) {
                member.message("party-leave-notify", name);
            }
        }
    }

    /**
     * To be called when a player requests a list of members of their party.
     * 
     * @param player
     */
    public final void onMembers(final ICPlayer player) {
        final String party = database.loadParty(player.getName());
        if (party == null) {
            player.message("party-members-error");
            return;
        }
        final List<String> members = database.loadPartyPlayers(party);
        player.message("party-members", StringUtils.join(members, ", "));
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
            player.message("p-error");
            return;
        }
        final List<String> memberNames = database.loadPartyPlayers(party);
        for (final String memberName : memberNames) {
            final ICPlayer member = player.getServer().findOnlinePlayer(memberName);
            if (member != null) {
                member.message("p", name, party, message);
            }
        }
    }
}
