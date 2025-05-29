package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class LobbyMember {
    long pointer;
    public final long id;

    LobbyMember(long pointer, long id) {
        this.id = id;
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteLobbyMemberNative(pointer));
    }

    public native boolean canLinkLobby();
    public native boolean isConnected();
    public native @Nullable User getUser();

    private native StringPair[] getMetadataNative();

    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative())
                .collect(Collectors.toMap(StringPair::key, StringPair::value));
    }
}
