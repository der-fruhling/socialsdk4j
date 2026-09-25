package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

public non-sealed class User extends SdkObject {
    public final long id;

    User(long pointer, long id) {
        super(pointer);
        this.id = id;
    }

    @Override
    final native void delete(long pointer);

    public native @Nullable String getAvatar();

    public native String getDisplayName();

    public native @Nullable Activity getActivityInfo();

    public native @Nullable String getGlobalName();

    public native boolean isProvisional();

    public native Relationship getRelationship();

    public native UserStatus getStatus();

    public native String getUsername();
}
