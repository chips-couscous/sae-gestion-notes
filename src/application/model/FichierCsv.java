/* 
 * FichierCsv.java                                                    20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model;

import application.model.exception.CopieFichierException;
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
    
    // Chemin par défaut du répertoire des sauvergardes
    private final static String REPERTOIRE_SAUVEGARDE = "./csv/";
    
    private final static String EXTENSION_FICHIER = ".csv";
        
    private static String cheminFichier;
   
    private String delimiteurFichier;
    
    private File fichier;
    
    /**
     * Constructeur d'un fichier CSV (.csv) en ayant le chemin connu.
     * @param type
     * @param chemin , le chemin du fichier
     * @throws ExtensionFichierException 
     */
    public FichierCsv(char type, String chemin) throws ExtensionFichierException {
        if (!fichierEstValide(chemin)) {
            throw new ExtensionFichierException("Fichier non valide.");
        }
        
        fichier = new File(chemin);
        
        // copie du fichier
        try {
            fichier = copierFichier(type);
        } catch (CopieFichierException e) {
            e.printStackTrace();
        }
        
        cheminFichier = fichier.getAbsolutePath();
        System.out.println(cheminFichier);
        delimiteurFichier = DELIMITEUR_DEFAUT;
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
                String[] rangee = ligne.split(delimiteurFichier);
                contenuFichier.add(rangee);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        return contenuFichier;
    }
    
    /**
     * TODO comment method role
     * @param contenuFichier
     */
    private static void analyserFichier(List<String[]> contenuFichier) {
        for (int i = 0; i < contenuFichier.size() ; i++) {
            for (int j = 0; j < contenuFichier.get(i).length; j++) {
                System.out.print(contenuFichier.get(i)[j]);
            }
            System.out.println();
        }
    }
    
    /**
     * Vérifie la validité d'un fichier, on vérifie si il existe et si
     * ce fichier est bien un fichier au format CSV (.csv).
     * @param chemin , le chemin du fichier
     * @return true si le chemin du fichier est valide, false sinon.
     */
    private static boolean fichierEstValide(String chemin) {
        
        File fichier = new File(chemin);

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
     * @throws CopieFichierException 
     */
    private File copierFichier(char typeDeFichier) throws CopieFichierException {

        String nomFichierCopie;
        String cheminFichierCopie;
        
        File copieFichier;
        File repertoireCopieFichier;
        
        switch (typeDeFichier) {
        case 's':
            nomFichierCopie = "ParametresSemestre" + EXTENSION_FICHIER;
            break;
        case 'r':
            nomFichierCopie = "ParametresRessource" + EXTENSION_FICHIER;
            break;
        default:
            throw new CopieFichierException("Le type du fichier importé ne "
                            + "correspond pas à fichier ressource ou semestre");
        }
        
        cheminFichierCopie = REPERTOIRE_SAUVEGARDE + nomFichierCopie;
        copieFichier = new File(cheminFichierCopie);
        
        // création de l'instance du répertoire "./csv/" dans lequel le fichier
        // sera sauvegardé
        repertoireCopieFichier = new File(REPERTOIRE_SAUVEGARDE);
        // création du répertoire si celui ci n'existe pas
        repertoireCopieFichier.mkdirs();

        // sauvegarde du fichier dans sa nouvelle destination
        // impression du contenu du fichier source pour le sauvegarder
        try (FileChannel fichierSource
                = new FileInputStream(fichier).getChannel();
             // écoute du fichier de sauvegarde pour la duplication du contenu
             // du fichier source
             FileChannel destinationSource
                 = new FileOutputStream(copieFichier).getChannel()) {
            // transfert du contenu du fichier source vers la sauvegarde
            destinationSource.transferFrom(fichierSource, 0,
                                           fichierSource.size());
            return new File(cheminFichierCopie);    
        } catch (IOException e) {
            throw new CopieFichierException
                      ("Le fichier n'a pas pu être sauvegardé");
        }
    }
    
    /** @return valeur de fichier */
    public File getFichier() {
        return fichier;
    }

    /** @return valeur de cheminFichier */
    public static String getCheminFichier() {
        return cheminFichier;
    }
    
    /** @param delimiteurFichier nouvelle valeur de delimiteurFichier */
    public void setDelimiteurFichier(String delimiteurFichier) {
        this.delimiteurFichier = delimiteurFichier;
    }

    /**
     * TODO comment method role
     * @param args
     * @throws ExtensionFichierException 
     */
    public static void main(String args[]) throws ExtensionFichierException {
        FichierCsv fichier = new FichierCsv('s', "Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\parametrage-sae.csv");
        fichier.setDelimiteurFichier(";");
        analyserFichier(fichier.lireFichier());
    }
}