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
	
   String intitule;
	
   String idEnseignement;
	
   /** TODO comment intial state
    * @param intitule
    * @param idEnseignement
    */
   public Enseignement(String intitule, String idEnseignement) {
	super();
	this.idEnseignement = idEnseignement;
		
   }

    /**
     * @return l'intitule de l'enseignement
     */
    public abstract String getIntitule();

}
