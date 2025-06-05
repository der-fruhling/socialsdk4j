package net.derfruhling.discord.socialsdk4j;

/**
 * This is essentially a light {@link ClientResult} used for socket stuff.
 * <a href="https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1Client.html#ac8d8140b9e1251e59e59a267f8a6295f">See the official documentation.</a>
 */
@SuppressWarnings("MissingJavadoc")
public enum ClientSocketResult {
    None,
    ConnectionFailed,
    UnexpectedClose,
    ConnectionCanceled;

    static ClientSocketResult from(int error) {
        return switch (error) {
            case 0 -> None;
            case 1 -> ConnectionFailed;
            case 2 -> UnexpectedClose;
            case 3 -> ConnectionCanceled;
            default -> throw new RuntimeException("Unknown error: " + error);
        };
    }

    /**
     * @return {@code true} if this result is successful.
     */
    public boolean isOk() {
        return this == None;
    }
}
