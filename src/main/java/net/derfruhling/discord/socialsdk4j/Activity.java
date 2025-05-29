package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Activity {
    public enum Type {
        Playing,
        Streaming,
        Listening,
        Watching,
        CustomStatus,
        Competing,
        HangStatus;

        public static Type from(int type) {
            return switch (type) {
                case 0 -> Playing;
                case 1 -> Streaming;
                case 2 -> Listening;
                case 3 -> Watching;
                case 4 -> CustomStatus;
                case 5 -> Competing;
                case 6 -> HangStatus;
                default -> throw new RuntimeException("illegal type: " + type);
            };
        }
    }

    private static native long createNewActivityNative();
    private static native void deleteActivityNative(long pointer);

    final long pointer = createNewActivityNative();

    public Activity() {
        long pointer = this.pointer;
        SocialSdk.cleaner.register(this, () -> {
            deleteActivityNative(pointer);
        });
    }

    private static native void addButton(long pointer, String label, String url);
    private static native void setName(long pointer, String name);
    private static native void setType(long pointer, int type);
    private static native void setState(long pointer, String state);
    private static native void setDetails(long pointer, String details);
    private static native void setActivityAssets(long pointer, @Nullable String largeImage, @Nullable String largeText, @Nullable String smallImage, @Nullable String smallText);
    private static native void setTimestamps(long pointer, long start, long end);
    private static native void setParty(long pointer, String id, int size, int maxSize, boolean isPublic);
    private static native void setSecrets(long pointer, String joinSecret);

    public Activity addButton(String label, String url) {
        addButton(pointer, label, url);
        return this;
    }

    public Activity setName(String name) {
        setName(pointer, name);
        return this;
    }

    public Activity setType(Type type) {
        setType(pointer, type.ordinal());
        return this;
    }

    public Activity setState(String state) {
        setState(pointer, state);
        return this;
    }

    public Activity setDetails(String details) {
        setDetails(pointer, details);
        return this;
    }

    public record Assets(
            @Nullable Asset large,
            @Nullable Asset small
    ) {}

    public record Asset(
            @NotNull String image,
            @Nullable String text
    ) {}

    public Activity setAssets(Assets assets) {
        String largeImage = assets.large != null ? assets.large.image : null;
        String largeText = assets.large != null ? assets.large.text : null;
        String smolImage = assets.small != null ? assets.small.image : null;
        String smolText = assets.small != null ? assets.small.text : null;
        setActivityAssets(pointer, largeImage, largeText, smolImage, smolText);
        return this;
    }

    public Activity setTimestamps(long start, long end) {
        setTimestamps(pointer, start, end);
        return this;
    }

    public Activity setParty(String id, int size, int maxSize, boolean isPublic) {
        setParty(pointer, id, size, maxSize, isPublic);
        return this;
    }

    public Activity setJoinSecret(String joinSecret) {
        setSecrets(pointer, joinSecret);
        return this;
    }
}
