package pl.rad.projekt_ares.ui;

import javafx.scene.layout.StackPane;
import pl.rad.projekt_ares.core.GameState;
import pl.rad.projekt_ares.core.PathsAres;
import pl.rad.projekt_ares.core.SaveManager;

public class ScreenRouter {
    private final StackPane root = new StackPane();
    private GameState state; // aktualny stan gry (jeśli gra uruchomiona)

    public StackPane getRoot() { return root; }

    public void showMainMenu() {
        root.getChildren().setAll(new MainMenuView(this));
    }

    public void startNewGame() {
        this.state = GameState.newGame();
        var err = SaveManager.save(PathsAres.defaultSaveFile(), state);
        if (err != null) {
            PromptOverlay.show(root, "⚠ " + err);
        }
        showMap(); // przejście do Mapy
    }

    public void loadCampaign() {
        var res = SaveManager.load(PathsAres.defaultSaveFile());
        if (!res.isOk()) {
            PromptOverlay.show(root, "⚠ " + res.error);
            return;
        }
        this.state = res.state;
        PromptOverlay.show(root, "✅ Wczytano zapis. Gracz: " + state.playerName,
                this::showMap);
    }

    public void showOptions() {
        root.getChildren().setAll(new OptionsPlaceholderView(this));
    }

    private void showMap() {
        root.getChildren().setAll(new MapView(this, state));
    }

    public void exitGame() { System.exit(0); }
}
