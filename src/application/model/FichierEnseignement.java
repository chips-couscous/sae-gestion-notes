/*
 * FichierRessource.java                                      9 nov. 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import application.model.exception.ExtensionFichierException;
import application.model.exception.ParametresSemestreException;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class FichierEnseignement extends FichierCsv {
    
    private Semestre semestre;

    /**
     * TODO comment intial state
     * @param chemin
     * @throws ExtensionFichierException 
     */
    public FichierEnseignement(String chemin) throws ExtensionFichierException {
        super('r', chemin);
    }
    
    
    /**
     * TODO comment method role
     * @throws ParametresSemestreException 
     *
     */
    private void decomposerFichier() throws ParametresSemestreException {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                String cellule = contenuFichier.get(i)[0];
                if(cellule.equals("Ressource")) {
                    i = decomposerEnseignement(i);
                    break;
                }
            }
        }
    }
    
    /**
     * TODO comment method role
     * 
     * @return 0
     */
    private int decomposerEnseignement(int numeroDeLigne) {
        String[] enseignementActif = contenuFichier.get(numeroDeLigne);
        String idEnseignementActif = enseignementActif[1];
        
        Enseignement enseignement = semestre.verifierEnseignementPresent(idEnseignementActif);
        
        boolean enseignementValide = false;
        
        if(enseignement != null) {
            enseignementValide = true;
        }
       
        if(enseignementValide) {
            // TODO : ajouterControl (class Semestre) 
        }
        
        return 0;
    }
    
    /**
     * TODO comment method role
     * 
     * @param args
     * @throws ExtensionFichierException
     * @throws ParametresSemestreException 
     */
    public static void main(String args[]) throws ExtensionFichierException, ParametresSemestreException {
        FichierEnseignement fichier = new FichierEnseignement("Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\ressources-sae.csv");
        fichier.setDelimiteurFichier(";");
        fichier.lireFichier();
        fichier.decomposerFichier();
    }
}
