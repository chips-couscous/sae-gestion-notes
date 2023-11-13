/*
 * Enseignement.java                                                 03/11/2023
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.regex.Pattern;

/**
 * Classe abstraite englobant les classes Sae, Portfolio et Ressource
 * @author tony.lapeyre
 */
public abstract class Enseignement {
    
    /** Contient l'intitulé de l'enseignement */
    protected String intitule;
        
    /** Contient l'identifiant de l'enseignement */
    protected String idEnseignement;
            
    /** 
     * Crée un enseignement 
     * @param intitule
     * @param idEnseignement
     */
    public Enseignement(String intitule, String idEnseignement) {
        this.intitule = intitule;
        this.idEnseignement = idEnseignement;       
    }
    
    /**
     * Vérifie si une regex correspond à l'identifiant de l'enseignement
     * @param regex       chaîne contenant l'expression régulière à vérifier
     * @param idEnseignement 
     * @return correct true si la regex correspond à l'identifiant de l'enseignement
     *                 false si la regex ne correspond pas à l'identifiant de l'enseignement
     */
    public static boolean estValide(String regex, String idEnseignement) {       
        // on compile la regex
        Pattern motif = Pattern.compile(regex);       
        
        // vrai si l'identifiant correspond à la regex
        return motif.matcher(idEnseignement).matches();
    }

    /** @return valeur de intitule */
    public String getIntitule() {
        return intitule;
    }

    /** @return valeur de idEnseignement */
    public String getIdEnseignement() {
        return idEnseignement;
    }
    
    public String toString() {
        return intitule + " " + idEnseignement;
    }
}