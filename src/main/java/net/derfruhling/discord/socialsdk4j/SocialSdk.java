package net.derfruhling.discord.socialsdk4j;

import net.derfruhling.discord.socialsdk4j.loader.SocialSdkLoader;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.Properties;
import java.util.function.BiConsumer;

public class SocialSdk {
    private static boolean isInitialized = false;

    public enum LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    private static BiConsumer<LogLevel, String> logCallback = (severity, message) -> {};

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

    public static void setLogCallback(BiConsumer<LogLevel, String> logCallback) {
        SocialSdk.logCallback = logCallback;
    }

    private static void javaLog(int severity, String message) {
        logCallback.accept(switch (severity) {
            case 1 -> LogLevel.DEBUG;
            case 2 -> LogLevel.INFO;
            case 3 -> LogLevel.WARN;
            case 4 -> LogLevel.ERROR;
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
