package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface LobbyExistenceChangedCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(long lobbyId);
}
