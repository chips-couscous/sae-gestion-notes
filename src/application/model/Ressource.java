<<<<<<< HEAD
/*
 * Ressource.java                                                    03/11/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

/**
 * Classe définissant une ressource 
 * @author tony.lapeyre
 */
public class Ressource extends Enseignement{
        
    /** Expression régulière de l'identifiant de la ressource */
    private static final String regexRessource = "R[1-6]\\.\\d[1-9]";
    
    /** Crée un enseignement ressource
     * @param intitule
     * @param idRessource 
     */
    public Ressource(String intitule, String idRessource) {
            super(intitule, idRessource);
            //Vérifie la validité de l'identifiant de la ressource
            if (!super.estValide(regexRessource, idRessource)) {
                throw new IllegalArgumentException();
            }
    }
}
=======
/*
 * Ressource.java                                                    25/10/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

/**
 * Classe définissant une ressource pour une compétence donnée
 * @author tony.lapeyre
 */
public class Ressource extends Enseignement{
	
	/** TODO comment intial state
	 * @param intitule
	 * @param idEnseignement
	 */
	public Ressource(String intitule, String idEnseignement) {
		super(intitule, idEnseignement);
	}
	
	/* non javadoc - @see application.model.Enseignement#getIntitule() */
	@Override
	public String getIntitule() {
	    return this.intitule;
	}
}
>>>>>>> 15ab7ba413348cb0b9526d087f7cb7eb321f7625
