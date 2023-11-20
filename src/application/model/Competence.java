/*
 * Competence.java                                                   24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.HashMap;
import java.util.TreeSet;

import application.model.Enseignement;

/**
 * Classe définissant une compétence 
 * @author tony.lapeyre
 */
public class Competence {
        
    /** intitulé de la compétence */
    private String intitule;
        
    /** poids de l'enseignement  */
    private int[] poidsEnseignement;
        
    /** liste contenant les enseignements englobés par la compétence */
    private HashMap<String,Enseignement> enseignementsCompetence;
        
    /** 
     * Crée une compétence contenant son intitule, son poids ainsi que 
     * la liste des enseignements contenus par celui-ci
     * @param intitule
     * @param poidsEnseignement
     * @param listeEnseignements 
     */
    public Competence(HashMap<String, Enseignement> listeEnseignements, String intitule, int[] poidsEnseignement) {
        this.intitule = intitule;
        this.poidsEnseignement = poidsEnseignement;
        this.enseignementsCompetence = new HashMap<>(listeEnseignements);
    }
}
