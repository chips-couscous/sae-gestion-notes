/* 
 * FichierCsv.java                                                    20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model;

import application.model.exception.ExtensionFichierException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/** 
 * Class permettant de réaliser des actions sur un fichier CSV (.csv)
 * <h3> Actions réalisables </h3>
 * <ul>
 *     <li> ouvrir le fichier </li>
 *     <li> lire le fichier </li>
 *     <li> analyser les délimiteurs </li>
 *     <li> extraires les données </li>
 * </ul>
 * 
 * @author thomas.lemaire
 */
public class FichierCsv {
    
    // Délimiteur de fichier CSV (.csv) par défaut
    private final static String DELIMITEUR_DEFAUT = ",";
        
    private static String cheminFichier;
   
    private String delimiteurFichier;
    
    private File fichier;
    
    /**
     * Constructeur d'un fichier CSV (.csv) en ayant le chemin connu.
     * @param chemin , le chemin du fichier
     * @throws ExtensionFichierException 
     */
    public FichierCsv(String chemin) throws ExtensionFichierException {
        if (!fichierEstValide(chemin)) {
            throw new ExtensionFichierException("Fichier non valide.");
        }
        cheminFichier = chemin;
        fichier = new File(chemin);
        delimiteurFichier = DELIMITEUR_DEFAUT;
    }
    
    /**
     * Cette fonction permet de créer une frame d'explorateur de fichier Windows
     * qui permet à l'utilisateur de sélectionner un fichier CSV qui contient
     * les paramètres du semestre qu'il souhaite importer.
     * @return Le chemin du fichier selectionné
     */
    public static String trouverCheminFichier() {
        try {
            // Choix de l'apparence de la fenêtre Windows
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créez un objet JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Personnalisez le titre de la boîte de dialogue
        fileChooser.setDialogTitle("Sélectionner un fichier CSV");

        // Créez un filtre pour les fichiers .csv
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Fichiers CSV (*.csv)", "csv");
        fileChooser.setFileFilter(filter);

        // Désactivez la possibilité de sélectionner tous les fichiers
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Affichez la boîte de dialogue de sélection de fichier
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // L'utilisateur a sélectionné un fichier
            File selectedFile = fileChooser.getSelectedFile();
            String chemin = selectedFile.getAbsolutePath();
            setCheminFichier(chemin);
            return chemin;
        }
        
        return null;
    }
    
    /**
     * Permet à partir d'un chemin donné de lire un fichier CSV et d'en soustraire
     * ligne par ligne les cellules.
     * @return true
     */
    private List<String[]> lireFichier() {
                
        // Contenu du fichier ligne par ligne
        List<String[]> contenuFichier = new ArrayList<>();

        // Lecture du fichier
        try (BufferedReader lecteur
             = new BufferedReader(new FileReader(cheminFichier))) {
            
            // Délimitation des rangées
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                // Délimitation des cellules dans les rangées
                String[] rangee = ligne.split(DELIMITEUR_DEFAUT);
                contenuFichier.add(rangee);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        return contenuFichier;
    }
    
    /**
     * Vérifie la validité d'un fichier, on vérifie si il existe et si
     * ce fichier est bien un fichier au format CSV (.csv).
     * @param chemin , le chemin du fichier
     * @return true si le chemin du fichier est valide, false sinon.
     */
    private boolean fichierEstValide(String chemin) {
        
        File fichier = new File(cheminFichier);

        // Vérifie si le fichier existe
        if (fichier.exists()) {
            String nomFichier = fichier.getName();
            int dernierPointIndex = nomFichier.lastIndexOf('.');

            if (dernierPointIndex > 0) {
                // Récupère l'extension du fichier existant
                String extension = nomFichier.substring(dernierPointIndex + 1);
                if (extension.equals("csv")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Sauvegarder le fichier importé pour l'avoir dans les fichiers de l'application
     * @return true si le fichier s'est bien sauvegardé, false sinon
     */
    private boolean sauvegarderFichier() {

        String nomNouveauFichier;
        String cheminFichierDuplique;
        
        File fichierDuplique;
        File repertoireFichier;
        File destinationFichier;
                
        do {
            // génération d'un nouveau nom aléatoire
            nomNouveauFichier = NomFichierDuplique();
            cheminFichierDuplique = "./csv/" + nomNouveauFichier + ".csv";
            fichierDuplique = new File(cheminFichierDuplique);
            // continue si le fichier existe déjà dans le répertoire
        } while (fichierDuplique.exists());     

        // ajout de l'extension CSV (.csv) dans le nom du fichier
        nomNouveauFichier += ".csv";
        // création de l'instance du répertoire "./csv/" dans lequel le fichier
        // sera sauvegardé
        repertoireFichier = new File("./csv/");
        // création du répertoire si celui ci n'existe pas
        repertoireFichier.mkdirs();
        // création de la destination du fichier à sauvegardé
        destinationFichier = new File(repertoireFichier, nomNouveauFichier);

        // sauvegarde du fichier dans sa nouvelle destination
        // impression du contenu du fichier source pour le sauvegarder
        try (FileChannel fichierSource
                = new FileInputStream(fichier).getChannel();
             // écoute du fichier de sauvegarde pour la duplication du contenu
             // du fichier source
             FileChannel destinationSource
                 = new FileOutputStream(destinationFichier).getChannel()) {
            // transfert du contenu du fichier source vers la sauvegarde
            destinationSource.transferFrom(fichierSource, 0,
                                           fichierSource.size());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * Génère une chaine de 12 caractères aléatoire pour la création d'un
     * nouveau nom de fichier lors de la sauvegarde
     * @return le nouveau nom du fichier sauvegarde
     */
    private String NomFichierDuplique() {
        // taille de la chaine de caractère généré
        final int TAILLE_NOM = 12;
        // nom du fichier sauvegardé
        StringBuilder nouveauNomFichier = new StringBuilder();

        // instance d'objet pour tirer un entier aléatoire
        Random aleatoire = new Random();
        // caractères utilisés pour la génération du nouveau nom de fichier
        String ensembleLettres = "abcdefghijklmnopqrstuvwxyz";
        
        // génération du nom de fichier
        for (int i = 0; i < TAILLE_NOM; i++) {
            // entier aléatoire dans la dimension des caractères utilisés
            int indexAleatoire = aleatoire.nextInt(ensembleLettres.length());
            // caractère pris aléatoirement dans son ensemble
            char charAleatoire = ensembleLettres.charAt(indexAleatoire);
            // ajout du caractère choisi dans le nom du fichier
            nouveauNomFichier.append(charAleatoire);
        }

        return nouveauNomFichier.toString();
    }
    
    /** @return valeur de cheminFichier */
    public static String getCheminFichier() {
        return cheminFichier;
    }

    /** @param cheminFichier nouvelle valeur de cheminFichier */
    public static void setCheminFichier(String cheminFichier) {
        FichierCsv.cheminFichier = cheminFichier;
    }
    
    /**
     * TODO comment method role
     * @param args
     * @throws ExtensionFichierException 
     */
    public static void main(String args[]) throws ExtensionFichierException {
        FichierCsv fichier = new FichierCsv(trouverCheminFichier());
        fichier.lireFichier();
        fichier.sauvegarderFichier();
    }
}
