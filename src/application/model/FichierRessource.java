/*
 * FichierRessource.java                                      9 nov. 2023
 * IUT Rodez, info1 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import application.model.exception.ExtensionFichierException;

/** TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class FichierRessource extends FichierCsv {

    /**
     * TODO comment intial state
     * @param chemin
     * @throws ExtensionFichierException 
     */
    public FichierRessource(String chemin) throws ExtensionFichierException {
        super('r', chemin);
    }
    
}
