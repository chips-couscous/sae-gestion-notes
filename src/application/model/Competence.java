/*
 * Competence.java                                                   24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.ArrayList;

import application.model.Enseignement;

/**
 * Classe définissant une compétence 
 * @author tony.lapeyre
 */
public class Competence {
        
    /** intitulé de la compétence */
    private String intitule;
        
    /** poids de l'enseignement  */
    private int poidsEnseignement;
        
    /** liste contenant les enseignements englobés par la compétence */
    private ArrayList<Enseignement> listeEnseignements;
        
    /** 
     * Crée une compétence contenant son intitule, son poids ainsi que 
     * la liste des enseignements contenus par celui-ci
     * @param intitule
     * @param poidsEnseignement
     * @param listeEnseignements 
     */
    public Competence(String intitule, int poidsEnseignement, ArrayList<Enseignement> listeEnseignements) {
        this.intitule = intitule;
        this.poidsEnseignement = poidsEnseignement;
        this.listeEnseignements = new ArrayList<>(listeEnseignements);
    }
}