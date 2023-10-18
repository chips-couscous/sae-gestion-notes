package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Vue extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Apllication Note");

        initRootLayout();
    }
    
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageNotes.fxml"));

            BorderPane firstScene = loader.load();

            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageParametreImporter.fxml"));
            BorderPane secondScene = loader.load();

            Scene firstSceneScene = new Scene(firstScene);
            Scene secondSceneScene = new Scene(secondScene);

            // Boutons pour la navigation
            Button boutonParametre = (Button) firstScene.lookup("#boutonParametreImporter");
            Button boutonNotes = (Button) secondScene.lookup("#boutonNotes");

            boutonParametre.setOnAction(event -> {
                primaryStage.setScene(secondSceneScene);
            });

            boutonNotes.setOnAction(event -> {
                primaryStage.setScene(firstSceneScene);
            });

            primaryStage.setScene(firstSceneScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
