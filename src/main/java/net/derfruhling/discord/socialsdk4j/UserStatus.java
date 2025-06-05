package net.derfruhling.discord.socialsdk4j;

/**
 * Represents the online status of a user.
 */
public enum UserStatus {
    /**
     * User is currently online. Discord represents this with a green circle.
     */
    Online,
    /**
     * User is currently offline. Discord represents this with a hollow gray
     * circle.
     */
    Offline,
    /**
     * User is blocked.
     */
    Blocked,
    /**
     * User is online, but idle. Discord represents this with a yellow crescent
     * moon shape.
     */
    Idle,
    /**
     * User is online, but in do-not-disturb mode. Discord represents this with
     * a red do-not-enter shape.
     */
    DoNotDisturb,
    /**
     * User is online, but would like to be perceived as offline.
     */
    Invisible,
    /**
     * User is streaming to a streaming platform. Discord represents this with
     * a dark purple hollow play button shape.
     */
    Streaming,
    /**
     * Unknown status.
     */
    Unknown;

    static UserStatus from(int value) {
        return switch (value) {
            case 0 -> Online;
            case 1 -> Offline;
            case 2 -> Blocked;
            case 3 -> Idle;
            case 4 -> DoNotDisturb;
            case 5 -> Invisible;
            case 6 -> Streaming;
            default -> Unknown;
        };
    }
}
