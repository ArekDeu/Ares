package pl.rad.projekt_ares.core;

import java.util.LinkedHashSet;
import java.util.Set;

/** Serializowalny stan gry (MVP). */
public class GameState {
    public int schemaVersion = 1;          // do przyszłych migracji
    public String playerName = "Bohater";
    public int playerHp = 30;
    public int playerMaxHp = 30;

    // pozycja gracza (kafle)
    public int playerX = 2;
    public int playerY = 2;

    /** Odkryte regiony mapy (np. R1, R2...). */
    public Set<String> unlockedRegions = new LinkedHashSet<>();

    public static GameState newGame() {
        GameState s = new GameState();
        s.unlockedRegions.add("R1");
        s.playerX = 2; // start w odkrytym R1 na trawie
        s.playerY = 2;
        return s;
    }

}
