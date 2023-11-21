/* 
 * SemestreException.java                                           25 oct. 2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model.exception;

/** 
 * Exception qui gère la création des paramètres d'un semestre.
 * @author thomas.lemaire
 */
public class ParametresSemestreException extends Exception {

    /** 
     * Constructeur de l'exception
     */
    public ParametresSemestreException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception
     * @param message à afficher lors de l'exception
     */
    public ParametresSemestreException(String message) {
        super(message);
    }
}