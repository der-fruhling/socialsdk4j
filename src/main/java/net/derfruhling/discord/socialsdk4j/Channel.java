package net.derfruhling.discord.socialsdk4j;

public record Channel(
        long id,
        String name,
        long[] recipients,
        ChannelType type
) {
    public Channel(long id, String name, long[] recipients, int type) {
        this(id, name, recipients, ChannelType.from(type));
    }
}
