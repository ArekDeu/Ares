package pl.rad.projekt_ares.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Menu główne gry z czterema przyciskami.
 */
public class MainMenuView extends VBox {

    public MainMenuView(ScreenRouter router) {
        setAlignment(Pos.CENTER);
        setSpacing(15);

        Button newGameBtn = new Button("Nowa gra");
        Button campaignBtn = new Button("Kampania");
        Button optionsBtn = new Button("Opcje");
        Button exitBtn = new Button("Wyjdź");

        newGameBtn.setOnAction(e -> router.startNewGame());
        campaignBtn.setOnAction(e -> router.loadCampaign());
        optionsBtn.setOnAction(e -> router.showOptions());

        exitBtn.setOnAction(e -> router.exitGame());

        getChildren().addAll(newGameBtn, campaignBtn, optionsBtn, exitBtn);
    }
}
