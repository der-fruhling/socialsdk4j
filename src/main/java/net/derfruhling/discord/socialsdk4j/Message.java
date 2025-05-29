package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Message {
    long pointer;

    public final long id;

    public Message(long pointer, long id) {
        this.id = id;
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteMessageNative(pointer));
    }

    public enum AdditionalContentType {
        Other,
        Attachment,
        Poll,
        VoiceMessage,
        Thread,
        Embed,
        Sticker;

        public static AdditionalContentType from(int type) {
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

    public record AdditionalContent(AdditionalContentType type, @Nullable String title, int count) {
        public AdditionalContent(int type, @Nullable String title, int count) {
            this(AdditionalContentType.from(type), title, count);
        }
    }

    public enum DisclosureType {
        MessageDataVisibleOnDiscord;

        public static DisclosureType from(int type) {
            return switch (type) {
                case 3 -> MessageDataVisibleOnDiscord;
                default -> throw new IllegalArgumentException("Unknown disclosure type: " + type);
            };
        }
    }

    public native @Nullable AdditionalContent getAdditionalContent();
    public native @Nullable User getAuthor();
    public native long getAuthorId();
    public native @Nullable Channel getChannel();
    public native long getChannelId();
    public native String getContent();
    private native int getDisclosureTypeNative();
    public native long getEditedTimestamp();
    public native @Nullable Lobby getLobby();
    private native StringPair[] getMetadataNative();
    public native String getRawContent();
    public native @Nullable User getRecipient();
    public native long getRecipientId();
    public native boolean isSentFromGame();
    public native long getSentTimestamp();

    public @Nullable DisclosureType getDisclosureType() {
        int v = getDisclosureTypeNative();

        if(v == -1) {
            return null;
        } else {
            return DisclosureType.from(v);
        }
    }

    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative())
                .collect(Collectors.toMap(StringPair::key, StringPair::value));
    }
}
