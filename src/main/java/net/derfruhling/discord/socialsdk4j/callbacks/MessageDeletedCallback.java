package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface MessageDeletedCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(long messageId, long channelId);
}
