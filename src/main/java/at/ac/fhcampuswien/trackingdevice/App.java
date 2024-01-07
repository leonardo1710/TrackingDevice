package at.ac.fhcampuswien.trackingdevice;

import at.ac.fhcampuswien.trackingdevice.configs.User;
import at.ac.fhcampuswien.trackingdevice.utils.SceneLoader;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        System.out.println("=== BEGINNING OF ESCAPEROOM ===");

        User u = User.getInstance();

        try {
            if (!u.isRegistered()) {
                SceneLoader.getInstance(stage, "registration.fxml", "Anmeldung zum Escape Room").start();
            } else {
                System.out.println("current riddle" + User.getInstance().getCurrentRiddle());
                SceneLoader.getInstance(stage, "riddle" + (User.getInstance().getCurrentRiddle()) + "Scene.fxml", "Riddle " + User.getInstance().getCurrentRiddle()).start();
            }
        } catch (Exception e) {
            System.err.println("Something went wrong while loading the scene. Contact support.");
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}