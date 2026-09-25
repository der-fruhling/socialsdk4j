package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;

@FunctionalInterface
public interface GenericResultCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result);
}
