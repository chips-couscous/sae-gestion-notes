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

	private Stage primaryStage;

	/** TODO comment method role
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Apllication Note");

		initRootLayout();
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

	/** TODO comment method role
	 * 
	 */
	public void initRootLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			if (Controlleur.fichierImporter()) {
				loader.setLocation(getClass().getResource("PageNotes.fxml"));
			} else {
				loader.setLocation(getClass().getResource("PageParametreImporter.fxml"));
			}
			BorderPane PageNotes = loader.load();
			Scene PageNoteScene = new Scene(PageNotes);
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