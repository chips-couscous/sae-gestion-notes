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
	 * @param listeCompetences
	 * @param scene
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
			if (ligneToutes.getId().equals("Toutes Non Cliquée")) {
				ligneToutes.setId("Toutes Cliquée");
				for (Node competence : grilleCompetence.getChildren()) {
					if (competence.getId().equals("Toutes Non Cliquée") || competence.getId().equals("Toutes Cliquée")) {
						competence.getStyleClass().removeAll("paneCompetenceCliquee");
						competence.getStyleClass().add("paneCompetenceNonCliquee");
						((Label) ((Pane) competence).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
						((Label) ((Pane) competence).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
						competence.setId("Toutes Non Cliquée");
					} else {
						competence.getStyleClass().removeAll("paneCompetenceCliquee");
						competence.getStyleClass().add("paneCompetenceNonCliquee");
						((Text) ((Pane) competence).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
						((Text) ((Pane) competence).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
						competence.setId("Non Cliquée");
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
				if (paneCompetence.getId().equals("Non Cliquée")) {
					paneCompetence.setId("Cliquée");
					for (Node competences : grilleCompetence.getChildren()) {
						if (competences.getId().equals("Toutes Non Cliquée") || competences.getId().equals("Toutes Cliquée")) {
							competences.getStyleClass().removeAll("paneCompetenceCliquee");
							competences.getStyleClass().add("paneCompetenceNonCliquee");
							((Label) ((Pane) competences).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
							((Label) ((Pane) competences).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
							competences.setId("Toutes Non Cliquée");
						} else {
							competences.getStyleClass().removeAll("paneCompetenceCliquee");
							competences.getStyleClass().add("paneCompetenceNonCliquee");
							((Text) ((Pane) competences).getChildren().get(0)).getStyleClass().removeAll("labelCompetenceCliquee");
							((Text) ((Pane) competences).getChildren().get(0)).getStyleClass().add("labelCompetenceNonCliquee");
							competences.setId("Non Cliquée");
						}
					}
					paneCompetence.getStyleClass().removeAll("paneCompetenceNonCliquee");
					paneCompetence.getStyleClass().add("paneCompetenceCliquee");
					texte.getStyleClass().removeAll("labelCompetenceNonCliquee");
					texte.getStyleClass().add("labelCompetenceCliquee");
				}else {
					paneCompetence.setId("Non Cliquée");
					paneCompetence.getStyleClass().removeAll("paneCompetenceCliquee");
					paneCompetence.getStyleClass().add("paneCompetenceNonCliquee");
					texte.getStyleClass().removeAll("labelCompetenceCliquee");
					texte.getStyleClass().add("labelCompetenceNonCliquee");
				}
				ajouterEnseignements(paneCompetence, competence.getListeEnseignements(), scene);
			});
		}
	}

	/**
	 * 
	 * @param triRessources
	 * @param grilleEnseignement
	 * @param listeEnseignement
	 * @param grilleNotes
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
	 * 
	 * @param competenceSelectionnee
	 * @param listeEnseignements
	 * @param scene
	 */
	private void ajouterEnseignements(Pane competenceSelectionnee, HashMap<Enseignement, Integer> listeEnseignements, Scene scene) {
		indiceEnseignement = 0;
		//Permet de mettre une taille à une ligne quand on l'ajoute
		GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
		grilleEnseignement.setVgap(10);
		grilleEnseignement.getChildren().clear();
		grilleEnseignement.getRowConstraints().clear();
		RowConstraints tailleEnseignement = new RowConstraints();
		tailleEnseignement.setMinHeight(40);
		tailleEnseignement.setPrefHeight(40);
		tailleEnseignement.setMaxHeight(40);
		for (Enseignement enseignement : listeEnseignements.keySet()) {
			grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
			Pane paneEnseignement = new Pane();
			paneEnseignement.setPrefSize(640, 40);
			paneEnseignement.setMinSize(640, 40);
			paneEnseignement.setMaxSize(640, 40);
			GridPane.setColumnSpan(paneEnseignement, 4);
			paneEnseignement.getStyleClass().add("paneEnseignement");
			paneEnseignement.setId("Non Cliquée");
			Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
			labelEnseignement.getStyleClass().add("labelCompetence");
			labelEnseignement.setPadding(new Insets(0, 5, 0, 5));
			paneEnseignement.getChildren().add(labelEnseignement);
			grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
			paneEnseignement.setOnMouseClicked(event -> {

				afficherControle(scene, paneEnseignement, enseignement);
			});
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
	 * 
	 */
	private void afficherControle(Scene scene, Pane enseignementSelectionne, Enseignement enseignement) {
		GridPane grilleEnseignement = ((GridPane)((ScrollPane)((Pane)((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
		if (gn.estUneRessource(enseignement)) {
			Ressource ressource = (Ressource) enseignement;
			Integer rowIndex = GridPane.getRowIndex(enseignementSelectionne);
			if ("Non Cliquée".equals(enseignementSelectionne.getId())) {
				enseignementSelectionne.setId("Cliquée");
				for (Node node : grilleEnseignement.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex) {
						GridPane.setRowIndex(node, row + ressource.getControlesRessource().size() + 1);
					}
				}
				int indiceControle = 1;
				Pane paneInformations = new Pane();
				GridPane.setColumnSpan(paneInformations, 4);
				Label labelTypeInfo = new Label("Type");
				Label labelPoidsInfo = new Label("Poids");
				Label labelDateInfo = new Label("Date");
				paneInformations.getChildren().add(labelTypeInfo);
				paneInformations.getChildren().add(labelPoidsInfo);
				paneInformations.getChildren().add(labelDateInfo);
				paneInformations.getStyleClass().add("paneInformations");
				paneInformations.setPrefSize(600, 40);
				paneInformations.setMinSize(600, 40);
				paneInformations.setMaxSize(600, 40);
				paneInformations.setTranslateX(40);
				labelTypeInfo.setLayoutX(0);
				labelPoidsInfo.setLayoutX(130);
				labelDateInfo.setLayoutX(260);
				labelTypeInfo.getStyleClass().add("labelInfo");
				labelPoidsInfo.getStyleClass().add("labelInfo");
				labelDateInfo.getStyleClass().add("labelInfo");
				labelTypeInfo.setPrefSize(130,40);
				labelPoidsInfo.setPrefSize(130,40);
				labelDateInfo.setPrefSize(130,40);
				labelTypeInfo.setAlignment(Pos.CENTER);
				labelPoidsInfo.setAlignment(Pos.CENTER);
				labelDateInfo.setAlignment(Pos.CENTER);
				GridPane.setHalignment(labelTypeInfo, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelPoidsInfo, javafx.geometry.HPos.CENTER);
				GridPane.setHalignment(labelDateInfo, javafx.geometry.HPos.CENTER);
				//GridPane.setMargin(paneInformations, new Insets(-10, 0, 0, 0));
				grilleEnseignement.add(paneInformations, 0, rowIndex + indiceControle);
				indiceControle++;
				for (Controle controle : ressource.getControlesRessource()) {
					Pane paneControle = new Pane();
					GridPane.setColumnSpan(paneControle, 4);
					Label labelType = new Label(controle.getTypeControle());
					Label labelPoids = new Label("" + controle.getPoidsControle());
					Label labelDate = new Label(controle.getDateControle());
					paneControle.getChildren().add(labelType);
					paneControle.getChildren().add(labelPoids);
					paneControle.getChildren().add(labelDate);
					paneControle.getStyleClass().add("paneControle");
					paneControle.setPrefSize(600, 40);
					paneControle.setMinSize(600, 40);
					paneControle.setMaxSize(600, 40);
					paneControle.setTranslateX(40);
					labelType.setLayoutX(0);
					labelPoids.setLayoutX(130);
					labelDate.setLayoutX(260);
					labelType.getStyleClass().add("labelControle");
					labelPoids.getStyleClass().add("labelControle");
					labelDate.getStyleClass().add("labelControle");
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
					//String[] parties = ((String) tabNote[0]).split("/");
					//String[] noteParams = { parties[0].trim(), parties[1].trim(), (String) tabNote[5], (String) tabNote[3], (String) tabNote[2] };

					// Action des boutons
					supprimer.setOnAction(event -> sceneSupprimerControle(grilleEnseignement, supprimer, enseignement, controle));
					modifier.setOnAction(event -> sceneModifierControle(grilleEnseignement,modifier,enseignement, controle.getTypeControle(), controle.getPoidsControle(), controle.getDateControle(), controle));
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
					labelType.setAlignment(Pos.CENTER);
					labelPoids.setAlignment(Pos.CENTER);
					labelDate.setAlignment(Pos.CENTER);
					GridPane.setHalignment(labelType, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelPoids, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
					GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);
					grilleEnseignement.add(paneControle, 0, rowIndex + indiceControle);
					grilleEnseignement.add(modifier, 4, rowIndex + indiceControle);
					grilleEnseignement.add(supprimer, 5, rowIndex + indiceControle);
					indiceControle++;
				}
			} else {
				enseignementSelectionne.setId("Non Cliquée");
				for (int indiceControle = 1; indiceControle <= ressource.getControlesRessource().size() + 1; indiceControle++) {
					int indice = indiceControle;
					grilleEnseignement.getChildren().removeIf(node ->
					GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex + indice));
					indiceEnseignement--;
				}
				for (Node node : grilleEnseignement.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex + 1) {
						GridPane.setRowIndex(node, row - ressource.getControlesRessource().size() - 1);
					}
				}
			}
		} else{
			Integer rowIndex = GridPane.getRowIndex(enseignementSelectionne);
			if ("Non Cliquée".equals(enseignementSelectionne.getId())) {
				enseignementSelectionne.setId("Cliquée");
				for (Node node : grilleEnseignement.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex) {
						GridPane.setRowIndex(node, row + 1);
					}
				}
				int indiceControle = 1;
				Pane paneInformations = new Pane();
				GridPane.setColumnSpan(paneInformations, 4);
				Label labelInfo = new Label("");
				if (gn.estUneSae(enseignement)){
					labelInfo.setText("Les SAE ne contiennent aucun contrôle mais seulement une note");
				} else if (gn.estUnPortfolio(enseignement)) {
					labelInfo.setText("Les Portfolio ne contiennent aucun contrôle mais seulement une note");
				}
				paneInformations.getChildren().add(labelInfo);
				paneInformations.getStyleClass().add("paneInformations");
				paneInformations.setPrefSize(600, 40);
				paneInformations.setMinSize(600, 40);
				paneInformations.setMaxSize(600, 40);
				labelInfo.setPrefSize(600, 40);
				labelInfo.setMinSize(600, 40);
				labelInfo.setMaxSize(600, 40);
				paneInformations.setTranslateX(40);
				labelInfo.getStyleClass().add("labelInfoSaePortfolio");
				labelInfo.setAlignment(Pos.CENTER);
				grilleEnseignement.add(paneInformations, 0, rowIndex + indiceControle);
			} else {
				enseignementSelectionne.setId("Non Cliquée");
				grilleEnseignement.getChildren().removeIf(node ->
				GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex + 1));
				indiceEnseignement--;
				for (Node node : grilleEnseignement.getChildren()) {
					Integer row = GridPane.getRowIndex(node);
					if (row != null && row > rowIndex + 1) {
						GridPane.setRowIndex(node, row - 1);
					}
				}
			}
		}
	}

	/**
	 * Affiche les notes saisie par l'utilisateur lorsque celui-ci se trouve sur
	 * la page notes
	 * @param enseignement 
	 *
	 */
	private void afficherNotes(GridPane grille, Enseignement enseignement) {
		if (enseignement == null) {
			int indiceGrille = 0;
			ArrayList<Object> notes = gn.getNotes(); // Contient tous les contrôles, SAE, Portfolio ayant une note
			ArrayList<Object[]> notesBien = new ArrayList<Object[]>();
			for (Object note : notes) {
				Object[] tabNote = new String[6];
				if (note instanceof Controle) {
					tabNote = noteControle(note);
				} else if (note instanceof Sae){
					tabNote = noteSae(note);
				} else {
					tabNote = notePortfolio(note);
				}
				notesBien.add(tabNote);
			}
			afficherNotesSelectionne(notesBien,indiceGrille,grille);
		}else {
			ArrayList<Object[]> notesBien = new ArrayList<Object[]>();
			int indiceGrille = 0;
			if (gn.estUneRessource(enseignement)) {
				String enseignementID = enseignement.getIdentifiantEnseignement();
				Ressource ressource = (Ressource) gn.trouverEnseignement(enseignementID);
				for (Controle controle : ressource.getControlesRessource()) {
					if (controle.aUneNote()) {
						Object[] tabNote = noteControle(controle);
						notesBien.add(tabNote);
					}
				}
			} else if (gn.estUneSae(enseignement)) {
				String enseignementID = enseignement.getIdentifiantEnseignement();
				Sae sae = (Sae) gn.trouverEnseignement(enseignementID);
				if (sae.aUneNote()) {
					Object[] tabNote = noteSae(sae);
					notesBien.add(tabNote);
				}
			} else if (gn.estUnPortfolio(enseignement)) {
				String enseignementID = enseignement.getIdentifiantEnseignement();
				Portfolio portfolio = (Portfolio) gn.trouverEnseignement(enseignementID);
				if (portfolio.aUneNote()) {
					Object[] tabNote = notePortfolio(portfolio);
					notesBien.add(tabNote);
				}
			}
			afficherNotesSelectionne(notesBien,indiceGrille,grille);
		}
	}

	private void afficherNotesSelectionne(ArrayList<Object[]> notes, int indiceGrille, GridPane grille) {
		int[] indice = new int[1];
		indice[0] = indiceGrille;
		GridPane mainGridPane = grille;
		RowConstraints taille = new RowConstraints();
		mainGridPane.getChildren().clear();
		mainGridPane.getRowConstraints().clear();
		taille.setPrefHeight(50);
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
				//Création des Label que l'on va afficher dans notre page
				Pane ligneNote = new Pane();
				Label labelNote = new Label((String) tabNote[0]);
				Label labelType = new Label((String) tabNote[2]);
				Label labelPoids = new Label((String) tabNote[1]);
				Label labelRessources = new Label((String) tabNote[3]);
				String date = (String) tabNote[4];
				String commentaire = (String) tabNote[5];
				Object identifiantControle = tabNote[7];
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
				afficherMessageSurvol(modifier, MESSAGE_MODIFIER_NOTE);
				Button supprimer = new Button();
				supprimer.setGraphic(imageViewSupprimer);
				afficherMessageSurvol(supprimer, MESSAGE_SUPPRIMER_NOTE);

				modifier.setMaxSize(30, 30);
				supprimer.setMaxSize(30, 30);
				// Taille des boutons
				modifier.setMaxSize(30, 30);
				supprimer.setMaxSize(30, 30);
				String[] parties = ((String) tabNote[0]).split("/");
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
				ligneNote.setPrefSize(640, 40);
				ligneNote.setId("Non Cliquée");
				GridPane.setColumnSpan(ligneNote, 4);
				labelNote.setMaxSize(130, 40);
				labelType.setMaxSize(130, 40);
				labelPoids.setMaxSize(130, 40);
				labelRessources.setMaxSize(250, 40);
				labelNote.setAlignment(Pos.CENTER);
				labelType.setAlignment(Pos.CENTER);
				labelPoids.setAlignment(Pos.CENTER);
				labelRessources.setAlignment(Pos.CENTER);
				modifier.setMaxSize(30, 30);
				supprimer.setMaxSize(30, 30);
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

				ligneNote.setOnMouseClicked(event -> {
					afficherCommentaire(mainGridPane, ligneNote, commentaire, date, indice[0]);
				});

				mainGridPane.getRowConstraints().add(taille);
				mainGridPane.add(labelNote, 0, indiceGrille);
				mainGridPane.add(labelType, 1, indiceGrille);
				mainGridPane.add(labelPoids, 2, indiceGrille);
				mainGridPane.add(labelRessources, 3, indiceGrille);
				mainGridPane.add(modifier, 4, indiceGrille);
				mainGridPane.add(supprimer, 5, indiceGrille);
				mainGridPane.add(ligneNote, 0, indiceGrille);

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
	 * Cette méthode permet de récupérer la scene de la page de paramètre d'importation des données
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneParametre() {
		try {
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageParametreImporter.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void changerSceneMoyenneRessource() {
		try {
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageMoyenneRessource.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
			GridPane grilleMoyenneRessource = ((GridPane)((ScrollPane)((Pane)((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent());
			afficherMoyenneDefaut(grilleMoyenneRessource, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void changerSceneMoyenneCompetence() {
		try {
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageMoyenneCompetence.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
			GridPane grilleMoyenneCompetence = ((GridPane)((ScrollPane)((Pane)((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent());
			afficherMoyenneDefaut(grilleMoyenneCompetence, false);
		} catch (IOException e) {
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
	 * Cette méthode permet de récupérer la scene de la page de paramètre de modification d'identité
	 * Elle la charge puis l'affiche en remplacant la scène précedente
	 * Si la scène n'est pas trouvée, la méthode lève l'exception IOException
	 */
	@FXML
	public void changerSceneModifier() {
		try {
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageParametreModifier.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
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
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageNotes.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
			GridPane grilleRessources = (GridPane)((ScrollPane) ((Pane)((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(3)).getContent();
			GridPane grilleNotes = (GridPane)((ScrollPane) ((Pane)((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(1)).getContent();
			afficherEnseignements(true, grilleRessources, gn.getSemestreGestionNotes().getEnseignementsSemestre(), grilleNotes);
			afficherNotes(grilleNotes, null);
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
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageParametrePartager.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
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
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageParametreReinitialiser.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
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
	public void changerSceneEnseignements() {
		try {
			/* Récupération du fichier qu'on veut charger */
			loader.setLocation(getClass().getResource("/application/vue/PageEnseignements.fxml"));
			Parent nouvelleScene = loader.load();
			Scene nouvelleSceneObjet = new Scene(nouvelleScene);
			Stage stage = (Stage) rootPane.getScene().getWindow(); // Récupérez la fenêtre actuelle.
			ajouterCompetence(gn.getSemestreGestionNotes().getCompetencesSemestre(), nouvelleSceneObjet);
			stage.setScene(nouvelleSceneObjet); //Affichage de la nouvelle scene
			nouvelleSceneObjet.getStylesheets().add(getClass().getResource("/application/vue/application.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
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
			//			if (paneLigne.getId()=="Cliquée") {
			//				gridPane.getChildren().removeIf(node ->
			//				GridPane.getRowIndex(node)  != null && GridPane.getRowIndex(node).equals(rowIndex + 1));
			//				indice--;
			//				for (Node node : gridPane.getChildren()) {
			//					Integer row = GridPane.getRowIndex(node);
			//					if (row != null && row > rowIndex) {
			//						GridPane.setRowIndex(node, row - 1);
			//					}
			//				}
			//			}
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
	 * 
	 * @param gridPane
	 * @param supprimer
	 * @param note
	 */
	public void sceneSupprimerControle(GridPane gridPane, Button supprimer, Enseignement enseignement, Controle controle) {
		try {
			/* Récupération du fichier FXML de note popUp et affichage */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageSupprimerControle.fxml"));
			Parent root = loader.load();
			Stage popupStage = new Stage();
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Supprimer Controle");
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
				supprimerControle(gridPane, supprimer, true, enseignement, controle);
				popupStage.close();
				afficherEnseignements(false, gridPane, gn.getSemestreGestionNotes().getEnseignementsSemestre(), gridPane);
			});
			/* Si l'utilisateur clique sur non, le popUp se ferme */
			boutonNon.setOnAction(e -> {
				popupStage.close();
				afficherEnseignements(false, gridPane, gn.getSemestreGestionNotes().getEnseignementsSemestre(), gridPane);
			});
			/* Affichage du popUp et attend sa fermeture */
			popupStage.showAndWait();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void supprimerControle(GridPane gridPane, Button boutonSupprimer, boolean supprimerLigne, Enseignement enseignement, Controle controleASupprimer) {
		if (controleASupprimer instanceof Controle) {
			String identifiantEnseignement = enseignement.getIdentifiantEnseignement();
			try { 	
				gn.supprimerControleAEnseignement(identifiantEnseignement, controleASupprimer);
			} catch (ControleInvalideException e) {
				System.out.println("Controle Impossible à supprimer car invalide");
			}
		}
		Integer rowIndex = GridPane.getRowIndex(boutonSupprimer);
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
			String[] choixCombo = new String[1];
			ressourceCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
				choixCombo[0] = ((String) ressourceCombo.getValue ()).substring(0, 5);
			});
			boutonValider.setOnAction(e -> {
				if (!type.getText().isEmpty() && !poids.getText().isEmpty()) {
					try {
						ajouterControle(type.getText(), poids.getText(), date.getText(),choixCombo[0]);
						List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
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
			affichageModifAjoutControle(recupPoids);
			String[] choixRessource = new String[2];
			choixRessource[1] = enseignementString.substring(0,5);
			// Modifier les informations dans la GridPane
			recupRessource.valueProperty().addListener((observable, oldValue, newValue) -> {
				choixRessource[0] = ((String) recupRessource.getValue ()).substring(0, 5);
				choixRessource[1] = choixRessource[0].substring(0,5);
			});
			boutonValider.setOnAction(e -> {
				//Vérification que la note est bien inférieure ou égale au dénominateur
				if (!recupType.getText().isEmpty() && !recupPoids.getText().isEmpty()) {
					// Récupérer les nouvelles informations saisies
					String nouveauType = recupType.getText();
					String nouveauPoids = recupPoids.getText();
					String nouvelleDate = recupDate.getText();
					//modifierNote(gridPane, boutonModifier, nouvelleNote, nouveauCommentaire, nouveauDenominateur, noteAModifier, gridPane);
					// Fermeture du popUp
					if ((((Ressource)enseignement).getSommePoidsControle() - Integer.parseInt(poidsControleString)) + Integer.parseInt(nouveauPoids)<= 100) {
						supprimerControle(gridPane, boutonValider, false, enseignement, controle);
						try {
							ajouterControle(nouveauType, nouveauPoids, nouvelleDate, choixRessource[1]);
							List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
							afficherEnseignements(false, gridPane,listeEnseignement, gridPane);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						/*
						System.out.println("Ici");
						Alert sommePoidsErreur = new Alert(AlertType.ERROR);
						sommePoidsErreur.setTitle("Erreur de modification");
						sommePoidsErreur.setHeaderText("La somme des poids après modification dépasse 100 \n"
													  +"Veuillez-remodifier en modifiant les valeurs");
						sommePoidsErreur.showAndWait();
						 */
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
	 * 
	 * @param gridPane
	 * @param ligneCliquée
	 * @param commentaire
	 * @param date
	 */
	private void afficherCommentaire(GridPane gridPane, Pane ligneCliquée, String commentaire, String date, int indiceGrille) {
		/*Récupération de l'indice de la ligne de la ligne cliquée */
		Integer rowIndex = GridPane.getRowIndex(ligneCliquée);
		if (ligneCliquée.getId()=="Non Cliquée") {
			for (Node node : gridPane.getChildren()) {
				Integer row = GridPane.getRowIndex(node);
				if (row != null && row > rowIndex) {
					GridPane.setRowIndex(node, row + 1);
				}
			}
			ligneCliquée.setId("Cliquée");
			int maxCaractereLigne = 60; // Nombre maximal de caractères par ligne
			String texteCommentaire = commentaire; // Votre texte long à afficher
			Text texte = new Text(texteCommentaire);
			texte.setWrappingWidth(maxCaractereLigne * 7); // La largeur de l'espace pour un nombre de caractères

			Label labelDate = new Label(date);
			Pane paneCommentaire = new Pane();
			Pane paneTexte = new Pane();
			gridPane.add(paneCommentaire, 0, rowIndex + 1);
			paneCommentaire.setPrefSize(540,40);
			GridPane.setColumnSpan(paneCommentaire, 4);
			paneTexte.getChildren().add(texte);
			paneCommentaire.getChildren().add(paneTexte);
			paneCommentaire.getChildren().add(labelDate);
			paneTexte.setLayoutX(0);
			paneTexte.setLayoutY(0);
			texte.setLayoutY(10);
			labelDate.setLayoutX(390);
			labelDate.setLayoutY(0);
			texte.getStyleClass().add("labelCommentaire");
			paneTexte.getStyleClass().add("paneTexte");
			labelDate.getStyleClass().add("labelDate");
			paneCommentaire.getStyleClass().add("paneCommentaire");
			labelDate.setPrefSize(250,50);
			labelDate.setAlignment(Pos.CENTER);
			GridPane.setHalignment(texte, javafx.geometry.HPos.CENTER);
			GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
			GridPane.setMargin(paneCommentaire, new Insets(-10, 0, 0, 0));
			indiceGrille++;
		}else {
			ligneCliquée.setId("Non Cliquée");
			gridPane.getChildren().removeIf(node ->
			GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex+1));
			for (Node node : gridPane.getChildren()) {
				Integer row = GridPane.getRowIndex(node);
				if (row != null && row > rowIndex+1) {
					GridPane.setRowIndex(node, row - 1);
				}
			}
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