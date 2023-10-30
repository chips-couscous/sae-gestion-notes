package application.vue;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/** TODO comment class responsibility (SRP)
 * @author tom
 *
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
	}

	/** TODO comment method role
	 * 
	 */
	public void initRootLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("PageNotes.fxml"));
			BorderPane PageNotes = loader.load();
			Scene PageNoteScene = new Scene(PageNotes);
			
			 // Ajoutez un logo à la barre de titre
            Image logo = new Image(getClass().getResourceAsStream("logo.png"));
            primaryStage.getIcons().add(logo);
			primaryStage.setScene(PageNoteScene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
