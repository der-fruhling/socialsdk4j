package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class User {
    long pointer;
    public final long id;

    User(long pointer, long id) {
        this.id = id;
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteUserNative(pointer));
    }

    public native @Nullable String getAvatar();
    public native String getDisplayName();
    public native @Nullable ActivityInfo getActivityInfo();
    public native @Nullable String getGlobalName();
    public native boolean isProvisional();
    public native Relationship getRelationship();
    public native StatusType getStatus();
    public native String getUsername();
}
