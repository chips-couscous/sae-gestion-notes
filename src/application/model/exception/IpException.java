/*
 * IpException.java                                      21 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère les problèmes d'adresse IP
 * @author tom.jammes
 */
public class IpException extends Exception {
    
    /** Constructeur de l'exception */
    public IpException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public IpException(String message) {
        super(message);
    }
}
