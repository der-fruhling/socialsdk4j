package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

public record Relationship(
        Type discordType,
        Type gameType,
        long id,
        @Nullable User user
) {
    public Relationship(int discord, int game, long id, long userPointer) {
        this(Type.fromId(discord), Type.fromId(game), id, userPointer != 0 ? new User(userPointer, id) : null);
    }

    public enum Type {
        None,
        Friend,
        Blocked,
        PendingIncoming,
        PendingOutgoing,
        Implicit,
        Suggestion;

        public static Type fromId(int id) {
            return switch(id) {
                case 0 -> None;
                case 1 -> Friend;
                case 2 -> Blocked;
                case 3 -> PendingIncoming;
                case 4 -> PendingOutgoing;
                case 5 -> Implicit;
                case 6 -> Suggestion;
                default -> throw new RuntimeException("Invalid relationship enum value: " + id);
            };
        }
    }
}
