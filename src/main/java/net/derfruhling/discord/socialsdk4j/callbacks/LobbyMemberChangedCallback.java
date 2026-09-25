package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface LobbyMemberChangedCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(long lobbyId, long userId);
}
