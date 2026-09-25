package net.derfruhling.discord.socialsdk4j;

public non-sealed class Call extends SdkObject {
    Call(long pointer) {
        super(pointer);
    }

    @Override
    final native void delete(long pointer);
}
