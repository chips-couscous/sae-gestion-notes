/*
 * FichierRessource.java                                             9 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ExtensionFichierException;
import application.model.exception.ParametresSemestreException;

/** 
 * TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 * @version 1.0
 */
public class FichierRessource extends FichierCsv {
    
    private HashMap<String, List<String[]>> ressourcesControles;

    /**
     * Constructeur d'un fichier ressource
     * @param chemin du fichier a importer
     * @throws ExtensionFichierException
     */
    public FichierRessource(String chemin) throws ExtensionFichierException {
        super('r', chemin);
        ressourcesControles = new HashMap<String, List<String[]>>();
    }
    
    
    /**
     * TODO comment method role
     * @return les données du fichier importé
     */
    public HashMap<String, List<String[]>> decomposerFichier() {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                
                String typeCelluleCsv = contenuFichier.get(i)[0];
                
                if(typeCelluleCsv.equals("Ressource")) {
                    i = decomposerRessource(i);
                }
                
            }
        }
        
        return ressourcesControles;
    }
    
    /**
     * Décompose une ressource pour en soustraire ses contrôles
     * @return le numéro de ligne du fichier à lire après l'analyse des contrôles
     * de la ressource
     */
    private int decomposerRessource(int numeroDeLigne) {
        String ressourceADecomposer = contenuFichier.get(numeroDeLigne)[1];
        
        String[] controleADecomposer;
        List<String[]> controles = new ArrayList<String[]>();
       
        int ligneControle = numeroDeLigne;
        ligneControle++;
        
        boolean finRessource = false;

        do {
            ligneControle++;
            controleADecomposer = contenuFichier.get(ligneControle);
            
            if (controleADecomposer.length > 0) {
                
                controles.add(controleADecomposer);
                
            } else {
                finRessource = true;
            }
            if(!(contenuFichier.size() > ligneControle+1)) {
                finRessource = true;
            }
            
        } while (!finRessource);
        
        ressourcesControles.put(ressourceADecomposer, controles);
        
        return ligneControle;
    }
}