package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a lobby that is linked to a channel.
 *
 * @param applicationId Application ID of the lobby.
 * @param lobbyId ID of the linked lobby.
 */
public record LinkedLobby(
        long applicationId,
        long lobbyId
) {
}
