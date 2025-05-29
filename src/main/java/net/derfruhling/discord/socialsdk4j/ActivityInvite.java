package net.derfruhling.discord.socialsdk4j;

public record ActivityInvite(
        long senderId,
        long channelId,
        long messageId,
        ActionType type,
        long applicationId,
        String partyId,
        String sessionId,
        boolean isValid
) {
    public ActivityInvite(long senderId, long channelId, long messageId, ActionType type, long applicationId, String partyId, String sessionId) {
        this(senderId, channelId, messageId, type, applicationId, partyId, sessionId, true);
    }

    public ActivityInvite(long senderId, long channelId, long messageId, int type, long applicationId, String partyId, String sessionId, boolean isValid) {
        this(senderId, channelId, messageId, ActionType.from(type), applicationId, partyId, sessionId, isValid);
    }

    public enum ActionType {
        Join(1),
        JoinRequest(5);

        public final int value;

        ActionType(int value) {
            this.value = value;
        }

        public static ActionType from(int type) {
            return switch (type) {
                case 1 -> Join;
                case 5 -> JoinRequest;
                default -> throw new IllegalArgumentException("Unknown activity invite action type: " + type);
            };
        }
    }
}
