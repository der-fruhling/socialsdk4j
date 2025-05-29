package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Lobby {
    long pointer;
    public final long id;

    Lobby(long pointer, long id) {
        this.id = id;
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteLobbyNative(pointer));
    }

    public native @Nullable CallInfo getCallInfo();
    public native @Nullable LobbyMember getMember(long id);
    public native @Nullable LinkedChannel getLinkedChannel();
    public native long[] getLobbyMemberIds();
    public native LobbyMember[] getLobbyMembers();
    private native StringPair[] getMetadataNative();

    public Map<String, String> getMetadata() {
        return Arrays.stream(getMetadataNative())
                .collect(Collectors.toMap(StringPair::key, StringPair::value));
    }
}
