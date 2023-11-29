/*
 * Controlleur.java                                                   24/10/2024
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
import application.model.Ressource;
import application.model.exception.CompetenceInvalideException;
import application.model.exception.ControleInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.IpException;
import application.model.exception.PortReseauException;
import application.model.exception.NoteInvalideException;
import application.model.exception.SemestreInvalideExecption;
import application.model.exception.UtilisateurInvalideException;
import application.model.exception.cheminFichierException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
 * @author tom.jammes
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
    Button boutonSelectionFichierPartager;
    @FXML
    Button boutonSelectionDossierReception;
    @FXML
    Button btnEnvoyerFichier;
    @FXML
    Button btnRecevoirFichier;
    @FXML
    Label ipUtilisateur;
    @FXML
    Label cheminFichierExport;
    @FXML
    Label cheminDossierReception;      
    @FXML
    TextField saisieIpServeur;
    @FXML
    TextField saisiePortClient;
    @FXML
    TextField saisiePortServeur;
    @FXML
    Button boutonAide;
    @FXML
    Button boutonSauvegarder;
    @FXML
    Button boutonPartagerFichier;
    @FXML
    ComboBox<String> ressourcesCombo;
    @FXML
    ComboBox<String> controleCombo;
    @FXML
    Label controle;
    @FXML
    Label etoile;
    @FXML
    ComboBox comboRessourcesControle;
    @FXML
    Button boutonReinitialisation;


    String prenomEtu;

    String nomEtu;

    int indice = 0;
    int indiceEnseignement = 0;

    FXMLLoader loader = new FXMLLoader(); // FXML loader permettant de charger un fichier FXML et de changer de scene

    private static final int LONGUEUR_MAX_LIGNE = 50;
    private static final int LONGUEUR_MAX = 200;

    private GestionNotes gn = GestionNotes.getInstance();


    final String MESSAGE_AIDE = "Aide";
    final String MESSAGE_SAUVEGARDE = "Sauvegarder";
    final String MESSAGE_AJOUT_NOTE = "Ajouter une note";
    final String MESSAGE_AJOUT_CONTROLE = "Ajouter un contrôle";
    final String MESSAGE_SUPPRIMER_NOTE = "Supprimer une note";
    final String MESSAGE_MODIFIER_NOTE = "Modifier une note";


    /**
     * Méthode permettant de lancer d'autres méthodes ou des attributs
     * directement au lancement de l'application
     */
    public void initialize() {
        if (listeNotes != null) {
            Scene sceneActuelle = rootPane.getScene();
            GridPane grilleRessources = (GridPane)((ScrollPane) ((Pane)(rootPane).getChildren().get(1)).getChildren().get(4)).getContent();
            afficherEnseignements(true, grilleRessources, gn.getSemestreGestionNotes().getEnseignementsSemestre(), sceneActuelle);
            afficherNotes();
        }
        if (ressourcesCombo != null) {
            ajoutRessourcesCombo();
            choixComboRessources(ressourcesCombo);
        }
        if (boutonSauvegarder != null) {
            boutonSauvegarder.setOnAction(event -> sauvegarder());
            //Récupération d'image pour nos boutons
            Image imageSauvegarder = new Image(getClass().getResourceAsStream("/application/controlleur/sauvegarder.png"));
            // On définit une ImageView afin de pouvoir mettre l'image dans le bouton
            ImageView imageViewSauvegarder = new ImageView(imageSauvegarder);
            imageViewSauvegarder.setFitWidth(30);
            imageViewSauvegarder.setFitHeight(30);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(-1.0);
            colorAdjust.setBrightness(1.0);
            imageViewSauvegarder.setEffect(colorAdjust);
            //Création des boutons avec leurs images
            boutonSauvegarder.setGraphic(imageViewSauvegarder);
            boutonSauvegarder.setText(""); // Désactive le texte du bouton
            boutonSauvegarder.getStyleClass().add("boutonSauvegarderAide");
        }
        if (boutonAide != null) {
            boutonAide.setOnAction(event -> afficherAide());
            //Récupération d'image pour nos boutons
            Image imageAide = new Image(getClass().getResourceAsStream("/application/controlleur/aide.png"));
            // On définit une ImageView afin de pouvoir mettre l'image dans le bouton
            ImageView imageViewAide = new ImageView(imageAide);
            imageViewAide.setFitWidth(30);
            imageViewAide.setFitHeight(30);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(-1.0);
            colorAdjust.setBrightness(1.0);
            imageViewAide.setEffect(colorAdjust);
            //Création des boutons avec leurs images
            boutonAide.setGraphic(imageViewAide);
            boutonAide.setText(""); // Désactive le texte du bouton
            boutonAide.getStyleClass().add("boutonSauvegarderAide");
        }
        
        if (boutonAjouterNote != null) {
            afficherMessageSurvol(boutonAjouterNote, MESSAGE_AJOUT_NOTE);
        }

        // Vérification de la présence des éléments fxml
        if (labelNomEtudiant != null && validerNom != null && texteNom != null && textePrenom != null) {
            // Le bouton pour changer le nom appelera la méthode de changement de nom au clic
            validerNom.setOnAction(event -> modifierNom(texteNom, textePrenom));
        }

        // Vérification de la présence des éléments fxml
        if (boutonImporterFichierProgramme != null && boutonImporterFichierRessource != null) {

            boutonImporterFichierProgramme.setOnAction(event -> selectionnerFichier(boutonImporterFichierProgramme));
            boutonImporterFichierRessource.setOnAction(event -> selectionnerFichier(boutonImporterFichierRessource));
        } 

        // Vérification de la présence des éléments fxml
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
        
        if (boutonReinitialisation != null) {
            boutonReinitialisation.setOnAction(event -> reinitialiserDonnees());
            
        }
    }

    /**
     * Appelé par le bouton sauvegarder
     * Demande la confirmation de la sauvegarde
     * Si réponse affirmative, appel la méthode de sauvegarde de GestionNote
     */
    private void sauvegarder() {
        try {
            Alert verifSauvegarde = new Alert(AlertType.CONFIRMATION);
            verifSauvegarde.setTitle("Sauvegarde");
            verifSauvegarde.setHeaderText("Cette sauvegarde va écraser les anciennes données sauvegardés");
            verifSauvegarde.setContentText("Voulez-vous vraiment sauvegarder ? ");
            verifSauvegarde.getButtonTypes().setAll(ButtonType.YES,ButtonType.CANCEL);
            verifSauvegarde.showAndWait();
            if (verifSauvegarde.getResult() == ButtonType.YES) {
                gn.serializerDonnees();
                Alert sauvegardeReussi = new Alert(AlertType.INFORMATION);
                sauvegardeReussi.setTitle("Sauvegarde réussi");
                sauvegardeReussi.setHeaderText("Les modifications ont bien été sauvegardés");
                sauvegardeReussi.showAndWait();
            }
        } catch (IOException e) {
            Alert erreurSauvegarde = new Alert(AlertType.ERROR);
            erreurSauvegarde.setTitle("Sauvegarde impossible");
            erreurSauvegarde.setHeaderText(e.getMessage());
            erreurSauvegarde.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Affiche une fenêtre Popup renseignant les aides
     *
     */
    private void afficherAide() {
        Alert boiteAide = new Alert(AlertType.INFORMATION);
        boiteAide.setTitle("Aide");
        boiteAide.setHeaderText("Les aides sont affichés");
        boiteAide.showAndWait();
    }

    /**
     * Cette méthode permet d'afficher dans la ScrollPane de gauche
     * de la page Enseignements les compétences actuellement importées.
     * On retrouve une option Toutes qui, si cliquée, affiche les 
     * ressources de toutes les compétences.
     * Chaque compétence est cliquable et afficheront si cliquée
     * la liste des ressources appartenant à cette compétence.
     * @param listeCompetences
     * @param scene
     */
    private void ajouterCompetence(List<Competence> listeCompetences, Scene scene) {
        
        /*
         * Entier correspondant au numéro d'index de la ligne
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
        
        /* 
         * Récupération de la grille des compétences
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
        
        /* 
         * Modification de l'ID de cette Pane pour identifier
         * si elle est cliquée ou non
         */
        ligneToutes.setId("Toutes Non Cliquée");
        
        /* Ajout du Label à la Pane*/
        ligneToutes.getChildren().add(labelToutes);
        labelToutes.setAlignment(Pos.CENTER);
        
        /* 
         * Récupération de la grille des Enseignements
         * soit la grille au milieu de la page Enseignements
         */
        GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
        
        /* Ajout de la ligne "Toutes" à la ligne d'index 0*/
        grilleCompetence.add(ligneToutes,0,0);
        
        /* Récupération de la liste de tous les enseignements */
        List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
        
        /* 
         * Ajout d'un évènement à la Pane "Toutes"
         * qui sera déclenchée.
         * Lorsque celle-çi sera cliquée, la méthode afficherEnseignements() sera déclenchée
         */
        ligneToutes.setOnMouseClicked(event -> {
            if (ligneToutes.getId().equals("Toutes Non Cliquées")) {
                ligneToutes.setId("Toutes Cliquées");
                for (Node competence : grilleCompetence.getChildren()) {
                    if (competence.getId().equals("Toutes Non Cliquées") || competence.getId().equals("Toutes Cliquées")) {
                        competence.getStyleClass().remove("paneCompetenceCliquee");
                        competence.getStyleClass().add("paneCompetenceNonCliquee");
                        //((Node) ((Pane) competence).getChildren()).getStyleClass().remove("labelCompetenceCliquee");
                        //((Node) ((Pane) competence).getChildren()).getStyleClass().add("labelCompetenceNonCliquee");
                        competence.setId("Toutes Non Cliquées");
                    } else {
                        competence.getStyleClass().remove("paneCompetenceCliquee");
                        competence.getStyleClass().add("paneCompetenceNonCliquee");
                        //((Node) ((Pane) competence).getChildren()).getStyleClass().remove("labelCompetenceNonCliquee");
                        //((Node) ((Pane) competence).getChildren()).getStyleClass().add("labelCompetenceCliquee");
                        competence.setId("Non Cliquée");
                    }
                }
                ligneToutes.getStyleClass().remove("paneCompetenceNonCliquee");
                ligneToutes.getStyleClass().add("paneCompetenceCliquee");
                //((Node) ligneToutes.getChildren()).getStyleClass().remove("labelCompetenceNonCliquee");
                //((Node) ligneToutes.getChildren()).getStyleClass().add("labelCompetenceCliquee");
            }else {
                ligneToutes.getStyleClass().remove("paneCompetenceCliquee");
                ligneToutes.getStyleClass().add("paneCompetenceNonCliquee");
                //((Node) ligneToutes.getChildren()).getStyleClass().remove("labelCompetenceCliquee");
                //((Node) ligneToutes.getChildren()).getStyleClass().add("labelCompetenceNonCliquee");
                ligneToutes.setId("Toutes Non Cliquées");
            }
            afficherEnseignements(false, grilleEnseignement, listeEnseignement , scene);
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
                        if (competences.getId().equals("Toutes Non Cliquées") || competences.getId().equals("Toutes Cliquées")) {
                            competences.getStyleClass().remove("paneCompetenceCliquee");
                            competences.getStyleClass().add("paneCompetenceNonCliquee");
                            //((Node) ((Pane) competences).getChildren()).getStyleClass().remove("labelCompetenceCliquee");
                            //((Node) ((Pane) competences).getChildren()).getStyleClass().add("labelCompetenceNonCliquee");
                            competences.setId("Toutes Non Cliquées");
                        } else {
                            competences.getStyleClass().remove("paneCompetenceCliquee");
                            competences.getStyleClass().add("paneCompetenceNonCliquee");
                            //((Node) ((Pane) competences).getChildren()).getStyleClass().remove("labelCompetenceNonCliquee");
                            //((Node) ((Pane) competences).getChildren()).getStyleClass().add("labelCompetenceCliquee");
                            competences.setId("Non Cliquée");
                        }
                    }
                    paneCompetence.getStyleClass().remove("paneCompetenceNonCliquee");
                    paneCompetence.getStyleClass().add("paneCompetenceCliquee");
                    //((Node) paneCompetence.getChildren()).getStyleClass().remove("labelCompetenceNonCliquee");
                    //((Node) paneCompetence.getChildren()).getStyleClass().add("labelCompetenceCliquee");
                } else {
                    paneCompetence.getStyleClass().remove("paneCompetenceCliquee");
                    paneCompetence.getStyleClass().add("paneCompetenceNonCliquee");
                    //((Node) paneCompetence.getChildren()).getStyleClass().remove("labelCompetenceCliquee");
                    //((Node) paneCompetence.getChildren()).getStyleClass().add("labelCompetenceNonCliquee");
                    paneCompetence.setId("Non Cliquée");
                }
                ajouterEnseignements(paneCompetence, competence.getListeEnseignements(), scene);
            });
        }
    }

    /**
     * TODO comment method role
     * @param triRessources
     * @param grilleEnseignement
     * @param listeEnseignement
     * @param scene
     */
    private void afficherEnseignements(boolean triRessources, GridPane grilleEnseignement, List<Enseignement> listeEnseignement, Scene scene) {
        int indiceEnseignement = 0;

        //Permet de mettre une taille à une ligne quand on l'ajoute
        grilleEnseignement.setVgap(10);
        grilleEnseignement.getChildren().clear();
        grilleEnseignement.getRowConstraints().clear();
        RowConstraints tailleEnseignement = new RowConstraints();
        tailleEnseignement.setMinHeight(40);
        tailleEnseignement.setPrefHeight(40);
        tailleEnseignement.setMaxHeight(40);

        if (triRessources) {
            Label labelToutes = new Label("Toutes");
            labelToutes.setPrefSize(185,30);
            labelToutes.getStyleClass().add("labelCompetence");
            Pane ligneToutes = new Pane();
            ligneToutes.setPrefSize(185,30);
            ligneToutes.setId("Toutes");
            ligneToutes.getStyleClass().add("paneCompetence");
            ligneToutes.getChildren().add(labelToutes);
            grilleEnseignement.add(ligneToutes,0,0);
            indiceEnseignement = 1;
        }
        for (Enseignement enseignement : listeEnseignement) {
            grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
            Pane paneEnseignement = new Pane();
            paneEnseignement.setPrefSize(640, 40);
            paneEnseignement.setMinSize(640, 40);
            paneEnseignement.setMaxSize(640, 40);
            GridPane.setColumnSpan(paneEnseignement, 4);
            paneEnseignement.getStyleClass().add("paneCompetence");
            paneEnseignement.setId("Non Cliquée");
            Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
            labelEnseignement.getStyleClass().add("labelCompetence");
            labelEnseignement.setPadding(new Insets(0, 5, 0, 5));
            paneEnseignement.getChildren().add(labelEnseignement);
            grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
            paneEnseignement.setOnMouseClicked(event -> afficherControle(scene, paneEnseignement, enseignement));
            indiceEnseignement++;
        }
    }

    /**
     * TODO comment method role
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
                /*
                        if (ligneNote.getId()=="Non Cliquée") {
                                labelType.getStyleClass().remove("labelType");
                                labelPoids.getStyleClass().remove("labelPoids");
                                labelRessources.getStyleClass().remove("labelRessources");
                                labelType.getStyleClass().add("labelTypeClique");
                                labelPoids.getStyleClass().add("labelPoidsClique");
                                labelRessources.getStyleClass().add("labelRessourcesClique");
                        }else {
                                labelType.getStyleClass().remove("labelTypeClique");
                                labelPoids.getStyleClass().remove("labelPoidsClique");
                                labelRessources.getStyleClass().remove("labelRessourcesClique");
                                labelType.getStyleClass().add("labelType");
                                labelPoids.getStyleClass().add("labelPoids");
                                labelRessources.getStyleClass().add("labelRessources");
                        }
                 */
                afficherControle(scene, paneEnseignement, enseignement);
            });
            indiceEnseignement++;
        }
    }

    /**
     * TODO comment method role
     * @param note
     * @param denominateur
     * @param commentaire
     */
    private void affichageModifAjoutNote(TextField note, TextField denominateur, TextArea commentaire) {
        // Applique un format de saisie spécifique aux TextField
        note.setTextFormatter(pattern("^1000$|^\\d{1,3}(\\.\\d{0,2})?$"));
        denominateur.setTextFormatter(pattern("^1000$|^\\d{1,3}?$"));
        commentaire.setWrapText(true);
        commentaire.textProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> {
            // Vérifie si la longueur du texte dépasse la limite
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
     * TODO comment method role
     *
     */
    private void ajoutRessourcesCombo() {
        ressourcesCombo.getItems().clear();
        List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
        for (Enseignement enseignement : listeEnseignement) {
            ressourcesCombo.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
        }
    }

    /**
     * TODO comment method role
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
     * TODO comment method role
     * @param combo
     * @param ressources
     */
    private void ajoutComboControle(ComboBox<String> combo, String ressources) {
        combo.getItems().clear();
        Enseignement enseignement = gn.trouverEnseignement(ressources);
        Ressource ressource = (Ressource) enseignement;
        List<Controle> listeControles = ressource.getControlesRessource();
        for (Controle controle: listeControles) {
            System.out.println(controle.getNoteControle());
            if (controle.getNoteControle() == null) {
                String affichageComboControle = controle.getIndentifiantControle().substring(controle.getIndentifiantControle().length() - 2);
                affichageComboControle += " " + controle.getTypeControle() + " " + controle.getDateControle();
                combo.getItems().add(affichageComboControle);
            }
        }
    }

    /** 
     * Sélectionne un dossier où le fichier exporté par un autre utilisateur va être réceptionné
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
     * Exporte un fichier à un autre utilisateur
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
            alert.setHeaderText("Le fichier a été envoyé avec succès");
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
     * pour que l'utilisateur puisse la transmettre à un autre utilisateur voulant lui
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
     * Cette méthode permet d'afficher un controle d'une ressource donnée
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
                labelTypeInfo.getStyleClass().add("labelCompetence");
                labelPoidsInfo.getStyleClass().add("labelCompetence");
                labelDateInfo.getStyleClass().add("labelCompetence");
                labelTypeInfo.setAlignment(Pos.CENTER);
                labelPoidsInfo.setAlignment(Pos.CENTER);
                labelDateInfo.setAlignment(Pos.CENTER);
                GridPane.setHalignment(labelTypeInfo, javafx.geometry.HPos.CENTER);
                GridPane.setHalignment(labelPoidsInfo, javafx.geometry.HPos.CENTER);
                GridPane.setHalignment(labelDateInfo, javafx.geometry.HPos.CENTER);
                GridPane.setMargin(paneInformations, new Insets(-10, 0, 0, 0));
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
                    labelType.getStyleClass().add("labelCompetence");
                    labelPoids.getStyleClass().add("labelCompetence");
                    labelDate.getStyleClass().add("labelCompetence");
                    labelType.setAlignment(Pos.CENTER);
                    labelPoids.setAlignment(Pos.CENTER);
                    labelDate.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(labelType, javafx.geometry.HPos.CENTER);
                    GridPane.setHalignment(labelPoids, javafx.geometry.HPos.CENTER);
                    GridPane.setHalignment(labelDate, javafx.geometry.HPos.CENTER);
                    //GridPane.setMargin(paneControle, new Insets(-10, 0, 0, 0));
                    grilleEnseignement.add(paneControle, 0, rowIndex + indiceControle);
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
        } else if (gn.estUneSae(enseignement)) {
            System.out.println("Sae");
        } else if (gn.estUnPortfolio(enseignement)) {
            System.out.println("Port");
        }
    }

    /**
     * Cette méthode permet l'ajout d'une note en fonction d'un index donné
     * Elle l'ajoute dans une grille et on y ajoute une note
     * avec son dénominateur, date, ressource et un commentaire si besoin
     * @param note valeur de la note que l'on veut afficher
     * @param commentaire de la note si l'utilisateur le veut
     * @param date de la note
     * @param denominateur de la note
     * @param index index de la ligne à laquelle on ajoute la note
     * @throws Exception si la note est invalide
     */
    private void ajouterNote(String note, String commentaire, String denominateur, String ressource, String controle, int index) throws Exception {

        String identifiantControle = "";
        String identifiantRessource = ressource.substring(0, 5);
        Enseignement enseignement = gn.trouverEnseignement(identifiantRessource);
        
        if (enseignement instanceof Ressource) {
            Ressource laRessource = (Ressource) enseignement;
            List<Controle> listeControles = laRessource.getControlesRessource();
            for (Controle leControle : listeControles) {
                if (leControle.getIndentifiantControle().substring(6).equals(controle.substring(0,2))) {
                    identifiantControle = leControle.getIndentifiantControle();
                }
            }

            // Ajout de la note dans le model
            try {
                gn.ajouterNoteAControle(identifiantControle, Double.parseDouble(note), Integer.parseInt(denominateur), commentaire);
                afficherNotes();
            } catch (NumberFormatException | NoteInvalideException e) {
                throw e;
            }
        } else {
            // Ajout de la note dans le model
            try {
                gn.ajouterNoteASaePortfolio(enseignement, Double.parseDouble(note), Integer.parseInt(denominateur), commentaire);
                afficherNotes();
            } catch (NumberFormatException | NoteInvalideException e) {
                throw e;
            }
        }

    }

    /**
     * Méthode qui permet de changer le nom et de l'afficher sur toute nos pages
     * L'affichage se fait dans un label présent sur toutes nos pages (sauf popUp)
     */
    public void afficherNom() {
        labelNomEtudiant.setText(gn.getUtilisateurGestionNotes());
    }

    /**
     * Cette méthode envoie au modèle le nom et prénom récupéré dans des Texfield
     * Elle appelle ensuite une méthode qui affiche le nouveau nom et prénom 
     * renvoyé par le modèle
     * @param nom est le nouveau Nom
     * @param prenom est le nouveau Prenom
     */
    public void modifierNom(TextField nom, TextField prenom) {
        String nomPrenomUtilisateurActuel = gn.getUtilisateurGestionNotes();
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
     * Cette méthode permet de récupérer la scène de la page de paramètre d'importation de données
     * Elle la charge puis l'affiche en remplacant la scène précédente
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

    /**
     * Cette méthode permet de récupérer la scène de la page de paramètre de modification d'identité
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
     * Cette méthode permet de récupérer la scène de la page où sont présentes les notes
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Affiche les notes saisies par l'utilisateur lorsque celui-ci se trouve sur
     * la page notes
     * 
     */
    private void afficherNotes() {
        ArrayList<Object> notes = gn.getNotes(); // Contient tous les contrôles, SAE, Portfolio ayant une note
        
        for (Object note : notes) {
             System.out.println(note);
        }
        // TODO aller voir github poour récupérer le code de ajouterNote
//        for (Object note : notes) {
//            /* Récupération des infos de la note */
//            if (note instanceof Controle) {
//                Controle controle = (Controle)note;
//            }
//            
//            //Création des Label que l'on va afficher dans notre page
//            Pane ligneNote = new Pane();
//            Label labelNote = new Label(note+ " / " + denominateur);
//            Label labelType = new Label(affichageControle);
//            Label labelPoids = new Label();
//            Label labelRessources = new Label(ressource);
//
//
//            //Récupération d'image pour nos boutons
//            Image imageModifier = new Image(getClass().getResourceAsStream("/application/controlleur/modifier.png"));
//            Image imageSupprimer = new Image(getClass().getResourceAsStream("/application/controlleur/supprimer.png"));
//            // On définit une ImageView afin de pouvoir mettre l'image dans le bouton
//            ImageView imageViewModifier = new ImageView(imageModifier);
//            ImageView imageViewSupprimer = new ImageView(imageSupprimer);
//
//            //Création des boutons avec leurs images
//            Button modifier = new Button();
//            modifier.setGraphic(imageViewModifier);
//            afficherMessageSurvol(modifier, MESSAGE_MODIFIER_NOTE);
//
//            Button supprimer = new Button();
//            supprimer.setGraphic(imageViewSupprimer);
//            afficherMessageSurvol(supprimer, MESSAGE_SUPPRIMER_NOTE);
//        }

    }

    /**
     * Cette méthode permet de récupérer la scène de la page de partage des paramètres
     * Elle la charge puis l'affiche en remplacant la scène précédente
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
     * Cette méthode permet de récupérer la scène de la page de paramètre de réinitialisation
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
     * Cette méthode permet de récupérer la scène de la page ou seront présentes les ressources.
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
     * Cette méthode permet d'afficher une fenêtre popUp sur laquelle on peut saisir plusieurs informations
     * On peut y saisir une note, un controle, une commentaire et une date.
     * La fenêtre popUp récupère les données saisies et, lors du clic sur le bouton de validation,
     * ajoute une note avec celles-ci
     */
    @FXML
    public void sceneAjouterNote() {
        try {
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
                        ajouterNote(note.getText(), commentaire.getText(), denominateur.getText(), ressource.getValue(), controle.getValue(), indice);
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
     * Réinitialise toutes les données de l'application
     * 
     */
    public void reinitialiserDonnees() {
        
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
            }
        } catch (ClassNotFoundException | UtilisateurInvalideException | IOException e) {
            Alert erreurReinitialisation = new Alert(AlertType.ERROR);
            erreurReinitialisation.setTitle("Données non réinitialisées");
            erreurReinitialisation.setHeaderText("Les données n'ont pas pu être réinitialisé");
            erreurReinitialisation.showAndWait();
        }
        
        
    }
    
    /**
     * Cette méthode permet d'afficher une fenêtre popUp sur lequel on peut modifier une note
     * On peut y saisir les nouvelles données pour la note, le contrôle, la commentaire et la date.
     * La fenêtre popUp récupère les données saisies et, lors du clic sur le bouton de validation,
     * Le bouton fait appel à une méthode qui modifie les données des notes de la bonne ligne
     * @param gridPane est la grille que l'on veut modifier
     * @param boutonModifier le bouton qui permet de modifier la note
     * @param note à modifier
     * @param denominateur à modifier
     * @param commentaire à modifier
     * @param ressource 
     * @param controle 
     * @param identifiantControle 
     * @param date à modifier
     */
    public void sceneModifierNote(GridPane gridPane, Button boutonModifier, String note, String denominateur, String commentaire, String ressource, String controle, String identifiantControle) {
        try {
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
            recupNote.setText(note);
            recupCommentaire.setText(commentaire);
            recupRessource.setValue(ressource);
            recupControle.setValue(controle);
            recupDenominateur.setText(denominateur);
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
                    modifierNote(gridPane, boutonModifier, nouvelleNote, nouveauCommentaire, nouveauDenominateur, recupRessource.getValue(), recupControle.getValue(), identifiantControle);
                    // Fermeture du popUp
                    popupStage.close();
                }
            });

            /* Ouvre le popUp et attend la fermeture */
            popupStage.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
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
    public void sceneSupprimerNote(GridPane gridPane, Button supprimer, String identifiantControle) {
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
                supprimerNote(gridPane, supprimer, true, identifiantControle);
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
    private void supprimerNote(GridPane gridPane, Button boutonSupprimer, boolean supprimerLigne, String identifiantControle) {
        Integer rowIndex = GridPane.getRowIndex(boutonSupprimer);
        gn.supprimerNoteAControle(identifiantControle);
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
            if (paneLigne.getId()=="Cliquée") {
                gridPane.getChildren().removeIf(node ->
                GridPane.getRowIndex(node)  != null && GridPane.getRowIndex(node).equals(rowIndex + 1));
                indice--;
                for (Node node : gridPane.getChildren()) {
                    Integer row = GridPane.getRowIndex(node);
                    if (row != null && row > rowIndex) {
                        GridPane.setRowIndex(node, row - 1);
                    }
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
                indice--;
            }
        }
    }


    /**
     * On récupère l'indice du bouton cliqué
     * Cette méthode permet de supprimer la ligne de la note que l'on veut modifier
     * On supprime donc la ligne mais sans retirer le trou
     * Puis on y rajoute la note à l'emplacement qui vient d'être surpprimé avec les données modifiés
     * @param gridPane est la grille que l'on veut modifierS
     * @param boutonModifier est le bouton cliqué dont on r"cupère l'indice de la ligne
     * @param note est la nouvelle valeur de la note
     * @param commentaire est le commentaire modifié de l'utilisateur
     * @param date est la nouvelle date à afficher 
     * @param denominateur est le nouveau dénominateur à afficher
     */
    private void modifierNote(GridPane gridPane, Button boutonModifier, String note, String commentaire, String denominateur, String ressource, String controle, String identifiantControle) {
        /* Récupération de l'index de la ligne du bouton cliqué */
        Integer rowIndex = GridPane.getRowIndex(boutonModifier);
        /* Supression de la note mais pas suppression de la ligne */
        try {
            gn.modifierNoteAControle(identifiantControle, Double.parseDouble(note), Integer.parseInt(denominateur), commentaire);
            changerSceneNotes(); // refersh de la page
        } catch (NumberFormatException | NoteInvalideException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            /* Récupération du fichier qu'on veut charger */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/vue/PageAjouterControle.fxml"));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Ajouter Controle");
            Scene popupScene = new Scene(root);
            popupStage.setScene(popupScene);
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

    private void ajoutRessourcesComboControle() {
        comboRessourcesControle.getItems().clear();
        List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
        for (Enseignement enseignement : listeEnseignement) {
            comboRessourcesControle.getItems().add(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
        }
    }

    /*
	private String choixComboRessourcesControle(ComboBox<String> combo) {
		String[] choixCombo = new String[1];
	    combo.valueProperty().addListener((observable, oldValue, newValue) -> {
	        choixCombo[0] = combo.getValue().substring(0, 5);
	        System.out.println("Combo : " + combo.getValue());
	    });
	    System.out.println(choixCombo[0]);
	    return choixCombo[0];
	}
     */


    /**
     * 
     * @param gridPane
     * @param ligneCliquée
     * @param commentaire
     * @param date
     */
    private void afficherCommentaire(GridPane gridPane, Pane ligneCliquée, String commentaire, String date) {
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
            indice++;
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
            indice--;
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
                    importationReussi.setTitle("Importation réussie");
                    importationReussi.setHeaderText("Les données ont été importées");
                    importationReussi.showAndWait();
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

    private void afficherMessageSurvol(Button bouton, String message) {
        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(message);
        javafx.scene.control.Tooltip.install(bouton, tooltip);
    }
}
