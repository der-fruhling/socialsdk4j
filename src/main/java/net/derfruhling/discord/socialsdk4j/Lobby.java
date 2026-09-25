package net.derfruhling.discord.socialsdk4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a lobby handle retrieved from {@link Client#getLobby(long)}.
 */
public non-sealed class Lobby extends SdkObject {
    /**
     * ID of this Lobby.
     */
    public final long id;

    Lobby(long pointer, long id) {
        super(pointer);
        this.id = id;
    }

    @Override
    final native void delete(long pointer);

    /**
     * @return Voice call info if it exists, {@code null} otherwise.
     */
    public native @Nullable CallInfo getCallInfo();

    /**
     * @param id User ID of the member to get information of.
     * @return {@link LobbyMember} if the user is part of the lobby,
     *         {@code null} otherwise.
     */
    public native @Nullable LobbyMember getMember(long id);

    /**
     * @return The channel linked to this lobby if it is linked, {@code null}
     *         otherwise.
     */
    public native @Nullable LinkedChannel getLinkedChannel();

    /**
     * @return Retrieves an array of all user IDs that are members of this
     *         lobby.
     */
    public native long[] getLobbyMemberIds();

    /**
     * @return Retrieves all {@link LobbyMember} handles that are in this
     *         lobby.
     */
    public native LobbyMember[] getLobbyMembers();

    private native StringPair[] getMetadataNative();

    /**
     * @return The metadata contained in this lobby.
     */
    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative()).collect(
            Collectors.toMap(StringPair::key, StringPair::value)
        );
    }
}
