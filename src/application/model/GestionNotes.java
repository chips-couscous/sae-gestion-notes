/*
 * GestionNotes.java                                      26 oct. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Modèle de l'application GestionNotes
 * 
 * @author tom.jammes
 */
public class GestionNotes {

    static Utilisateur identite = new Utilisateur("Nom", "Prenom");
    
    Semestre dataSemestre;

    /**
     * Crée une note à partir des données reçu par le contrôleur et l'ajoute à la
     * liste notes
     * 
     * @param valeur       valeur de la note
     * @param denominateur valeur sur la quelle la note est évalué, exemple: x/y
     *                     avec x la note y le dénominateur,0 <= x, x <= y et y !=
     *                     0...
     * @param nom          nom du controle dans le quel la note a été obtenue
     * @param description  description du contrôle donné par l'élève
     * @param date         date du contrôle. Peut être approximative, exemple début
     *                     janvier
     * @param id           identifiant du contrôle
     * @return true si le contrôle a pu être ajouté
     */
    public boolean ajouterNote(double valeur, int denominateur, 
            String nom, String description, String date, int id) {
        
        dataSemestre.ajouterNoteAControle(valeur, denominateur, nom, description, date, id);
        return false;
    }
    
    
    /* TODO Ajouter une méthode pour ajouter tous les enseignements dans la hashmap */
    
    /** 
     * @return le nombre de notes renseignés par l'élève
     */
    public int nbNotes() {
        return dataSemestre.nbNotes();
    }
    
    /**
     * Change l'identité de l'utilisateur
     * 
     * @param nom nouveau nom de l'utilisateur
     * @param prenom nouveau prénom de l'utilisateur
     */
    public static void changerIdentite(String nom, String prenom) {
        identite.setNom(nom);
        identite.setPrenom(prenom);
    }

    /**
     * @return une chaîne contenant le nom et le prénom de l'utilisateur
     */
    public static String afficherIdentite() {
        return identite.getNom() + " " + identite.getPrenom();
    }
}