package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {
	
	@FXML
	private Button boutonParametreImporter;
	
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
            Scene thirdSceneScene = new Scene(pageParametrePartager);
            Scene fourthSceneScene = new Scene(pageParametreReinitialiser);
            Scene fifthSceneScene = new Scene(pageParametreModifier);

            // Boutons pour la navigation
            
            redirection(thirdSceneScene, boutonParametreImporter);
            
            /*
            pageParametreImporter.setLeft(createButton("Vers mes notes", firstSceneScene));
            pageParametrePartager.setLeft(createButton("Vers mes notes", firstSceneScene));
            pageParametreReinitialiser.setLeft(createButton("Vers mes notes", firstSceneScene));
            pageParametreModifier.setLeft(createButton("Vers mes notes", firstSceneScene));
            
            pageNotes.setCenter(createButton("Importer des Paramètres", secondSceneScene));
            pageParametrePartager.setCenter(createButton("Importer des Paramètres", secondSceneScene));
            pageParametreReinitialiser.setCenter(createButton("Importer des Paramètres", secondSceneScene));
            pageParametreModifier.setCenter(createButton("Importer des Paramètres", secondSceneScene));
            
            pageParametreImporter.setRight(createButton("Partager mes Paramètres", thirdSceneScene));
            pageParametreImporter.setRight(createButton("Partager mes Paramètres", thirdSceneScene));
            pageParametreImporter.setRight(createButton("Partager mes Paramètres", thirdSceneScene));
            pageParametreImporter.setRight(createButton("Partager mes Paramètres", thirdSceneScene));
            
            primaryStage.setScene(firstSceneScene);
            primaryStage.show();
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Button createButton(String texte, Scene sceneVisee) {
        Button nomBouton = new Button(texte);
        nomBouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sceneVisee != null) {
                    primaryStage.setScene(sceneVisee);
                }
            }
        });
        return nomBouton;
    }
    
    private void redirection(Scene sceneVisee, Button boutonClique) {
    	boutonClique.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (sceneVisee != null) {
                    primaryStage.setScene(sceneVisee);
                }
            }
        });
    }
}
