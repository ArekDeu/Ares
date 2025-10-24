package pl.rad.projekt_ares.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OptionsPlaceholderView extends VBox {
    public OptionsPlaceholderView(ScreenRouter router) {
        setAlignment(Pos.CENTER);
        setSpacing(12);
        var lbl = new Label("OPCJE — (placeholder)");
        lbl.setStyle("-fx-text-fill: white;");
        var back = new Button("← Wróć");
        back.setOnAction(e -> router.showMainMenu());
        getChildren().addAll(lbl, back);
        setStyle("-fx-background-color: #1e1e1e;");
    }
}
