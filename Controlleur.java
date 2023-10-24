package application.Controlleur;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controlleur {
	

	
	

	@FXML
	BorderPane rootPane; // Assurez-vous que vous avez une référence à votre racine (root) ici.

	public void initialize() {
	    

	}

	@FXML
	public void changerSceneParametre() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/Vue/PageParametreImporter.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void changerSceneModifier() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/Vue/PageParametreModifier.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void changerSceneNotes() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/Vue/PageNotes.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void changerSceneParametrePartager() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/Vue/PageParametrePartager.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	@FXML
	public void changerSceneParametreReiniti() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/Vue/PageParametreReinitialiser.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
}
