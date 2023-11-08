/*
 * GestionNotes.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;

/**
 * Modele de l'application GestionNotes
 * 
 * @author tom.jammes
 */
public class GestionNotes {
    
    private static ArrayList<Note> notes = new ArrayList<>();

    /** @return valeur de notes */
    public static ArrayList<Note> getNotes() {
        return notes;
    }

    /** 
     * Crée une note à partir des données reçu par le contrôleur et l'ajoute 
     * à la liste notes
     * 
     * @param valeur valeur de la note 
     * @param denominateur valeur sur la quelle la note est évalué. Ex: 10/20 ou
     *          23/50 ...
     * @param matiere enseignement dans le quel la note a été obtenue
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @param forme type du contrôle. Ex: devoir sur table, tp noté, qcm, ...
     * @param description description du contrôle donné par l'élève
     * @param date date du contrôle. Peut être approximative, ex début janvier
     * @return true si la note a pu être ajouté
     */
    public static boolean ajouterNote(double valeur, int denominateur, Enseignement matiere, 
            int poids, String forme, String description, String date) {
        
        try {
            Note note = new Note(valeur,denominateur,matiere,poids,forme,description,date);
            notes.add(note);
            return true;
        } catch (IllegalArgumentException erreur) {
            return false;
        }
    }
}