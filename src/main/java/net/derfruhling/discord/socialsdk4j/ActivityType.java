package net.derfruhling.discord.socialsdk4j;

public enum ActivityType {
    Playing,
    Streaming,
    Listening,
    Watching,
    /**
     * @deprecated You probably don't want to use this.
     */
    @Deprecated
    CustomStatus,
    Competing,
    /**
     * @deprecated You probably don't want to use this.
     */
    @Deprecated
    HangStatus;

    public static ActivityType from(int type) {
        return switch (type) {
            case 0 -> Playing;
            case 1 -> Streaming;
            case 2 -> Listening;
            case 3 -> Watching;
            case 4 -> CustomStatus;
            case 5 -> Competing;
            case 6 -> HangStatus;
            default -> throw new RuntimeException("illegal type: " + type);
        };
    }
}
