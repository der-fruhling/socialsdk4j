package net.derfruhling.discord.socialsdk4j;

import net.derfruhling.discord.socialsdk4j.loader.SocialSdkLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.Properties;
import java.util.function.BiConsumer;

public class SocialSdk {
    private static final Logger log = LogManager.getLogger(SocialSdk.class);
    private static boolean isInitialized = false;

    private static BiConsumer<Level, String> logCallback = (severity, message) -> {
        log.log(severity, message.strip());
    };

    static final Cleaner cleaner = Cleaner.create();

    public static void initialize(SocialSdkLoader loader) {
        Properties info = new Properties();

        try {
            info.load(SocialSdk.class.getResourceAsStream("socialsdk4j.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loader.load(info.getProperty("socialsdk.name"), info.getProperty("socialsdk.version"));
        loader.load(info.getProperty("socialsdk4j.name"), info.getProperty("socialsdk4j.version"));
        isInitialized = true;
    }

    public static void ensureInitialized() {
        if(!isInitialized) throw new IllegalStateException("SocialSDK4J not initialized");
    }

    public static void setLogCallback(BiConsumer<Level, String> logCallback) {
        SocialSdk.logCallback = logCallback;
    }

    private static void javaLog(int severity, String message) {
        logCallback.accept(switch (severity) {
            case 1 -> Level.DEBUG;
            case 2 -> Level.INFO;
            case 3 -> Level.WARN;
            case 4 -> Level.ERROR;
            default -> throw new IllegalStateException("Unexpected severity value: " + severity);
        }, message);
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
