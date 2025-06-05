package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

public record ActivityInfo(
        ActivityType type,
        String name,
        @Nullable String state,
        @Nullable String details,
        Button[] buttons,
        long applicationId,
        @Nullable ActivityBuilder.Assets assets,
        @Nullable Timestamps timestamps,
        @Nullable Party party,
        @Nullable Secrets secrets,
        int supportedPlatforms
) {
    @SuppressWarnings("unused")
    private ActivityInfo(int type,
                         String name,
                         @Nullable String state,
                         @Nullable String details,
                         Button[] buttons,
                         long applicationId,
                         @Nullable String largeImage,
                         @Nullable String largeText,
                         @Nullable String smallImage,
                         @Nullable String smallText,
                         long timestampStart,
                         long timestampEnd,
                         @Nullable String partyId,
                         int partySize,
                         int partyMaxSize,
                         boolean partyIsPublic,
                         @Nullable String joinSecret,
                         int supportedPlatforms) {
        this(
                ActivityType.from(type),
                name,
                state,
                details,
                buttons,
                applicationId,
                largeImage != null || smallImage != null ? new ActivityBuilder.Assets(
                        largeImage != null ? new ActivityBuilder.Asset(largeImage, largeText) : null,
                        smallImage != null ? new ActivityBuilder.Asset(smallImage, smallText) : null
                ) : null,
                timestampStart != 0 ? new Timestamps(timestampStart, timestampEnd) : null,
                partyId != null ? new Party(partyId, partySize, partyMaxSize, partyIsPublic) : null,
                joinSecret != null ? new Secrets(joinSecret) : null,
                supportedPlatforms
        );
    }

    public record Button(String label, String url) {}
    public record Timestamps(long start, long end) {}
    public record Party(String id, int size, int maxSize, boolean isPublic) {}
    public record Secrets(String joinSecret) {}

    public static final int SUPPORTS_DESKTOP = 1;
    public static final int SUPPORTS_XBOX = 2;
    public static final int SUPPORTS_SAMSUNG = 4;
    public static final int SUPPORTS_IOS = 8;
    public static final int SUPPORTS_ANDROID = 16;
    public static final int SUPPORTS_EMBEDDED = 32;
    public static final int SUPPORTS_PS4 = 64;
    public static final int SUPPORTS_PS5 = 128;
}
