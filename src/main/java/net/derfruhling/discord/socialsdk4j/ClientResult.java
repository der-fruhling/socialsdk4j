package net.derfruhling.discord.socialsdk4j;

/**
 * This type represents a result passed to a callback after some operation
 * completes. It may represent failure, so it's contents should always be
 * checked.
 * <a href="https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1ClientResult.html">See the official documentation.</a>
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

    /**
     * @return The category of error this result represents, or
     *         {@link ClientResultType#None} if there is no error.
     */
    public ClientResultType type() {
        return ClientResultType.from(errorCode0(pointer));
    }

    /**
     * @return The Discord provided message for the failure, always in the
     *         user's Discord language.
     */
    public String message() {
        return errorMessage0(pointer);
    }

    /**
     * @return {@code true} if the operation should be retried.
     *
     * @see ClientResult#retryAfter()
     */
    public boolean retry() {
        return isRetryable0(pointer);
    }

    /**
     * @return Number of seconds to wait before retrying the operation.
     */
    public float retryAfter() {
        return getRetryDelay0(pointer);
    }

    @Override
    public String toString() {
        return "ClientResult {" + type() + ": " + message() + "}";
    }

    /**
     * @return {@code true} if the result represents success.
     */
    public boolean isSuccess() {
        return type() == ClientResultType.None;
    }
}
