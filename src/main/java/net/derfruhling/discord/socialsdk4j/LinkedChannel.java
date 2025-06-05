package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a channel that is linked to a lobby.
 *
 * @param id ID of the channel the lobby is linked to.
 * @param name Name of the channel the lobby is linked to.
 * @param guildId The guild the channel is contained in.
 */
public record LinkedChannel(
        long id,
        String name,
        long guildId
) {
}
