/*
 * MoyenneCompetenceException.java                                  16 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft"
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur le calcul d'une moyenne dans une compétence
 * @author thomas.lemaire
 * @version 1.0
 */
public class MoyenneCompetenceException extends Exception {

    /** Constructeur de l'exception */
    public MoyenneCompetenceException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public MoyenneCompetenceException(String message) {
        super(message);
    }
}

