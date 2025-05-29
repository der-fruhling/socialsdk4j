package net.derfruhling.discord.socialsdk4j;

public enum ExternalAuthType {
    OIDC,
    EpicOnlineServicesAccessToken,
    EpicOnlineServicesIdToken,
    SteamSessionTicket,
    /**
     * @deprecated You probably don't want to use this.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    UnityServicesIdToken;

    public static ExternalAuthType from(int type) {
        return switch(type) {
            case 0 -> OIDC;
            case 1 -> EpicOnlineServicesAccessToken;
            case 2 -> EpicOnlineServicesIdToken;
            case 3 -> SteamSessionTicket;
            case 4 -> UnityServicesIdToken;
            default -> throw new RuntimeException("invalid external auth type: " + type);
        };
    }
}
