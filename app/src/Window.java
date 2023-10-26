import java.io.IOException;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root;
        try {

            root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root);

            stage.setTitle("HTTP Requester");
            stage.show();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        launch();
    }

}
