package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface MessageIdCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(long messageId);
}
