package net.derfruhling.discord.socialsdk4j;

/**
 * <a href="https://discord.com/developers/docs/social-sdk/namespacediscordpp.html#ae19a5aed498bc40359e4d89d14ffb446">discordpp::ErrorType</a>
 */
public enum ClientResultError {
    None,
    NetworkError,
    HTTPError,
    ClientNotReady,
    Disabled,
    ClientDestroyed,
    ValidationError,
    Aborted,
    AuthorizationFailed,
    RPCError;

    public static ClientResultError from(int errorType) {
        return switch (errorType) {
            case 0 -> None;
            case 1 -> NetworkError;
            case 2 -> HTTPError;
            case 3 -> ClientNotReady;
            case 4 -> Disabled;
            case 5 -> ClientDestroyed;
            case 6 -> ValidationError;
            case 7 -> Aborted;
            case 8 -> AuthorizationFailed;
            case 9 -> RPCError;
            default -> throw new RuntimeException("illegal error type: " + errorType);
        };
    }
}
