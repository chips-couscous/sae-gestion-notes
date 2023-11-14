package application.controlleur;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import application.modele.GestionNote;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
	Pane paneCentre;
	@FXML
	TextField date;
	@FXML
	TextArea description;
	@FXML
	ScrollPane listeNotes;
	@FXML
	ComboBox<String> type;
	@FXML
	ComboBox<String> ressource;
	@FXML
	Button valider;
	@FXML
	GridPane grilleNotes;
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
		
		if (labelNomEtudiant != null && validerNom != null && texteNom != null && textePrenom != null) {
			validerNom.setOnAction(event -> modifierNom(texteNom, textePrenom));
		}
		 
		if (boutonImporterFichierProgramme != null && boutonImporterFichierRessource != null) {
			boutonImporterFichierProgramme.setOnAction(event -> selectionnerFichier());
			boutonImporterFichierRessource.setOnAction(event -> selectionnerFichier());
		} else if (boutonPartagerFichier != null) {
			boutonPartagerFichier.setOnAction(event -> selectionnerFichier());
		}
		
		if (labelNomEtudiant != null) {
			afficherNom();
		}
		 
	}


	private void ajouterNote(String note, String description, String date) {
		Label labelNote = new Label(note);
		Label labelDescription = new Label(description);
		Label labelDate = new Label(date);    
		Image imageModifier = new Image(getClass().getResourceAsStream("/application/controlleur/modifier.png"));
		Image imageSupprimer = new Image(getClass().getResourceAsStream("/application/controlleur/supprimer.png"));

		ImageView imageViewModifier = new ImageView(imageModifier);
		ImageView imageViewSupprimer = new ImageView(imageSupprimer);

		Button modifier = new Button();
		modifier.setGraphic(imageViewModifier);
		modifier.getStyleClass().add("hover-button");
		Button supprimer = new Button();
		supprimer.setGraphic(imageViewSupprimer);
		supprimer.getStyleClass().add("hover-button");
		GridPane mainGridPane = (GridPane) ((ScrollPane) ((Pane) ((BorderPane) listeNotes.getScene().getRoot()).getChildren().get(1)).getChildren().get(2)).getContent();
		mainGridPane.add(labelNote, 0, 0);
		mainGridPane.add(labelDescription, 1, 0);
		mainGridPane.add(labelDate, 2, 0);
		mainGridPane.add(modifier, 4, 0);
		mainGridPane.add(supprimer, 5, 0);
		GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
		GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);
		mainGridPane.setStyle("-fx-border-color: #E6E9F0; -fx-border-width: 2px; -fx-border-radius: 2px;");
		labelNote.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85; -fx-background-color: #E6E9F0;");
		labelDescription.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85;");
		labelDate.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85;");
		labelNote.setMaxSize(130, 40);
		labelDescription.setMaxSize(130, 40);
		labelDate.setMaxSize(130, 40);
		labelNote.setAlignment(Pos.CENTER);
		labelDescription.setAlignment(Pos.CENTER);
		labelDate.setAlignment(Pos.CENTER);
		modifier.setMaxSize(30, 30);
		supprimer.setMaxSize(30, 30);

		modifier.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
		supprimer.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
	}




	
	public void afficherNom() {
		labelNomEtudiant.setText(GestionNote.afficherIdentite());
	}


	public void modifierNom(TextField nom, TextField prenom) {
		GestionNote.changerIdentite(nom.getText(), prenom.getText());
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
			System.out.print("Je suis dans Parametre");
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
			System.out.print("Je suis dans Modifier");
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
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("../vue/application.css").toExternalForm());
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
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Popup");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);
			Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(0));
			TextField note = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0);
			TextArea description = (TextArea)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(0);
			TextField date = (TextField) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));


			boutonValider.setOnAction(e -> {
				// Add logic to update the main gridPane with entered information
				ajouterNote(note.getText(), description.getText(), date.getText());
				popupStage.close();
			});


			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	//Stage stage = (Stage) valider.getScene().getWindow();
	//stage.close();
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

