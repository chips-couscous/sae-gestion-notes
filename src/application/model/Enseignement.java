/*
 * Enseignement.java                                                 25/10/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

/**
 * Classe abstraite englobant les classes Sae, Portfolio et Ressource
 * @author tony.lapeyre
 */
public abstract class Enseignement {
    
	private String intitule;
	
	private String idEnseignement;
	
	/** TODO comment initial state
	 * @param intitule
	 * @param idEnseignement
	 */
	public Enseignement(String intitule, String idEnseignement) {
		super();
		this.intitule = intitule;
		this.idEnseignement = idEnseignement;
		
	}
	
	/** TODO comment method role
	 * @return l'intitule de l'enseignement
	 */
	public String getIntitule() {
	    return this.intitule;
	}
	
	/** TODO comment method role
	 * @return l'identifiant de l'enseignement
	 */
	public String getIdEnseignement() {
	    return this.idEnseignement;
	}

}
