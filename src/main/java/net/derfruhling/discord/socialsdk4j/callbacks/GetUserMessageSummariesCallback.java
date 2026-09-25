package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;
import net.derfruhling.discord.socialsdk4j.UserMessageSummary;

@FunctionalInterface
public interface GetUserMessageSummariesCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result, UserMessageSummary[] summaries);
}
