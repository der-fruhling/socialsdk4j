package net.derfruhling.discord.socialsdk4j;

public record ActivityInvite(
        long senderId,
        long channelId,
        long messageId,
        Type type,
        long applicationId,
        String partyId,
        String sessionId,
        boolean isValid
) {
    /**
     * Constructs a new activity invite.
     *
     * @param senderId User ID of the invite's sender.
     * @param channelId Channel ID the invite was sent in.
     * @param messageId Message ID of the invite.
     * @param type Type of the invite, determines how it must be handled.
     * @param applicationId Application ID the invite was sent from.
     * @param partyId Party ID set by the application in it's rich presence code.
     * @param sessionId Session ID of the user who sent the invite.
     */
    public ActivityInvite(long senderId, long channelId, long messageId, Type type, long applicationId, String partyId, String sessionId) {
        this(senderId, channelId, messageId, type, applicationId, partyId, sessionId, true);
    }

    ActivityInvite(long senderId, long channelId, long messageId, int type, long applicationId, String partyId, String sessionId, boolean isValid) {
        this(senderId, channelId, messageId, Type.from(type), applicationId, partyId, sessionId, isValid);
    }

    /**
     * Specifies the type of activity invite this is.
     */
    public enum Type {
        /**
         * The invite was sent by a party member to invite someone to the
         * party, and must be accepted by the targeted user.
         *
         * @see Client#acceptActivityInvite(ActivityInvite, Client.AcceptActivityInviteCallback)
         */
        Join(1),

        /**
         * The invite was sent by a non-member of the party to request an
         * activity invite from the targeted user.
         *
         * @see Client#sendActivityJoinRequestReply(ActivityInvite, Client.GenericResultCallback)
         */
        JoinRequest(5);

        final int value;

        Type(int value) {
            this.value = value;
        }

        static Type from(int type) {
            return switch (type) {
                case 1 -> Join;
                case 5 -> JoinRequest;
                default -> throw new IllegalArgumentException("Unknown activity invite type: " + type);
            };
        }
    }
}
