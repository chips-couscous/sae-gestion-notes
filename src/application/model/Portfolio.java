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
    private static final String regexPortfolio = "P[1-6]\\.[0-9]{2}";
    
    /** 
     * Crée un constructeur Portfolio
     * @param intitule
     * @param idPortfolio 
     * @param poids 
     */
    public Portfolio(String intitule, String idPortfolio, int poids) {
            super(intitule,idPortfolio, poids);
            this.intitule = "Portfolio";
            //Vérifie la validité de l'identifiant du portfolio
            if (!super.estValide(regexPortfolio, idPortfolio)) {
                throw new IllegalArgumentException();
            }
    }
}
