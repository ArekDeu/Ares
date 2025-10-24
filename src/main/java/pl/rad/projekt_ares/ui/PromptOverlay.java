package pl.rad.projekt_ares.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Prosty modalny „prompt” w oknie gry:
 * półprzezroczyste tło + kartka z tekstem i przyciskiem OK.
 */
public final class PromptOverlay extends StackPane {

    private PromptOverlay(String message, Runnable onClose) {
        // tło przyciemniające
        setStyle("-fx-background-color: rgba(0,0,0,0.55);");
        setPickOnBounds(true); // blokuje kliknięcia do widoków pod spodem

        // „kartka”
        Label text = new Label(message);
        text.setStyle("-fx-text-fill: white; -fx-font-size: 16;");

        Button ok = new Button("OK");
        ok.setDefaultButton(true);
        ok.setOnAction(e -> close(onClose));

        VBox card = new VBox(12, text, ok);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 14; -fx-padding: 16;");
        getChildren().add(card);
        setAlignment(card, Pos.CENTER);

        // animacja pojawienia
        show(card);
    }

    private void show(Node card) {
        card.setScaleX(0.85);
        card.setScaleY(0.85);
        card.setOpacity(0);
        var st = new ScaleTransition(Duration.millis(180), card);
        st.setToX(1); st.setToY(1);
        var ft = new FadeTransition(Duration.millis(180), card);
        ft.setToValue(1);
        new ParallelTransition(st, ft).play();
    }

    private void close(Runnable after) {
        Node card = getChildren().get(0);
        var st = new ScaleTransition(Duration.millis(140), card);
        st.setToX(0.85); st.setToY(0.85);
        var ft = new FadeTransition(Duration.millis(140), card);
        ft.setToValue(0);
        var fadeBg = new FadeTransition(Duration.millis(140), this);
        fadeBg.setToValue(0);
        var pt = new ParallelTransition(st, ft, fadeBg);
        pt.setOnFinished(e -> {
            // usuń siebie z rodzica
            ((StackPane)getParent()).getChildren().remove(this);
            if (after != null) after.run();
        });
        pt.play();
    }

    /** API: pokaż prompt na danym korzeniu (np. router.getRoot()). */
    public static void show(StackPane root, String message) {
        show(root, message, null);
    }

    public static void show(StackPane root, String message, Runnable onClose) {
        var overlay = new PromptOverlay(message, onClose);
        // start z przezroczystym tłem, ale kartka ma własny fade
        overlay.setOpacity(1);
        root.getChildren().add(overlay);
    }
}
