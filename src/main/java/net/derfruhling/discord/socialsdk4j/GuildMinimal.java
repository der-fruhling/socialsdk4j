package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a guild during lobby linking.
 * @param id ID of the guild.
 * @param name Name of the guild.
 */
public record GuildMinimal(
        long id,
        String name
) {
}
