package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a configured external auth type for authenticating provisional
 * accounts. You must configure this in the developer panel before users can
 * use them.
 */
@SuppressWarnings("MissingJavadoc")
public enum ExternalAuthType {
    OpenIDConnect,
    EpicOnlineServicesAccessToken,
    EpicOnlineServicesIdToken,
    SteamSessionTicket,
    /**
     * @deprecated You probably don't want to use this.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    UnityServicesIdToken;

    // this is an alias, the official SDK code calls it OIDC
    public static final ExternalAuthType OIDC = OpenIDConnect;

    public static ExternalAuthType from(int type) {
        return switch(type) {
            case 0 -> OpenIDConnect;
            case 1 -> EpicOnlineServicesAccessToken;
            case 2 -> EpicOnlineServicesIdToken;
            case 3 -> SteamSessionTicket;
            case 4 -> UnityServicesIdToken;
            default -> throw new RuntimeException("invalid external auth type: " + type);
        };
    }
}
