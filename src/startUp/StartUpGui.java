package startUp;

import gui.MenuSchermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartUpGui extends Application {

    /**
     * Wordt opgeroepen als GUI gekozen wordt in StartUp
     * Start GUI op
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        MenuSchermController msc = new MenuSchermController();
        primaryStage.setScene(new Scene(msc));
        primaryStage.show();
        primaryStage.setTitle("Munchkin");
        primaryStage.getIcons().add(new Image("resources/afbeeldingen/icon.png"));
    }

    public static void main() {
        launch();
    }
}
