package net.derfruhling.discord.socialsdk4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a lobby member handle.
 */
public non-sealed class LobbyMember extends SdkObject {
    /**
     * User ID of this member.
     */
    public final long id;

    LobbyMember(long pointer, long id) {
        super(pointer);
        this.id = id;
    }

    @Override
    final native void delete(long pointer);

    /**
     * @return {@code true} if this user can link the lobby to a channel.
     */
    public native boolean canLinkLobby();

    /**
     * @return {@code true} if this user is currently connected to the lobby.
     */
    public native boolean isConnected();

    /**
     * @return The {@link User} handle of this member, if it exists.
     */
    public native @Nullable User getUser();

    private native StringPair[] getMetadataNative();

    /**
     * @return The metadata associated with this member.
     */
    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative()).collect(
            Collectors.toMap(StringPair::key, StringPair::value)
        );
    }
}
