package application.controlleur;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import application.model.GestionNotes;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	
	int indice = 0;

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
		
		if (note != null && description != null && date != null ) {
			note.setTextFormatter(patternNote());
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
	    
	    Button supprimer = new Button();
	    supprimer.setGraphic(imageViewSupprimer);
	    
	    GridPane mainGridPane = (GridPane) ((ScrollPane) ((Pane) ((BorderPane) listeNotes.getScene().getRoot()).getChildren().get(1)).getChildren().get(2)).getContent();
	    
	    mainGridPane.add(labelNote, 0, indice);
	    mainGridPane.add(labelDescription, 1, indice);
	    mainGridPane.add(labelDate, 2, indice);
	    mainGridPane.add(modifier, 4, indice);
	    mainGridPane.add(supprimer, 5, indice);
	    
	    GridPane.setHalignment(labelNote, javafx.geometry.HPos.CENTER);
	    GridPane.setHalignment(labelDescription, javafx.geometry.HPos.CENTER);
	    GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
	    GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
	    GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);
	    
	    labelNote.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85;");
		labelDescription.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85;");
		labelDate.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #354B85;");
		
		
		modifier.setMaxSize(30, 30);
		supprimer.setMaxSize(30, 30);
		
		supprimer.setOnMouseEntered(event -> supprimer.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%;"));
		supprimer.setOnMouseExited(event -> supprimer.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;"));
		
		modifier.setOnMouseEntered(event -> modifier.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%;"));
		modifier.setOnMouseExited(event -> modifier.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;"));
		
		supprimer.setOnAction(event -> sceneSupprimerNote(mainGridPane, indice));
		
		modifier.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
		supprimer.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
		indice ++;
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
	        popupStage.setTitle("Modifier Note");
	        Scene popupScene = new Scene(root);
	        popupStage.setScene(popupScene);
	        Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(7)).getChildren().get(0));
	        TextField note = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0);
	        TextArea description = (TextArea)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(0);
	        TextField date = (TextField) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(6)).getChildren().get(0));

	        boutonValider.setOnAction(e -> {
	            ajouterNote(note.getText(), description.getText(), date.getText());
	            popupStage.close();
	        });

	        popupStage.showAndWait();
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public void sceneSupprimerNote(GridPane gridPane, int rowIndex) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageSupprimerNote.fxml"));
	        Parent root = loader.load();

	        Stage popupStage = new Stage();

	        popupStage.initModality(Modality.APPLICATION_MODAL);
	        popupStage.setTitle("Supprimer Note");
	        Scene popupScene = new Scene(root);
	        popupStage.setScene(popupScene);
	        Button boutonOui = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1))));
	        Button boutonNon = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(2))));
				        
	        
	        boutonOui.setOnMouseEntered(event -> boutonOui.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
	        boutonOui.setOnMouseExited(event -> boutonOui.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));
	        
	        boutonNon.setOnMouseEntered(event -> boutonNon.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
	        boutonNon.setOnMouseExited(event -> boutonNon.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));

	        boutonOui.setOnAction(e -> {
	            supprimerNote(gridPane, rowIndex);

	            popupStage.close();
	            
	        });
	        
	        boutonNon.setOnAction(e -> {
	            popupStage.close();
	        });
	     
	        

	        popupStage.showAndWait();
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	}

	
	
	private TextFormatter<Object> patternNote() {
		Pattern pattern = Pattern.compile("^1000$|^\\d{1,3}(\\.\\d{0,2})?$");

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
	
	
	
	private void supprimerNote(GridPane gridPane, int rowIndex) {
		
		ObservableList<Node> children = gridPane.getChildren();
	    
	    // Créez une copie de la liste des enfants pour éviter les problèmes de concurrence
	    List<Node> childrenCopy = new ArrayList<>(children);

	    // Supprimez tous les nœuds de la ligne spécifiée
	    childrenCopy.removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex);

	    // Mettez à jour la GridPane avec la nouvelle liste d'enfants
	    gridPane.getChildren().setAll(childrenCopy);
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