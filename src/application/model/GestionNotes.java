/*
 * GestionNotes.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;

/**
 * Modèle de l'application GestionNotes
 * 
 * @author tom.jammes
 */
public class GestionNotes {
    
    private static ArrayList<Note> notes = new ArrayList<>();

    /** @return valeur de notes */
    public static ArrayList<Note> getNotes() {
        return notes;
    }
    
    static Utilisateur identite = new Utilisateur("Nom", "Prenom");

    /** 
     * Crée une note à partir des données reçu par le contrôleur et l'ajoute 
     * à la liste notes
     * 
     * @param valeur valeur de la note 
     * @param denominateur valeur sur la quelle la note est évalué, exemple: 
     *        x/y avec x la note y le dénominateur,0 <= x, x <= y et y != 0...
     * @param matiere enseignement dans le quel la note a été obtenue
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @param forme type du contrôle, exemple: devoir sur table, tp noté, qcm, ...
     * @param description description du contrôle donné par l'élève
     * @param date date du contrôle. Peut être approximative, exemple début janvier
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
    
    public static void changerIdentite(String nom, String prenom) {
		identite.setNom(nom);
		identite.setPrenom(prenom);
	}
	
	public static String afficherIdentite() {
		return identite.getNom() + " " + identite.getPrenom();
	}
}