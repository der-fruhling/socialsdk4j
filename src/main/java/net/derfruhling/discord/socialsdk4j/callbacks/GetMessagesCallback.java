package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;
import net.derfruhling.discord.socialsdk4j.Message;

@FunctionalInterface
public interface GetMessagesCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result, Message[] messages);
}
