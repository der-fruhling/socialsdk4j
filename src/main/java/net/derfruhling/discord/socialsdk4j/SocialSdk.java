package net.derfruhling.discord.socialsdk4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SocialSdk {
    private static final Logger log = LogManager.getLogger(SocialSdk.class);

    static final Cleaner cleaner = Cleaner.create();

    private static String lib(String name) {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")) {
            return name + ".dll";
        } else if (os.contains("linux")) {
            return "lib" + name + ".so";
        } else if (os.contains("mac")) {
            return "lib" + name + ".dylib";
        } else {
            throw new RuntimeException("Unsupported OS: " + os);
        }
    }

    static {
        loadLibrary("DISCORD_PARTNERSDK_PATH", "discord_partner_sdk");
        loadLibrary("SOCIALSDK4J_PATH", "socialsdk4j");
    }

    private static void loadLibrary(String env, String name) {
        try {
            String envPath = System.getenv(env);

            if (envPath == null) {
                Path path = Path.of(".", lib(name));

                try(var is = SocialSdk.class.getResourceAsStream("/" + lib(name))) {
                    assert is != null;

                    log.info("Ensuring that library {} needs to be written again", name);
                    byte[] bytes = is.readAllBytes();

                    if(!Files.exists(path)) {
                        try {
                            Files.write(path, bytes);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to write library " + name, e);
                        }
                    } else {
                        byte[] real = Files.readAllBytes(path);

                        byte[] hashA = MessageDigest.getInstance("SHA-256").digest(bytes);
                        byte[] hashB = MessageDigest.getInstance("SHA-256").digest(real);

                        boolean differs = false;

                        for (int i = 0; i < hashA.length; i++) {
                            differs = hashA[i] != hashB[i];
                            if(differs) break;
                        }

                        if(differs) {
                            log.info("Need to recopy library {}", name);
                            try {
                                Files.write(path, bytes);
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to write library " + name, e);
                            }
                        }
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                System.load(path.toAbsolutePath().toString());
            } else {
                System.load(Path.of(envPath).toAbsolutePath().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ensureInitialized() {}

    private static void javaLog(int severity, String message) {
        log.log(switch(severity) {
            case 1 -> Level.DEBUG;
            case 2 -> Level.INFO;
            case 3 -> Level.WARN;
            case 4 -> Level.ERROR;
            default -> throw new IllegalStateException("Unexpected severity value: " + severity);
        }, message.strip());
    }

    static native long createClientNative();
    static native void deleteClientNative(long ptr);
    static native void deleteLobbyNative(long ptr);
    static native void deleteLobbyMemberNative(long ptr);
    static native void deleteCallNative(long ptr);
    static native void deleteUserNative(long ptr);
    static native void deleteMessageNative(long ptr);
    static native void runCallbacksNative();
}
