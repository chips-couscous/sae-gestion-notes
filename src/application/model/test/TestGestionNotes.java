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
import application.model.Sae;

/** 
 * Classe de test de la classe GestionNotes
 * Son rôle est de simuler le rôle du contrôleur 
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
        
        testAjoutNotesOk();
        testAjoutNotesNOk();
        testGetNotes();
    }
    
    /**
     * Test la méthode ajoutNote de la classe GestionNotes
     * Test avec des notes valides
     */
    private static void testAjoutNotesOk() {
        double valeur[] = {12.3, 19, 4};
        int denominateur[] = {20, 50, 6};
        Enseignement matiere[] = {new Ressource("Developpement Web","R2.03"),
                new Sae("Developpement d'application","S2.1"),
                new Ressource("Developpement Orienté Objet","R2.01")};
        int poids[] = {50, 20, 25};
        String forme[] =  {"Devoir sur table", "", "QCM"};
        String description[] = {"Premier controle de la ressource", "TP noté", ""};
        String date[] = {"Premiere semaine d'octobre", "24/10/2023", 
                "Début décembre"};
        
        int nbTestOk = 0;
        
        System.out.println("----- Test de ajoutNote avec des notes correctes");
        for (int i = 0; i < valeur.length; i++) {
               
            System.out.println("Ajout d'une note ...");
            if (GestionNotes.ajouterNote(valeur[i],denominateur[i],matiere[i],
                    poids[i],forme[i],description[i],date[i])) {
                nbTestOk++;
                
            } else {
               System.out.println("Erreur note non ajouté");
            }
        }
        
        if (nbTestOk == 3) {
            System.out.println("Tests réussis");
        }
    }
    
    /**
     * Test la méthode ajoutNote de la classe GestionNotes
     * Test avec des notes non valide valide
     */
    private static void testAjoutNotesNOk() {
        double valeur[] = {21, 19, 4};
        int denominateur[] = {20, 50, 2};
        Enseignement matiere[] = {new Ressource("Developpement Web","R2.03"),
                new Sae("Developpement d'application","S2.1"),
                new Ressource("Developpement Orienté Objet","R2.01")};
        int poids[] = {50, 110, 25};
        String forme[] =  {"Devoir sur table", "", "QCM"};
        String description[] = {"Premier controle de la ressource", "TP noté", ""};
        String date[] = {"Premiere semaine d'octobre", "24/10/2023", 
                "Début décembre"};
        
        int nbTestOk = 0;
        
        System.out.println("\n----- Test de ajoutNote avec des notes incorrectes");
        for (int i = 0; i < valeur.length; i++) {
               
            System.out.println("Ajout d'une note ...");
            if (!GestionNotes.ajouterNote(valeur[i],denominateur[i],matiere[i],
                    poids[i],forme[i],description[i],date[i])) {
                nbTestOk++;
                
            } else {
               System.out.println("Erreur note non ajouté");
            }
        }
        
        if (nbTestOk == 3) {
            System.out.println("Tests réussis");
        }
    }
    
    /**
     * Test la méthode getNotes() de la classe GestionNotes
     *
     */
    private static void testGetNotes() {
        ArrayList<Note> notes = GestionNotes.getNotes();
        
        System.out.println("\n------ Test de getNotes");
        if (notes.size() == 3) {
            System.out.println("Tests réussis");
        }
    }
}
