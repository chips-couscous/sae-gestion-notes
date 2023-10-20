package application.Vue;

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

			BorderPane PageNotes = loader.load();

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PageParametreImporter.fxml"));
			BorderPane PageParametreImporter = loader.load();

			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PageParametreReinitialiser.fxml"));
			BorderPane PageParametreReinitialiser = loader.load();
			
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PageParametrePartager.fxml"));
			BorderPane PageParametrePartager = loader.load();
			
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PageParametreModifier.fxml"));
			BorderPane PageParametreModifier = loader.load();



			Scene PageNoteScene = new Scene(PageNotes);
			Scene PageParametreImporterScene = new Scene(PageParametreImporter);
			Scene PageParametreReinitialiserScene = new Scene(PageParametreReinitialiser);
			Scene PageParametrePartagerScene = new Scene(PageParametrePartager);
			Scene PageParametreModifierScene = new Scene(PageParametreModifier);


			// Boutons Navigation depuis la page Note 
			Button boutonParametreImporter = (Button) PageNotes.lookup("#boutonParametreImporter");
			
			
			
			//Bouton Navigation depuis la page ParametreImporter
			//Bouton pour revenir sur la page Note depuis la page ParametreImporter
			Button boutonNotes = (Button) PageParametreImporter.lookup("#boutonNotes");
			
			//Bouton pour revenir sur la page ParametreImporter depuis la Page ParametreReinitialiser
			Button boutonParametreReinitialiser = (Button) PageParametreImporter.lookup("#boutonParametreReinitialiser");
			
			//Bouton pour aller sur la page ParametreModifier
			Button boutonParametreModifier = (Button) PageParametreImporter.lookup("#boutonParametreModifier");
			
			//bouton pour aller sur la page ParametrePartager
			Button boutonParametrePartager = (Button) PageParametreImporter.lookup("#boutonParametrePartager");
			
			
			
			//Bouton de Navigation depuis la page ParametreReinitialiser
			//Bouton pour revenir sur la page ParametreImporter depuis la Page ParametreReinitialiser
			Button boutonParametreImporter2 = (Button) PageParametreReinitialiser.lookup("#boutonParametreImporter2");
			
			//Bouton pour revenir sur la page Note depuis la page ParametreReinitialiser
			Button boutonNotes2 = (Button) PageParametreReinitialiser.lookup("#boutonNotes");
			
			//Bouton pour aller sur la page ParametreModifier
			Button boutonParametreModifier2 = (Button) PageParametreReinitialiser.lookup("#boutonParametreModifier");
			
			//bouton pour aller sur la page ParametrePartager
			Button boutonParametrePartager2 = (Button) PageParametreReinitialiser.lookup("#boutonParametrePartager");
			
			
			//Bouton de Navigation depuis la page Parametrepartager
			//Bouton pour revenir sur la page ParametreImporter depuis la Page ParametreReinitialiser
			Button boutonParametreImporter3 = (Button) PageParametrePartager.lookup("#boutonParametreImporter2");
			
			//Bouton pour revenir sur la page Note depuis la page ParametreReinitialiser
			Button boutonNotes3 = (Button) PageParametrePartager.lookup("#boutonNotes");
			
			//Bouton pour aller sur la page ParametreModifier
			Button boutonParametreModifier3 = (Button) PageParametrePartager.lookup("#boutonParametreModifier");
			
			//bouton pour aller sur la page ParametrePartager
			Button boutonParametreReinitialiser2 = (Button) PageParametrePartager.lookup("#boutonParametreReinitialiser");
			
			
			//Bouton de Navigation depuis la page ParametreModifier
			//Bouton pour revenir sur la page ParametreImporter depuis la Page ParametreReinitialiser
			Button boutonParametreImporter4 = (Button) PageParametreModifier.lookup("#boutonParametreImporter2");
			
			//Bouton pour revenir sur la page Note depuis la page ParametreReinitialiser
			Button boutonNotes4 = (Button) PageParametreModifier.lookup("#boutonNotes");
			
			//Bouton pour aller sur la page ParametreModifier
			Button boutonParametreReinitialiser3 = (Button) PageParametreModifier.lookup("#boutonParametreReinitialiser");
			
			//bouton pour aller sur la page ParametrePartager
			Button boutonParametrePartager3 = (Button) PageParametreModifier.lookup("#boutonParametrePartager");

			
			//On action depuis la page Importer
			boutonParametreImporter.setOnAction(event -> {
				primaryStage.setScene(PageParametreImporterScene);

			});

			boutonNotes.setOnAction(event -> {
				primaryStage.setScene(PageNoteScene);

			});

			boutonParametreReinitialiser.setOnAction(event -> {
				primaryStage.setScene(PageParametreReinitialiserScene);
			});
			
			boutonParametrePartager.setOnAction(event -> {
				primaryStage.setScene(PageParametrePartagerScene);
			});
			
			boutonParametreModifier.setOnAction(event -> {
				primaryStage.setScene(PageParametreModifierScene);
			});
			
			//Autre page
			
			boutonParametreImporter2.setOnAction(event -> {
				primaryStage.setScene(PageParametreImporterScene);
			});
			
			boutonNotes2.setOnAction(event -> {
				primaryStage.setScene(PageNoteScene);
			});
			
			boutonParametrePartager2.setOnAction(event -> {
				primaryStage.setScene(PageParametrePartagerScene);
			});
			
			boutonParametreModifier2.setOnAction(event -> {
				primaryStage.setScene(PageParametreModifierScene);
			});

			//Page Partager
			
			boutonParametreImporter3.setOnAction(event -> {
				primaryStage.setScene(PageParametreImporterScene);
			});
			
			boutonNotes3.setOnAction(event -> {
				primaryStage.setScene(PageNoteScene);
			});
			
			boutonParametrePartager3.setOnAction(event -> {
				primaryStage.setScene(PageParametrePartagerScene);
			});
			
			boutonParametreModifier3.setOnAction(event -> {
				primaryStage.setScene(PageParametreModifierScene);
			});
			
			
			//page modifier
			
			boutonParametreImporter4.setOnAction(event -> {
				primaryStage.setScene(PageParametreImporterScene);
			});
			
			boutonNotes4.setOnAction(event -> {
				primaryStage.setScene(PageNoteScene);
			});
			
			boutonParametreReinitialiser3.setOnAction(event -> {
				primaryStage.setScene(PageParametreReinitialiserScene);
			});
			
			boutonParametreReinitialiser2.setOnAction(event -> {
				primaryStage.setScene(PageParametreReinitialiserScene);
			});

			primaryStage.setScene(PageNoteScene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
