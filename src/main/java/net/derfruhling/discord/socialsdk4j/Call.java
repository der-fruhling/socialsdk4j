package net.derfruhling.discord.socialsdk4j;

public class Call {
    long pointer;

    public Call(long pointer) {
        SocialSdk.ensureInitialized();
        this.pointer = pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteCallNative(pointer));
    }
}
