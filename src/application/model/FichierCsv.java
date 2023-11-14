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

        String nomNouveauFichier;
        String cheminFichierDuplique;
        
        File fichierDuplique;
        File repertoireFichier;
                
        do {
            // génération d'un nouveau nom aléatoire
            nomNouveauFichier = typeDeFichier + "-" + NomFichierDuplique() + EXTENSION_FICHIER;
            cheminFichierDuplique = REPERTOIRE_SAUVEGARDE + nomNouveauFichier;
            fichierDuplique = new File(cheminFichierDuplique);
            // continue si le fichier existe déjà dans le répertoire
        } while (fichierDuplique.exists());     

        // création de l'instance du répertoire "./csv/" dans lequel le fichier
        // sera sauvegardé
        repertoireFichier = new File(REPERTOIRE_SAUVEGARDE);
        // création du répertoire si celui ci n'existe pas
        repertoireFichier.mkdirs();

        // sauvegarde du fichier dans sa nouvelle destination
        // impression du contenu du fichier source pour le sauvegarder
        try (FileChannel fichierSource
                = new FileInputStream(fichier).getChannel();
             // écoute du fichier de sauvegarde pour la duplication du contenu
             // du fichier source
             FileChannel destinationSource
                 = new FileOutputStream(fichierDuplique).getChannel()) {
            // transfert du contenu du fichier source vers la sauvegarde
            destinationSource.transferFrom(fichierSource, 0,
                                           fichierSource.size());
            
            return new File(cheminFichierDuplique);
    
        } catch (IOException e) {
            throw new CopieFichierException("Le fichier n'a pas pu être sauvegardé");
        }
    }
    
    /**
     * Génère une chaine de 12 caractères aléatoire pour la création d'un
     * nouveau nom de fichier lors de la sauvegarde
     * @return le nouveau nom du fichier sauvegarde
     */
    private static String NomFichierDuplique() {
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
        FichierCsv fichier = new FichierCsv('p', "Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\jdlezvzfrluv.csv");
        FichierCsv fichier1 = new FichierCsv('r', "Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\jdlezvzfrluv.csv");
        fichier.setDelimiteurFichier(";");
        analyserFichier(fichier.lireFichier());
    }
}