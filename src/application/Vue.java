/*
 * MainMDP.java                                      10 mai 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application;

import java.io.IOException;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;


/** MainLettre
 * @author constant.nguyen
 *
 */
public class Vue extends Application {
    @Override
    public void start(Stage primaryStage) {
            

        /* création d'un chargeur de code FXML
         * et chargement de la vue de l'application
         */
        FXMLLoader chargeurFXML = new FXMLLoader();
        chargeurFXML.setLocation(getClass().getResource("PageParametre.fxml"));
        Parent racine;
        try {
            racine = chargeurFXML.load();
            Scene scene = new Scene(racine);
            scene.getRoot().requestFocus();

            // on définit les caractéristiques de la fenêtre et lui associe la scène
            
         
            
            primaryStage.setTitle("Application note");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Programme principal
     * @param args non utilisé
     */
    public static void main(String[] args) {
        launch(args); // appelera la méthode start
    }
}