package net.derfruhling.discord.socialsdk4j;

import java.lang.ref.WeakReference;

public sealed abstract class SdkObject permits ActivityBuilder, Call, Client, Lobby, LobbyMember, Message, User {
    long pointer;

    public static final long NULL = 0;

    public SdkObject(long pointer) {
        this.pointer = pointer;
        WeakReference<SdkObject> self = new WeakReference<>(this);
        Class<?> clazz = this.getClass();

        SocialSdk.cleaner.register(this, () -> {
            SdkObject selfValue = self.get();
            if (selfValue != null) {
                selfValue.delete(pointer);
            } else {
                SocialSdk.log(SocialSdk.LogLevel.WARN, "SdkObject " + clazz + " was destroyed before it could be deleted!");
            }
        });
    }

    abstract void delete(long pointer);

    public long getPointer() {
        if(pointer == NULL) throw new NullPointerException();
        return pointer;
    }
}
