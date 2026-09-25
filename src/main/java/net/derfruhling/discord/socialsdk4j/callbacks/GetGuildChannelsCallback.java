package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;
import net.derfruhling.discord.socialsdk4j.GuildChannel;

@FunctionalInterface
public interface GetGuildChannelsCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result, GuildChannel[] channels);
}
