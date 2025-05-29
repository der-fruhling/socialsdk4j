package net.derfruhling.discord.socialsdk4j;

public record CodeVerifier(
        CodeChallenge challenge,
        String verifier
) {
}
