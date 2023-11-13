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

    /** @return valeur de semestre */
    public Semestre getSemestre() {
        return semestre;
    }


    /** @param semestre nouvelle valeur de semestre */
    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }


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
    public void decomposerFichier() throws ParametresSemestreException {
        for (int i = 0; i < contenuFichier.size(); i++) {
            if (contenuFichier.get(i).length > 0) {
                String cellule = contenuFichier.get(i)[0];
                if(cellule.equals("Ressource")) {
                    i = decomposerEnseignement(i);
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
    private int decomposerEnseignement(int numeroDeLigne) {
        String[] enseignementActif = contenuFichier.get(numeroDeLigne);
        String idEnseignementActif = enseignementActif[1];
        
        Enseignement enseignement = semestre.verifierEnseignementPresent(idEnseignementActif);
        
        boolean enseignementValide = false;
        
        if(enseignement != null) {
            enseignementValide = true;
        }
       
        if(enseignementValide) {
            
            int ligneControle = numeroDeLigne;
            ligneControle++;
            String[] controleADecomposer;
            
            boolean finEnseignement = false;
            
            
            do {
                ligneControle++;
                controleADecomposer = contenuFichier.get(ligneControle);
                
                if (controleADecomposer.length > 0 && contenuFichier.size() > ligneControle+1) {
                    Controle controleAAjouter;
                    
                    String formeControle = controleADecomposer[0];
                    String dateControle  = controleADecomposer[1];
                    int poidsControle = (int) Integer.parseInt(controleADecomposer[2]);
                                        
                    controleAAjouter = new Controle(poidsControle, formeControle, dateControle);
                    semestre.ajouterControleAEnseignement(enseignement, controleAAjouter);
                } else {
                    finEnseignement = true;
                }
                
            } while (!finEnseignement);
            
            return ligneControle;
        }
        
        return numeroDeLigne;
    }
}
