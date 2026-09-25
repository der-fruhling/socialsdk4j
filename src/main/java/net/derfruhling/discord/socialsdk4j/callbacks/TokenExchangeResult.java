package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.AuthorizationTokenType;

public record TokenExchangeResult(
        String accessToken,
        String refreshToken,
        AuthorizationTokenType type,
        int expiresIn,
        String[] scopes
) {
}
