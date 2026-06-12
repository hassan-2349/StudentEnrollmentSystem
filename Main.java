import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        App app = new App(stage);
        app.showLogin();
    }

    public static void main(String[] args) {
        launch(args);
    }
}