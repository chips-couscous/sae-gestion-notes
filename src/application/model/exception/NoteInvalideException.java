/*
 * NoteInvalideException.java                                       15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft"
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur une note invalide
 * @author thomas.lemaire
 * @version 1.0
 */
public class NoteInvalideException extends Exception {
    
    /** Constructeur de l'exception */
    public NoteInvalideException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public NoteInvalideException(String message) {
        super(message);
    }
}
