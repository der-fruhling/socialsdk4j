package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a relationship with a user.
 *
 * @param discordType The Discord relationship with a user.
 * @param gameType The game relationship with a user.
 * @param id ID of the user this object represents.
 * @param user Optional {@link User} handle.
 */
public record Relationship(
        Type discordType,
        Type gameType,
        long id,
        @Nullable User user
) {
    Relationship(int discord, int game, long id, long userPointer) {
        this(Type.fromId(discord), Type.fromId(game), id, userPointer != 0 ? new User(userPointer, id) : null);
    }

    /**
     * Possible types of relationship.
     */
    public enum Type {
        /**
         * No relationship, user is not friends and not blocked.
         *
         * @see Client#sendDiscordFriendRequest(long, Client.GenericResultCallback)
         * @see Client#sendDiscordFriendRequest(String, Client.GenericResultCallback)
         * @see Client#sendGameFriendRequest(long, Client.GenericResultCallback)
         * @see Client#sendGameFriendRequest(String, Client.GenericResultCallback)
         * @see Client#blockUser
         */
        None,
        /**
         * User is a friend.
         *
         * @see Client#removeDiscordAndGameFriend
         * @see Client#removeGameFriend
         */
        Friend,
        /**
         * User is blocked. If this is the case, it will be set for both
         * relationships.
         *
         * @see Client#unblockUser
         */
        Blocked,
        /**
         * This user has sent the currently logged-in user a friend request,
         * and is waiting for it to be accepted or rejected.
         *
         * @see Client#acceptDiscordFriendRequest
         * @see Client#acceptGameFriendRequest
         * @see Client#rejectDiscordFriendRequest
         * @see Client#rejectGameFriendRequest
         */
        PendingIncoming,
        /**
         * The currently logged-in user has sent this user a friend request,
         * and is waiting ffor it to be accepted or rejected.
         *
         * @see Client#cancelDiscordFriendRequest
         * @see Client#cancelGameFriendRequest
         */
        PendingOutgoing,

        /**
         * @deprecated You should probably not use this.
         */
        @Deprecated
        Implicit,

        /**
         * @deprecated You should probably not use this.
         */
        @Deprecated
        Suggestion;

        static Type fromId(int id) {
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
