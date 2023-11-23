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
import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import application.model.Competence;
import application.model.Controle;
import application.model.Enseignement;
import application.model.GestionNotes;
import application.model.Ressource;
import application.model.exception.CompetenceInvalideException;
import application.model.exception.ControleInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.IpException;
import application.model.exception.PortReseauException;
import application.model.exception.SemestreInvalideExecption;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
     * Méthode permmettant de lancer d'autres méthodes ou des attributs
     * directement au lancement de l'application
     */
    public void initialize() {
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

    private void afficherAide() {
        Alert boiteAide = new Alert(AlertType.INFORMATION);
        boiteAide.setTitle("Aide");
        boiteAide.setHeaderText("Les aides sont affichés");
        boiteAide.showAndWait();
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

    private void ajouterCompetence(List<Competence> listeCompetences, Scene scene) {
        int indiceCompetence = 1;
        int maxCaractere = 25;
        //Permet de mettre un taille à une ligne quand on l'ajoute
        RowConstraints taille = new RowConstraints();
        taille.setPrefHeight(50);
        GridPane grilleCompetence = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(4)).getContent());
        Label labelToutes = new Label("Toutes");
        labelToutes.setPrefSize(185,30);
        labelToutes.getStyleClass().add("labelCompetence");
        Pane ligneToutes = new Pane();
        ligneToutes.getStyleClass().add("paneCompetence");
        ligneToutes.setPrefSize(185,30);
        ligneToutes.setId("Toutes");
        ligneToutes.getChildren().add(labelToutes);
        labelToutes.setAlignment(Pos.CENTER);
        GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
        grilleCompetence.add(ligneToutes,0,0);
        List<Enseignement> listeEnseignement = gn.getSemestreGestionNotes().getEnseignementsSemestre();
        ligneToutes.setOnMouseClicked(event -> afficherEnseignements(false, grilleEnseignement, listeEnseignement , scene));

        for(Competence competence: gn.getSemestreGestionNotes().getCompetencesSemestre()) {

            String texteCompetence = competence.getIdentifiantCompetence() + " " + competence.getIntituleCompetence(); // Votre texte long à afficher
            Text texte = new Text(texteCompetence);
            texte.setWrappingWidth(maxCaractere * 7); // La largeur de l'espace pour un nombre de caractères
            Pane paneCompetence = new Pane();
            paneCompetence.getChildren().add(texte);
            texte.setTranslateY(5);
            grilleCompetence.getRowConstraints().addAll(taille);

            paneCompetence.getStyleClass().add("paneCompetence");

            paneCompetence.setPrefSize(185, 40);

            texte.getStyleClass().add("labelCompetence");

            texte.setLayoutY(10);

            paneCompetence.setPadding(new Insets(0,5,0,5));

            GridPane.setHalignment(texte, javafx.geometry.HPos.CENTER);

            texte.setTextAlignment(TextAlignment.CENTER);

            GridPane.setHalignment(paneCompetence, javafx.geometry.HPos.CENTER);

            grilleCompetence.add(paneCompetence, 0, indiceCompetence);

            indiceCompetence++;
            paneCompetence.setOnMouseClicked(event -> ajouterEnseignements(paneCompetence, competence.getListeEnseignements(), scene));
        }
    }

    /**
     * @param 
     *
     */
    private void afficherEnseignements(boolean triRessources, GridPane grilleEnseignement, List<Enseignement> listeEnseignement, Scene scene) {
        int indiceEnseignement = 0;
        if (triRessources) {
            Label labelToutes = new Label("Toutes");
            labelToutes.setPrefSize(185,30);
            labelToutes.setStyle("-fx-background-color: #E6E9F0; -fx-text-fill:#354B85; -fx-font-size: 14px; -fx-font-weight: bold;");
            Pane ligneToutes = new Pane();
            ligneToutes.setPrefSize(185,30);
            ligneToutes.setId("Toutes");
            ligneToutes.getChildren().add(labelToutes);
            grilleEnseignement.add(ligneToutes,0,0);
            indiceEnseignement = 1;
        }

        //Permet de mettre une taille à une ligne quand on l'ajoute
        grilleEnseignement.setVgap(10);
        grilleEnseignement.getChildren().clear();
        grilleEnseignement.getRowConstraints().clear();
        RowConstraints tailleEnseignement = new RowConstraints();
        tailleEnseignement.setMinHeight(40);
        tailleEnseignement.setPrefHeight(40);
        tailleEnseignement.setMaxHeight(40);
        for (Enseignement enseignement : listeEnseignement) {
            grilleEnseignement.getRowConstraints().addAll(tailleEnseignement);
            Pane paneEnseignement = new Pane();
            paneEnseignement.setPrefSize(640, 40);
            GridPane.setColumnSpan(paneEnseignement, 4);
            paneEnseignement.getStyleClass().add("paneCompetence");
            Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
            paneEnseignement.setPrefSize(630, 40);
            labelEnseignement.getStyleClass().add("labelCompetence");
            labelEnseignement.setPadding(new Insets(0, 5, 0, 5));
            paneEnseignement.getChildren().add(labelEnseignement);
            grilleEnseignement.add(paneEnseignement, 0, indiceEnseignement);
            paneEnseignement.setOnMouseClicked(event -> afficherControle(scene, paneEnseignement, enseignement));
            indiceEnseignement++;
        }
    }

    /**
     * @param hashMap 
     *
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
            GridPane.setColumnSpan(paneEnseignement, 4);
            paneEnseignement.getStyleClass().add("paneEnseignement");
            paneEnseignement.setId("Non Cliquée");
            Label labelEnseignement = new Label(enseignement.getIdentifiantEnseignement() + " " + enseignement.getIntituleEnseignement());
            paneEnseignement.setPrefSize(630, 40);
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
     * 
     */
    private void afficherControle(Scene scene, Pane enseignementSelectionne, Enseignement enseignement) {
        GridPane grilleEnseignement = ((GridPane)((ScrollPane) ((Pane) ((BorderPane) scene.getRoot()).getChildren().get(1)).getChildren().get(0)).getContent());
        if (gn.estUneRessource(enseignement)) {
            Ressource ressource = (Ressource) enseignement;
            Integer rowIndex = GridPane.getRowIndex(enseignementSelectionne);
            if (enseignementSelectionne.getId()=="Non Cliquée") {
                System.out.println("Ici");
                for (Node node : grilleEnseignement.getChildren()) {
                    Integer row = GridPane.getRowIndex(node);
                    if (row != null && row > rowIndex) {
                        GridPane.setRowIndex(node, row + ressource.getControlesRessource().size());
                    }
                }
                int indiceControle = 1;
                for (Controle controle : ressource.getControlesRessource()) {
                    enseignementSelectionne.setId("Cliquée");
                    Pane paneControle = new Pane();
                    GridPane.setColumnSpan(paneControle, 4);
                    Label labelType = new Label(controle.getTypeControle());
                    Label labelPoids = new Label("" + controle.getPoidsControle());
                    Label labelDate = new Label(controle.getDateControle());
                    paneControle.getChildren().add(labelType);
                    paneControle.getChildren().add(labelPoids);
                    paneControle.getChildren().add(labelDate);
                    paneControle.getStyleClass().add("paneControle");
                    paneControle.setPrefSize(640, 40);
                    paneControle.setMinSize(640, 40);
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
                    GridPane.setMargin(paneControle, new Insets(-10, 0, 0, 0));
                    grilleEnseignement.add(paneControle, 0, rowIndex + indiceControle);
                    indiceControle++;
                }
            }else {
                enseignementSelectionne.setId("Non Cliquée");
                for (int indiceControle = 1;indiceControle <= ressource.getControlesRessource().size();indiceControle++) {
                    int indice = indiceControle;
                    grilleEnseignement.getChildren().removeIf(node ->
                    GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node).equals(rowIndex + indice));
                }
                for (Node node : grilleEnseignement.getChildren()) {
                    Integer row = GridPane.getRowIndex(node);
                    if (row != null && row > rowIndex+1) {
                        GridPane.setRowIndex(node, row - ressource.getControlesRessource().size());
                    }
                    indiceEnseignement--;
                }
            }
        }else if (gn.estUneSae(enseignement)) {
            System.out.println("Sae");
        }else if (gn.estUnPortfolio(enseignement)) {
            System.out.println("Port");
        }
    }



    /**
     * Cette méthode permet l'ajout d'une note en fonction d'un index donné
     * Elle l'ajoute dans une grille et on y ajoute une note
     * avec son dénominateur, date, ressource et un commentaire si besoin
     * @param note est la valeur de la note que l'on veut afficher
     * @param commentaire de la note si l'utilisateur le veut
     * @param date de la note
     * @param denominateur de la note
     * @param index est l'index de la ligne à laquelle on ajoute la note
     */
    private void ajouterNote(String note, String commentaire, String denominateur, int index) {

        //Création des Label que l'on va afficher dans notre page
        Pane ligneNote = new Pane();
        Label labelNote = new Label(note+ " / " + denominateur);
        Label labelType = new Label("Type");
        Label labelPoids = new Label("Poids");
        Label labelRessources = new Label("Ressource");


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

        //Récupération de la grille à laquelle on veut ajouter les notes
        GridPane mainGridPane = (GridPane) ((ScrollPane) ((Pane) ((BorderPane) listeNotes.getScene().getRoot()).getChildren().get(1)).getChildren().get(2)).getContent();
        // Ajout des Labels créé precedemment dans la grille avec l'index correspondant
        ligneNote.setPrefSize(640, 40);
        mainGridPane.add(labelNote, 0, index);
        mainGridPane.add(labelType, 1, index);
        mainGridPane.add(labelPoids, 2, index);
        mainGridPane.add(labelRessources, 3, index);
        mainGridPane.add(modifier, 4, index);
        mainGridPane.add(supprimer, 5, index);
        mainGridPane.add(ligneNote, 0, index);
        GridPane.setColumnSpan(ligneNote, 4);
        ligneNote.setOpacity(0); // La ligneNote est transparente
        ligneNote.setMouseTransparent(false); // La ligneNote réagira aux événements de souris
        //Ligne non cliquée par défaut donc en rouge
        ligneNote.setId("Non Cliquée");
        ligneNote.setOnMouseClicked(event -> {
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
            afficherCommentaire(mainGridPane, ligneNote, commentaire, "date");
        });


        //Permet de mettre un taille à une ligne quand on l'ajoute
        RowConstraints taille = new RowConstraints();
        taille.setPrefHeight(40);
        mainGridPane.getRowConstraints().addAll(taille);
        // Taille des boutons
        modifier.setMaxSize(30, 30);
        supprimer.setMaxSize(30, 30);
        // Action des boutons
        supprimer.setOnAction(event -> sceneSupprimerNote(mainGridPane, supprimer));
        modifier.setOnAction(event -> sceneModifierNote(mainGridPane,modifier,note,denominateur,commentaire));
        //In crement de l'indice (correspondant à l'index afin d'ajout la ligne en dessous de la dernière
        indice ++;

        // Alignement des Label au centre de leur emplacement
        GridPane.setHalignment(labelNote, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(modifier, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(supprimer, javafx.geometry.HPos.CENTER);

        // Gestion du style de nos Labels et boutons
        labelNote.getStyleClass().add("labelNote");
        labelType.getStyleClass().add("labelType");
        labelPoids.getStyleClass().add("labelPoids");
        labelRessources.getStyleClass().add("labelRessources");

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

    }

    /**
     * Méthode qui permet de changer le nom et de l'afficher sur toute nos pages
     * L'affichage se fait dans un label présent sur toutes nos pages (sauf popUp)
     */
    private void afficherNom() {
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
            GridPane grilleRessources = (GridPane)((ScrollPane) ((Pane)((BorderPane) nouvelleSceneObjet.getRoot()).getChildren().get(1)).getChildren().get(4)).getContent();
            afficherEnseignements(true, grilleRessources, gn.getSemestreGestionNotes().getEnseignementsSemestre(), nouvelleSceneObjet);
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
            //TextField date = (TextField) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
            TextField denominateur = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(2));

            boutonValider.setOnAction(e -> {
                if (!note.getText().isEmpty() && !denominateur.getText().isEmpty() && Double.parseDouble(note.getText()) <= Double.parseDouble(denominateur.getText())) {
                    ajouterNote(note.getText(), commentaire.getText(), denominateur.getText(), indice);
                    popupStage.close();
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
     * @param date à modifier
     */
    public void sceneModifierNote(GridPane gridPane, Button boutonModifier, String note, String denominateur, String commentaire) {
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
            //TextField recupDate = (TextField) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
            TextField recupDenominateur = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(2));
            //On affiche dans les TextField les informations déja saisies par l'utilisateur
            recupNote.setText(note);
            recupCommentaire.setText(commentaire);
            //recupDate.setText(date);
            recupDenominateur.setText(denominateur);

            boutonValider.setOnAction(e -> {
                //Vérification que la note est bien inférieure ou égale au dénominateur
                if (!recupNote.getText().isEmpty() && !recupDenominateur.getText().isEmpty() 
                        && Double.parseDouble(recupNote.getText()) <= Double.parseDouble(recupDenominateur.getText())) {
                    // Récupérer les nouvelles informations saisies
                    String nouvelleNote = recupNote.getText();
                    String nouveauCommentaire = recupCommentaire.getText();
                    //String nouvelleDate = recupDate.getText();
                    String nouveauDenominateur = recupDenominateur.getText();
                    // Modifier les informations dans la GridPane
                    modifierNote(gridPane, boutonModifier, nouvelleNote, nouveauCommentaire, nouveauDenominateur);
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
     */
    public void sceneSupprimerNote(GridPane gridPane, Button supprimer) {
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
                supprimerNote(gridPane, supprimer, true);
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
    private void supprimerNote(GridPane gridPane, Button boutonSupprimer, boolean supprimerLigne) {
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
    private void modifierNote(GridPane gridPane, Button boutonModifier, String note, String commentaire, String denominateur) {
        /* Récupération de l'index de la ligne du bouton cliqué */
        Integer rowIndex = GridPane.getRowIndex(boutonModifier);
        /* Supression de la note mais pas suppression de la ligne */
        supprimerNote(gridPane, boutonModifier, false);
        /* Ajout d'une note à la place de l'ancienne */
        System.out.println(note + "\n" + commentaire + "\n" + denominateur + "\n" + rowIndex);
        ajouterNote(note, commentaire, denominateur, rowIndex);
    }

    /**
     * Cette méthode permet d'afficher un popUp sur lequel on peut saisir plusieurs informations
     * On peut y saisir une note, un controle, une commentaire et une date.
     * Le popUp récupère les données saisies et lors du clic sur le bouton de validation
     * On y ajoute une note avec les données saisies dans le popUp
     */
    @FXML
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
            TextField note = (TextField)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(0);
            TextArea commentaire = (TextArea)((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(3)).getChildren().get(0);
            //TextField date = (TextField) (((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(4)).getChildren().get(0));
            TextField denominateur = (TextField)(((Pane) ((GridPane) (popupScene.getRoot().getChildrenUnmodifiable()).get(0)).getChildren().get(1)).getChildren().get(2));
            boutonValider.setOnAction(e -> {
                if (!note.getText().isEmpty() && !denominateur.getText().isEmpty() && Double.parseDouble(note.getText()) <= Double.parseDouble(denominateur.getText())) {
                    ajouterNote(note.getText(), commentaire.getText(), denominateur.getText(), indice);
                    popupStage.close();
                }
            });
            /* Ouvre le popUp et attend sa fermeture */
            popupStage.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }



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
     * 
     * @param sceneActuelle
     */
    public static void creerFiltres(Scene sceneActuelle) {
        GridPane gridPaneRessources = (GridPane)((ScrollPane) ((Pane)((BorderPane) sceneActuelle.getRoot()).getChildren().get(1)).getChildren().get(4)).getContent();

        Label labelToutes = new Label("Toutes");
        labelToutes.setPrefSize(185,30);
        labelToutes.setStyle("-fx-background-color: #E6E9F0; -fx-text-fill:#354B85; -fx-font-size: 14px; -fx-font-weight: bold;");

        Pane ligneToutes = new Pane();
        ligneToutes.setPrefSize(185,30);
        ligneToutes.setId("Toutes");
        ligneToutes.getChildren().add(labelToutes);

        gridPaneRessources.add(ligneToutes,0,0);
    }
    private void filtrerRessource(Pane ressourceSelectionnée) {
        if (ressourceSelectionnée.getId() == "Toutes") {

        }else {

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
                    importationReussi.setTitle("Importation réussi");
                    importationReussi.setHeaderText("Importation réussi");
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