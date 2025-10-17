package net.derfruhling.discord.socialsdk4j.loader;

import java.nio.file.Path;

public record DirectoryLoader(Path base) implements SocialSdkLoader {
    @Override
    public void load(String component, String version) {
        System.load(base.resolve(Lib.name(component)).toAbsolutePath().toString());
    }
}
