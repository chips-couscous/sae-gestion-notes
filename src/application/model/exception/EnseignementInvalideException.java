/*
 * EnseignementInvalideException.java                               15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur un enseignement invalide
 * @author thomas.lemaire
 * @version 1.0
 */
public class EnseignementInvalideException extends Exception {

    /** Constructeur de l'exception */
    public EnseignementInvalideException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public EnseignementInvalideException(String message) {
        super(message);
    }   
}
