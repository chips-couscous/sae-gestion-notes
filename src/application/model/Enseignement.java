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
        boolean correct = true;       // vrai si l'identifiant correspond à la regex
        Pattern motif = Pattern.compile(regex);       // on compile la regex
            
        // on vérifie si l'identifiant d'enseignement correspond à la regex
        if (! motif.matcher(idEnseignement).matches()) {
            System.out.println("Erreur la chaîne " + idEnseignement 
                               + " est considérée comme invalide pour "
                               + motif);
            correct = false;
        }
        return correct;
    }

    /** @return valeur de intitule */
    public String getIntitule() {
        return intitule;
    }

    /** @return valeur de idEnseignement */
    public String getIdEnseignement() {
        return idEnseignement;
    }
}