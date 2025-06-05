package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the info of a guild channel, used when linking a lobby to a
 * channel.
 *
 * @param id ID of the channel.
 * @param name Name of the channel, excluding hash symbol.
 * @param isLinkable If {@code true}, the currently logged-in user could link
 *                   this channel at the time this object was retrieved.
 * @param isFullyPublic If {@code true}, this channel is accessible by all
 *                      members in the guild. Applications should use this to
 *                      warn users if they're trying to link a private channel.
 * @param linkedLobby The linked lobby, or {@code null} if no lobby is linked.
 */
public record GuildChannel(
        long id,
        String name,
        boolean isLinkable,
        boolean isFullyPublic,
        @Nullable LinkedLobby linkedLobby
) {
}
