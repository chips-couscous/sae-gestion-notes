/* 
 * ParametresSemestre.java                                            20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */
package application.model;

import application.model.exception.ParametresSemestreException;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class Semestre {

    private int numero;
    private char parcours;
    
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
    
    /** TODO comment method role
     */
    public String toString() {
        return "Semetre : " + numero + " / Parcours : " + parcours;
    }
}
