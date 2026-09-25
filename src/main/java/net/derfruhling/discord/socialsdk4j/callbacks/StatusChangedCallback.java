package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ClientSocketResult;
import net.derfruhling.discord.socialsdk4j.ClientStatus;

@FunctionalInterface
public interface StatusChangedCallback {
    @SuppressWarnings("MissingJavadoc")
    void invoke(ClientStatus status, ClientSocketResult error, int errorDetail);
}
