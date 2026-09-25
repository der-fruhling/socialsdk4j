package net.derfruhling.discord.socialsdk4j.callbacks;

import net.derfruhling.discord.socialsdk4j.ActivityInvite;

@FunctionalInterface
public interface ActivityInviteCallback {
    @SuppressWarnings({"unused", "MissingJavadoc"})
    void invoke(ActivityInvite invite);
}
