<<<<<<< HEAD
/*
 * Sae.java                                                          03/11/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteur
 */
package application.model;

/**
 * Classe définissant une Saé 
 * @author tony.lapeyre
 */
public class Sae extends Enseignement{
    
    /** Expression régulière de l'identifiant de la Saé */
    private static final String regexSae = "S[1-6]\\.\\d[1-9]";
    
    /** Crée un enseignement SAÉ
     * @param intitule
     * @param idSae 
     */
    public Sae(String intitule, String idSae) {
        super(intitule, idSae);
        //Vérifie la validité de l'identifiant de la Sae
        if (!super.estValide(regexSae, idSae)) {
            throw new IllegalArgumentException();
        }
    }
=======
/*
 * sae.java                                                          25/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteur
 */
package application.model;

/**
 * Classe définissant une Saé 
 * @author tony.lapeyre
 */
public class Sae extends Enseignement{
	
	/** TODO comment intial state
	 * @param intitule
	 * @param idEnseignement
	 */
	public Sae(String intitule, String idEnseignement) {
		super(intitule, idEnseignement);
	}

    /* non javadoc - @see application.model.Enseignement#getIntitule() */
    @Override
    public String getIntitule() {
        return this.intitule;
    }

>>>>>>> 15ab7ba413348cb0b9526d087f7cb7eb321f7625
}