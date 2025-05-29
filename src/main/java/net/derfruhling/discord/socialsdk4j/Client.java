package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Client {
    long pointer = SocialSdk.createClientNative();

    public Client() {
        SocialSdk.ensureInitialized();
        long pointer = this.pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteClientNative(pointer));
    }

    public void runCallbacks() {
        SocialSdk.runCallbacksNative();
    }

    public interface CompletionCallback {
        void invoke();
    }

    public interface GenericResultCallback {
        void invoke(ClientResult result);
    }

    public interface AuthorizationCallback {
        void invoke(ClientResult result, String code, String redirectUri);
    }

    public interface StatusChangedCallbackNative {
        void invoke(int status, int error, int errorDetail);
    }

    public interface TokenExchangeCallbackNative {
        void invoke(ClientResult result, String accessToken, String refreshToken, int type, int expiresIn, String scopes);
    }

    public interface TokenExchangeCallback {
        void invoke(ClientResult result, String accessToken, String refreshToken, AuthorizationTokenType type, int expiresIn, String[] scopes);
    }

    public interface StatusChangedCallback {
        void invoke(Status status, ClientSocketError error, int errorDetail);
    }

    public interface GetUserGuildsCallback {
        void invoke(ClientResult result, GuildMinimal[] guilds);
    }

    public interface GetGuildChannelsCallback {
        void invoke(ClientResult result, GuildChannel[] channels);
    }

    public interface CreateOrJoinLobbyCallback {
        void invoke(ClientResult result, long lobbyId);
    }

    public interface LobbyExistenceChangedCallback {
        void invoke(long lobbyId);
    }

    public interface LobbyMemberChangedCallback {
        void invoke(long lobbyId, long userId);
    }

    public interface SendMessageCallback {
        void invoke(ClientResult result, long messageId);
    }

    public interface MessageIdCallback {
        void invoke(long messageId);
    }

    public interface MessageDeletedCallback {
        void invoke(long messageId, long channelId);
    }

    public interface RelationshipChangedCallback {
        void invoke(long userId, boolean isDiscordRelationshipUpdate);
    }

    public interface ActivityInviteCallback {
        void invoke(ActivityInvite invite);
    }

    public interface AcceptActivityInviteCallback {
        void invoke(ClientResult result, String joinSecret);
    }

    public interface ActivityJoinCallback {
        void invoke(String joinSecret);
    }

    public enum Status {
        Disconnected,
        Connecting,
        Connected,
        Ready,
        Reconnecting,
        Disconnecting,
        HttpWait;

        public static Status from(int status) {
            return switch (status) {
                case 0 -> Disconnected;
                case 1 -> Connecting;
                case 2 -> Connected;
                case 3 -> Ready;
                case 4 -> Reconnecting;
                case 5 -> Disconnecting;
                case 6 -> HttpWait;
                default -> throw new IllegalStateException("Unknown status: " + status);
            };
        }
    }

    private static native CodeVerifier createAuthorizationCodeVerifierNative(long pointer);
    private static native void authorizeNative(long pointer, long clientId, String scopes, String state, long codeChallenge, AuthorizationCallback callback);
    private static native void getProvisionalTokenNative(long pointer, long applicationId, int externalAuthType, String token, TokenExchangeCallbackNative callback);
    private static native void getTokenNative(long pointer, long applicationId, String code, String codeVerifier, String redirectUri, TokenExchangeCallbackNative callback);
    private static native void getTokenFromProvisionalMergeNative(long pointer, long applicationId, String code, String codeVerifier, String redirectUri, int externalAuthType, String externalAuthToken, TokenExchangeCallbackNative callback);
    private static native void updateTokenNative(long pointer, int type, String token, GenericResultCallback callback);
    private static native void connectNative(long pointer);
    private static native void disconnectNative(long pointer);
    private static native void abortAuthorizeNative(long pointer);
    private static native boolean isAuthenticatedNative(long pointer);
    private static native void provisionalMergeCompletedNative(long pointer, boolean success);
    private static native void refreshTokenNative(long pointer, long applicationId, String refreshToken, TokenExchangeCallbackNative callback);

    private static native void openConnectedGameSettingsInDiscordNative(long pointer, GenericResultCallback callback);
    private static native void setGameWindowPidNative(long pointer, int pid);
    private static native void setStatusChangedCallbackNative(long pointer, StatusChangedCallbackNative callback);
    private static native void updateRichPresenceNative(long pointer, long activity, @Nullable GenericResultCallback callback);
    private static native User getCurrentUserNative(long pointer);
    private static native @Nullable User getUserNative(long pointer, long userId);
    private static native Relationship getRelationshipNative(long pointer, long userId);
    private static native Relationship[] getRelationshipsNative(long pointer);
    private static native void sendDiscordFriendRequestNative(long pointer, String username, GenericResultCallback callback);
    private static native void sendDiscordFriendRequestByIdNative(long pointer, long userId, GenericResultCallback callback);
    private static native void sendGameFriendRequestNative(long pointer, String username, GenericResultCallback callback);
    private static native void sendGameFriendRequestByIdNative(long pointer, long userId, GenericResultCallback callback);
    private static native void acceptDiscordFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void acceptGameFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void cancelDiscordFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void cancelGameFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void setRelationshipCreatedCallbackNative(long pointer, RelationshipChangedCallback callback);
    private static native void setRelationshipDeletedCallbackNative(long pointer, RelationshipChangedCallback callback);
    private static native void rejectDiscordFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void rejectGameFriendRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void removeDiscordAndGameFriendNative(long pointer, long userId, GenericResultCallback callback);
    private static native void removeGameFriendNative(long pointer, long userId, GenericResultCallback callback);
    private static native void blockUserNative(long pointer, long userId, GenericResultCallback callback);
    private static native void unblockUserNative(long pointer, long userId, GenericResultCallback callback);

    private static native void getUserGuildsNative(long pointer, GetUserGuildsCallback callback);
    private static native void getGuildChannelsNative(long pointer, long guildId, GetGuildChannelsCallback callback);
    private static native void createOrJoinLobbyNative(long pointer, String secret, CreateOrJoinLobbyCallback callback);
    private static native void createOrJoinLobbyWithMetadataNative(long pointer, String secret, StringPair[] lobbyMeta, StringPair[] memberMeta, CreateOrJoinLobbyCallback callback);
    private static native void leaveLobbyNative(long pointer, long lobbyId, GenericResultCallback callback);
    private static native void setLobbyCreatedCallbackNative(long pointer, LobbyExistenceChangedCallback callback);
    private static native void setLobbyDeletedCallbackNative(long pointer, LobbyExistenceChangedCallback callback);
    private static native void setLobbyUpdatedCallbackNative(long pointer, LobbyExistenceChangedCallback callback);
    private static native void setLobbyMemberAddedCallbackNative(long pointer, LobbyMemberChangedCallback callback);
    private static native void setLobbyMemberRemovedCallbackNative(long pointer, LobbyMemberChangedCallback callback);
    private static native void setLobbyMemberUpdatedCallbackNative(long pointer, LobbyMemberChangedCallback callback);
    private static native void setMessageCreatedCallbackNative(long pointer, MessageIdCallback callback);
    private static native void setMessageDeletedCallbackNative(long pointer, MessageDeletedCallback callback);
    private static native void setMessageUpdatedCallbackNative(long pointer, MessageIdCallback callback);
    private static native @Nullable Message getMessageNative(long pointer, long messageId);
    private static native @Nullable Lobby getLobbyNative(long pointer, long lobbyId);
    private static native void linkChannelToLobbyNative(long pointer, long lobbyId, long channelId, GenericResultCallback callback);
    private static native void unlinkChannelFromLobbyNative(long pointer, long lobbyId, GenericResultCallback callback);
    private static native void sendLobbyMessageNative(long pointer, long lobbyId, String message, SendMessageCallback callback);
    private static native void sendLobbyMessageWithMetadataNative(long pointer, long lobbyId, String message, StringPair[] metadata, SendMessageCallback callback);
    private static native void sendUserMessageNative(long pointer, long userId, String message, SendMessageCallback callback);
    private static native void sendUserMessageWithMetadataNative(long pointer, long userId, String message, StringPair[] metadata, SendMessageCallback callback);

    private static native Call startCallNative(long pointer, long channelId);
    private static native void endCallNative(long pointer, long channelId, CompletionCallback callback);
    private static native void endCallsNative(long pointer, CompletionCallback callback);

    private static native void sendActivityInviteNative(long pointer, long userId, String content, GenericResultCallback callback);
    private static native void acceptActivityInviteNative(long pointer, ActivityInvite invite, AcceptActivityInviteCallback callback);
    private static native void sendActivityJoinRequestNative(long pointer, long userId, GenericResultCallback callback);
    private static native void sendActivityJoinRequestReplyNative(long pointer, ActivityInvite invite, GenericResultCallback callback);
    private static native void setActivityInviteCreatedCallbackNative(long pointer, ActivityInviteCallback callback);
    private static native void setActivityInviteUpdatedCallbackNative(long pointer, ActivityInviteCallback callback);
    private static native void setActivityJoinCallbackNative(long pointer, ActivityJoinCallback callback);

    private static native String getDefaultCommunicationsScopesNative();
    private static native String getDefaultPresenceScopesNative();

    static {
        SocialSdk.ensureInitialized();
    }

    public static final String[] COMMUNICATIONS_SCOPES = getDefaultCommunicationsScopesNative().split(" ");
    public static final String[] PRESENCE_SCOPES = getDefaultPresenceScopesNative().split(" ");

    public CodeVerifier createAuthorizationCodeVerifier() {
        return createAuthorizationCodeVerifierNative(pointer);
    }

    public void authorize(long clientId, String[] scopes, String state, CodeChallenge challenge, AuthorizationCallback callback) {
        authorizeNative(pointer, clientId, String.join(" ", scopes), state, challenge.pointer, callback);
    }

    public void abortAuthorize() {
        abortAuthorizeNative(pointer);
    }

    private static @NotNull TokenExchangeCallbackNative tokenExchangeCallback(TokenExchangeCallback callback) {
        return (result, accessToken, refreshToken, type, expiresIn, scopes) -> {
            callback.invoke(result, accessToken, refreshToken, AuthorizationTokenType.from(type), expiresIn, scopes != null ? scopes.split(" ") : null);
        };
    }

    public void getProvisionalToken(long applicationId, ExternalAuthType externalAuthType, String token, TokenExchangeCallback callback) {
        getProvisionalTokenNative(pointer, applicationId, externalAuthType.ordinal(), token, tokenExchangeCallback(callback));
    }

    public void getToken(long applicationId, String code, String codeVerifier, String redirectUri, TokenExchangeCallback callback) {
        getTokenNative(pointer, applicationId, code, codeVerifier, redirectUri, tokenExchangeCallback(callback));
    }

    public void getTokenFromProvisionalMerge(long applicationId, String code, String codeVerifier, String redirectUri, ExternalAuthType externalAuthType, String externalAuthToken, TokenExchangeCallback callback) {
        getTokenFromProvisionalMergeNative(pointer, applicationId, code, codeVerifier, redirectUri, externalAuthType.ordinal(), externalAuthToken, tokenExchangeCallback(callback));
    }

    public void updateToken(AuthorizationTokenType type, String token, GenericResultCallback callback) {
        updateTokenNative(pointer, type.ordinal(), token, callback);
    }

    public void connect() {
        connectNative(pointer);
    }

    public void disconnect() {
        disconnectNative(pointer);
    }

    public boolean isAuthenticated() {
        return isAuthenticatedNative(pointer);
    }

    public void provisionalMergeCompleted(boolean success) {
        provisionalMergeCompletedNative(pointer, success);
    }

    public void refreshToken(long applicationId, String refreshToken, TokenExchangeCallback callback) {
        refreshTokenNative(pointer, applicationId, refreshToken, tokenExchangeCallback(callback));
    }

    public void openConnectedGameSettingsInDiscord(GenericResultCallback callback) {
        openConnectedGameSettingsInDiscordNative(pointer, callback);
    }

    public void setGameWindowPid(int pid) {
        setGameWindowPidNative(pointer, pid);
    }

    public void setStatusChangedCallback(StatusChangedCallback callback) {
        setStatusChangedCallbackNative(pointer, (status, error, errorDetail) -> {
            callback.invoke(Status.from(status), ClientSocketError.from(error), errorDetail);
        });
    }

    public void updateRichPresence(Activity activity, @Nullable GenericResultCallback callback) {
        updateRichPresenceNative(pointer, activity.pointer, callback);
    }

    public void updateRichPresence(Activity activity) {
        updateRichPresenceNative(pointer, activity.pointer, null);
    }

    public @NotNull User getUser() {
        return getCurrentUserNative(pointer);
    }

    public @Nullable User getUser(long userId) {
        return getUserNative(pointer, userId);
    }

    public void getUserGuilds(GetUserGuildsCallback callback) {
        getUserGuildsNative(pointer, callback);
    }

    public void getGuildChannels(long guildId, GetGuildChannelsCallback callback) {
        getGuildChannelsNative(pointer, guildId, callback);
    }

    public void createOrJoinLobby(String secret, CreateOrJoinLobbyCallback callback) {
        createOrJoinLobbyNative(pointer, secret, callback);
    }

    public void createOrJoinLobby(String secret, Map<String, String> lobbyMeta, Map<String, String> memberMeta, CreateOrJoinLobbyCallback callback) {
        createOrJoinLobbyWithMetadataNative(
                pointer,
                secret,
                lobbyMeta.entrySet().stream()
                        .map(v -> new StringPair(v.getKey(), v.getValue()))
                        .toArray(StringPair[]::new),
                memberMeta.entrySet().stream()
                        .map(v -> new StringPair(v.getKey(), v.getValue()))
                        .toArray(StringPair[]::new),
                callback
        );
    }

    public void leaveLobby(long lobbyId, GenericResultCallback callback) {
        leaveLobbyNative(pointer, lobbyId, callback);
    }

    public void setLobbyCreatedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyCreatedCallbackNative(pointer, callback);
    }

    public void setLobbyDeletedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyDeletedCallbackNative(pointer, callback);
    }

    public void setLobbyUpdatedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyUpdatedCallbackNative(pointer, callback);
    }

    public void setLobbyMemberAddedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberAddedCallbackNative(pointer, callback);
    }

    public void setLobbyMemberRemovedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberRemovedCallbackNative(pointer, callback);
    }

    public void setLobbyMemberUpdatedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberUpdatedCallbackNative(pointer, callback);
    }

    public void setMessageCreatedCallback(MessageIdCallback callback) {
        setMessageCreatedCallbackNative(pointer, callback);
    }

    public void setMessageDeletedCallback(MessageDeletedCallback callback) {
        setMessageDeletedCallbackNative(pointer, callback);
    }

    public void setMessageUpdatedCallback(MessageIdCallback callback) {
        setMessageUpdatedCallbackNative(pointer, callback);
    }

    public @Nullable Message getMessage(long messageId) {
        return getMessageNative(pointer, messageId);
    }

    public @Nullable Lobby getLobby(long lobbyId) {
        return getLobbyNative(pointer, lobbyId);
    }

    public void linkChannelToLobby(long lobbyId, long channelId, GenericResultCallback callback) {
        linkChannelToLobbyNative(pointer, lobbyId, channelId, callback);
    }

    public void unlinkChannelFromLobby(long lobbyId, GenericResultCallback callback) {
        unlinkChannelFromLobbyNative(pointer, lobbyId, callback);
    }

    public void sendLobbyMessage(long lobbyId, String message, SendMessageCallback callback) {
        sendLobbyMessageNative(pointer, lobbyId, message, callback);
    }

    public void sendLobbyMessageWithMetadata(long lobbyId, String message, Map<String, String> metadata, SendMessageCallback callback) {
        sendLobbyMessageWithMetadataNative(pointer, lobbyId, message, metadata.entrySet().stream()
                .map(v -> new StringPair(v.getKey(), v.getValue()))
                .toArray(StringPair[]::new), callback);
    }

    public void sendUserMessage(long userId, String message, SendMessageCallback callback) {
        sendUserMessageNative(pointer, userId, message, callback);
    }

    public void sendUserMessageWithMetadata(long userId, String message, Map<String, String> metadata, SendMessageCallback callback) {
        sendUserMessageWithMetadataNative(pointer, userId, message, metadata.entrySet().stream()
                .map(v -> new StringPair(v.getKey(), v.getValue()))
                .toArray(StringPair[]::new), callback);
    }

    public Call startCall(long channelId) {
        return startCallNative(pointer, channelId);
    }

    public void endCall(long channelId, CompletionCallback callback) {
        endCallNative(pointer, channelId, callback);
    }

    public void endCalls(CompletionCallback callback) {
        endCallsNative(pointer, callback);
    }

    public Relationship getRelationship(long userId) {
        return getRelationshipNative(pointer, userId);
    }

    public Relationship[] getRelationships() {
        return getRelationshipsNative(pointer);
    }

    public void sendDiscordFriendRequest(String username, GenericResultCallback callback) {
        sendDiscordFriendRequestNative(pointer, username, callback);
    }

    public void sendDiscordFriendRequest(long userId, GenericResultCallback callback) {
        sendDiscordFriendRequestByIdNative(pointer, userId, callback);
    }

    public void sendGameFriendRequest(String username, GenericResultCallback callback) {
        sendGameFriendRequestNative(pointer, username, callback);
    }

    public void sendGameFriendRequest(long userId, GenericResultCallback callback) {
        sendGameFriendRequestByIdNative(pointer, userId, callback);
    }

    public void acceptDiscordFriendRequest(long userId, GenericResultCallback callback) {
        acceptDiscordFriendRequestNative(pointer, userId, callback);
    }

    public void acceptGameFriendRequest(long userId, GenericResultCallback callback) {
        acceptGameFriendRequestNative(pointer, userId, callback);
    }

    public void cancelDiscordFriendRequest(long userId, GenericResultCallback callback) {
        cancelDiscordFriendRequestNative(pointer, userId, callback);
    }

    public void cancelGameFriendRequest(long userId, GenericResultCallback callback) {
        cancelGameFriendRequestNative(pointer, userId, callback);
    }

    public void setRelationshipCreatedCallback(RelationshipChangedCallback callback) {
        setRelationshipCreatedCallbackNative(pointer, callback);
    }

    public void setRelationshipDeletedCallback(RelationshipChangedCallback callback) {
        setRelationshipDeletedCallbackNative(pointer, callback);
    }

    public void rejectDiscordFriendRequest(long userId, GenericResultCallback callback) {
        rejectDiscordFriendRequestNative(pointer, userId, callback);
    }

    public void rejectGameFriendRequest(long userId, GenericResultCallback callback) {
        rejectGameFriendRequestNative(pointer, userId, callback);
    }

    public void removeDiscordAndGameFriend(long userId, GenericResultCallback callback) {
        removeDiscordAndGameFriendNative(pointer, userId, callback);
    }

    public void removeGameFriend(long userId, GenericResultCallback callback) {
        removeGameFriendNative(pointer, userId, callback);
    }

    public void sendActivityInvite(long userId, @Nullable String content, GenericResultCallback callback) {
        sendActivityInviteNative(pointer, userId, content == null ? "" : content, callback);
    }

    public void acceptActivityInvite(ActivityInvite invite, AcceptActivityInviteCallback callback) {
        acceptActivityInviteNative(pointer, invite, callback);
    }

    public void sendActivityJoinRequest(long userId, GenericResultCallback callback) {
        sendActivityJoinRequestNative(pointer, userId, callback);
    }

    public void sendActivityJoinRequestReply(ActivityInvite invite, GenericResultCallback callback) {
        sendActivityJoinRequestReplyNative(pointer, invite, callback);
    }

    public void setActivityInviteCreatedCallback(ActivityInviteCallback callback) {
        setActivityInviteCreatedCallbackNative(pointer, callback);
    }

    public void setActivityInviteUpdatedCallback(ActivityInviteCallback callback) {
        setActivityInviteUpdatedCallbackNative(pointer, callback);
    }

    public void setActivityJoinCallback(ActivityJoinCallback callback) {
        setActivityJoinCallbackNative(pointer, callback);
    }

    public void blockUser(long userId, GenericResultCallback callback) {
        blockUserNative(pointer, userId, callback);
    }

    public void unblockUser(long userId, GenericResultCallback callback) {
        unblockUserNative(pointer, userId, callback);
    }
}
