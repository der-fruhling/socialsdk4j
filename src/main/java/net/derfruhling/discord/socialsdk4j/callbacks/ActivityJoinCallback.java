package net.derfruhling.discord.socialsdk4j.callbacks;

@FunctionalInterface
public interface ActivityJoinCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(String joinSecret);
}
