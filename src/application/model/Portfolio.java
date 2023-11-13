/*
 * Portfolio.java                                                    25/10/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

/**
 * Classe définissant un Portfolio affilié à la classe Compétence
 * @author tony.lapeyre
 */
public class Portfolio extends Enseignement {
	
	
	/** TODO comment intial state
	 * @param intitule
	 * @param idEnseignement
	 */
	public Portfolio(String intitule, String idEnseignement) {
	    super(intitule, idEnseignement);
	    if(!estValide()) {
	        throw new IllegalArgumentException();
	    }
	}
	
	/**
	 * Prédicat sur la validité des arguments 
	 * @return true si l'intitule et l'identifiant sont valides
	 *         false si l'intitule ou l'identifiant est invalide
	 */
	private boolean estValide() {
	    if(!intituleEstValide(this.getIntitule()) || !idEnseignementEstValide(this.getIdEnseignement())) {
	        return false;
	    }
	    return true;
	}

    /** TODO comment method role
     * @return
     */
    private boolean idEnseignementEstValide(String idEnseignement) {
        if(idEnseignement.toUpperCase().charAt(0) != ('R')) {
            return false;
        }
        return true;
    }

    /** TODO comment method role
     * @param intitule
     * @return
     */
    private boolean intituleEstValide(String intitule) {
        if(intitule.isEmpty()) {
            return false;
        }
        return false;
    }
}
