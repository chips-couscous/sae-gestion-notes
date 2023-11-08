<<<<<<< HEAD
/*
 * Portfolio.java                                                    03/11/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

/**
 * Classe définissant un Portfolio 
 * @author tony.lapeyre
 */
public class Portfolio extends Enseignement {
    
    /** Expression régulière de l'identifiant du Portfolio */
    private static final String regexPortfolio = "P[1-6]\\.\\d[1-9]";
    
    /** 
     * Crée un constructeur Portfolio
     * @param intitule
     * @param idPortfolio 
     */
    public Portfolio(String intitule, String idPortfolio) {
            super(intitule,idPortfolio);
            this.intitule = "Portfolio";
            //Vérifie la validité de l'identifiant du portfolio
            if (!super.estValide(regexPortfolio, idPortfolio)) {
                throw new IllegalArgumentException();
            }
    }
}
=======
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
	}

    /* non javadoc - @see application.model.Enseignement#getIntitule() */
    @Override
    public String getIntitule() {
        return this.intitule;
    }
}
>>>>>>> 15ab7ba413348cb0b9526d087f7cb7eb321f7625
