package net.derfruhling.discord.socialsdk4j;

public enum StatusType {
    Online,
    Offline,
    Blocked,
    Idle,
    DoNotDisturb,
    Invisible,
    Streaming,
    Unknown;

    public static StatusType from(int value) {
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
