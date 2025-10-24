package pl.rad.projekt_ares.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Zapis/odczyt JSON z bezpiecznym zapisem atomowym i czytelnymi błędami. */
public final class SaveManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private SaveManager() {}

    /** Zapisuje stan gry do pliku. Zwraca null przy sukcesie, a tekst błędu przy porażce. */
    public static String save(Path file, GameState state) {
        try {
            Files.createDirectories(file.getParent());
            String json = GSON.toJson(state);
            // zapis atomowy: najpierw .tmp, potem rename
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.writeString(tmp, json, StandardCharsets.UTF_8);
            Files.deleteIfExists(file);
            Files.move(tmp, file);
            return null; // sukces
        } catch (IOException ex) {
            return "Nie udało się zapisać pliku zapisu: " + ex.getMessage();
        }
    }

    /** Próbuje wczytać stan. Zwraca rezultat – null gdy błąd. */
    public static LoadResult load(Path file) {
        try {
            if (!Files.exists(file)) {
                return LoadResult.error("Brak pliku zapisu: " + file.toAbsolutePath());
            }
            String json = Files.readString(file, StandardCharsets.UTF_8);
            GameState s = GSON.fromJson(json, GameState.class);
            if (s == null) return LoadResult.error("Plik zapisu pusty lub niepoprawny JSON.");
            if (s.schemaVersion != 1) return LoadResult.error("Niezgodna wersja zapisu (schemaVersion=" + s.schemaVersion + ").");
            return LoadResult.ok(s);
        } catch (Exception ex) {
            return LoadResult.error("Nie udało się wczytać zapisu: " + ex.getMessage());
        }
    }

    /** Prosty typ „Either” na wynik ładowania. */
    public static final class LoadResult {
        public final GameState state;
        public final String error;

        private LoadResult(GameState s, String e) { this.state = s; this.error = e; }
        public static LoadResult ok(GameState s)   { return new LoadResult(s, null); }
        public static LoadResult error(String e)   { return new LoadResult(null, e); }
        public boolean isOk() { return state != null; }
    }
}
