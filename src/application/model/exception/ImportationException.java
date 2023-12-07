package application.model.exception;

/*
 * 
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
