package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;

@FunctionalInterface
public interface CreateOrJoinLobbyCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result, long lobbyId);
}
