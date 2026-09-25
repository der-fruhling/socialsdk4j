package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientResult;
import net.derfruhling.discord.socialsdk4j.GuildMinimal;

@FunctionalInterface
public interface GetUserGuildsCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ClientResult result, GuildMinimal[] guilds);
}
