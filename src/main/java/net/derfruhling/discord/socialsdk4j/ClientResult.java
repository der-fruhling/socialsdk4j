package net.derfruhling.discord.socialsdk4j;

/**
 * <a href="https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1ClientResult.html">discordpp::ClientResult</a>
 */
public class ClientResult {
    private final long pointer;

    ClientResult(long pointer) {
        this.pointer = pointer;
    }

    private static native int errorCode0(long pointer);
    private static native String errorMessage0(long pointer);
    private static native boolean isRetryable0(long pointer);
    private static native float getRetryDelay0(long pointer);

    public ClientResultError type() {
        return ClientResultError.from(errorCode0(pointer));
    }

    public String message() {
        return errorMessage0(pointer);
    }

    public boolean retry() {
        return isRetryable0(pointer);
    }

    public float retryAfter() {
        return getRetryDelay0(pointer);
    }

    @Override
    public String toString() {
        return "ClientResult {" + type() + ": " + message() + "}";
    }

    public boolean isSuccess() {
        return type() == ClientResultError.None;
    }
}
