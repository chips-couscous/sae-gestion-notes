/* 
 * ParametresSemestre.java                                            20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ParametresSemestreException;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class Semestre {

    private int numero;
    private char parcours;
    
    private HashMap<Enseignement, List<Object[]>> listeEnseignement = new HashMap<Enseignement, List<Object[]>>(); 
    
    /**
     * Constructeur du semestre
     * @param numeroSemestre
     * @param parcoursSemestre
     * @throws ParametresSemestreException 
     */
    public Semestre(int numeroSemestre, String parcoursSemestre) 
            throws ParametresSemestreException {
        // Validation des données
       if(!semestreEstValide(numeroSemestre, parcoursSemestre)) {
           throw new ParametresSemestreException("Le semestre est invalide");
       }
       numero = numeroSemestre;
       parcours = parcoursSemestre.toLowerCase().charAt(0);
    }
    
    
    /**
     * Un semestre est considéré comme valide si :
     * son numéro est compris entre 0 et 7
     * et son parcours correspond à ces chaines ("tous", "a", "b", "c", "d")
     * @param numero du semestre
     * @param parcours du semestre
     * @return true si tous les éléments sont valides, false sinon
     */
    private boolean semestreEstValide(int numero, String parcours) {
        parcours = parcours.toLowerCase();
        return     numero > 0 && numero < 7
               && (parcours.equals("tous") || parcours.equals("néant") ||
                   parcours.equals("a")    || parcours.equals("b")     ||
                   parcours.equals("c")    || parcours.equals("d")); 
    }
    
    /**
     * TODO comment method role
     * @param enseignement 
     * @param competence 
     * @param poids 
     * @return true si l'enseignement a bien été ajouté, false sinon
     */
    public boolean ajouterCompetenceAEnseignement(Enseignement enseignement, Competence competence, int poids) {
        List<Object[]> listeCompetence;
        
        Object[] valeurCompetence = {competence, poids};
        
        listeCompetence = listeEnseignement.get(enseignement);
        listeCompetence.add(valeurCompetence);
        
        try {
            listeEnseignement.put(enseignement, listeCompetence);
        } catch (IllegalArgumentException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * TODO comment method role
     * @param enseignement
     * @return 2
     */
    public boolean ajouterEnseignement(Enseignement enseignement) {
        
        if(!listeEnseignement.containsKey(enseignement)) {
            
            List<Object[]> listeCompetence = new ArrayList<Object[]>();
            listeEnseignement.put(enseignement, listeCompetence);
            return true;
            
        }
        return false;
        
    }
    
    /** TODO comment method role
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Semestre : ").append(numero).append(" / Parcours : ").append(parcours).append("\n");

        for (Enseignement enseignement : listeEnseignement.keySet()) {
            sb.append("Enseignement: ").append(enseignement.getIntitule()).append(" (").append(enseignement.getIdEnseignement()).append(")\n");
            List<Object[]> listeCompetence = listeEnseignement.get(enseignement);
            for (Object[] competencePoids : listeCompetence) {
                Competence competence = (Competence) competencePoids[0];
                int poids = (int) competencePoids[1];
                sb.append("  - Competence: ").append(competence.getIntitule()).append(", Poids: ").append(poids).append("\n");
            }
        }

        return sb.toString();
    }
}
