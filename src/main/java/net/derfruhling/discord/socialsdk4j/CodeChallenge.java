package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a code challenge handle in the SDK. This is opaque.
 */
public final class CodeChallenge {
    final long pointer;

    CodeChallenge(long pointer) {
        this.pointer = pointer;
    }
}
