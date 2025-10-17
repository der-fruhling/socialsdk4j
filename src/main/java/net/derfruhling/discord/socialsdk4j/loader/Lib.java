package net.derfruhling.discord.socialsdk4j.loader;

final class Lib {
    private Lib() {}

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
