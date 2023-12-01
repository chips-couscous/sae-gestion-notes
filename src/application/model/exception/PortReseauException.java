/*
 * PortReseauException.java                                      21 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** 
 * Exception qui gère l'erreur sur un port de connexion invalide pour un échange réseau
 * @author tom.jammes
 */
public class PortReseauException extends Exception {

	/** Constructeur de l'exception */
	public PortReseauException() {
		super();
	}

	/** 
	 * Constructeur de l'exception avec un message d'erreur
	 * @param message d'erreur personnalisé
	 */
	public PortReseauException(String message) {
		super(message);
	}
}
