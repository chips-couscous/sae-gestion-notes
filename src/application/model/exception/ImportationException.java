/*
 * ImportationException.java 						3.12.23
 * IUT de Rodez, pas de droits d'auteur
 * pas de copyright ni de copyleft
 */

package application.model.exception;

/**
 * Exception qui gère l'erreur sur l'importation
 * @author thomas.izard
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @author constant.nguyen
 */
public class ImportationException extends Exception{
	
	/** Constructeur de l'exception */
    public ImportationException() {
        super();
    }
    
    /** 
     * Constructeur de l'exception avec un message d'erreur
     * @param message d'erreur personnalisé
     */
    public ImportationException(String message) {
        super(message);
    }
}
