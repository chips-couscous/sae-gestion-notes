/*
 * CopieFichierException.java                                      8 nov. 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.exception;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class CopieFichierException extends Exception {
    
    /**
     * TODO comment intial state
     *
     */
    public CopieFichierException() {
        super();
    }
    
    /**
     * TODO comment intial state
     * @param message
     */
    public CopieFichierException(String message) {
        super(message);
    }
}
