/*
 * CompetenceInvalideException.java                                 15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur une compétence invalide
 * @author thomas.lemaire
 * @version 1.0
 */
public class CompetenceInvalideException extends Exception {
    
    /** Constructeur de l'exception */
    public CompetenceInvalideException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public CompetenceInvalideException(String message) {
        super(message);
    }
}
