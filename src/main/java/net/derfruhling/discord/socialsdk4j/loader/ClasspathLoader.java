package net.derfruhling.discord.socialsdk4j.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClasspathLoader implements SocialSdkLoader {
    private static void loadLibrary(String name) throws IOException {
        Path path = Path.of(".", Lib.name(name));

        try(var is = Lib.class.getResourceAsStream("/" + Lib.name(name))) {
            assert is != null;

            byte[] bytes = is.readAllBytes();

            if(!Files.exists(path)) {
                try {
                    Files.write(path, bytes);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write component " + name, e);
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
                    try {
                        Files.write(path, bytes);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to write component " + name, e);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.load(path.toAbsolutePath().toString());
    }

    @Override
    public void load(String component, String version) {
        try {
            loadLibrary(component);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
