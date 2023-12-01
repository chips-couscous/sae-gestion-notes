/*
 * UtilisateurInvalideException.java                                17 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur un utilisateur invalide
 * @author thomas.lemaire
 * @version 1.0
 */
public class UtilisateurInvalideException extends Exception {
    
    /** Constructeur de l'exception */
    public UtilisateurInvalideException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public UtilisateurInvalideException(String message) {
        super(message);
    }
}
