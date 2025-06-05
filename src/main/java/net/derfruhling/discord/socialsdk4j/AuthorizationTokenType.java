package net.derfruhling.discord.socialsdk4j;

/**
 * A valid type for an authorization token.
 */
public enum AuthorizationTokenType {
    /**
     * It's incredibly unlikely this will ever be a thing that is possible to
     * use with the SDK, and attempting to is probably very easily detectable
     * and very much against their Terms of Service, but for some reason Discord
     * provides this value in this SDK.
     *
     * @deprecated You almost certainly don't want to use this.
     */
    @Deprecated
    User,

    /**
     * A Bearer token, the standard way to interface with the SDK.
     */
    Bearer;

    static AuthorizationTokenType from(int type) {
        return switch (type) {
            case 0 -> User;
            case 1 -> Bearer;
            default -> throw new RuntimeException("unknown authorization token type: " + type);
        };
    }
}
