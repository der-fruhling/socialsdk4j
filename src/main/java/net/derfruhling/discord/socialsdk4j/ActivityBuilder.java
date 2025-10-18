package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Allows building a rich presence activity.
 *
 * @see Client#updateRichPresence(ActivityBuilder, Client.GenericResultCallback)
 */
public class ActivityBuilder {
    private static native long createNewActivityNative();
    private static native void deleteActivityNative(long pointer);

    final long pointer = createNewActivityNative();

    /**
     * Creates a new empty activity builder.
     */
    public ActivityBuilder() {
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

    /**
     * Adds a custom button to this activity. It will have an "external link"
     * symbol to signify that it will open a URL in the browser.
     *
     * @param label The text of the button.
     * @param url The URL to visit when clicked.
     * @return {@code this}
     */
    public ActivityBuilder addButton(String label, String url) {
        addButton(pointer, label, url);
        return this;
    }

    /**
     * Sets the name field of the activity builder.
     *
     * <p>This is now valid to use as of SocialSDK 1.6 (implemented in
     * SocialSDK4J as of October 18, 2025)</p>
     *
     * @param name The new value.
     * @return {@code this}
     *
     */
    public ActivityBuilder setName(String name) {
        setName(pointer, name);
        return this;
    }

    /**
     * Sets the type of the activity. {@link ActivityType#Playing} is most
     * common, but others can be used here as well and will format the
     * rich presence differently.
     *
     * @param type The new type. Should not be {@link ActivityType#HangStatus}
     *             or {@link ActivityType#CustomStatus}
     * @return {@code this}
     */
    @SuppressWarnings("deprecation")
    public ActivityBuilder setType(ActivityType type) {
        assert type != ActivityType.HangStatus && type != ActivityType.CustomStatus;

        setType(pointer, type.ordinal());
        return this;
    }

    /**
     * Sets the state field of the activity. This is generally shown above
     * details and below the name of the application. If your presence has
     * a party, it may be shown beside the member count.
     *
     * @param state The new state value.
     * @return {@code this}
     */
    public ActivityBuilder setState(String state) {
        setState(pointer, state);
        return this;
    }

    /**
     * Sets the details field of the activity. This is generally shown below
     * state and above any buttons that may exist.
     *
     * @param details The new details value.
     * @return {@code this}
     */
    public ActivityBuilder setDetails(String details) {
        setDetails(pointer, details);
        return this;
    }

    @SuppressWarnings("MissingJavadoc")
    public record Assets(
            @Nullable Asset large,
            @Nullable Asset small
    ) {}

    @SuppressWarnings("MissingJavadoc")
    public record Asset(
            @NotNull String image,
            @Nullable String text
    ) {}

    /**
     * Sets assets for this presence. The large image will replace the default
     * application icon for the presence, while the small image will add a
     * small bit the bottom-right.
     *
     * @param assets The new assets.
     * @return {@code this}
     */
    public ActivityBuilder setAssets(Assets assets) {
        String largeImage = assets.large != null ? assets.large.image : null;
        String largeText = assets.large != null ? assets.large.text : null;
        String smolImage = assets.small != null ? assets.small.image : null;
        String smolText = assets.small != null ? assets.small.text : null;
        setActivityAssets(pointer, largeImage, largeText, smolImage, smolText);
        return this;
    }

    /**
     * Sets the timestamps for this presence. Passing small-ish values may
     * result in the SDK assuming it is in seconds and converting it into
     * milliseconds.
     *
     * @param start Set this to show a time elapsed counter, or zero if
     *              it's not relevant.
     * @param end Set this to show a time remaining counter, or zero if
     *            it's not relevant.
     * @return {@code this}
     */
    public ActivityBuilder setTimestamps(long start, long end) {
        setTimestamps(pointer, start, end);
        return this;
    }

    /**
     * Sets party info.
     *
     * @param id The party ID. Used to show other users avatars if they're in
     *           the same party.
     * @param size The current size of the party.
     * @param maxSize The maximum size of the party. If no maximum is specified,
     *                set this to zero.
     * @param isPublic If this is {@code true}, this party is public.
     * @return {@code this}
     */
    public ActivityBuilder setParty(String id, int size, int maxSize, boolean isPublic) {
        setParty(pointer, id, size, maxSize, isPublic);
        return this;
    }

    /**
     * Sets the join secret. Required for invites to work.
     *
     * @param joinSecret A game-specific join secret. Discord will handle
     *                   passing this value to the SDK when players join
     *                   the party.
     * @return {@code this}
     */
    public ActivityBuilder setJoinSecret(String joinSecret) {
        setSecrets(pointer, joinSecret);
        return this;
    }
}
