/* 
 * ExtensionFichierException.java                                     20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model.exception;

/** 
 * Exception qui gère l'extension d'un fichier si celui-ci n'est pas valide.
 * @author thomas.lemaire
 */
public class ExtensionFichierException extends Exception {

	/** 
	 * Constructeur de l'exception
	 */
	public ExtensionFichierException() {
		super();
	}

	/** 
	 * Constructeur de l'exception
	 * @param message à afficher lors de l'exception
	 */
	public ExtensionFichierException(String message) {
		super(message);
	}
}