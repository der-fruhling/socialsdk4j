package net.derfruhling.discord.socialsdk4j;

@SuppressWarnings("DeprecatedIsStillUsed")
public enum ChannelType {
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildText,
    /**
     * A direct message between two users.
     */
    DirectMessage,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildVoice,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GroupDm,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildCategory,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildNews,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildStore,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildNewsThread,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildPublicThread,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildPrivateThread,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildStageVoice,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildDirectory,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildForum,
    @SuppressWarnings("MissingJavadoc")
    @Deprecated
    GuildMedia,
    /**
     * A lobby. Lobbies are considered as channels.
     */
    Lobby,
    /**
     * An ephemeral direct message. I believe this channel type is used when
     * two provisional accounts DM each other. In that case, no message history
     * would need preserving, so it would be ephemeral, whereas if one of the
     * users had a full Discord account the message history would need to be
     * retained as it's viewable in the Discord client.
     */
    EphemeralDirectMessage;

    static ChannelType from(int type) {
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

    /**
     * @return {@code true} if this channel type represents a direct message.
     */
    public boolean isDm() {
        return this == DirectMessage || this == EphemeralDirectMessage;
    }
}
