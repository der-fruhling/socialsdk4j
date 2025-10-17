package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.NotNull;

public class DiscordException extends RuntimeException {
    public DiscordException(@NotNull ClientResult result) {
        super(result.type() + ": " + result.message());
        assert !result.isSuccess();
    }
}
