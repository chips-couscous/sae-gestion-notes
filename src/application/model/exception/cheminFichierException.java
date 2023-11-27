/*
 * cheminFichierException.java                                      21 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère les problème avec un chemin ded fichier
 * @author tom.jammes
 */
public class cheminFichierException extends Exception {
    
    /** Constructeur de l'exception */
    public cheminFichierException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public cheminFichierException(String message) {
        super(message);
    }
}
