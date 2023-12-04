/* 
 * SemestreInvalideExecption.java                                   16 nov. 2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */

package application.model.exception;

/** 
 * Exception qui gère la création d'un semestre.
 * @author thomas.lemaire
 */
public class SemestreInvalideExecption extends Exception {

	/** 
	 * Constructeur de l'exception
	 */
	public SemestreInvalideExecption() {
		super();
	}

	/** 
	 * Constructeur de l'exception
	 * @param message à afficher lors de l'exception
	 */
	public SemestreInvalideExecption(String message) {
		super(message);
	}
}