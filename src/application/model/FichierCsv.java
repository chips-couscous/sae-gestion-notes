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
import java.util.ArrayList;
import java.util.List;

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
    
    // Chemin par défaut d'un fichier avec un chemin non connu
    private final static String CHEMIN_DEFAUT = "";
    // Délimiteur de fichier CSV (.csv) par défaut
    private final static String DELIMITEUR_DEFAUT = ",";
        
    String cheminFichier;
    String delimiteurFichier;
    
    File fichier;
    
    /** 
     * Constructeur d'un fichier CSV (.csv)
     */
    public FichierCsv() {
        cheminFichier = CHEMIN_DEFAUT;
        delimiteurFichier = DELIMITEUR_DEFAUT;
    }

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
        delimiteurFichier = DELIMITEUR_DEFAUT;
    }
    
    /**
     * Cette fonction permet de créer une frame d'explorateur de fichier Windows
     * qui permet à l'utilisateur de sélectionner un fichier CSV qui contient
     * les paramètres du semestre qu'il souhaite importer.
     * @return Le chemin du fichier selectionné
     */
    private static String ouvrirFichier() {
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
            return selectedFile.getAbsolutePath();
        }
        
        return null;
    }
    
    /**
     * Permet à partir d'un chemin donné de lire un fichier CSV et d'en soustraire
     * ligne par ligne les cellules.
     * @param chemin , chemin du fichier à lire
     * @return true
     */
    private static List<String[]> lireFichier(String chemin) {
                
        // Contenu du fichier ligne par ligne
        List<String[]> contenuFichier = new ArrayList<>();

        // Lecture du fichier
        try 
        (BufferedReader lecteur = new BufferedReader(new FileReader(chemin))) {
            
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
                if (extension != "csv") {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
