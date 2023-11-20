package application.vue;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import application.controlleur.Controlleur;

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
			Scene PageNoteScene = new Scene(PageNotes);
			Controlleur.creerFiltres(PageNoteScene);
			 // Ajoutez un logo à la barre de titre
            Image logo = new Image(getClass().getResourceAsStream("logo.png"));
            primaryStage.getIcons().add(logo);
			primaryStage.setScene(PageNoteScene);
			primaryStage.show();
			PageNoteScene.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
