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
}