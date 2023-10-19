/* 
 * GestionNotes.java                                                  19.10.2023
 * © copyright Chips-Couscous, But Informatique 2, IUT de Rodez (12)
 */

package application.model;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


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
        
        analyserFichier(cheminFichier);
        
        return true; // Bouchon
    }
    
    /**
     * TODO comment method role
     * @param chemin
     * @return true
     */
    private static boolean analyserFichier(String chemin) {
        // TODO Coder la méthode
        return true; // Bouchon
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