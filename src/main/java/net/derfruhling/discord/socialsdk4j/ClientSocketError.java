package net.derfruhling.discord.socialsdk4j;

/**
 * <a href="https://discord.com/developers/docs/social-sdk/classdiscordpp_1_1Client.html#ac8d8140b9e1251e59e59a267f8a6295f">discordpp::Client::Error</a>
 */
public enum ClientSocketError {
    None,
    ConnectionFailed,
    UnexpectedClose,
    ConnectionCanceled;

    public static ClientSocketError from(int error) {
        return switch (error) {
            case 0 -> None;
            case 1 -> ConnectionFailed;
            case 2 -> UnexpectedClose;
            case 3 -> ConnectionCanceled;
            default -> throw new RuntimeException("Unknown error: " + error);
        };
    }

    public boolean isOk() {
        return this == None;
    }
}
