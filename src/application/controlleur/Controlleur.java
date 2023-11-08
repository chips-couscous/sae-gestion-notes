package application.controlleur;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import application.model.GestionNotes;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controlleur {

	@FXML
	BorderPane rootPane; //Récupération de l'ID de la BorderPane principale de la page
	
	@FXML
	TextField note;
	
	@FXML
	TextField date;
	
	@FXML
	TextArea description;
	
	@FXML
	ComboBox<String> type;
	
	@FXML
	ComboBox<String> ressource;
	
	@FXML
	Button valider;
	
	@FXML
	Button validerNom;
	
	@FXML
	Label labelNomEtudiant;
	
	@FXML
	TextField texteNom;
	
	@FXML
	TextField textePrenom;
	
	@FXML
	Button boutonImporterFichierProgramme;
	
	@FXML
	Button boutonImporterFichierRessource;
	
	@FXML
	Button boutonPartagerFichier;
	
	String prenomEtu;
	String nomEtu;
	FXMLLoader loader = new FXMLLoader();
	
	

	/**
	 * Méthode permmettant de lancer d'autres méthodes ou des attributs 
	 * directement au lancement de l'application
	 */
	public void initialize() {
		if (note != null && valider != null && date != null && description != null && type != null && ressource != null) {
			

			valider.setOnAction(event -> validerModifierNote(note,ressource, type, description, date));
		}
		
		if (labelNomEtudiant != null && validerNom != null && texteNom != null && textePrenom != null) {
	        validerNom.setOnAction(event -> modifierNom(texteNom, textePrenom));
	        
	    }
		
		if (boutonImporterFichierProgramme != null && boutonImporterFichierRessource != null) {
			boutonImporterFichierProgramme.setOnAction(event -> selectionnerFichier());
			boutonImporterFichierRessource.setOnAction(event -> selectionnerFichier());
		}else if (boutonPartagerFichier != null) {
			boutonPartagerFichier.setOnAction(event -> selectionnerFichier());
		}
		
		if (labelNomEtudiant != null) {
			afficherNom();
		}

	}
	
	

    public void afficherNom() {
    	labelNomEtudiant.setText(GestionNotes.afficherIdentite());
    }
    
	public void modifierNom(TextField nom, TextField prenom) {
		
		GestionNotes.changerIdentite(nom.getText(), prenom.getText());
		afficherNom();
		
	}
	
	/**
	 * Cette méthode permet de récupérer la scene de la page de paramètre d'importation des données
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneParametre() {
		try {
			
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
	public void sceneModifierNote() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageModifierNote.fxml"));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
            popupStage.initOwner(stage); // Utilise la fenêtre principale comme propriétaire
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Popup Window");
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait(); // Attend la fermeture de la popup
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void validerModifierNote(TextField note, ComboBox<String> ressource, ComboBox<String> type, TextArea description, TextField date) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        System.out.print("Jy suis");
        creerNote(note, ressource, type, description, date);
	}
	
	
	private TextFormatter<String> createFormatter(Pattern pattern) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (pattern.matcher(newText).matches() || newText.isEmpty()) {
                return change;
            } else {
                return null;
            }
        };

        return new TextFormatter<>(filter);
    }

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
            nouveauStage.initModality(Modality.APPLICATION_MODAL);
            nouveauStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    

    
    
    
    private GridPane creerNote(TextField note, ComboBox<String> ressource, ComboBox<String> type, TextArea description, TextField date) {
    	GridPane grilleNote = new GridPane();
    	
    	grilleNote.setPrefWidth(640);
    	grilleNote.setPrefHeight(40);
    	
    	// Créer des labels pour chaque colonne
        Label labelNote = new Label(note.getText());
        Label labelType = new Label(type.getValue());
        Label labelRessource = new Label(ressource.getValue());  

        // Ajouter les labels à la GridPane
        grilleNote.add(labelNote, 0, 0); // Colonne 1, Ligne 1
        grilleNote.add(labelType, 1, 0); // Colonne 2, Ligne 1
        grilleNote.add(labelRessource, 2, 0); // Colonne 4, Ligne 1

        return grilleNote;
    }
    
    private void selectionnerFichier() {
    	FileChooser explorateurFichier = new FileChooser();
    	explorateurFichier.setTitle("Sélectionner un fichier");

        // Affiche la boîte de dialogue et attend que l'utilisateur sélectionne un fichier
        Stage stageActuel = (Stage) rootPane.getScene().getWindow();
        java.io.File fichierChoisi = explorateurFichier.showOpenDialog(stageActuel);

        // Si l'utilisateur a sélectionné un fichier, affiche son chemin
        if (fichierChoisi != null) {
            System.out.println("Fichier sélectionné : " + fichierChoisi.getAbsolutePath());
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }
    }
}

