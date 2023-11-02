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
