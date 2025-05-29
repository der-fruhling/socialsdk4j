package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

public record GuildChannel(
        long id,
        String name,
        boolean isLinkable,
        boolean isFullyPublic,
        @Nullable LinkedLobby linkedLobby
) {
}
