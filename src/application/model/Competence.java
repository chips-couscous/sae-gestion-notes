/*
 * Competence.java                                                   24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe définissant une compétence 
 * @author tony.lapeyre
 */
public class Competence {
        
    /** intitulé de la compétence */
    private String intituleC;
        
    /** poids de l'enseignement  */
    private int[] poidsEnseignement;
        
    /** liste contenant les enseignements englobés par la compétence */
    private HashMap<String,ArrayList<Enseignement>> enseignementsCompetence;
        
    /** 
     * Crée une compétence contenant son intitule, son poids ainsi que 
     * la liste des enseignements contenus par celui-ci
     * @param intitule
     * @param poidsEnseignement
     * @param listeEnseignements 
     */
    public Competence(String intitule, int[] poidsEnseignement, ArrayList<Enseignement> listeEnseignements) {
        if (!estValide()) {
            throw new IllegalArgumentException("");
        }
        this.intituleC = intitule;
        this.poidsEnseignement = poidsEnseignement;
        this.enseignementsCompetence = new HashMap<>();
        enseignementsCompetence.put(intitule, listeEnseignements);
    }

    /** 
     * Prédicat définissant la validité des méthodes de validité
     * @return true
     */
    private boolean estValide() {
        if (!poidsValide() || !tailleListesValide() || !enseignementsValides()) {
            return false;
        }
        return true;
    }

    /**
     * Prédicat vérifiant la validité du numéro de la compétence
     * @return true si tous les identifiants d'enseignements 
     *              de la compétence sont différents 
     *         false si au moins 1 cas de similarité d'identifiant est présent
     */
    private boolean enseignementsValides() {
        for (int i = 0; i < enseignementsCompetence.size(); i++) {
            for(int j = i + 1; j < enseignementsCompetence.size(); i++) {
                // Si deux valeurs du tableau de enseignementsCompetence sont identiques
                if (enseignementsCompetence.get(intituleC).get(i).equals(enseignementsCompetence.get(intituleC).get(j))) {
                    return false;
                } 
            }
        }
        return true;
    }

    /** 
     * Prédicat vérifiant la taille des listes des poids des enseignements
     * et des enseignements de la compétence
     * @return true
     */
    private boolean tailleListesValide() {
        
        // Si la taille des listes des poids et des enseignements sont différentes
        if (poidsEnseignement.length != enseignementsCompetence.size()) {
            return false;
        // Si la taille de la liste des enseignements n'est pas situé dans l'intervalle [6,8]
        } else if (enseignementsCompetence.get(intituleC).size() < 6 
                   || enseignementsCompetence.get(intituleC).size() > 8) {
            return false;
        }
        return true;
    }

    /** 
     * Prédicat vérifiant le total des poids de chaque enseignement
     * @return true si le poids total est égal à 100
     *         false si le poids total est différent de 100
     */
    public boolean poidsValide() {
        int poidsTotal;
        poidsTotal = 0;
        
        // Aditionne chaque valeur de la liste des poids
        for (int i = 0; i < poidsEnseignement.length; i++) {
            poidsTotal += poidsEnseignement[i];
        }
        
        if(poidsTotal != 100) {
            return false;
        }
        return true;
    }
}

