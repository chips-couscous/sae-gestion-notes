/*
 * FichierSemestre.java                                      9 nov. 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ExtensionFichierException;

/**
 * TODO comment class responsibility (SRP)
 * 
 * @author thomas.lemaire
 *
 */
public class FichierSemestre extends FichierCsv {

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
        // super.analyserFichier();
    }

    /**
     * TODO comment method role
     *
     */
    private void decomposerFichier() {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                switch (contenuFichier.get(i)[0]) {
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
    }

    /**
     * TODO comment method role
     * 
     * @return 0
     */
    private int decomposerCompetence(int numeroDeLigne) {
        String[] competence = contenuFichier.get(numeroDeLigne);

        List<Enseignement> enseignementDeCompetence = new ArrayList<Enseignement>();

        int ligneEnseignement = numeroDeLigne;
        ligneEnseignement++;
        String[] enseignementADecomposer;
        
        boolean finCompetence = false;

        do {
            ligneEnseignement++;
            enseignementADecomposer = contenuFichier.get(ligneEnseignement);

            if (enseignementADecomposer.length > 0 && contenuFichier.size() > ligneEnseignement+1) {
                Enseignement enseignementAAjouter;

                enseignementAAjouter = decomposerEnseignement(enseignementADecomposer[0], enseignementADecomposer[1],
                        enseignementADecomposer[2], enseignementADecomposer[3]);

                enseignementDeCompetence.add(enseignementAAjouter);
            } else {
                finCompetence = true;
            }
            
        } while (!finCompetence);

        Competence competenceAAjouter = new Competence(enseignementDeCompetence, competence[2], competence[1]);

        GestionNotes.ajouterCompetence(competenceAAjouter);

        return ligneEnseignement;
    }

    /**
     * TODO comment method role
     * 
     * @param typeEvaluation
     * @param identifiant
     * @param libelle
     * @param poids
     * @return 0
     */
    private Enseignement decomposerEnseignement(String typeEvaluation, String identifiant, String libelle,
            String poids) {

        int poidsValeur = Integer.parseInt(poids);
        Enseignement enseignement;

        switch (typeEvaluation) {
        case "Ressource": {
            enseignement = new Ressource(libelle, identifiant, poidsValeur);
            break;
        }
        case "Portfolio": {
            enseignement = new Portfolio(libelle, identifiant, poidsValeur);
            break;
        }
        case "SAE": {
            enseignement = new Sae(libelle, identifiant, poidsValeur);
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
     */
    public static void main(String args[]) throws ExtensionFichierException {
        FichierSemestre fichier = new FichierSemestre("G:\\java\\workspace\\SAEGestionDeNotes\\csv\\qlnjtnqezjgt.csv");
        fichier.setDelimiteurFichier(";");
        fichier.lireFichier();
        fichier.decomposerFichier();

        GestionNotes.afficherCompetence();
    }
}
