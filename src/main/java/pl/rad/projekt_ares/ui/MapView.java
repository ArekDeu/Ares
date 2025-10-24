package pl.rad.projekt_ares.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import pl.rad.projekt_ares.core.*;

import java.util.ArrayList;
import java.util.List;

/** ekran mapy: rysowanie mapy + mgły + akcje gry */
public class MapView extends BorderPane {

    private final ScreenRouter router;
    private final GameState state;
    private final LevelMap map;
    private final FogOfWar fog;
    private final MapCanvas canvas;

    // pola pomocnicze (debug „pozycja gracza”)
    private int px = 5, py = 5;       // start testowy (w kaflach), możesz dostosować

    public MapView(ScreenRouter router, GameState state) {
        this.router = router;
        this.state = state;

        // ładowanie mapy
        this.map = LevelMap.generateDemo();

        // inicjalizacja mgły na bazie odkrytych regionów z GameState
        this.fog = new FogOfWar(map.width, map.height);
        this.fog.applyUnlockedRegions(map, state.unlockedRegions);

        // widok płótna
        this.canvas = new MapCanvas(map, fog);
        this.canvas.setPlayerPosition(state.playerX, state.playerY); // ustawienie gracza
        setCenter(canvas);
        BorderPane.setMargin(canvas, new Insets(12));
        setStyle("-fx-background-color: #1e1e1e;");

        // pasek przycisków
        Button btnWin   = new Button("Symuluj wygraną (odblokuj kolejny region)");
        Button btnSave  = new Button("Zapisz");
        Button btnBack  = new Button("← Wróć do menu");

        btnWin.setOnAction(e -> simulateWinAndUnlockNext());
        btnSave.setOnAction(e -> saveNow());
        btnBack.setOnAction(e -> router.showMainMenu());

        HBox bar = new HBox(10, btnWin, btnSave, btnBack);
        bar.setPadding(new Insets(10));
        bar.setAlignment(Pos.CENTER);
        setBottom(bar);

        // **sterowanie** – strzałki; fokus na widoku
        setFocusTraversable(true);
        setOnMouseClicked(e -> requestFocus()); // klik w mapę przejmuje fokus
        requestFocus();

        setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP    -> tryMove(0, -1);
                case DOWN  -> tryMove(0,  1);
                case LEFT  -> tryMove(-1, 0);
                case RIGHT -> tryMove(1,  0);
            }
        });

        // pierwszy render
        canvas.redraw();
    }

    /** ruch gracza o (dx,dy): tylko GRASS i tylko odkryte pola */
    private void tryMove(int dx, int dy) {
        int nx = state.playerX + dx;
        int ny = state.playerY + dy;

        // poza mapą lub nie-trawa → blokada
        if (!map.isPassable(nx, ny)) {
            PromptOverlay.show(router.getRoot(), "Nie można przejść (woda/góry lub poza mapą).");
            return;
        }
        // nieodkryte → blokada
        if (!fog.isVisible(nx, ny)) {
            PromptOverlay.show(router.getRoot(), "Obszar jeszcze nieodkryty.");
            return;
        }

        // aktualizacja pozycji gracza
        state.playerX = nx;
        state.playerY = ny;

        // odsłanianie wokół gracza (latarka)
        fog.revealCircle(state.playerX, state.playerY, 3);

        // odświeżenie widoku
        canvas.setPlayerPosition(state.playerX, state.playerY);
        canvas.redraw();
    }

    /** symulacja zwycięstwa: wybór kolejnego regionu i odblokowanie */
    private void simulateWinAndUnlockNext() {
        String next = nextLockedRegionId();
        if (next == null) {
            PromptOverlay.show(router.getRoot(), "Brak kolejnych regionów do odblokowania.");
            return;
        }
        state.unlockedRegions.add(next);
        LevelMap.Region r = map.regions.get(next);
        fog.revealRect(r.x0, r.y0, r.w, r.h); // aktualizacja maski
        canvas.redraw(); // odświeżenie renderu
        PromptOverlay.show(router.getRoot(), "Odblokowano region: " + next);
    }

    /** zapis gry (zapis stanu) */
    private void saveNow() {
        String err = SaveManager.save(PathsAres.defaultSaveFile(), state);
        if (err != null) {
            PromptOverlay.show(router.getRoot(), "⚠ " + err);
        } else {
            PromptOverlay.show(router.getRoot(), "✅ Zapisano grę.");
        }
    }

    /** wybór kolejnego nieodblokowanego regionu (prosta kolejność R1..R4) */
    private String nextLockedRegionId() {
        List<String> order = new ArrayList<>(map.regions.keySet()); // [R1,R2,R3,R4]
        for (String id : order) {
            if (!state.unlockedRegions.contains(id)) return id;
        }
        return null;
    }


}
