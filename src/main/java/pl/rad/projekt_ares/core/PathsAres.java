package pl.rad.projekt_ares.core;

import java.nio.file.Files;
import java.nio.file.Path;

/** Ścieżki projektu: saves/, config/ itd. Tworzy katalogi, jeśli brak. */
public final class PathsAres {
    private PathsAres() {}

    public static Path root() {
        // lokalnie obok JAR-a/projektu
        return Path.of(System.getProperty("user.dir"));
    }

    public static Path savesDir() {
        Path p = root().resolve("saves");
        ensureDir(p); return p;
    }

    public static Path configDir() {
        Path p = root().resolve("config");
        ensureDir(p); return p;
    }

    public static Path defaultSaveFile() {
        return savesDir().resolve("save.json");
    }

    private static void ensureDir(Path p) {
        try { Files.createDirectories(p); } catch (Exception ignored) {}
    }
}
