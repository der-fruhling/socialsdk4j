package net.derfruhling.discord.socialsdk4j;

/**
 * This type represents a result passed to a callback after some operation
 * completes. It may represent failure, so it's contents should always be
 * checked.
 * <a href="https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1ClientResult.html">See the official documentation.</a>
 */
public record ClientResult(ClientResultType type, String message, boolean retryable, float retryDelay) {
    ClientResult(long pointer) {
        this(ClientResultType.from(Results.errorCode(pointer)), Results.errorMessage(pointer), Results.isRetryable(pointer), Results.getRetryDelay(pointer));
        Results.delete(pointer);
    }

    private static class Results {
        private static native void delete(long pointer);

        private static native int errorCode(long pointer);

        private static native String errorMessage(long pointer);

        private static native boolean isRetryable(long pointer);

        private static native float getRetryDelay(long pointer);
    }

    public boolean isSuccess() {
        return type == ClientResultType.None;
    }
}
