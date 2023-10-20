/* 
 * GestionNotes.java                                                  19.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GestionNotes {

    /**
     * Permet à l'utilisateur d'importer les paramètres d'un semestre à 
     * partir d'un fichier .csv.
     * D'analyser ce fichier pour en soustraire les informations principales.
     * @return true si le fichier selectionné est valide, false sinon
     */
    public static boolean importerParametre() {
        String cheminFichier = ouvrirExplorateurWindows();
        
        if (cheminFichier == null) {
            return false;
        }
        
        List<String[]> contenuFichier = lireFichier(cheminFichier);
        
        if (contenuFichier == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Permet à partir d'un chemin donné de lire un fichier CSV et d'en soustraire
     * ligne par ligne les cellules.
     * @param chemin , chemin du fichier à lire
     * @return true
     */
    private static List<String[]> lireFichier(String chemin) {
        
        // Délimiteur utilisé par le fichier csv pour séparer les cellules
        final String DELIMITEUR_CELLULE = ";";
        
        // Contenu du fichier ligne par ligne
        List<String[]> contenuFichier = new ArrayList<>();

        // Lecture du fichier
        try 
        (BufferedReader lecteur = new BufferedReader(new FileReader(chemin))) {
            
            // Délimitation des rangées
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                // Délimitation des cellules dans les rangées
                String[] rangee = ligne.split(DELIMITEUR_CELLULE);
                contenuFichier.add(rangee);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return contenuFichier;
    }
    
    /**
     * Cette fonction permet de créer une frame d'explorateur de fichier Windows
     * qui permet à l'utilisateur de sélectionner un fichier CSV qui contient
     * les paramètres du semestre qu'il souhaite importer.
     * @return Le chemin du fichier selectionné
     */
    private static String ouvrirExplorateurWindows() {
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
     * TODO comment method role
     * @param args
     */
    public static void main(String[] args) {
        importerParametre();
    }
}