/*
 * MoyenneRessourceException.java                                   16 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft"
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur le calcul d'une moyenne dans une ressource
 * @author thomas.lemaire
 * @version 1.0
 */
public class MoyenneRessourceException extends Exception {

    /** Constructeur de l'exception */
    public MoyenneRessourceException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public MoyenneRessourceException(String message) {
        super(message);
    }
}