package net.derfruhling.discord.socialsdk4j;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The main part of the SocialSDK. Most actions are initiated by calling
 * methods on this object.
 */
public class Client {
    long pointer = SocialSdk.createClientNative();

    public Client() {
        SocialSdk.ensureInitialized();
        long pointer = this.pointer;
        SocialSdk.cleaner.register(this, () -> SocialSdk.deleteClientNative(pointer));
    }

    /**
     * Runs all pending callbacks the SocialSDK wants to call. This must be run
     * in order to make use of the SocialSDK properly.
     */
    public void runCallbacks() {
        SocialSdk.runCallbacksNative();
    }

    public interface CompletionCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke();
    }

    public interface GenericResultCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result);
    }

    public interface AuthorizationCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, String code, String redirectUri);
    }

    public interface StatusChangedCallbackNative {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(int status, int error, int errorDetail);
    }

    public interface TokenExchangeCallbackNative {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, String accessToken, String refreshToken, int type, int expiresIn, String scopes);
    }

    public interface TokenExchangeCallback {
        @SuppressWarnings("MissingJavadoc")
        void invoke(ClientResult result, String accessToken, String refreshToken, AuthorizationTokenType type, int expiresIn, String[] scopes);
    }

    public interface StatusChangedCallback {
        @SuppressWarnings("MissingJavadoc")
        void invoke(Status status, ClientSocketResult error, int errorDetail);
    }

    public interface GetUserGuildsCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, GuildMinimal[] guilds);
    }

    public interface GetGuildChannelsCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, GuildChannel[] channels);
    }

    public interface CreateOrJoinLobbyCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, long lobbyId);
    }

    public interface LobbyExistenceChangedCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(long lobbyId);
    }

    public interface LobbyMemberChangedCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(long lobbyId, long userId);
    }

    public interface SendMessageCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, long messageId);
    }

    public interface MessageIdCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(long messageId);
    }

    public interface MessageDeletedCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(long messageId, long channelId);
    }

    public interface RelationshipChangedCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(long userId, boolean isDiscordRelationshipUpdate);
    }

    public interface ActivityInviteCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ActivityInvite invite);
    }

    public interface AcceptActivityInviteCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, String joinSecret);
    }

    public interface ActivityJoinCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(String joinSecret);
    }

    public interface GetMessagesCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, Message[] messages);
    }

    public interface GetUserMessageSummariesCallback {
        @SuppressWarnings({"unused", "MissingJavadoc"})
        void invoke(ClientResult result, UserMessageSummary[] summaries);
    }

    /**
     * Represents the status of this client. The most important status is
     * {@link Status#Ready} as almost nothing can be done (other than
     * authorize) before the client reaches this state.
     *
     * @see Client#setStatusChangedCallback
     */
    public enum Status {
        /**
         * The client is not connected to Discord.
         */
        Disconnected,
        /**
         * The client is actively connecting to Discord.
         */
        Connecting,
        /**
         * The client is connected to Discord, but not ready to do stuff.
         */
        Connected,
        /**
         * The client is ready for action!
         */
        Ready,
        /**
         * The client is reconnecting to Discord.
         */
        Reconnecting,
        /**
         * The client is disconnecting from Discord.
         */
        Disconnecting,
        /**
         * Waiting on HTTP?
         */
        HttpWait;

        static Status from(int status) {
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
    private static native @Nullable User getCurrentUserV2Native(long pointer);
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
    private static native void getLobbyMessagesWithLimitNative(long pointer, long lobbyId, int limit, GetMessagesCallback callback);
    private static native void getUserMessagesWithLimitNative(long pointer, long userId, int limit, GetMessagesCallback callback);
    private static native void getUserMessageSummariesNative(long pointer, GetUserMessageSummariesCallback callback);

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

    /**
     * Contains the default communications scopes. If you want to include
     * the SocialSDK's chat features in your game, you'll want to use this.
     */
    public static final String[] COMMUNICATIONS_SCOPES = getDefaultCommunicationsScopesNative().split(" ");

    /**
     * Contains the default presence scopes. If you just want to use the
     * SocialSDK's rich presence features, you'll want to use this instead of
     * {@link Client#COMMUNICATIONS_SCOPES}. Note that the SocialSDK is
     * quite large for a library to do this, you may want to consider
     * using a smaller-sized and more dedicated library if you just want
     * rich-presence.
     */
    public static final String[] PRESENCE_SCOPES = getDefaultPresenceScopesNative().split(" ");

    /**
     * Creates a {@link CodeVerifier} that can be used to authenticate a
     * standard Discord user. This method is included to mimic the way that
     * the C++ SocialSDK handles this as separate object.
     *
     * @return A new {@link CodeVerifier}.
     */
    @Contract("-> new")
    public CodeVerifier createAuthorizationCodeVerifier() {
        return createAuthorizationCodeVerifierNative(pointer);
    }

    /**
     * Authorizes a standard Discord user.
     *
     * @param clientId The client ID of your application. You can find this in
     *                 the developer panel.
     * @param scopes The scopes to request. You can include most scopes here
     *               as well as the SocialSDK scopes contained in
     *               {@link Client#COMMUNICATIONS_SCOPES} and
     *               {@link Client#PRESENCE_SCOPES}.
     * @param state A randomized, unique state value used to correlate a user's
     *              authorization request with their authenticated state. This
     *              should be random and not predictable.
     * @param challenge The challenge value retrieved from
     *                  {@link CodeVerifier#challenge()}
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void authorize(long clientId, @NotNull String[] scopes, @NotNull String state, @NotNull CodeChallenge challenge, @NotNull AuthorizationCallback callback) {
        authorizeNative(pointer, clientId, String.join(" ", scopes), state, challenge.pointer, callback);
    }

    /**
     * Aborts the ongoing authorization.
     */
    public void abortAuthorize() {
        abortAuthorizeNative(pointer);
    }

    private static @NotNull TokenExchangeCallbackNative tokenExchangeCallback(@NotNull TokenExchangeCallback callback) {
        return (result, accessToken, refreshToken, type, expiresIn, scopes) -> {
            callback.invoke(result, accessToken, refreshToken, AuthorizationTokenType.from(type), expiresIn, scopes != null ? scopes.split(" ") : null);
        };
    }

    /**
     * Retrieves the provisional Discord account token.
     *
     * @param applicationId The application ID of your application. This can be
     *                      found in the developer panel. AFAIK, this is always
     *                      the same as a client ID.
     * @param externalAuthType The kind of external auth service to try
     *                         authorizing with.
     * @param token The actual token acquired from the external auth service.
     *              This will be sent to Discord.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void getProvisionalToken(long applicationId, @NotNull ExternalAuthType externalAuthType, @NotNull String token, @NotNull TokenExchangeCallback callback) {
        getProvisionalTokenNative(pointer, applicationId, externalAuthType.ordinal(), token, tokenExchangeCallback(callback));
    }

    /**
     * Retrieves the Discord bearer token after a successful authorization.
     * The documentation recommends using the provisional merge function if
     * you already have the token required to ensure that one user does not
     * end up with both a real account and a provisional account.
     *
     * @param applicationId The application ID of your application. This can be
     *                      found in the developer panel. AFAIK, this is always
     *                      the same as a client ID.
     * @param code The code obtained from a successful authorization.
     * @param codeVerifier The verifier retrieved from {@link CodeVerifier#verifier()}
     * @param redirectUri The redirect URI used.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#authorize(long, String[], String, CodeChallenge, AuthorizationCallback)
     * @see Client#getTokenFromProvisionalMerge(long, String, String, String, ExternalAuthType, String, TokenExchangeCallback)
     */
    public void getToken(long applicationId, @NotNull String code, @NotNull String codeVerifier, @NotNull String redirectUri, @NotNull TokenExchangeCallback callback) {
        getTokenNative(pointer, applicationId, code, codeVerifier, redirectUri, tokenExchangeCallback(callback));
    }

    /**
     * Retrieves the Discord bearer token after a successful authorization,
     * additionally authorizing with the external auth service and merging
     * the provisional account into this real account if necessary.
     *
     * @param applicationId The application ID of your application. This can be
     *                      found in the developer panel. AFAIK, this is always
     *                      the same as a client ID.
     * @param code The code obtained from a successful authorization.
     * @param codeVerifier The verifier retrieved from {@link CodeVerifier#verifier()}
     * @param redirectUri The redirect URI used.
     * @param externalAuthType The kind of external auth service to try
     *                         authorizing with.
     * @param externalAuthToken The actual token acquired from the external
     *                          auth service. This will be sent to Discord.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#authorize(long, String[], String, CodeChallenge, AuthorizationCallback)
     * @see Client#getToken(long, String, String, String, TokenExchangeCallback)
     * @see Client#getProvisionalToken(long, ExternalAuthType, String, TokenExchangeCallback)
     */
    public void getTokenFromProvisionalMerge(long applicationId, @NotNull String code, @NotNull String codeVerifier, @NotNull String redirectUri, @NotNull ExternalAuthType externalAuthType, @NotNull String externalAuthToken, @NotNull TokenExchangeCallback callback) {
        getTokenFromProvisionalMergeNative(pointer, applicationId, code, codeVerifier, redirectUri, externalAuthType.ordinal(), externalAuthToken, tokenExchangeCallback(callback));
    }

    /**
     * Updates the token used by this client. Call {@link Client#connect()}
     * afterward to connect to Discord.
     *
     * @param type Type of the token. You should set this to
     *             {@link AuthorizationTokenType#Bearer}.
     * @param token The Discord token obtained from the authorization flow.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void updateToken(@NotNull AuthorizationTokenType type, @NotNull String token, @NotNull GenericResultCallback callback) {
        updateTokenNative(pointer, type.ordinal(), token, callback);
    }

    /**
     * Connects to Discord using the set token. The client should use
     * {@link Client#setStatusChangedCallback(StatusChangedCallback)} to handle
     * the transition to {@link Status#Ready}, indicating that the client is
     * ready for use.
     */
    public void connect() {
        connectNative(pointer);
    }

    /**
     * Disconnects the client from Discord.
     */
    public void disconnect() {
        disconnectNative(pointer);
    }

    /**
     * @return {@code true} if this client is authenticated.
     */
    public boolean isAuthenticated() {
        return isAuthenticatedNative(pointer);
    }

    /**
     * Call this to indicate that the user successfully merged their
     * provisional account or declined to do so. This is needed when
     * certain actions are requested as provisional accounts cannot
     * perform them.
     *
     * @param success If {@code true}, the provisional merge was successful
     *                and the SDK will retry the action that needed that.
     *                If {@code false}, the SDK will cancel said operation.
     */
    public void provisionalMergeCompleted(boolean success) {
        provisionalMergeCompletedNative(pointer, success);
    }

    /**
     * Refreshes the access token using a refresh token obtained from the
     * authorization flow.
     *
     * @param applicationId The application ID of your application. This can be
     *                      found in the developer panel. AFAIK, this is always
     *                      the same as a client ID.
     * @param refreshToken The refresh token, not the access token.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#updateToken(AuthorizationTokenType, String, GenericResultCallback)
     */
    public void refreshToken(long applicationId, @NotNull String refreshToken, @NotNull TokenExchangeCallback callback) {
        refreshTokenNative(pointer, applicationId, refreshToken, tokenExchangeCallback(callback));
    }

    /**
     * Opens the connected game settings in the Discord app if it is found,
     * otherwise opens a browser window. Only works for real Discord accounts.
     *
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void openConnectedGameSettingsInDiscord(@NotNull GenericResultCallback callback) {
        openConnectedGameSettingsInDiscordNative(pointer, callback);
    }

    /**
     * <p>Set the process ID of the program that will create the game window. Used
     * for the overlay.</p>
     *
     * <p>Discord should auto-detect this well enough so you don't really need
     * to use this unless you're using the SocialSDK in a launcher or
     * something.</p>
     *
     * @param pid Process ID of the game.
     */
    public void setGameWindowPid(int pid) {
        setGameWindowPidNative(pointer, pid);
    }

    /**
     * Sets a handler that will be called whenever the client changes it's
     * status.
     *
     * @param callback Callback that will be called whenever the client changes
     *                 it's status.
     *
     * @see Client#runCallbacks()
     */
    public void setStatusChangedCallback(@NotNull StatusChangedCallback callback) {
        setStatusChangedCallbackNative(pointer, (status, error, errorDetail) -> {
            callback.invoke(Status.from(status), ClientSocketResult.from(error), errorDetail);
        });
    }

    /**
     * Updates rich presence information.
     *
     * @param activity An {@link ActivityBuilder} object.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void updateRichPresence(@NotNull ActivityBuilder activity, @NotNull GenericResultCallback callback) {
        updateRichPresenceNative(pointer, activity.pointer, callback);
    }

    /**
     * Retrieves the current {@link User} that's logged in with the SDK. Must
     * not be called before the client is ready.
     *
     * @return A {@link User} object, never null.
     * @deprecated Use {@link Client#getCurrentUser()} instead.
     */
    @Deprecated(forRemoval = true, since = "SocialSDK 1.6, SocialSDK4J 1.0")
    public @NotNull User getUser() {
        return getCurrentUserNative(pointer);
    }

    /**
     * Retrieves the current {@link User} that's logged in with the SDK.
     *
     * @return A {@link User} object. May be null if no value exists.
     */
    public @Nullable User getCurrentUser() {
        return getCurrentUserV2Native(pointer);
    }

    /**
     * Retrieves the {@link User} object associated with a particular user ID.
     * Must not be called before the client is ready.
     *
     * @param userId The ID of the user.
     * @return A {@link User} object, or null if the user is not found.
     */
    public @Nullable User getUser(long userId) {
        return getUserNative(pointer, userId);
    }

    /**
     * Retrieves a list of guilds that the logged in user is a part of. Used
     * for linking lobbies to channels.
     * Must not be called before the client is ready. Additionally, this will
     * probably fail if you use it on a provisional account.
     *
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void getUserGuilds(GetUserGuildsCallback callback) {
        getUserGuildsNative(pointer, callback);
    }

    /**
     * Retrieves a list of channels that the logged-in user can view in a
     * particular guild. Only text channels are returned.
     *
     * @param guildId The guild ID.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#getUserGuilds(GetUserGuildsCallback)
     */
    public void getGuildChannels(long guildId, GetGuildChannelsCallback callback) {
        getGuildChannelsNative(pointer, guildId, callback);
    }

    /**
     * Joins a lobby. If the lobby does not exist, creates it.
     *
     * @param secret A unique secret identifying the lobby. Any user with the
     *               same secret can join the same lobby provided they're
     *               using the same application.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#createOrJoinLobby(String, Map, Map, CreateOrJoinLobbyCallback)
     */
    public void createOrJoinLobby(String secret, CreateOrJoinLobbyCallback callback) {
        createOrJoinLobbyNative(pointer, secret, callback);
    }

    /**
     * Joins a lobby. If the lobby does not exist, creates it.
     *
     * @param secret A unique secret identifying the lobby. Any user with the
     *               same secret can join the same lobby provided they're
     *               using the same application.
     * @param lobbyMeta If the lobby is created, this metadata will be stored.
     *                  Otherwise, the existing metadata will be kept intact.
     * @param memberMeta After joining the lobby, the new member will have
     *                   this metadata.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#createOrJoinLobby(String, CreateOrJoinLobbyCallback)
     */
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

    /**
     * Leaves a lobby.
     *
     * @param lobbyId The ID of the lobby to leave.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void leaveLobby(long lobbyId, GenericResultCallback callback) {
        leaveLobbyNative(pointer, lobbyId, callback);
    }

    /**
     * Sets a handler that the SDK will call when it becomes part of a lobby.
     *
     * @param callback A callback that will be executed when a lobby is joined.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyCreatedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyCreatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when it is no longer part of a
     * lobby.
     *
     * @param callback A callback that will be executed when a lobby is left.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyDeletedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyDeletedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a lobby is updated.
     *
     * @param callback A callback that will be executed when a lobby is
     *                 updated.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyUpdatedCallback(LobbyExistenceChangedCallback callback) {
        setLobbyUpdatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a user joins a lobby that the
     * currently logged-in user is a part of.
     *
     * @param callback A callback that will be executed when a lobby member
     *                 joins.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyMemberAddedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberAddedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a user leaves a lobby that the
     * currently logged-in user is a part of.
     *
     * @param callback A callback that will be executed when a lobby member
     *                 leaves.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyMemberRemovedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberRemovedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a user in a lobby that the
     * currently logged-in user is a part of is updated.
     *
     * @param callback A callback that will be executed when a lobby member
     *                 is updated.
     *
     * @see Client#runCallbacks()
     */
    public void setLobbyMemberUpdatedCallback(LobbyMemberChangedCallback callback) {
        setLobbyMemberUpdatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a message is created in a
     * lobby that the currently logged-in user is a part of, or that it
     * receives as part of a DM between users.
     *
     * @param callback A callback that will be executed when a message is created.
     *
     * @see Client#runCallbacks()
     */
    public void setMessageCreatedCallback(MessageIdCallback callback) {
        setMessageCreatedCallbackNative(pointer, callback);
    }

    /**
     * <p>Sets a handler that the SDK will call when a message is deleted in a
     * lobby that the currently logged-in user is a part of, or one that is
     * removed from a DM between users.</p>
     *
     * <p>Note that the message is not accessible via the SDK once this
     * callback is called. If the message content is needed for whatever
     * reason, it should be stored on creation.</p>
     *
     * @param callback A callback that will be executed when a message is deleted.
     *
     * @see Client#runCallbacks()
     */
    public void setMessageDeletedCallback(MessageDeletedCallback callback) {
        setMessageDeletedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call when a message is edited in a
     * lobby that the currently logged-in user is a part of, or one that is
     * removed from a DM between users.
     *
     * @param callback A callback that will be executed when a message is edited.
     *
     * @see Client#runCallbacks()
     */
    public void setMessageUpdatedCallback(MessageIdCallback callback) {
        setMessageUpdatedCallbackNative(pointer, callback);
    }

    /**
     * Attempts to retrieve a message by ID. The SDK keeps upto 25 message
     * in-memory. Messages sent before the SDK is started cannot be accessed
     * through this.
     *
     * @param messageId ID of the message to retrieve.
     * @return A {@link Message} handle, or {@code null} if it isn't known about.
     */
    public @Nullable Message getMessage(long messageId) {
        return getMessageNative(pointer, messageId);
    }

    /**
     * Attempts to retrieve a lobby by ID. The currently logged-in user must
     * be a member of the lobby to be visible to this method.
     *
     * @param lobbyId ID of the lobby to retrieve.
     * @return A {@link Lobby} handle.
     */
    public @Nullable Lobby getLobby(long lobbyId) {
        return getLobbyNative(pointer, lobbyId);
    }

    /**
     * Links a channel to a lobby. In order to do this, all of the following
     * requirements must be met:
     *
     * <ul>
     *     <li>the currently logged-in user must be a member of the lobby;</li>
     *     <li>as a lobby member, the flag {@code 1 << 0} must be set (which
     *     can only be achieved through the <a href="https://discord.com/developers/docs/social-sdk/server_apis.html">Server APIs</a>);</li>
     *     <li>the channel must be a text channel and cannot be age-restricted;</li>
     *     <li>the channel must not be linked to a lobby already;</li>
     *     <li>the currently logged-in user must have the Manage Channels, View
     *     Channel, and Send Messages permissions in the channel.</li>
     * </ul>
     *
     * @param lobbyId The ID of the lobby to update.
     * @param channelId The ID of the channel to link to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void linkChannelToLobby(long lobbyId, long channelId, GenericResultCallback callback) {
        linkChannelToLobbyNative(pointer, lobbyId, channelId, callback);
    }

    /**
     * Unlinks a channel from the provided lobby. The currently logged-in user
     * must be a member of the lobby and have the flag {@code 1 << 0} set in
     * their lobby member info, which can only be achieved through the
     * <a href="https://discord.com/developers/docs/social-sdk/server_apis.html">Server APIs</a>.
     *
     * @param lobbyId The ID of the lobby to update.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void unlinkChannelFromLobby(long lobbyId, GenericResultCallback callback) {
        unlinkChannelFromLobbyNative(pointer, lobbyId, callback);
    }

    /**
     * Sends a message to a lobby. The currently logged-in user must be a
     * member of the lobby.
     *
     * @param lobbyId The ID of the lobby to send a message to.
     * @param message The message. Markdown and message formatting is supported.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendLobbyMessage(long lobbyId, String message, SendMessageCallback callback) {
        sendLobbyMessageNative(pointer, lobbyId, message, callback);
    }

    /**
     * Sends a message to a lobby. The currently logged-in user must be a
     * member of the lobby.
     *
     * @param lobbyId The ID of the lobby to send a message to.
     * @param message The message, limited to 2,000 characters maximum.
     *                Markdown and message formatting is supported.
     * @param metadata Some metadata to include with the message. This will not
     *                 be visible to regular Discord users, but will be visible
     *                 within the SDK.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendLobbyMessageWithMetadata(long lobbyId, String message, Map<String, String> metadata, SendMessageCallback callback) {
        sendLobbyMessageWithMetadataNative(pointer, lobbyId, message, metadata.entrySet().stream()
                .map(v -> new StringPair(v.getKey(), v.getValue()))
                .toArray(StringPair[]::new), callback);
    }

    /**
     * Sends a message to a user.
     *
     * <p>
     * From the SDK documentation:
     * <blockquote>
     *     A message can be sent between two users in the following situations:
     *     <ol>
     *         <li>Both users are online and in the game and have not blocked each other</li>
     *         <li>Both users are friends with each other</li>
     *         <li>Both users share a mutual Discord server and have previously DM'd each other on Discord</li>
     *     </ol>
     * </blockquote>
     *
     * @param userId The ID of the user to send a message to.
     * @param message The message, limited to 2,000 characters maximum.
     *                Markdown and message formatting is supported.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendUserMessage(long userId, String message, SendMessageCallback callback) {
        sendUserMessageNative(pointer, userId, message, callback);
    }

    /**
     * Sends a message to a user.
     *
     * <p>
     * From the SDK documentation:
     * <blockquote>
     *     A message can be sent between two users in the following situations:
     *     <ol>
     *         <li>Both users are online and in the game and have not blocked each other</li>
     *         <li>Both users are friends with each other</li>
     *         <li>Both users share a mutual Discord server and have previously DM'd each other on Discord</li>
     *     </ol>
     * </blockquote>
     *
     * @param userId The ID of the user to send a message to.
     * @param message The message, limited to 2,000 characters maximum.
     *                Markdown and message formatting is supported.
     * @param metadata Some metadata to include with the message. This will not
     *                 be visible to regular Discord users, but will be visible
     *                 within the SDK.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendUserMessageWithMetadata(long userId, String message, Map<String, String> metadata, SendMessageCallback callback) {
        sendUserMessageWithMetadataNative(pointer, userId, message, metadata.entrySet().stream()
                .map(v -> new StringPair(v.getKey(), v.getValue()))
                .toArray(StringPair[]::new), callback);
    }

    /**
     * Retrieves some messages from a lobby the user is a part of. From the SDK
     * docs, max limit is 200 and messages from the last 72 hours are available.
     *
     * @param lobbyId ID of a lobby. The current user must be in this lobby.
     * @param limit The maximum number of messages to retrieve, max 200.
     * @param callback Called when the operation completes or fails. In case of
     *                 failure, the array passed here will be empty and the
     *                 result will contain info about the issue.
     */
    public void getLobbyMessagesWithLimit(long lobbyId, int limit, GetMessagesCallback callback) {
        getLobbyMessagesWithLimitNative(pointer, lobbyId, limit, callback);
    }

    /**
     * Retrieves some messages from a DM the user is a part of. From the SDK
     * docs, max limit is 200 and messages from the last 72 hours are available.
     *
     * @param userId ID of a user.
     * @param limit The maximum number of messages to retrieve, max 200.
     * @param callback Called when the operation completes or fails. In case of
     *                 failure, the array passed here will be empty and the
     *                 result will contain info about the issue.
     */
    public void getUserMessagesWithLimit(long userId, int limit, GetMessagesCallback callback) {
        getUserMessagesWithLimitNative(pointer, userId, limit, callback);
    }

    /**
     * <p>Starts a call in the specified voice channel. For a lobby, pass in the
     * lobby ID instead.</p>
     *
     * <p>On macOS, additional permissions are required for microphone access,
     * which must be set in order for this feature to work.</p>
     *
     * @param channelId ID of the channel to connect to.
     * @return A {@link Call} handle.
     */
    public Call startCall(long channelId) {
        return startCallNative(pointer, channelId);
    }

    /**
     * Ends the call in the specified channel.
     *
     * @param channelId ID of the channel to end the call in.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void endCall(long channelId, CompletionCallback callback) {
        endCallNative(pointer, channelId, callback);
    }

    /**
     * Ends all calls managed by the SDK.
     *
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void endCalls(CompletionCallback callback) {
        endCallsNative(pointer, callback);
    }

    /**
     * Gets the currently logged-in user's relationship with the specified
     * user.
     *
     * @param userId ID of the user to query.
     * @return A {@link Relationship} object detailing the relationship.
     */
    public Relationship getRelationship(long userId) {
        return getRelationshipNative(pointer, userId);
    }

    /**
     * Gets all the currently logged-in user's relationships.
     *
     * @return An array of {@link Relationship} objects detailing the relationships.
     */
    public Relationship[] getRelationships() {
        return getRelationshipsNative(pointer);
    }

    /**
     * Sends a Discord friend request by username.
     *
     * @param username Unique username of the user to send a friend request to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Relationship#discordType()
     */
    public void sendDiscordFriendRequest(String username, GenericResultCallback callback) {
        sendDiscordFriendRequestNative(pointer, username, callback);
    }

    /**
     * Sends a Discord friend request by user ID.
     *
     * @param userId ID of the user to send a friend request to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Relationship#discordType()
     */
    public void sendDiscordFriendRequest(long userId, GenericResultCallback callback) {
        sendDiscordFriendRequestByIdNative(pointer, userId, callback);
    }

    /**
     * Sends a game friend request by username.
     *
     * @param username Unique username of the user to send a friend request to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Relationship#gameType()
     */
    public void sendGameFriendRequest(String username, GenericResultCallback callback) {
        sendGameFriendRequestNative(pointer, username, callback);
    }

    /**
     * Sends a game friend request by user ID.
     *
     * @param userId ID of the user to send a friend request to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Relationship#gameType()
     */
    public void sendGameFriendRequest(long userId, GenericResultCallback callback) {
        sendGameFriendRequestByIdNative(pointer, userId, callback);
    }

    /**
     * Accepts an incoming Discord friend request.
     *
     * @param userId ID of the user sending the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void acceptDiscordFriendRequest(long userId, GenericResultCallback callback) {
        acceptDiscordFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Accepts an incoming game friend request.
     *
     * @param userId ID of the user sending the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void acceptGameFriendRequest(long userId, GenericResultCallback callback) {
        acceptGameFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Cancels an outgoing Discord friend request.
     *
     * @param userId ID of the user receiving the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void cancelDiscordFriendRequest(long userId, GenericResultCallback callback) {
        cancelDiscordFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Cancels an outgoing game friend request.
     *
     * @param userId ID of the user receiving the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void cancelGameFriendRequest(long userId, GenericResultCallback callback) {
        cancelGameFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Sets a handler that the SDK will call whenever a relationship is created
     * or updated.
     *
     * @param callback A callback that will be executed whenever a relationship
     *                 is created or updated.
     *
     * @see Client#runCallbacks()
     */
    public void setRelationshipCreatedCallback(RelationshipChangedCallback callback) {
        setRelationshipCreatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that the SDK will call whenever a relationship is deleted.
     *
     * @param callback A callback that will be executed whenever a relationship
     *                 is deleted.
     *
     * @see Client#runCallbacks()
     */
    public void setRelationshipDeletedCallback(RelationshipChangedCallback callback) {
        setRelationshipDeletedCallbackNative(pointer, callback);
    }

    /**
     * Rejects an incoming Discord friend request.
     *
     * @param userId ID of the user sending the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void rejectDiscordFriendRequest(long userId, GenericResultCallback callback) {
        rejectDiscordFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Rejects an incoming game friend request.
     *
     * @param userId ID of the user sending the request.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void rejectGameFriendRequest(long userId, GenericResultCallback callback) {
        rejectGameFriendRequestNative(pointer, userId, callback);
    }

    /**
     * Removes both Discord and game friend status with the specified user.
     *
     * @param userId ID of the user to sever the relationship with.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#removeGameFriend(long, GenericResultCallback)
     */
    public void removeDiscordAndGameFriend(long userId, GenericResultCallback callback) {
        removeDiscordAndGameFriendNative(pointer, userId, callback);
    }

    /**
     * Removes just game friend status with the specified user.
     *
     * @param userId ID of the user to sever the relationship with.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     * @see Client#removeDiscordAndGameFriend(long, GenericResultCallback)
     */
    public void removeGameFriend(long userId, GenericResultCallback callback) {
        removeGameFriendNative(pointer, userId, callback);
    }

    /**
     * Sends an activity invite to the specified user. You should have used
     * {@link Client#updateRichPresence(ActivityBuilder, GenericResultCallback)} first
     * to set additional fields required for invites to work.
     *
     * @param userId ID of the user to invite.
     * @param content Optional message content to be included.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendActivityInvite(long userId, @Nullable String content, GenericResultCallback callback) {
        sendActivityInviteNative(pointer, userId, content == null ? "" : content, callback);
    }

    /**
     * Accepts an incoming activity invite.
     *
     * @param invite An activity invite received from the SDK.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void acceptActivityInvite(ActivityInvite invite, AcceptActivityInviteCallback callback) {
        acceptActivityInviteNative(pointer, invite, callback);
    }

    /**
     * Sends an activity join request to the specified user.
     *
     * @param userId ID of the user to send a join request to.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendActivityJoinRequest(long userId, GenericResultCallback callback) {
        sendActivityJoinRequestNative(pointer, userId, callback);
    }

    /**
     * Accepts an incoming activity invite.
     *
     * @param invite An activity invite received from the SDK.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void sendActivityJoinRequestReply(ActivityInvite invite, GenericResultCallback callback) {
        sendActivityJoinRequestReplyNative(pointer, invite, callback);
    }

    /**
     * Sets a handler that will be called by the SDK whenever it receives an
     * activity invite or join request. These are differentiated by
     * {@link ActivityInvite#type()}.
     *
     * @param callback A callback that will be executed when an activity
     *                 invite is created.
     *
     * @see Client#runCallbacks()
     */
    public void setActivityInviteCreatedCallback(ActivityInviteCallback callback) {
        setActivityInviteCreatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that will be called by the SDK whenever an activity
     * invite is updated, e.g. the player count changed, or it became invalid.
     * Note that activity invites can become valid again after becoming invalid.
     *
     * @param callback A callback that will be executed when an activity
     *                 invite is updated.
     *
     * @see Client#runCallbacks()
     */
    public void setActivityInviteUpdatedCallback(ActivityInviteCallback callback) {
        setActivityInviteUpdatedCallbackNative(pointer, callback);
    }

    /**
     * Sets a handler that will be called by the SDK to join a lobby with a
     * join secret. This is used both for joins initiated via the SDK and joins
     * initiated via the Discord app.
     *
     * @param callback A callback that will be executed when the player
     *                 tries to join someone else.
     *
     * @see Client#runCallbacks()
     */
    public void setActivityJoinCallback(ActivityJoinCallback callback) {
        setActivityJoinCallbackNative(pointer, callback);
    }

    /**
     * Blocks the specified user. Their friend status will be severed and their
     * relationship will be updated to {@link Relationship.Type#Blocked}. The
     * target user will not know that the player blocked them.
     *
     * @param userId ID of the user to block.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void blockUser(long userId, GenericResultCallback callback) {
        blockUserNative(pointer, userId, callback);
    }

    /**
     * Unblocked the specified user.
     *
     * @param userId ID of the user to unblock.
     * @param callback A callback that will be executed when the operation completes.
     *
     * @see Client#runCallbacks()
     */
    public void unblockUser(long userId, GenericResultCallback callback) {
        unblockUserNative(pointer, userId, callback);
    }
}
