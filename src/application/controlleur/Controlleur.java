/*
 * Controlleur.java                                               	24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.controlleur;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import application.model.Competence;
import application.model.Controle;
import application.model.Enseignement;
import application.model.GestionNotes;
import application.model.Note;
import application.model.Portfolio;
import application.model.Ressource;
import application.model.Sae;
import application.model.exception.ControleInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.IpException;
import application.model.exception.MoyenneCompetenceException;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;
import application.model.exception.PortReseauException;
import application.model.exception.UtilisateurInvalideException;
import application.model.exception.cheminFichierException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Classe permettant le controle de la vue et le lien entre le model et la vue
 * @author thomas.izard
 * @author constant.nguyen
 */
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
	TextField denominateur;
	@FXML
	TextArea commentaire;
	@FXML
	ScrollPane listeNotes;
	@FXML
	ScrollPane listeCompetencesTri;
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
	Button boutonAjouterNote;
	@FXML
	Button boutonPartagerFichier;
	@FXML
	Button boutonAide;
	@FXML
	Button boutonSelectionFichierPartager;
	@FXML
	Button boutonSelectionDossierReception;
	@FXML
	Button btnEnvoyerFichier;
	@FXML
	Button boutonReinitialisation;
	@FXML
	Button btnRecevoirFichier;
	@FXML
	Button boutonNotes;
	@FXML
	Button boutonParametreImporter;
	@FXML
	Button boutonParametreImporter2;
	@FXML
	Button boutonParametrePartager;
	@FXML
	Button boutonParametreModifier;
	@FXML
	Button boutonParametreReinitialiser;
	@FXML
	Button boutonRessources;
	@FXML
	Label ipUtilisateur;
	@FXML
	Label cheminFichierExport;
	@FXML
	Label erreurChamps;
	@FXML
	Label cheminDossierReception;     
	@FXML
	TextField saisieIpServeur;
	@FXML
	TextField saisiePortClient;
	@FXML
	TextField saisiePortServeur;
	@FXML
	Button boutonSauvegarder;
	@FXML
	ComboBox<String> ressourcesCombo;
	@FXML
	ComboBox<String> controleCombo;
	@FXML
	Label controle;
	@FXML
	Label etoile;
	@FXML
	ComboBox<String> comboRessourcesControle;

	String prenomEtu;

	String nomEtu;

	//int indice = 0;
	int indiceEnseignement = 0;
	
	/* Nombre Maximum de caracteres dans une ligne de commentaire */ 
	private static final int LONGUEUR_MAX_LIGNE = 50;
	private static final int LONGUEUR_MAX = 200;

	/* Récupération de l'instance du Model Gestion Notes permettant la liaison entre la vue et le model */
	private static GestionNotes gn = GestionNotes.getInstance();

	FXMLLoader loader = new FXMLLoader(); // FXML loader permettant de charger un fichier FXML et de changer de scene
	
	/* Messages qui apparaissent lors du survol en fonction du bouton */
	final String MESSAGE_AIDE = "Aide";
	final String MESSAGE_SAUVEGARDE = "Sauvegarder";
	final String MESSAGE_AJOUT_NOTE = "Ajouter une note";
	final String MESSAGE_AJOUT_CONTROLE = "Ajouter un contrôle";
	final String MESSAGE_SUPPRIMER_NOTE = "Supprimer une note";
	final String MESSAGE_MODIFIER_NOTE = "Modifier une note";

	/**
	 * Méthode permmettant de lancer d'autres méthodes ou des attributs
	 * directement au lancement de l'application
	 */
	public void initialize() {
		
		/* Vérification de la présence du bouton de réalisation sur la page */
		if (boutonReinitialisation != null) {
			/* Si le bouton est présent, on lui attribut le fait de réinitialiser les données */
			boutonReinitialisation.setOnAction(event -> reinitialiserDonnees());
		}
		
		/* vérification de la présence du bouton d'importation sur la page */
		if (boutonImporterFichierProgramme != null) {
			
			/* Test permettant de savoir si un fichier Semestre a déja été importé ou non */
			if (!fichierImporter()) {
				
				/* Si il n'est pas importé, on fait appel à une méthode qui désactive les autres boutons */
				desactiverAutresBoutons();
			}
		}
		
		/* Vérification de la présence du Label qui affiche si une erreur de saisie à lieu */
		if (erreurChamps != null) {
			/* Permet de ne pas afficher l'erreur tant que l'utilisateur n'a rien saisi */
			erreurChamps.setVisible(false);
		}

		/* Vérification de la présence de la grille contenant la liste des notes */
		if (listeNotes != null) {
			
			/* Récupération de la Grille ou sont affichées toutes les ressources */
			GridPane grilleRessources = (GridPane)((ScrollPane) ((Pane)(rootPane).getChildren().get(1)).getChildren().get(3)).getContent();
			/* Récupération de la Grille ou sont affichées toutes les notes */
			GridPane grilleNotes = (GridPane)((ScrollPane) ((Pane)(rootPane).getChildren().get(1)).getChildren().get(1)).getContent();
			/* Ajout à la grille des ressources, les ressources présentes dans le model */
			afficherEnseignements(true, grilleRessources, gn.getSemestreGestionNotes().getEnseignementsSemestre(), grilleNotes);
			/* Affichage de toutes les notes dans la grilleNote si la grille est affichée */
			afficherNotes(grilleNotes,null);
		}
		
		/* vérification de la présence de la ComboBox ou sont affichées les ressources */
		if (ressourcesCombo != null) {
			/* Ajout de toutes les ressources dans la ComboBox */
			ajoutRessourcesCombo();
			/* Méthode permettant de vérifier en permanence le choix saisi dans la ComboBox */
			choixComboRessources(ressourcesCombo);
		}
		
		/* vérification de la présence du bouton de sauvegarde des données */
		if (boutonSauvegarder != null) {
			/* On affecte au bouton le fait de sauvegarder les données de l'application et du model */
			boutonSauvegarder.setOnAction(event -> sauvegarder());
			//Récupération d'image pour nos boutons
			Image imageSauvegarder = new Image(getClass().getResourceAsStream("/application/controlleur/sauvegarder.png"));
			// On définit une ImageView afin de pouvoir mettre l'image dans le bouton
			ImageView imageViewSauvegarder = new ImageView(imageSauvegarder);
			/* Taille des images de nos boutons */
			imageViewSauvegarder.setFitWidth(30);
			imageViewSauvegarder.setFitHeight(30);
			/* Change la couleur de notre image */
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setContrast(-1.0);
			colorAdjust.setBrightness(1.0);
			imageViewSauvegarder.setEffect(colorAdjust);
			/* On met le style dans nos boutons */
			boutonSauvegarder.setGraphic(imageViewSauvegarder);
			boutonSauvegarder.setText(""); // Désactive le texte du bouton
			boutonSauvegarder.getStyleClass().add("boutonSauvegarderAide");
		}
		/* Vérification de la présence du bouton d'aide à l'utilisateur */
		if (boutonAide != null) {
			/* On affecte à notre boutons le fait d'afficher l'aide */
			boutonAide.setOnAction(event -> afficherAide());
			//Récupération d'image pour nos boutons
			Image imageAide = new Image(getClass().getResourceAsStream("/application/controlleur/aide.png"));
			// On définit une ImageView afin de pouvoir mettre l'image dans le bouton
			ImageView imageViewAide = new ImageView(imageAide);
			/* Taille des images de nos boutons */
			imageViewAide.setFitWidth(30);
			imageViewAide.setFitHeight(30);
			/* Change la couleur de notre image */
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setContrast(-1.0);
			colorAdjust.setBrightness(1.0);
			imageViewAide.setEffect(colorAdjust);
			/* On met le style dans nos boutons */
			boutonAide.setGraphic(imageViewAide);
			boutonAide.setText(""); // Désactive le texte du bouton
			boutonAide.getStyleClass().add("boutonSauvegarderAide");
		}
		
		/* Vérification de la présence de la ComboBox où sont affichés les controles */
		if (comboRessourcesControle != null) {
			/* On ajoute les controles dans la ComboBox */
			ajoutRessourcesComboControle();
		}
		/* Vérification de la présence du bouton d'ajout de note */
		if (boutonAjouterNote != null) {
			afficherMessageSurvol(boutonAjouterNote, MESSAGE_AJOUT_NOTE);
		}
		/* Vérification de la présence des éléments fxml */
		if (labelNomEtudiant != null && validerNom != null && texteNom != null && textePrenom != null) {
			/* Le bouton pour changer le nom appelera la méthode de changement de nom au clic */
			validerNom.setOnAction(event -> modifierNom(texteNom, textePrenom));
		}

		/* Vérification de la présence des éléments fxml */
		if (boutonImporterFichierProgramme != null && boutonImporterFichierRessource != null) {
			/* Les boutons d'mportation appeleront la selection des fichiers */
			boutonImporterFichierProgramme.setOnAction(event -> selectionnerFichier(boutonImporterFichierProgramme));
			boutonImporterFichierRessource.setOnAction(event -> selectionnerFichier(boutonImporterFichierRessource));

		} else if (boutonPartagerFichier != null) {
			/* Le bouton de partage de fichier appelera la selection de fichier */
			boutonPartagerFichier.setOnAction(event -> selectionnerFichier(boutonPartagerFichier));
		}
		/* Vérification de la présence des éléments fxml */
		if (labelNomEtudiant != null) {
			afficherNom(); // Affichage du nom
		}
		// Vérification de la présence des éléments fxml
		if (note != null && commentaire != null && denominateur != null) {
			// Appliquer un format de saisie spécifique aux TextField
			note.setTextFormatter(pattern("^1000$|^\\d{1,3}(\\.\\d{0,2})?$"));
			denominateur.setTextFormatter(pattern("^1000$|^\\d{1,3}?$"));
			commentaire.setWrapText(true);
			commentaire.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
				// Vérifier si la longueur du texte dépasse la limite
				if (nouvelleValeur.length() > LONGUEUR_MAX_LIGNE) {
					int dernierEspace = nouvelleValeur.lastIndexOf(" ", LONGUEUR_MAX_LIGNE);
					if (dernierEspace > 0 && dernierEspace == LONGUEUR_MAX_LIGNE) {
						commentaire.replaceText(dernierEspace, dernierEspace + 1, "\n"); // Insérer un retour à la ligne après le dernier espace avant la limite
					}
				}
				if (nouvelleValeur.length() > LONGUEUR_MAX) {
					commentaire.setText(ancienneValeur); // Revenir à la valeur précédente si la limite est dépassée
				}
			});
		}
		/* Vérification de la présence des éléments pour la page partager paramètres */
		if (boutonSelectionFichierPartager != null && ipUtilisateur != null
				&& saisieIpServeur != null && btnEnvoyerFichier != null
				&& btnRecevoirFichier != null && cheminFichierExport != null
				&& saisieIpServeur != null && saisiePortClient != null
				&& saisiePortServeur != null && boutonSelectionDossierReception != null
				&& cheminDossierReception != null) {
			boutonSelectionFichierPartager.setOnAction(event -> selectionnerFichier(boutonSelectionFichierPartager));
			boutonSelectionDossierReception.setOnAction(event -> selectionnerDossierReception());
			btnEnvoyerFichier.setOnAction(event -> envoyerFichier());
			btnRecevoirFichier.setOnAction(event -> recevoirFichier());
			saisiePortClient.setTextFormatter(pattern("^\\d{1,5}?$"));
			saisiePortServeur.setTextFormatter(pattern("^\\d{1,5}?$"));
			afficherIP();
		}
	}

	/**
	 * Méthode permettant de désactiver des boutons
	 * Elle est utile pour désactiver les clics de tous les boutons
	 * présents sur la page d'importation si aucun fichier n'a été importé
	 */
	public void desactiverAutresBoutons() {
		boutonNotes.setDisable(true);
		boutonParametreImporter.setDisable(true);
		boutonParametreImporter2.setDisable(true);
		boutonParametrePartager.setDisable(true);
		boutonParametreModifier.setDisable(true);
		boutonParametreReinitialiser.setDisable(true);
		boutonRessources.setDisable(true);
	}

	/**
	 * Méthode permettant de réactiver des boutons
	 * Elle est utile pour réactiver les clics de tous les boutons
	 * présents sur la page d'importation si un fichier a déja été importé
	 */
	private void activerAutresBoutons() {
		boutonNotes.setDisable(false);
		boutonParametreImporter.setDisable(false);
		boutonParametreImporter2.setDisable(false);
		boutonParametrePartager.setDisable(false);
		boutonParametreModifier.setDisable(false);
		boutonParametreReinitialiser.setDisable(false);
		boutonRessources.setDisable(false);
	}

	/**
	 * Méthode qui permet de renvoyer le boolean du model.
	 * Ce boolean indique si un fichier a déja été importé ou non
	 */
	public static boolean fichierImporter() {
		return gn.getFichierImporte();
	}

	/**
	 * Permet la gestion de l'affichage de la page de modification d'une note.
	 * Elle force l'utilisateur à ne saisir un certain Pattern (pour les notes que des chiffres à virgules)
	 * Elle permet aussi de gérer la tailles, les retours à la ligne 
	 * et la limite de caracteres du commentaire
	 * @param note
	 * @param denominateur
	 * @param commentaire
	 */
	private void affichageModifAjoutNote(TextField note, TextField denominateur, TextArea commentaire) {
		// Appliquer un format de saisie spécifique aux TextField
		note.setTextFormatter(pattern("^1000$|^\\d{1,3}(\\.\\d{0,2})?$"));
		denominateur.setTextFormatter(pattern("^1000$|^\\d{1,3}?$"));
		commentaire.setWrapText(true);
		/* Vérifie en permanence la saisie de l'utilisateur pour ne pas dépasser une certaine longueur de ligne et de caracteres */
		commentaire.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
			// Vérifier si la longueur du texte dépasse la limite
			if (nouvelleValeur.length() > LONGUEUR_MAX_LIGNE) {
				int dernierEspace = nouvelleValeur.lastIndexOf(" ", LONGUEUR_MAX_LIGNE);
				if (dernierEspace > 0 && dernierEspace == LONGUEUR_MAX_LIGNE) {
					commentaire.replaceText(dernierEspace, dernierEspace + 1, "\n"); // Insérer un retour à la ligne après le dernier espace avant la limite
				}
			}
			if (nouvelleValeur.length() > LONGUEUR_MAX) {
				commentaire.setText(ancienneValeur); // Revenir à la valeur précédente si la limite est dépassée
			}
		});
	}

	/**
	 * Méthode permettant de forcer une certaine saisie à l'utilisateur
	 * pour renseigner le poid. 
	 * Il ne pourra que saisir des nombres compris entre 1 et 100.
	 * @param poids
	 */
	private void affichageModifAjoutControle(TextField poids) {
		// Appliquer un format de saisie spécifique aux TextField
		poids.setTextFormatter(pattern("^100$|^\\d{1,2}?$"));
	}

	/**
	 * Appelé par le bouton sauvegarder
	 * Demande la confirmation de la sauvegarde
	 * Si réponse affirmative, appel la méthode de sauvegarde de GestionNote
	 */
	private void sauvegarder() {
		try {
			/* Affiche un message d'alerte pour prévenir l'utilisateur de ce qu'il va se passer */
			Alert verifSauvegarde = new Alert(AlertType.CONFIRMATION);
			/* Titre de l'laerte */
			verifSauvegarde.setTitle("Sauvegarde");
			/* Texte présent dans l'alerte */
			verifSauvegarde.setHeaderText("Cette sauvegarde va écraser les anciennes données sauvegardés");
			verifSauvegarde.setContentText("Voulez-vous vraiment sauvegarder ? ");
			/* Affiche de deux boutons dans l'alerte permettant un choix */
			verifSauvegarde.getButtonTypes().setAll(ButtonType.YES,ButtonType.CANCEL);
			verifSauvegarde.showAndWait();
			/* Si reponse de l'utilisateur est OUI */
			if (verifSauvegarde.getResult() == ButtonType.YES) {
				/* On serialize l'application et on affiche une alerte disant que les données sont sauvegarder */
				gn.serializerDonnees();
				Alert sauvegardeReussi = new Alert(AlertType.INFORMATION);
				sauvegardeReussi.setTitle("Sauvegarde réussi");
				sauvegardeReussi.setHeaderText("Les modifications ont bien été sauvegardés");
				sauvegardeReussi.showAndWait();
			}
		} catch (IOException e) {
			/* Si la serialization n'a pas pu se faire on leve une exception 
			 * et on affiche une alerte indiquant qu'il n'a pas été possible de sauvegarder */
			Alert erreurSauvegarde = new Alert(AlertType.ERROR);
			erreurSauvegarde.setTitle("Sauvegarde impossible");
			erreurSauvegarde.setHeaderText(e.getMessage());
			erreurSauvegarde.showAndWait();
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode permettant d'afficher l'aide à l'utilisateur
	 * Elle est appelée quand le bouton d'aide est cliqué
	 */
	private void afficherAide() {
		Alert boiteAide = new Alert(AlertType.INFORMATION);
		boiteAide.setTitle("Aide");
		boiteAide.setHeaderText("Les aides sont affichés");
		boiteAide.showAndWait();
	}

	/**
	 * Cette méthode permet d'afficher dans la ScrollPane de gauche
	 * de la page Enseignements, les compétences actuellement importées.
	 * On retrouve une option Toutes qui si cliquée affiche les 
	 * ressources de toutes les compétences.
	 * Chaque compétence est cliquable et afficheront si cliquée
	 * la liste des ressources appartenant à cette compétence.
	 * @param listeCompetences la liste des compétences de l'application
	 * @param scene la scene où l'ajout sera fait
	 */
	private void ajouterCompetence(List<Competence> listeCompetences, Scene scene) {
		/* Entier correspondant au numéro d'index de la ligne
		 * à laquelle doit s'afficher la compétence actuelle.
		 * Cet entier est initialisé à 1 car la ligne d'index 0 
		 * est occupée par la ligne "Toutes". 
		 */
		int indiceCompetence = 1;
		/* Nombre maximum de caractères par ligne*/
		int maxCaractere = 25;
		//Permet de mettre un taille à une ligne quand on l'ajoute
		RowConstraints taille = new RowConstraints();
		taille.setPrefHeight(50);
		/* Récupération de la grille des compétences
		 * soit la grille à gauche de la page Enseignements
		 */
		GridPane grilleCompetence = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(4)).getContent());
		/* Création d'un label pour la ligne "Toutes"*/
		Label labelToutes = new Label("Toutes");
		/* Ajustement de la taille et du style du Label/Texte*/
		labelToutes.setPrefSize(185,30);
		labelToutes.getStyleClass().add("labelCompetence");
		/* Création d'une Pane cliquable qui contiendra le label Toutes*/
		Pane ligneToutes = new Pane();
		/* Ajustement de la taille et du style de la Pane*/
		ligneToutes.getStyleClass().add("paneCompetence");
		ligneToutes.getStyleClass().add("paneCompetenceNonCliquee");
		ligneToutes.setPrefSize(185,30);
		/* Modification de l'ID de cette Pane pour identifier
		 * si elle est cliquée ou non
		 */
		ligneToutes.setId("Toutes Non Cliquée");
		/* Ajout du Label à la Pane*/
		ligneToutes.getChildren().add(labelToutes);
		labelToutes.setAlignment(Pos.CENTER);
		/* Récupération de la grille des Enseignements
		 * soit la grille au milieu de la page Enseignements
		 */
		GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
		/* Ajout de la ligne "Toutes" à la ligne d'index 0*/
		grilleCompetence.add(ligneToutes,0,0);
		/* Récupération de la liste de tous les enseignements*/
		List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
		/* Ajout d'un évènement à la Pane "Toutes"
		 * qui sera déclenché lorsque celle-çi sera cliquée
		 * la méthode afficherEnseignements() sera déclenchée
		 */
		ligneToutes.setOnMouseClicked(event -> {
			/* Si la Pane "Toutes" n'était pas sélectionnée avant le clic */
			if (ligneToutes.getId().equals("Toutes Non Cliquée")) {
				/* Modification de son ID à 'cliquée' */
				ligneToutes.setId("Toutes Cliquée");
				/* Parcours de la liste des competences contenues
				 * dans la grille des competences de la page Enseignements
				 */
				for (Node competence : grilleCompetence.getChildren()) {
					/* Si il s'agit de la Pane "Toutes" qui est cliquée alors : */
					if (competence.getId().equals("Toutes Non Cliquée") || competence.getId().equals("Toutes Cliquée")) {
						/* Suppression de toutes les classes de style dont l'identifiant 
						 * est "paneCompetenceCliquee" à la pane "Toutes" */
						competence.getStyleClass().removeAll("paneCompetenceCliquee");
						/* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
						 * à la pane "Toutes" */
						competence.getStyleClass().add("paneCompetenceNonCliquee");
						/* Suppression de toutes les classes de style dont l'identifiant 
						 * est "labelCompetenceCliquee" au label de la pane "Toutes" */
						((Label) ((Pane) competence).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
						/* Ajout de la classe de style dont l'identifiant 
						 * est "labelCompetenceNonCliquee" au label de la pane "Toutes" */
						((Label) ((Pane) competence).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
						/* Modification de l'ID de la pane "Toutes"
						 * à "Toutes Non Cliquée"
						 */
						competence.setId("Toutes Non Cliquée");
					} else {
						/* Suppression de toutes les classes de style dont l'identifiant 
						 * est "paneCompetenceCliquee" à la pane de cette competence */
						competence.getStyleClass().removeAll("paneCompetenceCliquee");
						/* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
						 * à la pane de cette competence */
						competence.getStyleClass().add("paneCompetenceNonCliquee");
						/* Suppression de toutes les classes de style dont l'identifiant 
						 * est "labelCompetenceCliquee" au Text de cette competence */
						((Text) ((Pane) competence).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
						/* Ajout de la classe de style dont l'identifiant 
						 * est "labelCompetenceNonCliquee" au Text de cette competence */
						((Text) ((Pane) competence).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
						/* Modification de l'ID de la pane de cette compétence
						 * à "Non Cliquée" car si une pane est sélectionnée
						 * alors toutes les autres ne le sont pas
						 */
						competence.setId("Non Cliquée");
					}
				}
				/* Suppression de toutes les classes de style dont l'identifiant 
				 * est "paneCompetenceNonCliquee" à la pane "Toutes" */
				ligneToutes.getStyleClass().removeAll("paneCompetenceNonCliquee");
				/* Ajout d'une classe de la classe de style "paneCompetenceCliquee" 
				 * à la pane "Toutes" */
				ligneToutes.getStyleClass().add("paneCompetenceCliquee");
				/* Suppression de toutes les classes de style dont l'identifiant 
				 * est "labelCompetenceNonCliquee" à la pane "Toutes" */
				labelToutes.getStyleClass().removeAll("labelCompetenceNonCliquee");
				/* Ajout d'une classe de la classe de style "labelCompetenceCliquee" 
				 * à la pane "Toutes" */
				labelToutes.getStyleClass().add("labelCompetenceCliquee");
			}else {
				/* Modification de l'ID de la pane "Toutes
				 * à "Toutes Non Cliquée" car si une pane est sélectionnée
				 * alors elle ne l'est pas
				 */
				ligneToutes.setId("Toutes Non Cliquée");
				/* Suppression de toutes les classes de style dont l'identifiant 
				 * est "paneCompetenceCliquee" à la pane "Toutes" */
				ligneToutes.getStyleClass().removeAll("paneCompetenceCliquee");
				/* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
				 * à la pane "Toutes" */
				ligneToutes.getStyleClass().add("paneCompetenceNonCliquee");
				/* Suppression de toutes les classes de style dont l'identifiant 
				 * est "labelCompetenceCliquee" à la pane "Toutes" */
				labelToutes.getStyleClass().removeAll("labelCompetenceCliquee");
				/* Ajout d'une classe de la classe de style "labelCompetenceNonCliquee" 
				 * à la pane "Toutes" */
				labelToutes.getStyleClass().add("labelCompetenceNonCliquee");
			}
			/* Appel à la méthode afficherEnseignements :
	         * - Le booléen est fixé à false car on souhaite afficher dans la grille
	         *   de tri de competences dans la page Enseignements
	         * - grilleEnseignement correspond à la grille des enseignements de la page Enseignements
	         * - listeEnseignement correspond à la liste des Enseignements de la competence selectionnée
	         * - grilleEnseignement correspond à la grille des enseignements de la page Enseignements
	         */
			afficherEnseignements(false, grilleEnseignement, listeEnseignement , grilleEnseignement);
		});

		/* Parcours de la liste des compétence importées*/
		for(Competence competence: gn.getSemestreGestionNotes().getCompetencesSemestre()) {
			/* Récupération du nom d'une compétence
			 * Identifiant + Intitulé
			 */
			String texteCompetence = competence.getIdentifiantCompetence() + " " + competence.getIntituleCompetence();
			/* Création d'un objet de type Text 
			 * contenant le nom d'une compétence
			 */
			Text texte = new Text(texteCompetence);
			texte.setWrappingWidth(maxCaractere * 7); // La largeur de l'espace pour un nombre de caractères
			/* Création d'une Pane qui contiendra le texte*/
			Pane paneCompetence = new Pane();
			/* Ajout d'un id pour définir la compétence
			 * comme non cliquée.
			 */
			paneCompetence.setId("Non Cliquée");
			/* Ajout du texte à la Pane*/
			paneCompetence.getChildren().add(texte);
			/* Espacement du texte afn de le centrer*/
			texte.setTranslateY(5);
			/* Ajout de la contrainte de taille aux lignes*/
			grilleCompetence.getRowConstraints().addAll(taille);
			/* Modification de style et de taille*/
			paneCompetence.getStyleClass().add("paneCompetence");
			paneCompetence.getStyleClass().add("paneCompetenceNonCliquee");
			paneCompetence.setPrefSize(185, 40);

			texte.getStyleClass().add("labelCompetence");

			/* Gestion d'alignement*/
			texte.setLayoutY(10);
			paneCompetence.setPadding(new Insets(0,5,0,5));

			GridPane.setHalignment(texte, javafx.geometry.HPos.CENTER);
			texte.setTextAlignment(TextAlignment.CENTER);
			GridPane.setHalignment(paneCompetence, javafx.geometry.HPos.CENTER);

			/* Ajoute de la compétence à la grille*/
			grilleCompetence.add(paneCompetence, 0, indiceCompetence);
			/* Incrément de l'indice*/
			indiceCompetence++;
			/* Ajout d'un évènement à la pane de la compétence
			 * qui sera déclenchée lorsque la pane sera cliquée.
			 * Cet évenement appelle la méthode ajouterEnseignements
			 * qui affiche les Enseignements appartenant à cette compétence.
			 */
			paneCompetence.setOnMouseClicked(event -> {
			    /* Si la Pane de la compétence n'était pas sélectionnée avant le clic */
			    if (paneCompetence.getId().equals("Non Cliquée")) {
			        /* Modification de son ID à 'cliquée' */
			        paneCompetence.setId("Cliquée");
			        /* Parcours de la liste des compétences contenues
			         * dans la grille des compétences de la page Enseignements
			         */
			        for (Node competences : grilleCompetence.getChildren()) {
			            /* Si il s'agit de la Pane "Toutes" qui est cliquée alors : */
			            if (competences.getId().equals("Toutes Non Cliquée") || competences.getId().equals("Toutes Cliquée")) {
			            	/* Suppression de toutes les classes de style dont l'identifiant 
							 * est "paneCompetenceCliquee" à la pane "Toutes"*/
			                competences.getStyleClass().removeAll("paneCompetenceCliquee");
			                /* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
							 * à la pane "Toutes" */
			                competences.getStyleClass().add("paneCompetenceNonCliquee");
			                /* Suppression de toutes les classes de style dont l'identifiant 
							 * est "labelCompetenceCliquee" au label de la pane "Toutes" */
			                ((Label) ((Pane) competences).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
			                /* Ajout d'une classe de la classe de style "labelCompetenceNonCliquee" 
							 * au label de la pane "Toutes" */
			                ((Label) ((Pane) competences).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
			                /* Modification de l'ID de Toutes à "Toutes Non Cliquée" */
			                competences.setId("Toutes Non Cliquée");
			            } else {
			            	/* Suppression de toutes les classes de style dont l'identifiant 
							 * est "paneCompetenceCliquee" à la pane de cette competence */
			                competences.getStyleClass().removeAll("paneCompetenceCliquee");
			                /* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
							 * à la pane de cette competence */
			                competences.getStyleClass().add("paneCompetenceNonCliquee");
			                /* Suppression de toutes les classes de style dont l'identifiant 
							 * est "labelCompetenceCliquee" au label de la pane de cette competence */
			                ((Text) ((Pane) competences).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
			                /* Ajout d'une classe de la classe de style "labelCompetenceNonCliquee" 
							 * au label de la pane de cette competence */
			                ((Text) ((Pane) competences).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
			                /* Modification de l'ID de la competence à "Non Cliquée" */
			                competences.setId("Non Cliquée");
			            }
			        }
			        /* Suppression de toutes les classes de style dont l'identifiant 
					 * est "paneCompetenceNonCliquee" à la pane de la compétence sélectionnée */
			        paneCompetence.getStyleClass().removeAll("paneCompetenceNonCliquee");
			        /* Ajout d'une classe de la classe de style "paneCompetenceCliquee" 
					 * à la pane de la compétence sélectionnée */
			        paneCompetence.getStyleClass().add("paneCompetenceCliquee");
			        /* Suppression de toutes les classes de style dont l'identifiant 
					 * est "paneCompetenceCliquee" à la pane de la compétence sélectionnée */
			        texte.getStyleClass().removeAll("labelCompetenceNonCliquee");
			        /* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
					 * à la pane de la compétence sélectionnée */
			        texte.getStyleClass().add("labelCompetenceCliquee");
			    } else {
			        /* Modification de l'ID de la Pane de la compétence
			         * à "Non Cliquée" car si elle était sélectionnée avant le clic
			         * alors elle ne le sera plus après
			         */
			        paneCompetence.setId("Non Cliquée");
			        /* Suppression de toutes les classes de style dont l'identifiant 
					 * est "paneCompetenceCliquee" à la pane de la compétence sélectionnée */
			        paneCompetence.getStyleClass().removeAll("paneCompetenceCliquee");
			        /* Ajout d'une classe de la classe de style "paneCompetenceNonCliquee" 
					 * à la pane de la compétence sélectionnée */
			        paneCompetence.getStyleClass().add("paneCompetenceNonCliquee");
			        /* Suppression de toutes les classes de style dont l'identifiant 
					 * est "labelCompetenceCliquee" au texte de la pane de la compétence sélectionnée */
			        texte.getStyleClass().removeAll("labelCompetenceCliquee");
			        /* Ajout d'une classe de la classe de style "labelCompetenceNonCliquee" 
					 * au texte de la pane de la compétence sélectionnée */
			        texte.getStyleClass().add("labelCompetenceNonCliquee");
			    }
			    /* Appel à la méthode ajouterEnseignements :
			     * - paneCompetence correspond à la Pane de la compétence cliquée
			     * - competence.getListeEnseignements() correspond à la liste des enseignements de la compétence sélectionnée
			     * - scene correspond à la scène de la page Enseignements
			     */
			    ajouterEnseignements(paneCompetence, competence.getListeEnseignements(), scene);
			});

		}
	}

	/**
	 * Cette méthode permet d'afficher soit dans la grille
	 * de tri par ressource de la page Notes soit dans la grille
	 * principale des enseignements de la page Enseignements
	 * Si on affiche dans la grille de tri de la page Notes : 
	 * - On retrouve une option Toutes qui si cliquée affiche les 
	 *   controles de tout les enseignements
	 * - Chaque enseignement est cliquable et appellera
	 *   la méthode afficherNotes qui affichera les notes
	 * Si on affiche dans la grille principale de la page Enseignements : 
	 * - Chaque enseignement est cliquable et appellera
	 *   la méthode afficherControle
	 * @param triRessources un booléen permettant de définir si on souhaite
	 *        afficher dans la grille de tri par ressource ou non
	 * @param grilleEnseignement correspond à la grille principale 
	 *        des enseignements de la page Enseignements
	 * @param listeEnseignement correspond à la liste 
	 * 		  des Enseignements de l'application
	 * @param grilleNotes correspond à la grille
	 * 		  de tri par ressource de la page Notes
	 */
	private void afficherEnseignements(boolean triRessources, GridPane grilleEnseignement, List<Enseignement> listeEnseignement, GridPane grilleNotes) {
		int indiceEnseignement = 0;
		int maxCaractere = 25;
		Scene scene = grilleNotes.getScene();

		//Permet de mettre une taille à une ligne quand on l'ajoute
		grilleEnseignement.setVgap(10);
		grilleEnseignement.getChildren().clear();
		grilleEnseignement.getRowConstraints().clear();
		RowConstraints tailleEnseignement = new RowConstraints();
		tailleEnseignement.setPrefHeight(50);

		if (triRessources) {
			Label labelToutes = new Label("Toutes");
			labelToutes.setPrefSize(175,40);
			labelToutes.getStyleClass().add("labelCompetence");
			labelToutes.setPadding(new Insets(0,5,0,5));
			Pane ligneToutes = new Pane();
			ligneToutes.setPrefSize(185,40);
			ligneToutes.setId("Toutes Non Cliquée");
			ligneToutes.getStyleClass().add("paneCompetence");
			ligneToutes.getStyleClass().add("paneCompetenceNonCliquee");
			ligneToutes.getChildren().add(labelToutes);
			grilleEnseignement.add(ligneToutes,0,0);
			indiceEnseignement = 1;
			ligneToutes.setOnMouseClicked(event -> {
				if (ligneToutes.getId().equals("Toutes Non Cliquée")) {
					ligneToutes.setId("Toutes Cliquée");
					for (Node enseignement : grilleEnseignement.getChildren()) {
						if (enseignement.getId().equals("Toutes Non Cliquée") || enseignement.getId().equals("Toutes Cliquée")) {
							enseignement.getStyleClass().removeAll("paneCompetenceCliquee");
							enseignement.getStyleClass().add("paneCompetenceNonCliquee");
							((Label) ((Pane) enseignement).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
							((Label) ((Pane) enseignement).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
							enseignement.setId("Toutes Non Cliquée");
						} else {
							enseignement.getStyleClass().removeAll("paneCompetenceCliquee");
							enseignement.getStyleClass().add("paneCompetenceNonCliquee");
							((Text) ((Pane) enseignement).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
							((Text) ((Pane) enseignement).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
							enseignement.setId("Non Cliquée");
						}
					}
					ligneToutes.getStyleClass().removeAll("paneCompetenceNonCliquee");
					ligneToutes.getStyleClass().add("paneCompetenceCliquee");
					labelToutes.getStyleClass().removeAll("labelCompetenceNonCliquee");
					labelToutes.getStyleClass().add("labelCompetenceCliquee");
				}else {
					ligneToutes.setId("Toutes Non Cliquée");
					ligneToutes.getStyleClass().removeAll("paneCompetenceCliquee");
					ligneToutes.getStyleClass().add("paneCompetenceNonCliquee");
					labelToutes.getStyleClass().removeAll("labelCompetenceCliquee");
					labelToutes.getStyleClass().add("labelCompetenceNonCliquee");
				}
				afficherNotes(grilleNotes, null);
			});

			for (Enseignement enseignement : listeEnseignement) {
				/* Récupération du nom d'une compétence
				 * Identifiant + Intitulé
				 */
				String texteEnseignement = enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement();
				/* Création d'un objet de type Text 
				 * contenant le nom d'une compétence
				 */
				Text texte = new Text(texteEnseignement);
				texte.setWrappingWidth(maxCaractere * 7); // La largeur de l'espace pour un nombre de caractères
				/* Création d'une Pane qui contiendra le texte*/
				Pane paneEnseignement = new Pane();
				/* Ajout d'un id pour définir la compétence
				 * comme non cliquée.
				 */
				paneEnseignement.setId("Non Cliquée");
				/* Ajout du texte à la Pane*/
				paneEnseignement.getChildren().add(texte);
				/* Espacement du texte afn de le centrer*/
				texte.setTranslateY(15);
				texte.setTranslateX(5);
				texte.getStyleClass().add("labelCompetence");

				grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
				GridPane.setColumnSpan(paneEnseignement, 4);
				paneEnseignement.getStyleClass().add("paneCompetence");
				paneEnseignement.getStyleClass().add("paneCompetenceNonCliquee");
				grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
				paneEnseignement.setId("Non Cliqué");
				paneEnseignement.setOnMouseClicked(event -> {
					if (paneEnseignement.getId().equals("Non Cliquée")) {
						paneEnseignement.setId("Cliquée");
						for (Node enseignements : grilleEnseignement.getChildren()) {
							if (enseignements.getId().equals("Toutes Non Cliquée") || enseignements.getId().equals("Toutes Cliquée")) {
								enseignements.getStyleClass().removeAll("paneCompetenceCliquee");
								enseignements.getStyleClass().add("paneCompetenceNonCliquee");
								((Label) ((Pane) enseignements).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
								((Label) ((Pane) enseignements).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
								enseignements.setId("Toutes Non Cliquée");
							} else {
								enseignements.getStyleClass().removeAll("paneCompetenceCliquee");
								enseignements.getStyleClass().add("paneCompetenceNonCliquee");
								((Text) ((Pane) enseignements).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
								((Text) ((Pane) enseignements).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
								enseignements.setId("Non Cliquée");
							}
						}
						paneEnseignement.getStyleClass().removeAll("paneCompetenceNonCliquee");
						paneEnseignement.getStyleClass().add("paneCompetenceCliquee");
						texte.getStyleClass().removeAll("labelCompetenceNonCliquee");
						texte.getStyleClass().add("labelCompetenceCliquee");
					}else {
						paneEnseignement.setId("Non Cliquée");
						paneEnseignement.getStyleClass().removeAll("paneCompetenceCliquee");
						paneEnseignement.getStyleClass().add("paneCompetenceNonCliquee");
						texte.getStyleClass().removeAll("labelCompetenceCliquee");
						texte.getStyleClass().add("labelCompetenceNonCliquee");
					}
					afficherNotes(grilleNotes, enseignement);
				});
				indiceEnseignement++;
			}
		} else {
			for (Enseignement enseignement : listeEnseignement) {
				/* Récupération du nom d'une compétence
				 * Identifiant + Intitulé
				 */
				Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
				labelEnseignement.getStyleClass().add("labelEnseignement");
				labelEnseignement.setPadding(new Insets(0,5,0,5));
				/* Création d'une Pane qui contiendra le texte*/
				Pane paneEnseignement = new Pane();
				paneEnseignement.setPrefSize(640,40);
				paneEnseignement.setMinSize(640,40);
				paneEnseignement.setMaxSize(640,40);
				/* Ajout d'un id pour définir la compétence
				 * comme non cliquée.
				 */
				paneEnseignement.setId("Non Cliquée");
				paneEnseignement.getChildren().add(labelEnseignement);

				grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
				GridPane.setColumnSpan(paneEnseignement, 4);
				paneEnseignement.getStyleClass().add("paneEnseignement");
				grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
				paneEnseignement.setOnMouseClicked(event -> afficherControle(scene, paneEnseignement, enseignement));
				indiceEnseignement++;
			}
		}
	}

	/**
	 * Méthode permettant l'ajout de note dans le model
	 * Elle permet de tester si une Note appartient à une SAE, Portfolio ou Ressource
	 * Si c'est un Portfolio ou SAE, les parametres à saisir pour ajouter la note sont les mêmes
	 * SI c'est une ressource une autre méthode d'ajout du model est appelée 
	 * @param note est la note que l'utilisateur a saisi
	 * @param commentaire est optionnel, ne peut dépasser une certaine longueur
	 * @param denominateur
	 * @param ressource est la ressource appartenant au controle, peut être une SAE, Potrfolio ou ressource
	 * @param controle, n'est disponnible que si la note appartient à une ressource. Il ajoute une note au controle selectionné
	 * @throws Exception
	 */
	private void ajouterNote(String note, String commentaire, String denominateur, String ressource, String controle) throws Exception {
		/* Variable permettant de retrouver l'identifiant du controle plus tard */
		String identifiantControle = "";
		/* On enleve les derniers caracteres pour ne garder que les 5 premiers ce qui nous donne l'identifiant de la ressource */
		String identifiantRessource = ressource.substring(0, 5);
		/* On recherche dans le model la ressource lié à l'identifiant */
		Enseignement enseignement = gn.trouverEnseignement(identifiantRessource);
		/* Récupération de la grille à laquelle on veut ajouter les notes */
		GridPane grille = (GridPane)((ScrollPane)((Pane)(rootPane).getChildren().get(1)).getChildren().get(1)).getContent();
		/* Test permettant de vérifier si la ressource est une ressource et non une SAE ou Portfolio */
		if (enseignement instanceof Ressource) {
			/* Notre String devient une ressource si elle était bien une ressource */
			Ressource laRessource = (Ressource) enseignement;
			/* Récupération de tout les controles liés à la ressource sélectionnée */
			List<Controle> listeControles = laRessource.getControlesRessource();
			/* Parcours de cette liste de controle */
			for (Controle leControle : listeControles) {
				/* Test permettant de retrouver le controle choisi dans la liste des controles de la ressource */
				if (leControle.getIndentifiantControle().substring(6).equals(controle.substring(0,2))) {
					/* On définit notre identifiant du controle dans la String créé au début */
					identifiantControle = leControle.getIndentifiantControle();
				}
			}
			/* On essaye d'ajouter la note au model si c'est une ressource */
			try {
				/* Ajout de la note dans le model avec les parametres remplis */
				gn.ajouterNoteAControle(identifiantControle, Double.parseDouble(note), Integer.parseInt(denominateur), commentaire);
				/* Affichage des notes dans la grille qui permet d'afficher les notes */
				afficherNotes(grille, null);
			/* On lève les exceptions si besoin */
			} catch (NumberFormatException | NoteInvalideException e) {
				throw e;
			}
		/* Si la note n'est pas une ressource, elle est donc une SAE ou Portfolio */
		} else {
			/* Ajout de la note dans le model si c'est une SAE ou Portfolio */
			try {
				/* Méthode permettant d'ajouter dans le model la note si possible */
				gn.ajouterNoteASaePortfolio(enseignement, Double.parseDouble(note), Integer.parseInt(denominateur), commentaire);
				/* Affichage des notes dans la grille des notes */
				afficherNotes(grille, null);
			/* On lève les exceptions si besoin */
			} catch (NumberFormatException | NoteInvalideException e) {
				throw e;
			}
		}
	}

	/**
	 * Cette méthode permet d'ajouter et d'afficher les enseignements
	 * d'une compétence sélectionnée
	 * Chaque enseignement est cliquable et appellera
	 * la méthode afficherControle qui affichera les controles
	 * de cet enseignement
	 * @param competenceSelectionnee correspond à la pane 
	 * 		  de la competence selectionnée soit la competence
	 * 		  dont on veut afficher les enseignements
	 * @param listeEnseignements correspond à la liste
	 * 		  des enseignements de la competence selectionnée
	 * @param scene correspond à la scene de la grille
	 * 		  dans laquelle on souhaite afficher les enseignements
	 */
	private void ajouterEnseignements(Pane competenceSelectionnee, HashMap<Enseignement, Integer> listeEnseignements, Scene scene) {
		/* indice correspondant à l'index de la grille 
		 * auquel l'enseignement devra etre ajouté et affiché
		 */
		indiceEnseignement = 0;
		/* Permet de récupérer la grille des enseignements de la page Enseignements */
		GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
		/* Ajout d'un décalage de 10 pixels entre chaque ligne */
		grilleEnseignement.setVgap(10);
		/* Remise à zéro du contenu de la grille */
		grilleEnseignement.getChildren().clear();
		/* Retrait des contraintes des lignes de la grille*/
		grilleEnseignement.getRowConstraints().clear();
		/* Création d'une contrainte de ligne fixant
		 * la hauteur des lignes à 40 pixels
		 */
		RowConstraints tailleEnseignement = new RowConstraints();
		tailleEnseignement.setMinHeight(40);
		tailleEnseignement.setPrefHeight(40);
		tailleEnseignement.setMaxHeight(40);
		/* Parcours des enseignements contenus dans la HashMap */
		for (Enseignement enseignement : listeEnseignements.keySet()) {
			/* Ajout de la contrainte de ligne à la ligne actuelle */
			grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
			/* Création d'une pane pour l'enseignement */
			Pane paneEnseignement = new Pane();
			/* Gestion de la taille de la Pane */
			paneEnseignement.setPrefSize(640, 40);
			paneEnseignement.setMinSize(640, 40);
			paneEnseignement.setMaxSize(640, 40);
			/* Attribution d'un columnSpan de 4 à la pane de l'enseignement
			 * La pane occupe 4 colonnes dans la grille
			 */
			GridPane.setColumnSpan(paneEnseignement, 4);
			/* Ajout d'une feuille de style à la pane de l'enseignement */
			paneEnseignement.getStyleClass().add("paneEnseignement");
			/* Attribution d'un ID par défaut pour détecter son état*/
			paneEnseignement.setId("Non Cliquée");
			/* Création d'un label contenant l'identifiant
			 * et l'intitule de l'enseignement
			 */
			Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
			/* Ajout d'une feuille de style au label */
			labelEnseignement.getStyleClass().add("labelCompetence");
			/* Ajout d'un padding sur la gauche et la droite */
			labelEnseignement.setPadding(new Insets(0, 5, 0, 5));
			/* Ajout du label à la pane */
			paneEnseignement.getChildren().add(labelEnseignement);
			/* Ajout de la pane à la grille */
			grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
			/* Création d'un évènement déclenché lorsque la pane est cliquée */
			paneEnseignement.setOnMouseClicked(event -> {
				/* Affichage des controles de l'enseignement cliqué */
				afficherControle(scene, paneEnseignement, enseignement);
			});
			/* Incrément de l'indice */
			indiceEnseignement++;
		}
	}

	/**
	 * Méthode permettant d'ajouter toutes les ressources à la ComboBox
	 * On y retrouve des ressources, SAE ou Portoflio
	 */
	private void ajoutRessourcesCombo() {
		/* Supression de tous les éléments déja présents dans la comboBox */
		ressourcesCombo.getItems().clear();
		/* On récupère la liste des enseignements du semestre */
		List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
		for (Enseignement enseignement : listeEnseignement) {
			if (gn.estUneSae(enseignement)) {
				Sae sae = (Sae) enseignement;
				if(sae.getNoteSae() == null) {
					ressourcesCombo.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
				}
			} else if (gn.estUnPortfolio(enseignement)) {
				Portfolio portfolio = (Portfolio) enseignement;
				if(portfolio.getNotePortfolio() == null) {
					ressourcesCombo.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
				}
			} else {
				ressourcesCombo.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
			}
		}
	}

	/**
	 * 
	 * @param combo
	 */
	private void choixComboRessources(ComboBox<String> combo) {
		combo.valueProperty().addListener((observable, oldValue, newValue) -> {
			String choix = combo.getValue().substring(0,5);
			if (!gn.estUneRessource(gn.trouverEnseignement(choix))) {
				controleCombo.setVisible(false);
				etoile.setVisible(false);
				controle.setVisible(false);
			} else {
				controleCombo.setVisible(true);
				etoile.setVisible(true);
				controle.setVisible(true);
				ajoutComboControle(controleCombo, choix);
			}
		});
	}

	/**
	 * 
	 * @param combo
	 * @param ressources
	 */
	private void ajoutComboControle(ComboBox<String> combo, String ressources) {
		combo.getItems().clear();
		Enseignement enseignement = gn.trouverEnseignement(ressources);
		Ressource ressource = (Ressource) enseignement;
		List<Controle> listeControles = ressource.getControlesRessource();
		for (Controle controle: listeControles) {
			if (controle.getNoteControle() == null) {
				String affichageComboControle = controle.getIndentifiantControle().substring(controle.getIndentifiantControle().length() - 2);
				affichageComboControle += " " + controle.getTypeControle() + " " + controle.getDateControle();
				combo.getItems().add(affichageComboControle);
			}
		}
	}
	/**
	 * Selectionne un dossier où recevoir le fichier exporté par un autre utilisateur
	 * Demande également à l'utilisateur le nom du fichier souhaité
	 */
	private void selectionnerDossierReception() {
		final String NOM_FICHIER_DEFAUT = "fichierReçu.csv";
		/* Déclaration d'un objet DirectoryChooser pour ouvrir un explorateur */
		DirectoryChooser explorateurDossier = new DirectoryChooser();
		explorateurDossier.setTitle("Selectionnez le dossier de reception");
		Stage stageActuel = (Stage) rootPane.getScene().getWindow();
		/* Récupération du dossier et de son chemin */
		File dossierSelectionne = explorateurDossier.showDialog(stageActuel);
		if (dossierSelectionne != null) {
			String cheminDossier = dossierSelectionne.getAbsolutePath();
			/* Choix du nom a donner au fichier reçu */
			TextInputDialog choixNom = new TextInputDialog("fichierRecu");
			choixNom.setTitle("Nom fichier");
			choixNom.setHeaderText("Choisissez le nom du fichier");
			choixNom.setContentText("Nom du fichier :");
			choixNom.showAndWait();
			String nomFichier = choixNom.getResult();
			if(nomFichier == null ||nomFichier.equals("")) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Nom du fichier");
				alert.setHeaderText("Le nom saisi est incorrect");
				alert.setContentText("Nom par défaut choisi (\""+ NOM_FICHIER_DEFAUT +"\")");
				alert.showAndWait();
				nomFichier = NOM_FICHIER_DEFAUT;
			} else {
				nomFichier += ".csv";
			}
			cheminDossierReception.setText("Dossier : " + cheminDossier + "\\" + nomFichier);
		}
	}
	/**
	 * Met en attente l'application pour recevoir un fichier d'un autre utilisateur
	 */
	private void recevoirFichier() {
		int port;
		String cheminReceptionFichier = cheminDossierReception.getText();
		Alert alertReception = new Alert(AlertType.INFORMATION);
		if (!saisiePortServeur.getText().equals("")) {
			port = Integer.parseInt(saisiePortServeur.getText());
		} else {
			port = 0;
		}
		if (!cheminReceptionFichier.equals("")) {
			cheminReceptionFichier = cheminReceptionFichier.substring(10);
		}
		try {
			alertReception.setTitle("Réception de fichier");
			alertReception.setHeaderText("Attente de connexion ...");
			alertReception.show();
			gn.recevoirFichier(port,cheminReceptionFichier);
			alertReception.close();
			/* Affichage d'un popup pour informer du succès de la reception */
			Alert alertRecu = new Alert(AlertType.INFORMATION);
			alertRecu.setTitle("Fichier reçu");
			alertRecu.setHeaderText("Fichier reçu avec succés");
			alertRecu.setContentText("Chemin : " + cheminReceptionFichier);
			alertRecu.showAndWait();
		} catch (PortReseauException | IOException | cheminFichierException e) {
			alertReception.close();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Reception impossible");
			alert.setHeaderText(e.getMessage());
			alert.showAndWait();
		}
	}
	/**
	 * Exporte un fichier a un autre utilisateur
	 */
	private void envoyerFichier() {
		int port;
		String ipServeur = saisieIpServeur.getText();
		String cheminFichier = cheminFichierExport.getText();
		if (!saisiePortClient.getText().equals("")) {
			port = Integer.parseInt(saisiePortClient.getText());
		} else {
			port = 0;
		}
		if (!cheminFichier.equals("")) {
			cheminFichier = cheminFichier.substring(10); // On enlève le "Fichier : "
		}
		try {
			gn.envoyerFichier(ipServeur, port, cheminFichier);
			/* Affichage d'un popup pour informer du succès de l'envoi */
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Fichier envoyé");
			alert.setHeaderText("Le fichier a été envoyé avec succés");
			alert.showAndWait();
		} catch (IpException | PortReseauException | cheminFichierException |
				IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Envoi impossible");
			alert.setHeaderText(e.getMessage());
			alert.showAndWait();
		} catch (NullPointerException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Envoi impossible");
			alert.setHeaderText("Le fichier ne peut pas être envoyé");
			alert.setContentText("Le fichier ne respecte pas l'alphabet utilisable (Voir aide)");
			alert.showAndWait();
		}
	}
	/**
	 * Affiche l'adresse IP de la machine sur le réseau local
	 * pour que l'utilisateur puisse la transmettre a un quelqu'un qui veut lui
	 * envoyer un fichier
	 * @param le label dans lequel on affiche l'IP
	 */
	private void afficherIP() {
		String adresseIPLocale;
		try {
			InetAddress inetadr = InetAddress.getLocalHost();
			adresseIPLocale = (String) inetadr.getHostAddress();
			ipUtilisateur.setText(adresseIPLocale);
		} catch (UnknownHostException e) {
			ipUtilisateur.setText("IP inconnue");
		}
	}
	/**
	 * Cette méthode permet d'afficher la liste des controles 
	 * d'un enseignement sélectionné
	 * - S'il s'agit d'une Ressource, affiche la liste des controles
	 * - S'il s'agit d'une Sae, affiche un message expliquant
	 * 	 qu'une Sae ne possède pas de controle
	 * - S'il s'agit d'un Portfolio, affiche un message expliqant
	 * 	 qu'un Portfolio ne possède pas de controle 
	 * @param scene correspond à la scene de la grille
	 * 		  dans laquelle on souhaite afficher les enseignements
	 * @param enseignementSelectionne correspond à la pane 
	 * 		  de l'enseignement selectionné soit l'enseignement
	 * 		  dont on veut afficher les controles
	 * @param enseignement correspond à l'enseignement sélectionné
	 */
	private void afficherControle(Scene scene, Pane enseignementSelectionne, Enseignement enseignement) {
		/* Récupération de la grille principale de la page Enseignements
		 * contenant les enseignements et leur(s) controle(s)
		 */
		GridPane grilleEnseignement = ((GridPane)((ScrollPane)((Pane)((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
		/* Si l'enseignement sélectionné est une Ressource */
		if (gn.estUneRessource(enseignement)) {
			/* Création d'un objet Ressource à partir de l'enseignement */
			Ressource ressource = (Ressource) enseignement;
			/* Récupération de l'indice dans la grille de l'enseignement */
			Integer rowIndex = GridPane.getRowIndex(enseignementSelectionne);
			/* Si l'enseignement n'était pas sélectionné avant d'être cliqué */
			if ("Non Cliquée".equals(enseignementSelectionne.getId())) {
				/* Changement de son ID */
				enseignementSelectionne.setId("Cliquée");
				/* Parcours des objets de la grille */
				for (Node node : grilleEnseignement.getChildren()) {
					/* Récupération de l'indice de l'objet actuel */
					Integer row = GridPane.getRowIndex(node);
					/* Si l'indice de l'objet courant est superieur
					 * à l'indice de l'enseignement sélectionné
					 */
					if (row != null && row > rowIndex) {
						/* Décalage de l'objet dans la grille */
						GridPane.setRowIndex(node, row + ressource.getControlesRessource().size() + 1);
					}
				}
				/* Inititialisation d'un indice pour l'affichage
				 * des controles
				 */
				int indiceControle = 1;
				/* Création de la grille contenant le nom
				 * des colonnes
				 */
				Pane paneInformations = new Pane();
				/* Attribution d'un columnSpan à la pane d'informations
				 * Car celle-ci occupe 4 colonnes
				 */
				GridPane.setColumnSpan(paneInformations, 4);
				/* Création des labels contenant le nom des colonnes */
				Label labelTypeInfo = new Label("Type");
				Label labelPoidsInfo = new Label("Poids");
				Label labelDateInfo = new Label("Date");
				/* Ajout des labels à la pane
				paneInformations.getChildren().add(labelTypeInfo);
				paneInformations.getChildren().add(labelPoidsInfo);
				paneInformations.getChildren().add(labelDateInfo);
				/* Ajout d'une feuille de style à la pane d'informations */
				paneInformations.getStyleClass().add("paneInformations");
				/* Ajustement de la taille de la pane d'informations */
				paneInformations.setPrefSize(600, 40);
				paneInformations.setMinSize(600, 40);
				paneInformations.setMaxSize(600, 40);
				/* Decalage de la pane de 40 pixels sur la droite */
				paneInformations.setTranslateX(40);
				/* Placement des labels sur la pane */
				labelTypeInfo.setLayoutX(0);
				labelPoidsInfo.setLayoutX(130);
				labelDateInfo.setLayoutX(260);
				/* Ajout d'une feuille de style aux labels */
				labelTypeInfo.getStyleClass().add("labelInfo");
				labelPoidsInfo.getStyleClass().add("labelInfo");
				labelDateInfo.getStyleClass().add("labelInfo");
				/* Gestion de la taille des labels */
				labelTypeInfo.setPrefSize(130,40);
				labelPoidsInfo.setPrefSize(130,40);
				labelDateInfo.setPrefSize(130,40);
				/* Alignement au centre de ces labels */
				labelTypeInfo.setAlignment(Pos.CENTER);
				labelPoidsInfo.setAlignment(Pos.CENTER);
				labelDateInfo.setAlignment(Pos.CENTER);
				GridPane.setHalignment(labelTypeInfo, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelPoidsInfo, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelDateInfo, javafx.geometry.HPos.CENTER);
				/* Ajout de la pane des Informations à la grille */
				grilleEnseignement.add(paneInformations, 0, rowIndex + indiceControle);
				/* Incrément de l'indice */
				indiceControle++;
				/* Parcours de la liste des controles de la ressource */
				for (Controle controle : ressource.getControlesRessource()) {
					/* Création d'une pane pour le controle */
					Pane paneControle = new Pane();
					/* Ajout d'un columnSpan de 4 à cette pane
					 * Car celle-ci occupe 4 colonnes 
					 */
					GridPane.setColumnSpan(paneControle, 4);
					/* Création des labels contenant les informations du controle */
					Label labelType = new Label(controle.getTypeControle());
					Label labelPoids = new Label("" + controle.getPoidsControle());
					Label labelDate = new Label(controle.getDateControle());
					/* Ajout des labels à la pane du controle */
					paneControle.getChildren().add(labelType);
					paneControle.getChildren().add(labelPoids);
					paneControle.getChildren().add(labelDate);
					/* Ajout d'une feuille de style à la pane du controle */
					paneControle.getStyleClass().add("paneControle");
					/* Ajustement de la taille de la pane */
					paneControle.setPrefSize(600, 40);
					paneControle.setMinSize(600, 40);
					paneControle.setMaxSize(600, 40);
					/* Decalage de la pane de 40 pixels vers la droite */
					paneControle.setTranslateX(40);
					/* Position des labels dans la pane du controle */
					labelType.setLayoutX(0);
					labelPoids.setLayoutX(130);
					labelDate.setLayoutX(260);
					/* Ajout d'une feuille de style aux labels */
					labelType.getStyleClass().add("labelControle");
					labelPoids.getStyleClass().add("labelControle");
					labelDate.getStyleClass().add("labelControle");
					/* Definition de la taille des labels */
					labelType.setPrefSize(130,40);
					labelPoids.setPrefSize(130,40);
					labelDate.setPrefSize(130,40);
					//Récupération d'image pour nos boutons
					Image imageModifier = new Image(getClass().getResourceAsStream("/application/controlleur/modifier.png"));
					Image imageSupprimer = new Image(getClass().getResourceAsStream("/application/controlleur/supprimer.png"));
					// On définit une ImageView afin de pouvoir mettre l'image dans le bouton
					ImageView imageViewModifier = new ImageView(imageModifier);
					ImageView imageViewSupprimer = new ImageView(imageSupprimer);
					//Création des boutons avec leurs images
					Button modifier = new Button();
					modifier.setGraphic(imageViewModifier);
					afficherMessageSurvol(modifier, MESSAGE_MODIFIER_NOTE);
					Button supprimer = new Button();
					supprimer.setGraphic(imageViewSupprimer);
					afficherMessageSurvol(supprimer, MESSAGE_SUPPRIMER_NOTE);
					// Taille des boutons
					modifier.setMaxSize(30, 30);
					supprimer.setMaxSize(30, 30);
					// Action des boutons
					supprimer.setOnAction(event -> 
					/* Appel à la méthode sceneSupprimerControle qui affiche
					 *  une popUp pour supprimer ou non le controle
					 */
					sceneSupprimerControle(grilleEnseignement, supprimer, enseignement, controle));
					modifier.setOnAction(event ->
					/* Appel à la méthode sceneModifierControle qui affiche
					 *  une popUp pour modifier ou non le controle
					 */
					sceneModifierControle(grilleEnseignement,modifier,enseignement, controle.getTypeControle(), controle.getPoidsControle(), controle.getDateControle(), controle));
					modifier.getStyleClass().add("boutonSupprimerModifier");
					supprimer.getStyleClass().add("boutonSupprimerModifier");
					// Permet de changer la couleur des boutons au survol
					supprimer.setOnMouseEntered(event -> {
						imageViewSupprimer.setBlendMode(BlendMode.SRC_OVER);
						imageViewSupprimer.setOpacity(0.4); // Choisir le niveau d'opacité pour obtenir la couleur désirée
						supprimer.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%; ");
						imageViewSupprimer.setStyle("-fx-effect: innershadow(gaussian, #ffffff, 10, 1.0, 0, 0);");
					});
					supprimer.setOnMouseExited(event -> {
						imageViewSupprimer.setBlendMode(null);
						imageViewSupprimer.setOpacity(1.0);
						imageViewSupprimer.setStyle(""); // Retirer le style ajouté précédemment
						supprimer.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
					});
					modifier.setOnMouseEntered(event -> {
						imageViewModifier.setBlendMode(BlendMode.SRC_OVER);
						imageViewModifier.setOpacity(0.4); // Choisir le niveau d'opacité pour obtenir la couleur désirée
						imageViewModifier.setStyle("-fx-effect: innershadow(gaussian, #ffffff, 10, 1.0, 0, 0);");
						modifier.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%;");
					});
					modifier.setOnMouseExited(event -> {
						imageViewModifier.setBlendMode(null);
						imageViewModifier.setOpacity(1.0);
						imageViewModifier.setStyle(""); // Retirer le style ajouté précédemment
						modifier.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
					});
					/* Positionnement au centre des labels et boutons */
					labelType.setAlignment(Pos.CENTER);
					labelPoids.setAlignment(Pos.CENTER);
					labelDate.setAlignment(Pos.CENTER);
					GridPane.setHalignment(labelType, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelPoids, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);
					/* Ajout de la pane et des boutons à la grille */
					grilleEnseignement.add(paneControle, 0, rowIndex + indiceControle);
					grilleEnseignement.add(modifier, 4, rowIndex + indiceControle);
					grilleEnseignement.add(supprimer, 5, rowIndex + indiceControle);
					/* Increment de l'indice */
					indiceControle++;
				}
			/* Si l'enseignement était sélectionné avant d'être cliqué */
			} else {
				/* Modification de l'ID de l'enseignement */
				enseignementSelectionne.setId("Non Cliquée");
				/* Parcours des controles affichés dans la grille */
				for (int indiceControle = 1; indiceControle <= ressource.getControlesRessource().size() + 1; indiceControle++) {
					int indice = indiceControle;
					/* Suppresion de l'affichage du controle */
					grilleEnseignement.getChildren().removeIf(node ->
					GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex + indice));
					/* Décrément de l'indice */
					indiceEnseignement--;
				}
				/* Parcours des éléments de la grille */
				for (Node node : grilleEnseignement.getChildren()) {
					/* Récupération de l'indice de l'element actuel */
					Integer row = GridPane.getRowIndex(node);
					/* Si l'indice n'est pas null et superieur 
					 * à celui de l'enseignement actuel */
					if (row != null && row > rowIndex + 1) {
						/* Déplacement de l'element dans la grille */
						GridPane.setRowIndex(node, row - ressource.getControlesRessource().size() - 1);
					}
				}
			}
			
		} else{
			/* Récupération de l'indice de l'enseignement sélectionné */
			Integer rowIndex = GridPane.getRowIndex(enseignementSelectionne);
			/* Si l'enseignement n'était pas sélectionné avant 
			 * d'etre cliqué 
			 */
			if ("Non Cliquée".equals(enseignementSelectionne.getId())) {
				/* Modification de son ID */
				enseignementSelectionne.setId("Cliquée");
				/* Parcours des éléments de la grille */
				for (Node node : grilleEnseignement.getChildren()) {
					/* Récupération de l'indice de l'élément actuel */
					Integer row = GridPane.getRowIndex(node);
					/* Si celui-ci est superieur à celui
					 * de l'enseignement selectionne
					 */
					if (row != null && row > rowIndex) {
						/* Décalage de l'élément dans la grille */
						GridPane.setRowIndex(node, row + 1);
					}
				}
				/* Déclaration d'un indice pour les controles */
				int indiceControle = 1;
				/* Création d'une pane informative */
				Pane paneInformations = new Pane();
				/* Attribution d'un columnSpan de 4 
				 * car celle-ci occupe 4 colonnes
				 */
				GridPane.setColumnSpan(paneInformations, 4);
				/* Création d'un label sans contenu */
				Label labelInfo = new Label("");
				/* Si l'enseignement séléctionné est une Sae */
				if (gn.estUneSae(enseignement)){
					/* Modification du texte du label */
					labelInfo.setText("Les SAE ne contiennent aucun contrôle mais seulement une note");
				/* Si l'enseignement séléctionné est un Portfolio */
				} else if (gn.estUnPortfolio(enseignement)) {
					/* Modification du texte du label */
					labelInfo.setText("Les Portfolio ne contiennent aucun contrôle mais seulement une note");
				}
				/* Ajout du label à la pane Informative */
				paneInformations.getChildren().add(labelInfo);
				/* Ajout d'une page de Style à la pane informative */
				paneInformations.getStyleClass().add("paneInformations");
				/* Ajustement de la taille de la pane informative */
				paneInformations.setPrefSize(600, 40);
				paneInformations.setMinSize(600, 40);
				paneInformations.setMaxSize(600, 40);
				/* Ajustement de la taille du label */
				labelInfo.setPrefSize(600, 40);
				labelInfo.setMinSize(600, 40);
				labelInfo.setMaxSize(600, 40);
				/* Décalage de la pane de 40 pixels sur la droite */
				paneInformations.setTranslateX(40);
				/* Ajout d'une feuille de style au label */
				labelInfo.getStyleClass().add("labelInfoSaePortfolio");
				/* Alignement du label */
				labelInfo.setAlignment(Pos.CENTER);
				/* Ajout de la pane Informative à la grille */
				grilleEnseignement.add(paneInformations, 0, rowIndex + indiceControle);
			/* Si l'enseignement était sélectionné avant d'être cliqué */
			} else {
				/* Modification de l'ID de la pane */
				enseignementSelectionne.setId("Non Cliquée");
				/* Suppression de l'affichage des controles */
				grilleEnseignement.getChildren().removeIf(node ->
				GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex + 1));
				/* Décrément de l'indice */
				indiceEnseignement--;
				/* Parcours des éléments de la grille */
				for (Node node : grilleEnseignement.getChildren()) {
					/* Récupération de l'indice de l'élément */
					Integer row = GridPane.getRowIndex(node);
					/* Décalage des éléments dont l'indice est superieur
					 * à celui de l'enseignement séléctionné
					 */
					if (row != null && row > rowIndex + 1) {
						GridPane.setRowIndex(node, row - 1);
					}
				}
			}
		}
	}

	/**
	 * Affiche les notes saisie par l'utilisateur 
	 * selon l'enseignement séléctionné
	 * @param grille correspond à la grille dans laquelle
	 * 		  les notes devront être ajoutées
	 * @param enseignement correspond à l'enseignement séléctionné
	 * 		  soit par quel enseignement l'on tri
	 */
	private void afficherNotes(GridPane grille, Enseignement enseignement) {
		/* Si nous filtrons par aucun enseignement */
		if (enseignement == null) {
			/* Création d'un indice pour la grille */
			int indiceGrille = 0;
			ArrayList<Object> notes = gn.getNotes(); // Contient tous les contrôles, SAE, Portfolio ayant une note
			/* Création d'une liste vide contenant les notes filtrées */
			ArrayList<Object[]> notesFiltrées = new ArrayList<Object[]>();
			/* Parcours de la liste des notes de l'application */
			for (Object note : notes) {
				/* Création d'un tableau contenant
				 * les informations d'une note
				 */
				Object[] tabNote = new String[6];
				/* Si la note est un controle */
				if (note instanceof Controle) {
					/* Récupération des notes du controle */
					tabNote = noteControle(note);
				} else if (note instanceof Sae){
					/* Récupération de la note de la Sae */
					tabNote = noteSae(note);
				} else {
					/* Récupération de la note du portfolio */
					tabNote = notePortfolio(note);
				}
				/* Ajout des notes au tableau des notes filtrées */
				notesFiltrées.add(tabNote);
			}
			/* Appel à la méthode afficherNotesSelectionne qui affiche
			 * la liste des notes
			 * - notesFiltrées correspond à la liste des notes filtrées
			 * - indiceGrille correspond à l'indice à laquelel
			 *   le prochain élément doit être affiché
			 * - grille correspond à la grille où les notes doivent
			 *   être ajoutées et affichées
			 */
			afficherNotesSelectionne(notesFiltrées,indiceGrille,grille);
		}else {
			/* Création d'une liste vide contenant les notes filtrées */
			ArrayList<Object[]> notesFiltrées = new ArrayList<Object[]>();
			/* Création d'un indice pour la grille */
			int indiceGrille = 0;
			/* Si l'enseignement de tri est une ressource */
			if (gn.estUneRessource(enseignement)) {
				/* Récupération de l'identifiant de l'enseignement */
				String enseignementID = enseignement.getIdentifiantEnseignement();
				/* Création d'un objet Ressource à partir de l'enseignement */
				Ressource ressource = (Ressource) gn.trouverEnseignement(enseignementID);
				/* Parcours de la liste des controles de la ressource */
				for (Controle controle : ressource.getControlesRessource()) {
					/* Si le controle possède une note*/
					if (controle.aUneNote()) {
						/* Récupération des informations de la note*/
						Object[] tabNote = noteControle(controle);
						/* Ajout de la note à la liste des notes filtrées */
						notesFiltrées.add(tabNote);
					}
				}
			/* Si l'enseignement est une Sae */
			} else if (gn.estUneSae(enseignement)) {
				/* Récupération de l'identifiant de l'enseignement */
				String enseignementID = enseignement.getIdentifiantEnseignement();
				/* Création d'un objet Sae à partir de l'enseignement */
				Sae sae = (Sae) gn.trouverEnseignement(enseignementID);
				/* Si la sae possède une note*/
				if (sae.aUneNote()) {
					/* Récupération des informations de la note*/
					Object[] tabNote = noteSae(sae);
					/* Ajout de la note à la liste des notes filtrées */
					notesFiltrées.add(tabNote);
				}
			} else if (gn.estUnPortfolio(enseignement)) {
				/* Récupération de l'identifiant de l'enseignement */
				String enseignementID = enseignement.getIdentifiantEnseignement();
				/* Création d'un objet Portfolio à partir de l'enseignement */
				Portfolio portfolio = (Portfolio) gn.trouverEnseignement(enseignementID);
				/* Si le portfolio possède une note*/
				if (portfolio.aUneNote()) {
					/* Récupération des informations de la note*/
					Object[] tabNote = notePortfolio(portfolio);
					/* Ajout de la note à la liste des notes filtrées */
					notesFiltrées.add(tabNote);
				}
			}
			/* Appel à la méthode afficherNotesSelectionne qui affiche
			 * la liste des notes de l'enseignement séléctionné
			 * - notesFiltrées correspond à la liste des notes filtrées
			 * - indiceGrille correspond à l'indice à laquelel
			 *   le prochain élément doit être affiché
			 * - grille correspond à la grille où les notes doivent
			 *   être ajoutées et affichées
			 */
			afficherNotesSelectionne(notesFiltrées,indiceGrille,grille);
		}
	}
	
	/**
	 * Affiche les notes saisie par l'utilisateur 
	 * selon l'enseignement séléctionné
	 * @param notes correspond à la liste des notes de l'enseignement 
	 * 		  sélectionné ou toutes les notes si aucun ne l'est
	 * @param indiceGrille correspond à l'indice de la grille
	 * 		  auquel le prochain élément doit être ajouté
	 * @param grille correspond à la grille dans laquelle
	 * 		  les notes devront être ajoutées et affichées
	 */
	private void afficherNotesSelectionne(ArrayList<Object[]> notes, int indiceGrille, GridPane grille) {
		/* Création d'un indice sous forme de tableau
		 * pour éviter les bug d'enclosing (déclaration)
		 */
		int[] indice = new int[1];
		indice[0] = indiceGrille;
		GridPane mainGridPane = grille;
		/* Création d'une contrainte de ligne 
		 * pour limiter leur hauteur */
		RowConstraints taille = new RowConstraints();
		/* Remise à zéro du contenu de la gridPane */
		mainGridPane.getChildren().clear();
		/* Remise à zéro des contraintes de ligne de la grille */
		mainGridPane.getRowConstraints().clear();
		/* taille de la contrainte fixée à 50 pixels */
		taille.setPrefHeight(50);
		/* Si la liste des notes à afficher est vide */
		if (notes.isEmpty()) {
			Pane panePasDeNote = new Pane();
			panePasDeNote.setPrefSize(724, 375);

			Label labelPasDeNote = new Label("Aucune note saisie");
			labelPasDeNote.getStyleClass().add("labelPasDeNote");

			panePasDeNote.getChildren().add(labelPasDeNote);
			labelPasDeNote.setPrefSize(724, 375);

			// Centrer le labelPasDeNote dans panePasDeNote
			labelPasDeNote.setAlignment(Pos.CENTER);
			// Centrer la panePasDeNote au milieu de la GridPane
			GridPane.setColumnSpan(panePasDeNote, 6);
			GridPane.setHalignment(panePasDeNote, HPos.CENTER); // Centre horizontalement
			GridPane.setValignment(panePasDeNote, VPos.CENTER); // Centre verticalement


			mainGridPane.add(panePasDeNote, 0, indiceGrille);
		} else {
			for (Object[] tabNote : notes) {
				/* Création d'une pane contenant la note actuelle */
				Pane ligneNote = new Pane();
				/* Création des label avec récupération des infos
				 * sur la note
				 */
				Label labelNote = new Label((String) tabNote[0]);
				Label labelType = new Label((String) tabNote[2]);
				Label labelPoids = new Label((String) tabNote[1]);
				Label labelRessources = new Label((String) tabNote[3]);
				String date = (String) tabNote[4];
				String commentaire = (String) tabNote[5];
				Object identifiantControle = tabNote[7];
				/* Ajout d'une feuille de style au label contenant la note */
				labelNote.getStyleClass().add("labelNote");
				//Récupération d'image pour nos boutons
				Image imageModifier = new Image(getClass().getResourceAsStream("/application/controlleur/modifier.png"));
				Image imageSupprimer = new Image(getClass().getResourceAsStream("/application/controlleur/supprimer.png"));
				// On définit une ImageView afin de pouvoir mettre l'image dans le bouton
				ImageView imageViewModifier = new ImageView(imageModifier);
				ImageView imageViewSupprimer = new ImageView(imageSupprimer);
				//Création des boutons avec leurs images
				Button modifier = new Button();
				modifier.setGraphic(imageViewModifier);
				/* Appel à la méthode afficherMessageSurvol
				 * - modifier correspond au bouton modifier de la note
				 * - MESSAGE_MODIFIER_NOTE est la constante contenant
				 * 	 le message à afficher lorsque l'on passe notre souris
				 *   sur le bouton modifier
				 */
				afficherMessageSurvol(modifier, MESSAGE_MODIFIER_NOTE);
				Button supprimer = new Button();
				supprimer.setGraphic(imageViewSupprimer);
				/* Appel à la méthode afficherMessageSurvol
				 * - modifier correspond au bouton supprimer de la note
				 * - MESSAGE_SUPPRIMER_NOTE est la constante contenant
				 * 	 le message à afficher lorsque l'on passe notre souris
				 *   sur le bouton supprimer
				 */
				afficherMessageSurvol(supprimer, MESSAGE_SUPPRIMER_NOTE);
				// Taille des boutons
				modifier.setMaxSize(30, 30);
				supprimer.setMaxSize(30, 30);
				/* Récupération de la note et du dénominateur
				 * soit retrait du '/'
				 */
				String[] parties = ((String) tabNote[0]).split("/");
				/* Tableau contenant les parametres de la note */
				String[] noteParams = { parties[0].trim(), parties[1].trim(), (String) tabNote[5], (String) tabNote[3], (String) tabNote[2] };
				// Action des boutons
				supprimer.setOnAction(event -> sceneSupprimerNote(mainGridPane, supprimer, tabNote[6]));
				modifier.setOnAction(event -> sceneModifierNote(mainGridPane,modifier,noteParams, tabNote[6], identifiantControle));
				// Alignement des Label au centre de leur emplacement
				GridPane.setHalignment(labelNote, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);
				// Gestion du style de nos Labels et boutons
				labelNote.getStyleClass().add("labelNote");
				labelType.getStyleClass().add("labelType");
				labelPoids.getStyleClass().add("labelPoids");
				labelRessources.getStyleClass().add("labelRessources");
				/* Ajustement de la taille de la pane de la note */
				ligneNote.setPrefSize(640, 40);
				/* Modification de l'ID de la pane */
				ligneNote.setId("Non Cliquée");
				/* Attribution d'un columnSpan de 4 à la pane
				 * de la note car celle-ci occupe 4 colonnes
				 */
				GridPane.setColumnSpan(ligneNote, 4);
				/* Gestion de la taille des labels */
				labelNote.setMaxSize(130, 40);
				labelType.setMaxSize(130, 40);
				labelPoids.setMaxSize(130, 40);
				labelRessources.setMaxSize(250, 40);
				/* Positionnement et alignement des labels */
				labelNote.setAlignment(Pos.CENTER);
				labelType.setAlignment(Pos.CENTER);
				labelPoids.setAlignment(Pos.CENTER);
				labelRessources.setAlignment(Pos.CENTER);
				/* Modification de la taille des boutons */
				modifier.setMaxSize(30, 30);
				supprimer.setMaxSize(30, 30);
				/* Ajout de feuilles de styles aux boutons */
				modifier.getStyleClass().add("boutonSupprimerModifier");
				supprimer.getStyleClass().add("boutonSupprimerModifier");
				// Permet de changer la couleur des boutons au survol
				supprimer.setOnMouseEntered(event -> {
					imageViewSupprimer.setBlendMode(BlendMode.SRC_OVER);
					imageViewSupprimer.setOpacity(0.4); // Choisir le niveau d'opacité pour obtenir la couleur désirée
					supprimer.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%; ");
					imageViewSupprimer.setStyle("-fx-effect: innershadow(gaussian, #ffffff, 10, 1.0, 0, 0);");
				});
				supprimer.setOnMouseExited(event -> {
					imageViewSupprimer.setBlendMode(null);
					imageViewSupprimer.setOpacity(1.0);
					imageViewSupprimer.setStyle(""); // Retirer le style ajouté précédemment
					supprimer.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
				});
				modifier.setOnMouseEntered(event -> {
					imageViewModifier.setBlendMode(BlendMode.SRC_OVER);
					imageViewModifier.setOpacity(0.4); // Choisir le niveau d'opacité pour obtenir la couleur désirée
					imageViewModifier.setStyle("-fx-effect: innershadow(gaussian, #ffffff, 10, 1.0, 0, 0);");
					modifier.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0; -fx-background-radius: 100%;");
				});
				modifier.setOnMouseExited(event -> {
					imageViewModifier.setBlendMode(null);
					imageViewModifier.setOpacity(1.0);
					imageViewModifier.setStyle(""); // Retirer le style ajouté précédemment
					modifier.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85; -fx-background-radius: 100%;");
				});
				/* Création d'un évènement déclenché lorsque la ligne 
				 * contenant les informations de la note est cliquée
				 */
				ligneNote.setOnMouseClicked(event -> {
					/* Appel à la méthode afficher Commentaire qui 
					 * affiche un commentaire sous la note
					 * - mainGridPane correspond à la grille des notes
					 * - ligneNote correspond à la pane de la note cliquée
					 * - commentaire correspond au commentaire de la note
					 * - date correspond à la date de la note
					 * - indice[0] correspond à l'indice de la note dans la grille
					 */
					afficherCommentaire(mainGridPane, ligneNote, commentaire, date, indice[0]);
				});
				/* Ajout de la contrainte de ligne précédement
				 * crée à la ligne actuelle
				 */
				mainGridPane.getRowConstraints().add(taille);
				/* Ajout des éléments de la note à la grille */
				mainGridPane.add(labelNote, 0, indiceGrille);
				mainGridPane.add(labelType, 1, indiceGrille);
				mainGridPane.add(labelPoids, 2, indiceGrille);
				mainGridPane.add(labelRessources, 3, indiceGrille);
				mainGridPane.add(modifier, 4, indiceGrille);
				mainGridPane.add(supprimer, 5, indiceGrille);
				mainGridPane.add(ligneNote, 0, indiceGrille);
				/* Incrément de l'indice */
				indiceGrille ++;
			}
		}
	}
	
	private Object[] noteControle(Object note) {
		Object[] tabNote = new Object[8];
		Controle controle = (Controle) note;
		tabNote[0] = controle.getNoteControle().getValeurNote() + " / " + controle.getNoteControle().getDenominateurNote();
		tabNote[1] = controle.getPoidsControle() + "";
		tabNote[2] = controle.getTypeControle();
		String identifiantRessource = controle.getIndentifiantControle().substring(0, 5);
		Enseignement enseignement = gn.trouverEnseignement(identifiantRessource);
		tabNote[3] = enseignement.getIntituleEnseignement();
		tabNote[4] = controle.getDateControle();
		tabNote[5] = controle.getNoteControle().getCommentaire();
		tabNote[6] = note;
		tabNote[7] = note;

		return tabNote;
	}
	private Object[] noteSae(Object note) {
		Object[] tabNote = new Object[8];
		Sae controle = (Sae) note;
		Note noteControle = controle.getNoteSae();
		tabNote[0] = noteControle.getValeurNote() + " / " + noteControle.getDenominateurNote();
		tabNote[1] = "100";
		tabNote[2] = "SAE";
		tabNote[3] = controle.getIntituleEnseignement();
		tabNote[4] = "";
		tabNote[5] = noteControle.getCommentaire();
		tabNote[6] = note;
		tabNote[7] = note;
		return tabNote;

	}
	private Object[] notePortfolio(Object note) {
		Object[] tabNote = new Object[8];
		Portfolio controle = (Portfolio) note;
		Note noteControle = controle.getNotePortfolio();
		tabNote[0] = noteControle.getValeurNote() + " / " + noteControle.getDenominateurNote();
		tabNote[1] = "100";
		tabNote[2] = controle.getIntituleEnseignement();
		tabNote[3] = controle.getIntituleEnseignement();
		tabNote[4] = "";
		tabNote[5] = noteControle.getCommentaire();
		tabNote[6] = note;
		tabNote[7] = note;
		return tabNote;
	}

	/**
	 * Méthode qui permet de changer le nom et de l'afficher sur toute nos pages
	 * L'affichage se fait dans un label présent sur toutes nos pages (sauf popUp)
	 */
	public void afficherNom() {
		labelNomEtudiant.setText(gn.getUtilisateurGestionNotes());
	}

	/**
	 * Cette méthode envoie au model le nom et prénom récupéré dans des Texfield
	 * Elle appelle ensuite une méthode qui affiche le nouveau nom et prénom
	 * renvoyé par le model
	 * @param nom est le nouveau Nom
	 * @param prenom est le nouveau Prenom
	 */
	public void modifierNom(TextField nom, TextField prenom) {
		try {
			gn.setUtilisateurGestionNotes(nom.getText(), prenom.getText());
			Alert validerUtilisateur = new Alert(AlertType.INFORMATION);
			validerUtilisateur.setTitle("Changement utilisateur");
			validerUtilisateur.setHeaderText("L'utilisateur a bien été changé");
			afficherNom();
			validerUtilisateur.showAndWait();
		} catch (UtilisateurInvalideException e) {
			Alert erreurNomPrenom = new Alert(AlertType.ERROR);
			erreurNomPrenom.setTitle("Utilisateur invalide");
			erreurNomPrenom.setHeaderText(e.getMessage());
			erreurNomPrenom.showAndWait();
		}
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de paramètre d'importation des données.
	 * Elle charge la nouvelle scène et l'affiche en remplaçant la scène précédente.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneParametre() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreImporter.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	    } catch (IOException e) {
	        /* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de moyenne des ressources.
	 * Elle charge la nouvelle scène, l'affiche et affiche la moyenne par défaut.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneMoyenneRessource() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageMoyenneRessource.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	        /* Récupération de la gridPane contenant les moyennes par Ressource */
	        GridPane grilleMoyenneRessource = ((GridPane) ((ScrollPane) ((Pane) ((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent());
	        /* Appel à la méthode afficherMoyenneDefaut :
	         * - grilleMoyenneRessource correspond à la grille contenant les moyennes par ressource
	         * - le booléen est fixé à vrai car nous souhaitons afficher les moyennes par ressource
	         */
	        afficherMoyenneDefaut(grilleMoyenneRessource, true);
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de moyenne des compétences.
	 * Elle charge la nouvelle scène, l'affiche et affiche la moyenne par défaut.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneMoyenneCompetence() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageMoyenneCompetence.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	        /* Récupération de la gridPane contenant les moyennes par competence */
	        GridPane grilleMoyenneCompetence = ((GridPane) ((ScrollPane) ((Pane) ((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent());
	        /* Appel à la méthode afficherMoyenneDefaut :
	         * - grilleMoyenneCompetence correspond à la grille contenant les moyennes par competence
	         * - le booléen est fixé à faux car nous souhaitons afficher les moyennes par compétence
	         */
	        afficherMoyenneDefaut(grilleMoyenneCompetence, false);
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de paramètre de modification.
	 * Elle charge la nouvelle scène et l'affiche.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneModifier() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreModifier.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page des notes.
	 * Elle charge la nouvelle scène, l'affiche et affiche les enseignements et les notes.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneNotes() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageNotes.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	        /* Récupération de la gridPane contenant les enseignements sur la Page Notes */
	        GridPane grilleEnseignements = (GridPane) ((ScrollPane) ((Pane) ((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(3)).getContent();
	        /* Récupération de la gridPane contenant les notes sur la Page Notes */
	        GridPane grilleNotes = (GridPane) ((ScrollPane) ((Pane) ((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent();
	        /* Appel à la méthode afficherEnseignements :
	         * - Le booléen est fixé à true car on souhaite afficher les enseignements dans la grille de tri
	         * - grilleEnseignements correspond à la grille de tri sur la page Notes
	         * - gn.getSemestreGestionNotes().getEnseignementsSemestre() correspond à la liste 
	         * des Enseignements actuellement dans l'application
	         * - grilleNotes correspond à la grille contenant les notes sur la page Notes
	         */
	        afficherEnseignements(true, grilleEnseignements, gn.getSemestreGestionNotes().getEnseignementsSemestre(), grilleNotes);
	        /* Appel à la méthode afficherNotes :
	         * - grilleNotes correspond à la grille contenant les notes sur la page Notes
	         * - enseignement est fixé à null car on ne souhaite pas trié par un enseignement précis.
	         * 	 Autrement dit, on souhaite afficher toutes les notes
	         */
	        afficherNotes(grilleNotes, null);
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de paramètre de partage.
	 * Elle charge la nouvelle scène et l'affiche.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneParametrePartager() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageParametrePartager.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page de paramètre de réinitialisation.
	 * Elle charge la nouvelle scène et l'affiche.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneParametreReiniti() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageParametreReinitialiser.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Cette méthode permet de changer la scène vers la page des enseignements.
	 * Elle charge la nouvelle scène, ajoute les compétences et l'affiche.
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException.
	 */
	@FXML
	public void changerSceneEnseignements() {
	    try {
	        /* Récupération du fichier qu'on veut charger */
	        loader.setLocation(getClass().getResource("/application/vue/PageEnseignements.fxml"));
	        /* Chargement de la scène */
	        Parent nouvelleScene = loader.load();
	        /* Conversion de la scène chargée en créant un nouvel objet Scene */
	        Scene nouvelleSceneObjet = new Scene(nouvelleScene);
	        Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
	        ajouterCompetence(gn.getSemestreGestionNotes().getCompetencesSemestre(), nouvelleSceneObjet);
	        stage.setScene(nouvelleSceneObjet); // Affichage de la nouvelle scène
	        /* Attribution d'une feuille de style à la Scene */
	        nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
	    } catch (IOException e) {
	    	/* Message d'erreur en cas d'exception */
	        e.printStackTrace();
	    }
	}

	/**
	 * Affiche les notes saisie par l'utilisateur lorsque celui-ci se trouve sur
	 * la page notes
	 * @param enseignement 
	 *
	 */
	private void afficherMoyenneDefaut(GridPane grille, boolean parRessource) {
		grille.getChildren().clear();

		Pane paneDefaut = new Pane();
		Label messageDefaut = new Label("Veuillez-cliquer surle bouton \"Calculer mes moyennes\"\n"
				+" çi-dessous pour calculer et afficher vos moyennes");
		Button boutonCalculer = new Button("Calculer mes moyennes");

		paneDefaut.getChildren().addAll(messageDefaut,boutonCalculer);

		paneDefaut.setPrefSize(724,375);
		messageDefaut.setPrefSize(724,295);
		boutonCalculer.setPrefSize(200,40);
		messageDefaut.setAlignment(Pos.CENTER);
		boutonCalculer.setAlignment(Pos.CENTER);
		boutonCalculer.setTranslateX(524/2);
		boutonCalculer.setTranslateY(335/2 + 20);

		messageDefaut.getStyleClass().add("labelMoyenneNonCalculee");
		boutonCalculer.getStyleClass().add("boutonMoyenneNonCalculee");

		grille.add(paneDefaut, 0, 0);

		boutonCalculer.setOnMouseClicked(event -> {
			grille.getChildren().clear();
			afficherMoyenne(grille, parRessource);
		});
	}

	private void afficherMoyenne(GridPane grille, boolean parRessource) {
		int indiceGrille = 0;
		grille.getChildren().clear();
		grille.getRowConstraints().clear();
		RowConstraints taille = new RowConstraints();
		taille.setPrefHeight(50);
		if (parRessource) {
			for (Enseignement enseignement : gn.getSemestreGestionNotes().getEnseignementsSemestre()) {
				if (gn.estUneRessource(enseignement)) {
					String identifiant = enseignement.getIdentifiantEnseignement();
					String moyenneString = "";
					try {
						gn.calculerMoyenneEnseignement(identifiant);
						Note noteMoyenne = gn.moyenneEnseignemnt(identifiant);
						double moyenne = noteMoyenne.getValeurNote();
						moyenneString = moyenne + moyenneString;
					} catch (MoyenneRessourceException e) {
						moyenneString = "Moyenne Incalculable";
						//e.printStackTrace();
					} catch (NoteInvalideException e) {
						moyenneString = "Moyenne Incalculable";
						//e.printStackTrace();
					}
					//Création des Label que l'on va afficher dans notre page
					Label labelIdentifiant = new Label(identifiant);
					Label labelIntitule = new Label(enseignement.getIntituleEnseignement());
					Label labelMoyenne = new Label(moyenneString);

					labelIdentifiant.getStyleClass().add("labelNote");
					labelIntitule.getStyleClass().add("labelNote");
					labelMoyenne.getStyleClass().add("labelNote");
					GridPane.setHalignment(labelIdentifiant, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelIntitule, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelMoyenne, javafx.geometry.HPos.CENTER);
					labelIdentifiant.setMaxSize(100, 40);
					labelIntitule.setMaxSize(400, 40);
					labelMoyenne.setMaxSize(224, 40);
					labelIdentifiant.setAlignment(Pos.CENTER);
					labelIntitule.setAlignment(Pos.CENTER);
					labelMoyenne.setAlignment(Pos.CENTER);
					grille.getRowConstraints().add(taille);
					grille.add(labelIdentifiant, 0, indiceGrille);
					grille.add(labelIntitule, 1, indiceGrille);
					grille.add(labelMoyenne, 2, indiceGrille);
					indiceGrille ++;
				}
			}
		} else {
			for (Competence competence : gn.getSemestreGestionNotes().getCompetencesSemestre()) {
				for(Enseignement enseignement: (competence.getListeEnseignements()).keySet()) {
					try {
						gn.calculerMoyenneEnseignement(enseignement.getIdentifiantEnseignement());
					} catch (MoyenneRessourceException | NoteInvalideException e) {
					}
				}
				String identifiant = competence.getIdentifiantCompetence();
				String moyenneString = "";
				Note noteMoyenne = gn.moyenneCompetence(identifiant);
				try {
					gn.calculerMoyenneCompetence(identifiant);
					noteMoyenne = gn.moyenneCompetence(identifiant);
					double moyenne = noteMoyenne.getValeurNote();
					moyenneString = moyenne + moyenneString;
				} catch (MoyenneCompetenceException e) {
					moyenneString = "Moyenne Incalculable";
				} catch (MoyenneRessourceException e) {
					moyenneString = "Moyenne Incalculable";
				} catch (NoteInvalideException e) {
					moyenneString = "Moyenne Incalculable";
				}	
				//Création des Label que l'on va afficher dans notre page
				Label labelIdentifiant = new Label(identifiant);
				Label labelIntitule = new Label(competence.getIntituleCompetence());
				Label labelMoyenne = new Label(moyenneString);

				labelIdentifiant.getStyleClass().add("labelNote");
				labelIntitule.getStyleClass().add("labelNote");
				labelMoyenne.getStyleClass().add("labelNote");
				GridPane.setHalignment(labelIdentifiant, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelIntitule, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelMoyenne, javafx.geometry.HPos.CENTER);
				labelIdentifiant.setMaxSize(100, 40);
				labelIntitule.setMaxSize(400, 40);
				labelMoyenne.setMaxSize(224, 40);
				labelIdentifiant.setAlignment(Pos.CENTER);
				labelIntitule.setAlignment(Pos.CENTER);
				labelMoyenne.setAlignment(Pos.CENTER);
				grille.getRowConstraints().add(taille);
				grille.add(labelIdentifiant, 0, indiceGrille);
				grille.add(labelIntitule, 1, indiceGrille);
				grille.add(labelMoyenne, 2, indiceGrille);
				indiceGrille ++;
			}
		}
	}

	/**
	 * Cette méthode permet d'afficher un popUp sur lequel on peut saisir plusieurs informations
	 * On peut y saisir une note, un controle, une commentaire et une date.
	 * Le popUp récupère les données saisies et lors du clic sur le bouton de validation
	 * On y ajoute une note avec les données saisies dans le popUp
	 */
	@FXML
	public void sceneAjouterNote() {
		try {
			Scene scenePrincipale = rootPane.getScene();
			/* Récupération du fichier qu'on veut charger */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageAjouterNote.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Ajouter Note");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);
			/* Récupération des éléments FXML que l'on veut  */
			Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
			TextField note = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0);
			TextArea commentaire = (TextArea)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(0);
			ComboBox<String> ressource = (ComboBox<String>) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(2)).getChildren().get(0));
			ComboBox<String> controle = (ComboBox<String>) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(0));
			TextField denominateur = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(2));
			affichageModifAjoutNote(note, denominateur, commentaire);
			boutonValider.setOnAction(e -> {
				try {
					ajouterNote(note.getText(), commentaire.getText(), denominateur.getText(), ressource.getValue(), controle.getValue());
					popupStage.close();
				} catch (Exception e1) {
					System.err.println("Impossible d'ajouter la note, veuillez remplir tous les champs");
				}
			});
			/* Ouvre le popUp et attend sa fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet d'afficher un popUp sur lequel on peut modifier une note
	 * On peut y saisir les nouvelles données pour la note, le controle, la commentaire et la date.
	 * Le popUp récupère les données saisies et lors du clic sur le bouton de validation
	 * Le bouton fait appel à une méthode qui modifie les données des notes de la bonne ligne
	 * @param gridPane est la grille que l'on veut modifier
	 * @param boutonModifier le bouton qui permet de modifier la note
	 * @param note à modifier
	 * @param denominateur à modifier
	 * @param commentaire à modifier
	 * @param ressource
	 * @param controle
	 * @param tabNote
	 * @param date à modifier
	 */
	public void sceneModifierNote(GridPane gridPane, Button boutonModifier, String[] noteParams, Object noteAModifier, Object identifiant) {
		try {
			String leControle = "";
			/* Récupération du fichier qu'on veut charger */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageModifierNote.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Modifier Note");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);
			/* Récupération des éléments FXML que l'on veut */
			Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
			TextField recupNote = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0);
			TextArea recupCommentaire = (TextArea)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(0);
			ComboBox<String> recupRessource = (ComboBox<String>) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(2)).getChildren().get(0));
			ComboBox<String> recupControle = (ComboBox<String>) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(0));
			TextField recupDenominateur = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(2));
			//On affiche dans les TextField les informations déja saisies par l'utilisateur
			if (identifiant instanceof Controle) {

				Controle controle = (Controle) identifiant;
				String identifiantRessource = controle.getIndentifiantControle().substring(0, 5);
				Enseignement enseignement = gn.trouverEnseignement(identifiantRessource);
				leControle = controle.getIndentifiantControle().substring(controle.getIndentifiantControle().length() - 2);
				leControle += " " + controle.getTypeControle() + " " + controle.getDateControle();
				controle.setNoteControle(null);
			} else if (identifiant instanceof Sae) {
				Sae sae = (Sae) identifiant;
				sae.setNoteSae(null);
			} else if (identifiant instanceof Portfolio){
				Portfolio portfolio = (Portfolio) identifiant;
				portfolio.setNotePortfolio(null);
			}


			recupNote.setText(noteParams[0]);
			recupCommentaire.setText(noteParams[2]);
			recupRessource.setValue(verifierRessource(noteParams[3])); 
			recupControle.setValue(leControle);
			recupDenominateur.setText(noteParams[1]);
			affichageModifAjoutNote(recupNote, recupDenominateur, recupCommentaire);
			boutonValider.setOnAction(e -> {
				//Vérification que la note est bien inférieure ou égale au dénominateur
				if (!recupNote.getText().isEmpty() && !recupDenominateur.getText().isEmpty()
						&& Double.parseDouble(recupNote.getText()) <= Double.parseDouble(recupDenominateur.getText())
						&& recupRessource.getValue() != null && recupControle.getValue() != null) {
					// Récupérer les nouvelles informations saisies
					String nouvelleNote = recupNote.getText();
					String nouveauCommentaire = recupCommentaire.getText();
					//String nouvelleDate = recupDate.getText();
					String nouveauDenominateur = recupDenominateur.getText();
					// Modifier les informations dans la GridPane
					String nouvelleRessource = recupRessource.getValue();
					String nouveauControle = recupControle.getValue();
					//modifierNote(gridPane, boutonModifier, nouvelleNote, nouveauCommentaire, nouveauDenominateur, noteAModifier, gridPane);
					// Fermeture du popUp
					try {
						ajouterNote(nouvelleNote, nouveauCommentaire, nouveauDenominateur, nouvelleRessource, nouveauControle);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					popupStage.close();
				}
			});
			/* Ouvre le popUp et attend la fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	private String verifierRessource(String ressource) {
		String touteLaRessource = null;
		List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
		for (Enseignement enseignement : listeEnseignement) {
			if (ressource.equals(enseignement.getIntituleEnseignement())) {
				touteLaRessource =  enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement();
			}
		}
		return touteLaRessource;
	}

	/**
	 * Cette méthode permet d'afficher un popUp sur lequel il ya 2 boutons
	 * Si l'utilisateur clique sur le bouton oui, la note de la ligne en question sera supprimée
	 * En revanche si il ferme le popUp ou clique sur le bouton Non,
	 * la note ne sera pas supprimé
	 * @param gridPane est la grille ou l'on veut supprimer une note
	 * @param supprimer est le bouton cliqué dont on va récupérer la ligne
	 * @param identifiantControle
	 */
	public void sceneSupprimerNote(GridPane gridPane, Button supprimer, Object note) {
		try {
			/* Récupération du fichier FXML de note popUp et affichage */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageSupprimerNote.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Supprimer Note");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);

			/* Récupération des 2 éléments FXML qui sont nos boutons oui et non */
			Button boutonOui = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(0))));
			Button boutonNon = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1))));
			/* Ajout de style au survol du bouton*/
			boutonOui.setOnMouseEntered(event -> boutonOui.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
			boutonOui.setOnMouseExited(event -> boutonOui.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));
			/* Ajout de style au survol du bouton*/
			boutonNon.setOnMouseEntered(event -> boutonNon.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
			boutonNon.setOnMouseExited(event -> boutonNon.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));
			/* Action du bouton quand il sera cliqué, il appelera supprimer note et fermera le popUp */
			boutonOui.setOnAction(e -> {
				supprimerNote(gridPane, supprimer, true, note);
				popupStage.close();
			});
			/* Si l'utilisateur clique sur non, le popUp se ferme */
			boutonNon.setOnAction(e -> {
				popupStage.close();
			});
			/* Affichage du popUp et attend sa fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de supprimer une note sur une gridPane en récuprérant un indice du bouton
	 * Elle permet aussi d'enlever la ligne et ne pas laisser de trou si il y a une suppression de note
	 * @param gridPane est la gridPane que l'on veut modifier
	 * @param boutonSupprimer est le bouton cliqué dont on récupère l'indice de la ligne
	 * @param supprimerLigne Ce boolean permet de savoir si on veut supprimer entièrement la ligne
	 */
	private void supprimerNote(GridPane gridPane, Button boutonSupprimer, boolean supprimerLigne, Object noteASupprimer) {
		Integer rowIndex = GridPane.getRowIndex(boutonSupprimer);
		gn.supprimerNote(noteASupprimer);
		if (rowIndex != null) {
			// Récupérer la Pane de la première colonne de la ligne d'indice rowIndex
			Pane paneLigne = null;
			for (Node node : gridPane.getChildren()) {
				Integer row = GridPane.getRowIndex(node);
				Integer col = GridPane.getColumnIndex(node);
				if (row != null && col != null && row.equals(rowIndex) && node instanceof Pane) {
					paneLigne = (Pane) node;
				}
			}
			gridPane.getChildren().removeIf(node ->
			GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex));
			if (supprimerLigne) {
				for (Node node : gridPane.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex) {
						GridPane.setRowIndex(node, row - 1);
					}
				}
			}
		}
		afficherNotes(gridPane,null);
	}

	/**
	 * Cette méthode ouvre et affiche une fenêtre de type PopPup
	 * afin de confirmer si l'utilisateur veut bel et bien 
	 * supprimer le controle de l'application
	 * @param gridPane correspond à la grille contenant le controle
	 * @param supprimer correspond au bouton supprimer
	 * 		  ayant déclenché l'appel à cette méthode
	 * @param enseignement correspond à l'enseignement du controle
	 * @param controle correspond au controle à supprimer
	 */
	public void sceneSupprimerControle(GridPane gridPane, Button supprimer, Enseignement enseignement, Controle controle) {
		try {
			/* Récupération du fichier FXML de note popUp et affichage */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageSupprimerControle.fxml"));
			Parent root = loader.load();
			/* Création d'une nouvelle fenêtre */
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Supprimer Controle");
			/* Création d'un objet scene à partir 
			 * du ficher FXML précédement chargé
			 */
			Scene popupScene = new Scene(root);
			/* Attribution de la scene à la fenêtre */
			popupStage.setScene(popupScene);
			
			/* Récupération des 2 éléments FXML qui sont nos boutons oui et non */
			Button boutonOui = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(0))));
			Button boutonNon = ((((Button) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1))));
			/* Ajout de style au survol du bouton*/
			boutonOui.setOnMouseEntered(event -> boutonOui.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
			boutonOui.setOnMouseExited(event -> boutonOui.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));
			/* Ajout de style au survol du bouton*/
			boutonNon.setOnMouseEntered(event -> boutonNon.setStyle("-fx-background-color: #354B85; -fx-text-fill: #e6e9f0;"));
			boutonNon.setOnMouseExited(event -> boutonNon.setStyle("-fx-background-color: #e6e9f0; -fx-text-fill: #354B85;"));
			/* Action du bouton quand il sera cliqué, il appelera supprimerControle et fermera le popUp */
			boutonOui.setOnAction(e -> {
				/* Appel à la méthode supprimerControle qui 
				 * supprime le controle de l'application
				 * - gridPane correspond à la grille contenant le controle
				 * - supprimer correspond au bouton supprimer ayant
				 *   déclenché l'appel à cette méthode
				 * - le booleen est fixé à true car on souhaite supprimer
				 *   l'affichage de cette ligne
				 * - enseignement correspond à l'enseignement auquel
				 *   le controle est attribué
				 * - controle correspond au controle à supprimer
				 */
				supprimerControle(gridPane, supprimer, true, enseignement, controle);
				/* Fermeture de la popUp */
				popupStage.close();
				/* Appel à la méthode afficherEnseignements qui 
				 * affiche les enseignements sur la page Enseignements
				 * - Le booléen est fixé à false car on souhaite afficher
				 * Dans ce cas de figure, nous appelons la méthode pour
				 * rafraichir l'affichage apres la suppression ou non
				 * pour éviter tout bug de rendu
				 *   les enseignements sur la page Enseignements de l'application
				 *   et non dans la liste de tri de la page Notes
				 * - gridPane correspond à la grille contenant les enseignements
				 * - gn.getSemestreGestionNotes().getEnseignementsSemestre()
				 *   correspond à la liste des enseignements de l'application
				 * - gridPane correspond à la grille contenant les enseignements
				 */ 
				afficherEnseignements(false, gridPane, gn.getSemestreGestionNotes().getEnseignementsSemestre(), gridPane);
			});
			/* Si l'utilisateur clique sur non, le popUp se ferme */
			boutonNon.setOnAction(e -> {
				popupStage.close();
				/* Appel à la méthode afficherEnseignements qui 
				 * affiche les enseignements sur la page Enseignements
				 * Dans ce cas de figure, nous appelons la méthode pour
				 * rafraichir l'affichage apres la suppression ou non
				 * pour éviter tout bug de rendu
				 * - Le booléen est fixé à false car on souhaite afficher
				 *   les enseignements sur la page Enseignements de l'application
				 *   et non dans la liste de tri de la page Notes
				 * - gridPane correspond à la grille contenant les enseignements
				 * - gn.getSemestreGestionNotes().getEnseignementsSemestre()
				 *   correspond à la liste des enseignements de l'application
				 * - gridPane correspond à la grille contenant les enseignements
				 */ 
				afficherEnseignements(false, gridPane, gn.getSemestreGestionNotes().getEnseignementsSemestre(), gridPane);
			});
			/* Affichage du popUp et attend sa fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode supprime l'affichage d'un controle
	 * sur l'application et appelle des méthodes qui vont
	 * elles supprimer le controle de l'application
	 * @param gridPane correspond à la grille contenant le controle
	 * @param boutonSupprimer correspond au bouton supprimer ayant
	 * 		  été cliqué
	 * @param supprimerLigne est un booléen pour savoir si l'on 
	 * 		  confirme la suppression ou non
	 * @param enseignement correspond à l'enseignement du controle
	 * @param controleASupprimer correspond au controle
	 *        que l'on souhaite supprimer
	 */
	private void supprimerControle(GridPane gridPane, Button boutonSupprimer, boolean supprimerLigne, Enseignement enseignement, Controle controleASupprimer) {
		/* Si le controle a supprimer est un controle de Ressource */
		if (controleASupprimer instanceof Controle) {
			/* Récupération de l'identifiant de l'enseignement */
			String identifiantEnseignement = enseignement.getIdentifiantEnseignement();
			try { 	
				/* Appel à la méthode supprimerControleAEnseignement
				 * de GestionNotes qui supprime à un enseignement
				 * le controle de sa liste
				 */
				gn.supprimerControleAEnseignement(identifiantEnseignement, controleASupprimer);
			} catch (ControleInvalideException e) {
				System.out.println("Controle Impossible à supprimer car invalide");
			}
		}
		/* Récupération de l'indice du controle à supprimer
		 * à l'aide de l'indice de son bouton supprimer
		 */
		Integer rowIndex = GridPane.getRowIndex(boutonSupprimer);
		if (rowIndex != null) {
			// Récupérer la Pane de la première colonne de la ligne d'indice rowIndex
			Pane paneLigne = null;
			/* Parcours des éléments de la grille */
			for (Node node : gridPane.getChildren()) {
				/* Récupération des indices de lignes et de colonnes */
				Integer row = GridPane.getRowIndex(node);
				Integer col = GridPane.getColumnIndex(node);
				/* Si l'indice de la ligne de l'élément courant 
				 * correspond à l'indice de la ligne du controle
				 * a supprimer
				 */
				if (row != null && col != null && row.equals(rowIndex) && node instanceof Pane) {
					paneLigne = (Pane) node;
				}
			}
			/* Suppression de l'affichage de la ligne 
			 * précédent récupérée
			 */
			gridPane.getChildren().removeIf(node ->
			GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex));
			/* Si l'utilisateur confirme la suppression */
			if (supprimerLigne) {
				/* Décalage des indices des autres éléments */
				for (Node node : gridPane.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex) {
						GridPane.setRowIndex(node, row - 1);
					}
				}
			}
		}
	}

	/**
	 * Cette méthode permet d'afficher un popUp sur lequel on peut saisir plusieurs informations
	 * On peut y saisir une note, un controle, une commentaire et une date.
	 * Le popUp récupère les données saisies et lors du clic sur le bouton de validation
	 * On y ajoute une note avec les données saisies dans le popUp
	 */
	public void sceneAjouterControle() {
		try {
			Scene sceneActuelle = rootPane.getScene();
			/* Récupération du fichier qu'on veut charger */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageAjouterControle.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Ajouter Controle");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);
			GridPane grilleEnseignement = ((GridPane)((ScrollPane)((Pane)((BorderPane) sceneActuelle.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
			/* Récupération des éléments FXML que l'on veut  */
			Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
			TextField type = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(2)).getChildren().get(0);
			TextField poids = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(2);
			TextField date = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(1);
			@SuppressWarnings("unchecked")
			ComboBox<String> ressourceCombo = (ComboBox<String>)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0));
			/* Création d'un tableau qui contiendra le choix de l'utilisateur */
			String[] choixCombo = new String[1];
			/* Ajout d'un écouteur d'évènement sur la comboBox */
			ressourceCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
				/* Récupération du choix de l'utilisateur */
				choixCombo[0] = ((String) ressourceCombo.getValue ()).substring(0, 5);
			});
			/* Evenement déclenché lorsque l'utilisateur clique sur valider */
			boutonValider.setOnAction(e -> {
				/* Vérifie si tout les champs obligatories sont saisies */
				if (!type.getText().isEmpty() && !poids.getText().isEmpty()) {
					try {
						/* Appel à la méthode ajouterControle qui ajoute
						 * et affiche le controle avec les informations saisies
						 * - type.getText() correspond au type du controle saisi
						 * - poids.getText() correspond au poids du controle saisi
						 * - date.getText() correspond à la date saisie
						 * - choixCombo[0] correspond au choix de la ressource
						 */
						ajouterControle(type.getText(), poids.getText(), date.getText(),choixCombo[0]);
						/* Récupération de la liste des enseignements dans une liste */
						List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
						/* Appel à la méthode afficherEnseignements()
						 * - le booléen est défini à false car on souhaite
						 *   afficher sur la page Enseignements
						 * - grilleEnseignement correspond à la grille des enseignements
						 * - listeEnseignement correspond à la liste des enseignements de l'application
						 * - grilleEnseignement correspond à la grille des enseignements
						 */
						afficherEnseignements(false, grilleEnseignement,listeEnseignement, grilleEnseignement);
					} catch (ControleInvalideException e1) {
						e1.printStackTrace();
					}
					popupStage.close();
				}
			});
			/* Ouvre le popUp et attend sa fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}



	private void ajouterControle(String type, String poids, String date, String ressource) throws ControleInvalideException {
		int valeurPoids = Integer.parseInt(poids);
		if(!gn.ajouterControleAEnseignement(ressource, type, date, valeurPoids)) {
			Alert erreurSauvegarde = new Alert(AlertType.ERROR);
			erreurSauvegarde.setTitle("Ajout de controle Impossible");
			erreurSauvegarde.setHeaderText("La somme des poids dépasse 100\nModifier le poids de ce controle\nOu modifier le poids de ceux existants");
			erreurSauvegarde.showAndWait();
		} 
	}

	/**
	 * Cette méthode permet d'afficher une fenêtre popUp
	 * pour modifier les informations d'un controle
	 * Le popUp récupère les données saisies et lors du clic 
	 * sur le bouton de validation cela modifie le controle
	 * - gridPane correspond à la grille des controle
	 * - boutonModifier correspond au bouton cliqué
	 * - enseignement correspond à l'enseignement du controle
	 * - typeControle correspond au type du controle avant modification
	 * - poidsControle correspond au poids du controle avant modification
	 * - dateControle correspond à la date du controle avant modification
	 * - controle correspond au controle à modifier
	 */
	public void sceneModifierControle(GridPane gridPane, Button boutonModifier, Enseignement enseignement, String typeControle, int poidsControle, String dateControle, Controle controle) {
		try {
			/* Récupération du fichier qu'on veut charger */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageModifierControle.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Modifier Controle");
			Scene popupScene = new Scene(root);
			popupStage.setScene(popupScene);
			/* Récupération des éléments FXML que l'on veut */
			Button boutonValider = (Button) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
			ComboBox<String> recupRessource = (ComboBox<String>) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0));
			TextField recupType = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(2)).getChildren().get(0);
			TextField recupPoids = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(5)).getChildren().get(2);
			TextField recupDate = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(1));
			//On affiche dans les TextField les informations déja saisies par l'utilisateur
			String poidsControleString = poidsControle + "";
			String enseignementString = enseignement.getIdentifiantEnseignement() + enseignement.getIntituleEnseignement();
			recupType.setText(typeControle);
			recupPoids.setText(poidsControleString);
			recupDate.setText(dateControle);
			recupRessource.setValue(enseignementString);
			/* Appel à la méthode affichageModifAjourControle
			 * qui applique le pattern avec le regex
			 * - recupPoids correspond à la zone de saisie du poids
			 */
			affichageModifAjoutControle(recupPoids);
			/* Tableau qui contiendra le choix de l'utilisateur */
			String[] choixRessource = new String[2];
			/* Récupération de lidentifiant de l'enseignement */
			choixRessource[1] = enseignementString.substring(0,5);
			/* Ajout d'un écouteur sur la comboBox du choix de ressource */
			recupRessource.valueProperty().addListener((observable, oldValue, newValue) -> {
				/* Récupération des choix utilisateurs */
				choixRessource[0] = ((String) recupRessource.getValue ()).substring(0, 5);
				choixRessource[1] = choixRessource[0].substring(0,5);
			});
			/* Evenement déclenché lorsque le bouton valider est cliqué */
			boutonValider.setOnAction(e -> {
				/* Vérification que tout les champs obligatoires sont saisies */
				if (!recupType.getText().isEmpty() && !recupPoids.getText().isEmpty()) {
					/* Récupération des informations saisies */
					String nouveauType = recupType.getText();
					String nouveauPoids = recupPoids.getText();
					String nouvelleDate = recupDate.getText();
					/* Si le nouveauPoids est valide */
					if ((((Ressource)enseignement).getSommePoidsControle() - Integer.parseInt(poidsControleString)) + Integer.parseInt(nouveauPoids)<= 100) {
						/* Suppression du controle avant modification */
						supprimerControle(gridPane, boutonValider, false, enseignement, controle);
						try {
							/* Ajout d'un controle avec les valeurs saisies apres modification */
							ajouterControle(nouveauType, nouveauPoids, nouvelleDate, choixRessource[1]);
							/* Récupération de la liste des enseignements dans une liste */
							List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
							/* Appel à la méthode afficherEnseignements()
							 * - le booléen est défini à false car on souhaite
							 *   afficher sur la page Enseignements
							 * - grilleEnseignement correspond à la grille des enseignements
							 * - listeEnseignement correspond à la liste des enseignements de l'application
							 * - grilleEnseignement correspond à la grille des enseignements
							 */
							afficherEnseignements(false, gridPane,listeEnseignement, gridPane);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					popupStage.close();
				}
			});
			/* Ouvre le popUp et attend la fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}







	private void ajoutRessourcesComboControle() {
		comboRessourcesControle.getItems().clear();
		List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
		for (Enseignement enseignement : listeEnseignement) {
			if (enseignement instanceof Ressource) {
				comboRessourcesControle.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
			}
		}
	}

	/**
	 * Cette méthode permet d'afficher un bandeau contenant
	 * le commentaire et la date si renseignés
	 * @param gridPane correspond à la grille des notes
	 * @param ligneCliquée correspond à la Note cliquée
	 * @param commentaire correspond au commentaire de la note
	 * @param date correspond à la date de la note
	 * @param indiceGrille correspond à l'indice de la note
	 */
	private void afficherCommentaire(GridPane gridPane, Pane ligneCliquée, String commentaire, String date, int indiceGrille) {
		/*Récupération de l'indice de la ligne de la ligne cliquée */
		Integer rowIndex = GridPane.getRowIndex(ligneCliquée);
		/* Si la ligne n'était pas cliqué avant le second clic */
		if (ligneCliquée.getId()=="Non Cliquée") {
			/* Parcours des éléments de la grille */
			for (Node node : gridPane.getChildren()) {
				/* Récupération de l'indice de l'élément */
				Integer row = GridPane.getRowIndex(node);
				/* Décalage des éléments dont l'indice est 
				 * superieur à celui de la note cliquée
				 */
				if (row != null && row > rowIndex) {
					GridPane.setRowIndex(node, row + 1);
				}
			}
			/* Modification de l'ID de la pane */
			ligneCliquée.setId("Cliquée");
			int maxCaractereLigne = 60; // Nombre maximal de caractères par ligne
			String texteCommentaire = commentaire; // Votre texte long à afficher
			Text texte = new Text(texteCommentaire);
			texte.setWrappingWidth(maxCaractereLigne * 7); // La largeur de l'espace pour un nombre de caractères
			/* Création d'un label contenant la date */
			Label labelDate = new Label(date);
			/* Création de panes */
			Pane paneCommentaire = new Pane();
			Pane paneTexte = new Pane();
			/* Ajout de la pane à la grille */
			gridPane.add(paneCommentaire, 0, rowIndex + 1);
			/* Modification de la taille */
			paneCommentaire.setPrefSize(540,40);
			/* Ajout d'un columnSpan de 4 car cette
			 * pane occupe 4 colonnes
			 */
			GridPane.setColumnSpan(paneCommentaire, 4);
			/* Ajout des éléments */
			paneTexte.getChildren().add(texte);
			paneCommentaire.getChildren().add(paneTexte);
			paneCommentaire.getChildren().add(labelDate);
			/* Positionnement des éléments */
			paneTexte.setLayoutX(0);
			paneTexte.setLayoutY(0);
			texte.setLayoutY(10);
			labelDate.setLayoutX(390);
			labelDate.setLayoutY(0);
			/* Attribution des feuilles de style aux éléments */
			texte.getStyleClass().add("labelCommentaire");
			paneTexte.getStyleClass().add("paneTexte");
			labelDate.getStyleClass().add("labelDate");
			paneCommentaire.getStyleClass().add("paneCommentaire");
			/* Positionnement et alignement */
			labelDate.setPrefSize(250,40);
			labelDate.setAlignment(Pos.CENTER);
			GridPane.setHalignment(texte, javafx.geometry.HPos.CENTER);
			GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
			GridPane.setMargin(paneCommentaire, new Insets(-10, 0, 0, 0));
			/* Incrément de l'indice */
			indiceGrille++;
		}else {
			/* Modification de l'ID de la pane*/
			ligneCliquée.setId("Non Cliquée");
			/* Suppression du bandeau si il était affiché
			 * avant le clic
			 */
			gridPane.getChildren().removeIf(node ->
			GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex+1));
			/* Parcours des éléments de la grille */
			for (Node node : gridPane.getChildren()) {
				/* Récupération de l'indice de l'élément actuel */
				Integer row = GridPane.getRowIndex(node);
				/* Décalage des éléments dont l'indice est 
				 * superieur à celui du commentaire
				 */
				if (row != null && row > rowIndex+1) {
					GridPane.setRowIndex(node, row - 1);
				}
			}
			/* Décrément de l'indice */
			indiceGrille--;
		}
	}

	/**
	 * Crée un TextFormatter pour valider et filtrer les saisies dans un TextField.
	 * Ce TextFormatter garantit que les saisies correspondent à un modèle spécifique.
	 * @param le pattern qui doit être respecté
	 * @return TextFormatter pour le TextField
	 */
	private TextFormatter<Object> pattern(String regexp) {
		// Modèle avec une expression régulière
		Pattern pattern = Pattern.compile(regexp);
		// Filtre pour valider et filtrer les saisies selon le modèle défini
		UnaryOperator<TextFormatter.Change> filter = change -> {
			String newText = change.getControlNewText();
			// Vérifie si le nouveau texte correspond au motif défini ou s'il est vide
			if (pattern.matcher(newText).matches() || newText.isEmpty()) {
				return change; // Autorise la saisie
			} else {
				return null; // Rejette la saisie
			}
		};
		// Retourne un TextFormatter configuré avec le filtre défini
		return new TextFormatter<>(filter);
	}
	/**
	 * Permet d'ouvrir un explorateur de fichier
	 * Et de choisir un fichier
	 * @throws EnseignementInvalideException 
	 * @throws ControleInvalideException 
	 * @throws ExtensionFichierException
	 */
	private void selectionnerFichier(Button boutonClique) {
		FileChooser explorateurFichier = new FileChooser();
		explorateurFichier.setTitle("Sélectionner un fichier");
		explorateurFichier.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
		// Affiche la boîte de dialogue et attend que l'utilisateur sélectionne un fichier
		Stage stageActuel = (Stage) rootPane.getScene().getWindow();
		File fichierChoisi = explorateurFichier.showOpenDialog(stageActuel);
		// Si l'utilisateur a sélectionné un fichier, affiche son chemin
		if (fichierChoisi != null) {
			String nomFichier = fichierChoisi.getAbsolutePath();
			if (boutonClique == boutonImporterFichierProgramme) {
				try {
					System.out.println("J'importe semestre");
					gn.importerParametrageSemestre(nomFichier);
					Alert importationReussi = new Alert(AlertType.INFORMATION);
					importationReussi.setTitle("Importation réussi");
					importationReussi.setHeaderText("Importation réussi");
					importationReussi.showAndWait();
					activerAutresBoutons();
				} catch (Exception e) {
					Alert importationErreur = new Alert(AlertType.ERROR);
					importationErreur.setTitle("Erreur d'importation");
					importationErreur.setHeaderText(e.getMessage());
					importationErreur.showAndWait();
				}
			} else if (boutonClique == boutonImporterFichierRessource) {
				try {
					gn.importerParametrageEnseignement(nomFichier);
					System.out.println("J'importe ressources");
					Alert importationReussi = new Alert(AlertType.INFORMATION);
					importationReussi.setTitle("Importation réussi");
					importationReussi.setHeaderText("Importation réussi");
					importationReussi.showAndWait();
				} catch (Exception e) {
					Alert importationErreur = new Alert(AlertType.ERROR);
					importationErreur.setTitle("Erreur d'importation");
					importationErreur.setHeaderText(e.getMessage());
					importationErreur.showAndWait();
				}
			} else if (boutonClique == boutonSelectionFichierPartager) {
				cheminFichierExport.setText("Fichier : " + nomFichier);
			}

		}
	}

	/** 
	 * Réinitialise toutes les données de l'application
	 * 
	 */
	private void reinitialiserDonnees() {
		try {
			Alert donneesEffacees = new Alert(AlertType.CONFIRMATION);
			donneesEffacees.setTitle("Réinitialisation");
			donneesEffacees.setContentText("Etes-vous sûr(e) de vouloir réinitialiser l'application ?");
			donneesEffacees.getButtonTypes().setAll(ButtonType.YES,ButtonType.NO);
			donneesEffacees.showAndWait();
			if (donneesEffacees.getResult() == ButtonType.YES) {
				gn.reinitialiserGestionNotes();
				gn = GestionNotes.getInstance();
				afficherNom();
				Alert reinitialisationReussi = new Alert(AlertType.INFORMATION);
				reinitialisationReussi.setTitle("Données réinitialisées");
				reinitialisationReussi.setHeaderText("Les données ont bien été réinitialisé");
				reinitialisationReussi.showAndWait();
				gn.setFichierImporte(false);
				changerSceneParametre();
			}
		} catch (ClassNotFoundException | UtilisateurInvalideException | IOException e) {
			Alert erreurReinitialisation = new Alert(AlertType.ERROR);
			erreurReinitialisation.setTitle("Données non réinitialisées");
			erreurReinitialisation.setHeaderText("Les données n'ont pas pu être réinitialisé");
			erreurReinitialisation.showAndWait();
		}
	}

	private void afficherMessageSurvol(Button bouton, String message) {
		javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(message);
		javafx.scene.control.Tooltip.install(bouton, tooltip);
	}
}