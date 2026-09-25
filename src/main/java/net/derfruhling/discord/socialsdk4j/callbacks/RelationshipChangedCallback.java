package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface RelationshipChangedCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(long userId, boolean isDiscordRelationshipUpdate);
}
