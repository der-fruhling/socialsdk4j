package net.derfruhling.discord.socialsdk4j.loader;

import net.derfruhling.discord.socialsdk4j.SocialSdk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Lib {
    private static final Logger log = LogManager.getLogger();

    static String name(String name) {
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
}
