package application.vue;
import java.io.IOException;
import application.controlleur.Controlleur;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;
/** TODO comment class responsibility (SRP)
 * @author tom.jammes
 * @author thomas.izard
 * @author constant.nguyen
 */
public class Vue extends Application {

	/* Stage correspondant au stage principal de l'application au lancement */
	private Stage primaryStage;

	/**
	 * La méthode principale qui démarre l'application JavaFX.
	 * @param args Un tableau de chaînes d'arguments passées à l'application
	 */
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Application Note");

		// Initialisation de la mise en page racine (Root Layout)
		initRootLayout();

		// Gestionnaire pour la fermeture de la fenêtre principale
		primaryStage.setOnCloseRequest(event -> {
			event.consume(); // Empêche la fermeture de la fenêtre par défaut

			// Affichage de la boîte de dialogue de confirmation
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Voulez-vous vraiment quitter ?");
			alert.setContentText("Toute modification non sauvegardée sera perdue.");

			// Boutons de confirmation
			ButtonType buttonTypeYes = new ButtonType("Oui");
			ButtonType buttonTypeNo = new ButtonType("Non");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

			// Gestion des actions selon le bouton cliqué
			alert.showAndWait().ifPresent(buttonType -> {
				if (buttonType == buttonTypeYes) {
					// Si l'utilisateur confirme, fermer l'application
					primaryStage.close();
				}
			});
		});
	}


	/**
	 * Premet de créer et lancer l'application sur une page FXML
	 */
	public void initRootLayout() {
		try {
			/* Création d'un élément permettant de charger une page FXML */
			FXMLLoader loader = new FXMLLoader();
			/* Test permettant de savoir si un fichier a déja été importé ou non */
			if (Controlleur.fichierImporter()) {
				/* Si un fichier a déja été importé, on lance la page Notes */
				loader.setLocation(getClass().getResource("PageNotes.fxml"));
			} else {
				/* Si le fichier n'a pas été importé, on lance la page d'importation des parametres */
				loader.setLocation(getClass().getResource("PageParametreImporter.fxml"));
			}
			/* On charge la borderPane de notre page */
			BorderPane PageNotes = loader.load();
			/* On crée la scene à partir de ce qu'on vient de charger */
			Scene PageNoteScene = new Scene(PageNotes);
			// Ajoutez un logo à la barre de titre
			Image logo = new Image(getClass().getResourceAsStream("logo.png"));
			/* On rajoute le logo à notre stage */
			primaryStage.getIcons().add(logo);
			/* On définit la scene comme principale */
			primaryStage.setScene(PageNoteScene);
			/* On affiche notre scene */
			primaryStage.show();
			/* On rajoute notre fichier CSS dans notre application */
			PageNoteScene.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}