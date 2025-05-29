package net.derfruhling.discord.socialsdk4j;

public enum AuthorizationTokenType {
    User,
    Bearer;

    public static AuthorizationTokenType from(int type) {
        return switch (type) {
            case 0 -> User;
            case 1 -> Bearer;
            default -> throw new RuntimeException("unknown authorization token type: " + type);
        };
    }
}
