package net.derfruhling.discord.socialsdk4j;

@SuppressWarnings("DeprecatedIsStillUsed")
public enum ChannelType {
    @Deprecated GuildText,
    DirectMessage,
    @Deprecated GuildVoice,
    @Deprecated GroupDm,
    @Deprecated GuildCategory,
    @Deprecated GuildNews,
    @Deprecated GuildStore,
    @Deprecated GuildNewsThread,
    @Deprecated GuildPublicThread,
    @Deprecated GuildPrivateThread,
    @Deprecated GuildStageVoice,
    @Deprecated GuildDirectory,
    @Deprecated GuildForum,
    @Deprecated GuildMedia,
    Lobby,
    EphemeralDirectMessage;

    public static ChannelType from(int type) {
        return switch (type) {
            case 0 -> GuildText;
            case 1 -> DirectMessage;
            case 2 -> GuildVoice;
            case 3 -> GroupDm;
            case 4 -> GuildCategory;
            case 5 -> GuildNews;
            case 6 -> GuildStore;
            case 10 -> GuildNewsThread;
            case 11 -> GuildPublicThread;
            case 12 -> GuildPrivateThread;
            case 13 -> GuildStageVoice;
            case 14 -> GuildDirectory;
            case 15 -> GuildForum;
            case 16 -> GuildMedia;
            case 17 -> Lobby;
            case 18 -> EphemeralDirectMessage;
            default -> throw new IllegalArgumentException("Unknown channel type: " + type);
        };
    }

    public boolean isDm() {
        return this == DirectMessage || this == EphemeralDirectMessage;
    }
}
