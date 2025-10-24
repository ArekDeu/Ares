package pl.rad.projekt_ares;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.rad.projekt_ares.ui.ScreenRouter;

/**
 * Główna klasa aplikacji.
 * Odpowiada za uruchomienie gry i połączenie z routerem ekranów.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ScreenRouter router = new ScreenRouter();

        Scene scene = new Scene(router.getRoot(), 960, 600);
        stage.setTitle("Projekt Ares");
        stage.setScene(scene);

        router.showMainMenu(); // pokaż menu na start

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
