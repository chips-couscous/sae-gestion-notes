/*
 * Competence.java                                                   24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import application.model.Enseignement;

/**
 * Classe définissant une compétence 
 * @author tony.lapeyre
 */
public class Competence {
        
    /** intitulé de la compétence */
    private String intitule;
    
    /** identifiant de la compétence */
    private String idCompetence;
       
    /** 
     * Crée une compétence contenant son intitule, son poids ainsi que 
     * la liste des enseignements contenus par celui-ci
     * @param intitule
     * @param idCompetence 
     */
    public Competence(String intitule, String idCompetence) {
        this.intitule = intitule;
        this.idCompetence = idCompetence;
    }

    /** @return valeur de intitule */
    public String getIntitule() {
        return intitule;
    }
}
