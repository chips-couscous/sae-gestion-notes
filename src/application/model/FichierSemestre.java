/*
 * FichierSemestre.java                                              9 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ExtensionFichierException;
import application.model.exception.ParametresSemestreException;

/**
 * TODO comment class responsibility (SRP)
 * 
 * @author thomas.lemaire
 * @version 1.0
 */
public class FichierSemestre extends FichierCsv implements Serializable {
    
    private int numeroSemestre;
    
    private String parcoursSemestre;
    
    private HashMap<String[], List<String[]>> competencesEnseignements;
    
    /**
     * Constructeur d'un fichier semestre
     * @param chemin du fichier a importer
     * @throws ExtensionFichierException
     */
    public FichierSemestre(String chemin) throws ExtensionFichierException {
        super('s', chemin);
        competencesEnseignements = new HashMap<String[], List<String[]>>();
    }

    /**
     * TODO comment method role
     * @return les données du fichier importé
     * @throws ParametresSemestreException 
     */
    public HashMap<String[], List<String[]>> decomposerFichier() {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                
                String typeCelluleCsv = contenuFichier.get(i)[0];
                
                switch (typeCelluleCsv) {
                case "Semestre": {
                    setNumeroSemestre(Integer.parseInt(contenuFichier.get(i)[1]));
                    setParcoursSemestre(contenuFichier.get(i+1)[1]);
                    
                    break; 
                }
                case "Compétence": {
                    i = decomposerCompetence(i);
                    break;
                }
                default:
                    break;
                }
            }
        }
        
        return competencesEnseignements;
    }

    /**
     * Décompose une compétence pour en soustraire ses enseignements
     * @return le numéro de ligne du fichier à lire après l'analyse des enseignements
     * de la compétence
     */
    private int decomposerCompetence(int numeroDeLigne) {
        String[] competenceADecomposer = contenuFichier.get(numeroDeLigne);

        int ligneEnseignement = numeroDeLigne;
        ligneEnseignement++;
        
        String[] enseignementADecomposer;
        List<String[]> enseignements = new ArrayList<String[]>();
        
        boolean finCompetence = false;

        do {
            ligneEnseignement++;
            enseignementADecomposer = contenuFichier.get(ligneEnseignement);

            if (enseignementADecomposer.length > 0) {
               
                enseignements.add(enseignementADecomposer);
                
            } else {
                finCompetence = true;
            }
            if (!(contenuFichier.size() > ligneEnseignement+1)) {
                finCompetence = true;
            }
            
        } while (!finCompetence);
        
        competencesEnseignements.put(competenceADecomposer, enseignements);

        return ligneEnseignement;
    }

    /** @return valeur de numeroSemestre */
    public int getNumeroSemestre() {
        return numeroSemestre;
    }

    /** @param numeroSemestre nouvelle valeur de numeroSemestre */
    public void setNumeroSemestre(int numeroSemestre) {
        this.numeroSemestre = numeroSemestre;
    }

    /** @return valeur de parcoursSemestre */
    public String getParcoursSemestre() {
        return parcoursSemestre;
    }

    /** @param parcoursSemestre nouvelle valeur de parcoursSemestre */
    public void setParcoursSemestre(String parcoursSemestre) {
        this.parcoursSemestre = parcoursSemestre;
    }
}