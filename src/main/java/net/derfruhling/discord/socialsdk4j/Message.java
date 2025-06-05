package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Message {
    long pointer;

    /**
     * ID of this message.
     */
    public final long id;

    public Message(long pointer, long id) {
        this.id = id;
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteMessageNative(pointer));
    }

    /**
     * Represents some content that cannot be rendered in-game.
     */
    public enum AdditionalContentType {
        /**
         * Something else.
         */
        Other,

        /**
         * An attachment, such as an image, video, or file.
         * GIFs are just links.
         */
        Attachment,

        /**
         * A poll.
         */
        Poll,

        /**
         * A voice message.
         */
        VoiceMessage,

        /**
         * A thread.
         */
        Thread,

        /**
         * An embed, probably created by a bot.
         */
        Embed,

        /**
         * A sticker.
         */
        Sticker;

        static AdditionalContentType from(int type) {
            return switch (type) {
                case 0 -> Other;
                case 1 -> Attachment;
                case 2 -> Poll;
                case 3 -> VoiceMessage;
                case 4 -> Thread;
                case 5 -> Embed;
                case 6 -> Sticker;
                default -> throw new IllegalArgumentException("Unknown additional content type: " + type);
            };
        }
    }

    /**
     * Represents additional content that cannot be rendered in-game.
     *
     * @param type Type of the content that could not be rendered.
     * @param title An optional name, may be {@code null}.
     * @param count The count of this item in the message.
     */
    public record AdditionalContent(AdditionalContentType type, @Nullable String title, int count) {
        AdditionalContent(int type, @Nullable String title, int count) {
            this(AdditionalContentType.from(type), title, count);
        }
    }

    @SuppressWarnings("MissingJavadoc")
    public enum DisclosureType {
        MessageDataVisibleOnDiscord;

        static DisclosureType from(int type) {
            return switch (type) {
                case 3 -> MessageDataVisibleOnDiscord;
                default -> throw new IllegalArgumentException("Unknown disclosure type: " + type);
            };
        }
    }

    /**
     * @return Additional content that cannot be rendered in-game, if any is
     *         present. These were probably sent by users in a linked channel.
     */
    public native @Nullable AdditionalContent getAdditionalContent();

    /**
     * @return A {@link User} handle representing the author of the message, if
     *         one exists.
     */
    public native @Nullable User getAuthor();

    /**
     * @return User ID of the message's author.
     */
    public native long getAuthorId();

    /**
     * @return A {@link Channel} handle representing the channel the message
     *         was sent in, or {@code null} if none exists.
     */
    public native @Nullable Channel getChannel();

    /**
     * @return ID of the channel the message was sent in.
     */
    public native long getChannelId();

    /**
     * @return Content of the message, sanitized.
     *
     * @see Message#getRawContent()
     */
    public native String getContent();

    private native int getDisclosureTypeNative();

    /**
     * @return A timestamp in milliseconds since epoch when the message was
     *         last edited.
     */
    public native long getEditedTimestamp();

    /**
     * @return A {@link Lobby} handle associated with the message, if one exists.
     */
    public native @Nullable Lobby getLobby();

    private native StringPair[] getMetadataNative();

    /**
     * @return The raw content, with markup and the like left in.
     */
    public native String getRawContent();

    /**
     * @return A {@link User} handle of the recipient of the message if it was
     *         sent in a DM.
     */
    public native @Nullable User getRecipient();

    /**
     * @return User ID of the message recipient if it was sent in a DM.
     */
    public native long getRecipientId();

    /**
     * @return {@code true} if this message was sent in-game.
     */
    public native boolean isSentFromGame();

    /**
     * @return A timestamp in milliseconds since epoch when the message was
     *         created.
     */
    public native long getSentTimestamp();

    /**
     * Discord injects a fake message the first time a user sends a message
     * in a game using the SocialSDK to inform users that their messages are
     * visible on Discord. This method returns non-null for these messages.
     *
     * @return {@code null} for regular messages,
     *         {@link DisclosureType#MessageDataVisibleOnDiscord} otherwise.
     */
    public @Nullable DisclosureType getDisclosureType() {
        int v = getDisclosureTypeNative();

        if(v == -1) {
            return null;
        } else {
            return DisclosureType.from(v);
        }
    }

    /**
     * @return The metadata associated with the message.
     */
    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative())
                .collect(Collectors.toMap(StringPair::key, StringPair::value));
    }
}
