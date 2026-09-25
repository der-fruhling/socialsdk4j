package net.derfruhling.discord.socialsdk4j;

/**
 * Represents the status of this client. The most important status is
 * {@link ClientStatus#Ready} as almost nothing can be done (other than
 * authorize) before the client reaches this state.
 *
 * @see Client#setStatusChangedCallback
 */
public enum ClientStatus {
    /**
     * The client is not connected to Discord.
     */
    Disconnected,
    /**
     * The client is actively connecting to Discord.
     */
    Connecting,
    /**
     * The client is connected to Discord, but not ready to do stuff.
     */
    Connected,
    /**
     * The client is ready for action!
     */
    Ready,
    /**
     * The client is reconnecting to Discord.
     */
    Reconnecting,
    /**
     * The client is disconnecting from Discord.
     */
    Disconnecting,
    /**
     * Waiting on HTTP?
     */
    HttpWait;

    static ClientStatus from(int status) {
        return switch (status) {
            case 0 -> Disconnected;
            case 1 -> Connecting;
            case 2 -> Connected;
            case 3 -> Ready;
            case 4 -> Reconnecting;
            case 5 -> Disconnecting;
            case 6 -> HttpWait;
            default -> throw new IllegalStateException(
                    "Unknown status: " + status
            );
        };
    }
}
