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
    private static final String regexRessource = "R[1-6]\\.[0-9]{2}";
    
    /** Crée un enseignement ressource
     * @param intitule
     * @param idRessource 
     * @param poids 
     */
    public Ressource(String intitule, String idRessource, int poids) {
            super(intitule, idRessource, poids);
            //Vérifie la validité de l'identifiant de la ressource
            if (!super.estValide(regexRessource, idRessource)) {
                throw new IllegalArgumentException();
            }
    }
}
