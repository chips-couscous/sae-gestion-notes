/*
 * FichierSemestre.java                                      9 nov. 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ExtensionFichierException;
import application.model.exception.ParametresSemestreException;

/**
 * TODO comment class responsibility (SRP)
 * 
 * @author thomas.lemaire
 *
 */
public class FichierSemestre extends FichierCsv {
    
    private Semestre semestre;

    /**
     * TODO comment intial state
     * 
     * @param chemin
     * @throws ExtensionFichierException
     */
    public FichierSemestre(String chemin) throws ExtensionFichierException {
        super('s', chemin);
    }

    protected void lireFichier() {
        super.lireFichier();
    }

    /**
     * TODO comment method role
     * @throws ParametresSemestreException 
     *
     */
    private void decomposerFichier() throws ParametresSemestreException {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                switch (contenuFichier.get(i)[0]) {
                case "Semestre": {
                    int numeroSemestre = Integer.parseInt(contenuFichier.get(i)[1]);
                    semestre = new Semestre(numeroSemestre, contenuFichier.get(i+1)[1]);
                    break; 
                }
                case "Compétence": {
                    i = decomposerCompetence(i);
                    break;
                }
                default:
                    break;
                // throw new IllegalArgumentException("Unexpected value: " +
                // contenuFichier.get(i)[0]);
                }
            }
        }
        
        System.out.println(semestre.toString());
    }

    /**
     * TODO comment method role
     * 
     * @return 0
     */
    private int decomposerCompetence(int numeroDeLigne) {
        String[] competence = contenuFichier.get(numeroDeLigne);
        Competence competenceAAjouter = new Competence(competence[2], competence[1]);

        int ligneEnseignement = numeroDeLigne;
        ligneEnseignement++;
        String[] enseignementADecomposer;
        
        boolean finCompetence = false;

        do {
            ligneEnseignement++;
            enseignementADecomposer = contenuFichier.get(ligneEnseignement);

            if (enseignementADecomposer.length > 0 && contenuFichier.size() > ligneEnseignement+1) {
                
                
                Enseignement compositionEnseignement;
                Enseignement enseignementAAjouter;
                int poidsEnseignement;
                
                enseignementAAjouter = semestre.verifierEnseignementPresent(enseignementADecomposer[1]);
                
                if(enseignementAAjouter == null) {
                    enseignementAAjouter = decomposerEnseignement(enseignementADecomposer[0], enseignementADecomposer[1],
                            enseignementADecomposer[2]);
                    semestre.ajouterEnseignement(enseignementAAjouter);
                }
                
                poidsEnseignement = (int) Integer.parseInt(enseignementADecomposer[3]);
                semestre.ajouterCompetenceAEnseignement(enseignementAAjouter, competenceAAjouter, poidsEnseignement);
            } else {
                finCompetence = true;
            }
            
        } while (!finCompetence);

        return ligneEnseignement;
    }

    /**
     * TODO comment method role
     * 
     * @param typeEvaluation
     * @param identifiant
     * @param libelle
     * @return 0
     */
    private Enseignement decomposerEnseignement(String typeEvaluation, String identifiant, String libelle) {
        Enseignement enseignement;

        switch (typeEvaluation) {
        case "Ressource": {
            enseignement = new Ressource(libelle, identifiant);
            break;
        }
        case "Portfolio": {
            enseignement = new Portfolio(libelle, identifiant);
            break;
        }
        case "SAE": {
            enseignement = new Sae(libelle, identifiant);
            break;
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + typeEvaluation);
        }
        
        return enseignement;
    }

    /**
     * TODO comment method role
     * 
     * @param args
     * @throws ExtensionFichierException
     * @throws ParametresSemestreException 
     */
    public static void main(String args[]) throws ExtensionFichierException, ParametresSemestreException {
        FichierSemestre fichier = new FichierSemestre("Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\parametrage-sae.csv");
        fichier.setDelimiteurFichier(";");
        fichier.lireFichier();
        fichier.decomposerFichier();
    }
}
