package net.derfruhling.discord.socialsdk4j;

import net.derfruhling.discord.socialsdk4j.loader.ClasspathLoader;
import net.derfruhling.discord.socialsdk4j.loader.DirectoryLoader;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class TestMain {
    public static final long APP_ID = 1367390201621512313L;

    public static void main(String[] args) throws Exception {
        SocialSdk.initialize(new DirectoryLoader(Path.of("build", "testEnv")));
        Client client = getClient();

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

    private static @NotNull Client getClient() {
        Client client = new Client();

        client.setLobbyCreatedCallback(lobbyId -> {
            System.out.printf("lobby %d created\n", lobbyId);
        });

        client.setLobbyDeletedCallback(lobbyId -> {
            System.out.printf("lobby %d deleted\n", lobbyId);
        });

        client.setStatusChangedCallback((status, error, errorDetail) -> {
            if(status == Client.Status.Ready) {
                client.updateRichPresence(new ActivityBuilder()
                        .setType(ActivityType.Playing)
                        .setState("miawing")
                        .setDetails("in test miaw"))
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });

                client.createOrJoinLobby("miaw-secret")
                        .thenAccept(lobbyId -> System.out.printf("lobby %d created / joined", lobbyId))
                        .exceptionally(throwable -> {
                            throwable.printStackTrace();
                            return null;
                        });

                client.openConnectedGameSettingsInDiscord().exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
            }
        });
        return client;
    }
}
