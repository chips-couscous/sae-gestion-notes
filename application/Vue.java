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

            BorderPane pageNotes = loader.load();

            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageParametreImporter.fxml"));
            BorderPane pageParametreImporter = loader.load();
            
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageParametreModifier.fxml"));
            BorderPane pageParametreModifier = loader.load();
            
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageParametrePartager.fxml"));
            BorderPane pageParametrePartager = loader.load();
            
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("PageParametreReinitialiser.fxml"));
            BorderPane pageParametreReinitialiser = loader.load();

            Scene firstSceneScene = new Scene(pageNotes);
            Scene secondSceneScene = new Scene(pageParametreImporter);
            Scene thirdSceneScene = new Scene(pageParametreModifier);
            Scene fourthSceneScene = new Scene(pageParametrePartager);
            Scene fifthSceneScene = new Scene(pageParametreReinitialiser);

            // Boutons pour la navigation
            Button boutonNotes = (Button) pageNotes.lookup("#boutonNotes");
            
            Button boutonParametreImporter = (Button) pageNotes.lookup("#boutonParametreImporter");
            pageParametreModifier.lookup("#boutonParametreImporter");
            pageParametrePartager.lookup("#boutonParametreImporter");
            pageParametreReinitialiser.lookup("#boutonParametreImporter");
            
            Button boutonParametreModifier = (Button) pageParametreModifier.lookup("#boutonParametreModifier");
            Button boutonParametrePartager = (Button) pageParametrePartager.lookup("#boutonParametrePartager");
            Button boutonParametreReinitialiser = (Button) pageParametreReinitialiser.lookup("#boutonParametreReinitialiser");

            // Evenement déclanché lorsque boutonNotes est cliqué
            // Redirige vers la page Notes
            boutonNotes.setOnAction(event -> {
                primaryStage.setScene(firstSceneScene);
            });
            
            // Evenement déclanché lorsque boutonParametre est cliqué
            // Redirige vers la page Paramètres
            boutonParametreImporter.setOnAction(event -> {
                primaryStage.setScene(secondSceneScene);
            });
            
            // Evenement déclanché lorsque boutonParametre est cliqué
            // Redirige vers la page Paramètres
            boutonParametreModifier.setOnAction(event -> {
                primaryStage.setScene(thirdSceneScene);
            });
            
            // Evenement déclanché lorsque boutonParametre est cliqué
            // Redirige vers la page Paramètres
            boutonParametrePartager.setOnAction(event -> {
                primaryStage.setScene(fourthSceneScene);
            });
            
            // Evenement déclanché lorsque boutonParametre est cliqué
            // Redirige vers la page Paramètres
            boutonParametreReinitialiser.setOnAction(event -> {
                primaryStage.setScene(fifthSceneScene);
            });

            primaryStage.setScene(firstSceneScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
