package application;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Test extends Application {

    private Stage primaryStage;
    private Scene pageNotesScene, pageParametreScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Changement de Scène");

        // Charger les scènes depuis les fichiers FXML
        try {
            FXMLLoader notesLoader = new FXMLLoader(getClass().getResource("PageNotes.fxml"));
            Parent notesRoot = notesLoader.load();
            pageNotesScene = new Scene(notesRoot);

            FXMLLoader parametreLoader = new FXMLLoader(getClass().getResource("PageParametre.fxml"));
            Parent parametreRoot = parametreLoader.load();
            pageParametreScene = new Scene(parametreRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(pageParametreScene);
        primaryStage.show();
    }
}
