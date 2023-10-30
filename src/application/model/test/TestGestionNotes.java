/*
 * TestGestionNotes.java                                      30 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import java.util.ArrayList;

import application.model.Enseignement;
import application.model.GestionNotes;
import application.model.Note;
import application.model.Ressource;

/** 
 * Classe de test de la classe GestionNotes
 * 
 * @author tom.jammes
 */
public class TestGestionNotes {
    
    /** 
     * Méthode de test
     * 
     * @param args non utilisé
     */
    public static void main(String[] args) {
        
        double valeur = 12.3;
        int denominateur = 20;
        Ressource matiere = new Ressource("Developpement Web","R2.03");
        int poids = 50;
        String forme = "Devoir sur table";
        String description = "Premier controle de la ressource";
        String date = "Premiere semaine d'octobre";
        
        System.out.println("Ajout d'une note");
        if (GestionNotes.ajouterNote(valeur,denominateur,matiere,poids,forme,
                description,date)) {
            System.out.println(".....\nNote ajouté");
            ArrayList<Note> notes = GestionNotes.getNotes();
            for (int i = 0; i < notes.size(); i++) {
                System.out.println("Note " + i + " : " + notes.get(i).getValeur()
                        + "/" + notes.get(i).getDenominateur() + " en " 
                        + notes.get(i).getMatiere());
            }
            
        } else {
           System.out.println(".....\\nErreur note non ajouté");
        }
    }
}
