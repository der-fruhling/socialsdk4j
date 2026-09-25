package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.AuthorizationTokenType;
import net.derfruhling.discord.socialsdk4j.ClientResult;

@FunctionalInterface
public interface TokenExchangeCallback {
    @SuppressWarnings("MissingJavadoc")
    void invoke(
            ClientResult result,
            String accessToken,
            String refreshToken,
            AuthorizationTokenType type,
            int expiresIn,
            String[] scopes
    );
}
