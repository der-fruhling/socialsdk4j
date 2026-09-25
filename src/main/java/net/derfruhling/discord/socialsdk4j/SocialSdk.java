package net.derfruhling.discord.socialsdk4j;

import java.io.IOException;
import java.lang.ref.Cleaner;
import java.util.Properties;
import java.util.function.BiConsumer;
import net.derfruhling.discord.socialsdk4j.loader.SocialSdkLoader;

public class SocialSdk {

    private static boolean isInitialized = false;

    public enum LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }

    private static BiConsumer<LogLevel, String> logCallback = (
        severity,
        message
    ) -> {};

    static final Cleaner cleaner = Cleaner.create();

    public static void initialize(SocialSdkLoader loader) {
        Properties info = new Properties();

        try {
            info.load(
                SocialSdk.class.getResourceAsStream("socialsdk4j.properties")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loader.load(
            info.getProperty("socialsdk.name"),
            info.getProperty("socialsdk.version")
        );
        loader.load(
            info.getProperty("socialsdk4j.name"),
            info.getProperty("socialsdk4j.version")
        );
        isInitialized = true;
    }

    /**
     * @throws IllegalStateException The {@link SocialSdk#initialize}
     *                               function has not yet been called
     */
    public static void ensureInitialized() {
        if (!isInitialized) throw new IllegalStateException(
            "SocialSDK4J not initialized"
        );
    }

    public static void setLogCallback(
        BiConsumer<LogLevel, String> logCallback
    ) {
        SocialSdk.logCallback = logCallback;
    }

    public static void log(LogLevel level, String message) {
        logCallback.accept(level, message);
    }

    private static void javaLog(int severity, String message) {
        log(
            switch (severity) {
                case 1 -> LogLevel.DEBUG;
                case 2 -> LogLevel.INFO;
                case 3 -> LogLevel.WARN;
                case 4 -> LogLevel.ERROR;
                default -> throw new IllegalStateException(
                    "Unexpected severity value: " + severity
                );
            },
            message
        );
    }

    static long createClient() {
        ensureInitialized();

        return createClientNative();
    }

    private static native long createClientNative();

    /**
     * Runs all pending callbacks the SocialSDK wants to call. This must be run
     * in order to make use of the SocialSDK properly.
     */
    public static native void runCallbacks();
}
