package net.derfruhling.discord.socialsdk4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestMain {
    public static final long APP_ID = 1367390201621512313L;

    public static void main(String[] args) throws Exception {
        Logger log = LogManager.getLogger();
        log.info("miaw");

        Client client = new Client();

        client.setLobbyCreatedCallback(lobbyId -> {
            log.info("lobby {} created", lobbyId);
        });

        client.setLobbyDeletedCallback(lobbyId -> {
            log.info("lobby {} deleted", lobbyId);
        });

        client.setStatusChangedCallback((status, error, errorDetail) -> {
            if(status == Client.Status.Ready) {
                client.updateRichPresence(new ActivityBuilder()
                        .setType(ActivityType.Playing)
                        .setState("miawing")
                        .setDetails("in test miaw"), result -> {
                    if(!result.isSuccess()) {
                        log.error("Failed to update rich presence: {}", result.message());
                    }
                });

                client.createOrJoinLobby("miaw-secret", (result, lobbyId) -> {
                    if (result.isSuccess()) {
                        log.info("lobby {} created / joined", lobbyId);
                    } else {
                        log.info("lobby {} failed to create: {}", lobbyId, result);
                    }
                });

                client.openConnectedGameSettingsInDiscord(result -> {
                    if (!result.isSuccess()) {
                        log.info("failed to open connected games settings: {}", result);
                    }
                });
            }
        });

        CodeVerifier verifier = client.createAuthorizationCodeVerifier();
        client.authorize(APP_ID, Client.COMMUNICATIONS_SCOPES, "miaw miaw", verifier.challenge(), (authResult, code, redirectUri) -> {
            if (authResult.isSuccess()) {
                client.getToken(APP_ID, code, verifier.verifier(), redirectUri, (tokenResult, accessToken, refreshToken, type, expiresIn, scopes) -> {
                    if (tokenResult.isSuccess()) {
                        client.updateToken(AuthorizationTokenType.Bearer, accessToken, result -> {
                            if (result.isSuccess()) {
                                client.connect();
                            }
                        });
                    }
                });
            }
        });

        while(true) {
            client.runCallbacks();
            Thread.sleep(15);
        }
    }
}
