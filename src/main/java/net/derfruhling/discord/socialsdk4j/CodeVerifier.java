package net.derfruhling.discord.socialsdk4j;

/**
 * Represents a code verifier bundle provided by the SDK for client-side
 * authentication.
 *
 * @param challenge A challenge passed to {@link Client#authorize}
 * @param verifier A verifier passed to {@link Client#getToken} or {@link Client#getTokenFromProvisionalMerge}
 */
public record CodeVerifier(
        CodeChallenge challenge,
        String verifier
) {
}
