package application.controlleur;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/** TODO comment class responsibility (SRP)
 * @author tom
 *
 */
public class Controlleur {
	
	@FXML
	BorderPane rootPane; //Récupération de l'ID de la BorderPane principale de la page

	/**
	 * Méthode permmettant de lancer d'autres méthodes ou des attributs 
	 * directement au lancement de l'application
	 */
	public void initialize() {
	    

	}
	
	/**
	 * Cette méthode permet de récupérer la scene de la page de paramètre d'importation des données
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneParametre() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreImporter.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Cette méthode permet de récupérer la scene de la page de paramètre de modification d'identité
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneModifier() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreModifier.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	/**
	 * Cette méthode permet de récupérer la scene de la page ou sont présentes les notes
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneNotes() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageNotes.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Cette méthode permet de récupérer la scene de la page de partage des paramètres
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneParametrePartager() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageParametrePartager.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * Cette méthode permet de récupérer la scene de la page de paramètre de réinitialisation
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneParametreReiniti() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreReinitialiser.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Cette méthode permet de récupérer la scene de la page ou seront présentes les ressrouces
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneRessources() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageRessources.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/** TODO comment method role
	 * 
	 */
	@FXML
	public void sceneModifierNote() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/application/vue/PageRessources.fxml"));
	        Parent nouvelleScene = loader.load();
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);

	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.

	        stage.setScene(nouvelleSceneObjet);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	@FXML
    private void ouvrirModifierNote() {
        try {
            Stage stageActuel = (Stage) rootPane.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageModifierNote.fxml"));
            Parent root = loader.load();

            // Créez une nouvelle scène pour la page FXML
            Scene nouvelleScene = new Scene(root);

            // Créez une nouvelle fenêtre
            Stage nouveauStage = new Stage();
            nouveauStage.setTitle("Modifier une Note");

            // Associez la scène à la nouvelle fenêtre
            nouveauStage.setScene(nouvelleScene);

            // Montrez la nouvelle fenêtre sans bloquer la fenêtre principale
            nouveauStage.initOwner(stageActuel);
            nouveauStage.initModality(Modality.WINDOW_MODAL);
            nouveauStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@FXML
    private void supprimerNote() {
        try {
            Stage stageActuel = (Stage) rootPane.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageSupprimerNote.fxml"));
            Parent root = loader.load();

            // Créez une nouvelle scène pour la page FXML
            Scene nouvelleScene = new Scene(root);

            // Créez une nouvelle fenêtre
            Stage nouveauStage = new Stage();
            nouveauStage.setTitle("Supprimer une Note");

            // Associez la scène à la nouvelle fenêtre
            nouveauStage.setScene(nouvelleScene);

            // Montrez la nouvelle fenêtre sans bloquer la fenêtre principale
            nouveauStage.initOwner(stageActuel);
            nouveauStage.initModality(Modality.WINDOW_MODAL);
            nouveauStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}

