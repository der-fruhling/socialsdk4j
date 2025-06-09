package net.derfruhling.discord.socialsdk4j.loader;

@FunctionalInterface
public interface SocialSdkLoader {
    void load(String component, String version);
}
