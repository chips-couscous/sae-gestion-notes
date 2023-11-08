package application.controlleur;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

	ArrayList<String[]> listeNote = new ArrayList<String[]>(); // [note, type, ate, desp]

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

	/**
	 * Méthode permmettant de lancer d'autres méthodes ou des attributs 
	 * directement au lancement de l'application
	 */
	public void initialize() {
		if (note != null && valider != null && date != null && description != null && type != null && ressource != null) {
			Pattern notePattern = Pattern.compile("^(1?[0-9],?[0-9]?)|(20,?0?)$"); // Format de note valide
			note.setTextFormatter(createFormatter(notePattern));

			System.out.println("appel a validerModifierNote");

			valider.setOnAction(event -> validerModifierNote(note, ressource, type, description, date));
		}
		if (labelNomEtudiant != null && validerNom != null && texteNom != null && textePrenom != null) {
			validerNom.setOnAction(event -> modifierNom(texteNom, textePrenom, labelNomEtudiant));
		}

		if (boutonImporterFichierProgramme != null && boutonImporterFichierRessource != null) {
			boutonImporterFichierProgramme.setOnAction(event -> selectionnerFichier());
			boutonImporterFichierRessource.setOnAction(event -> selectionnerFichier());
		}else if (boutonPartagerFichier != null) {
			boutonPartagerFichier.setOnAction(event -> selectionnerFichier());
		}
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

	@FXML
	public void sceneModifierNote() {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageModifierNote.fxml"));
	        Parent root = loader.load();
	        Stage popupStage = new Stage();
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        popupStage.initModality(Modality.APPLICATION_MODAL);
	        popupStage.setTitle("Modifier Note");

	        // Associez la scène à la nouvelle fenêtre (popup)
	        Scene scene = new Scene(root);
	        popupStage.setScene(scene);

	        // Montrez la nouvelle fenêtre (popup) de manière modale
	        popupStage.initModality(Modality.APPLICATION_MODAL);
	        popupStage.initOwner(stage);
	        popupStage.showAndWait(); // Attend la fermeture de la popup
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void validerModifierNote(TextField note, ComboBox<String> ressource, ComboBox<String> type, TextArea description, TextField date) {
	    Stage stage = (Stage) valider.getScene().getWindow(); // Récupérez la fenêtre modale
	    stage.close(); // Fermez la fenêtre modale

	    // Appelez la méthode creerNote avec les paramètres
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

	private void modifierNom(TextField nom, TextField prenom, Label labelEtudiant) {
		labelEtudiant.setText(nom.getText() + " " + prenom.getText());
	}

	private void creerNote(TextField note, ComboBox<String> ressource, ComboBox<String> type, TextArea description, TextField date) {
		System.out.println("Dans creerNote");
		Scene sceneActuelle = (Scene) rootPane.getScene();
	    Object userData = sceneActuelle.getUserData();
	    
	    if (userData == null || !userData.toString().equals("ScenePageNote")) {
	        // Naviguez vers la scène "PageNotes.fxml" si vous n'êtes pas déjà sur cette scène
	        changerSceneNotes();
	        System.out.println("Pas bonne scene");
	        // return; // Quittez la méthode pour éviter d'ajouter la grilleNote sur la mauvaise scène
	    }
	    sceneActuelle = (Scene) rootPane.getScene();
		System.out.println(sceneActuelle.getUserData());
		
		// Créez la grilleNote
		GridPane grilleNote = new GridPane();
		grilleNote.setPrefWidth(640);
		grilleNote.setPrefHeight(40);

		// Créez des labels pour chaque colonne
		Label labelNote = new Label(note.getText());
		Label labelDescription = new Label(description.getText());
		Label labelDate = new Label(date.getText());

		Button boutonTest = new Button("Test");

		// Ajoutez les labels à la grilleNote
		grilleNote.add(labelNote, 0, 0); // Colonne 1, Ligne 1
		grilleNote.add(labelDescription, 1, 0); // Colonne 2, Ligne 1
		grilleNote.add(labelDate, 2, 0); // Colonne 3, Ligne 1
		grilleNote.add(boutonTest, 3, 0);

		// Récupérez la scène et le BorderPane principaux

		BorderPane grillePrincipale = (BorderPane) sceneActuelle.getRoot();

		// Récupérez le Pane sur lequel vous souhaitez ajouter la grilleNote
		Pane paneCentre = (Pane) sceneActuelle.lookup("#paneCentre");

		if (paneCentre != null) {
			System.out.println("YOUHOU");
			// Ajoutez grilleNote au centre de la Pane
			paneCentre.getChildren().add(grilleNote);
			grilleNote.setVisible(true);
			grilleNote.setLayoutX(500);
			grilleNote.setLayoutY(500);
			grilleNote.setBorder(new Border(new BorderStroke(Color.BLACK,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		} else {
			System.out.println("L'élément avec l'identifiant 'grillePane' n'a pas été trouvé sur la scène PageNotes.fxml.");
		}
	}


	private void selectionnerFichier() {
		FileChooser explorateurFichier = new FileChooser();
		explorateurFichier.setTitle("Sélectionner un fichier");
		explorateurFichier.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
		
		// Affiche la boîte de dialogue et attend que l'utilisateur sélectionne un fichier
		Stage stageActuel = (Stage) rootPane.getScene().getWindow();
		java.io.File fichierChoisi = explorateurFichier.showOpenDialog(stageActuel);

		// Si l'utilisateur a sélectionné un fichier, affiche son chemin
		if (fichierChoisi != null) {
			System.out.println("Fichier sélectionné : " + fichierChoisi.getAbsolutePath());
			String nomFichier = fichierChoisi.getAbsolutePath();
			int tailleNomFichier = nomFichier.length();
			String extension = nomFichier.substring(tailleNomFichier -3, tailleNomFichier);
			if (extension.equals("csv")) {
				System.out.println("Extension valide");
			}else {
				System.out.println("Extension invalide");
			}
		} else {
			System.out.println("Aucun fichier sélectionné.");
		}
	}
}

