/*
 * TestGestionNotes.java                                      2 déc. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import java.io.IOException;

import application.model.GestionNotes;

/** Classe de tests unitaire de la classe GestionNotes
 * @author tom.jammes
 */
public class TestGestionNotes {
    
    /** 
     * Lance toutes les méthodes de tests
     * @param args non utilisé
     */
    public static void main(String[] args) {
        testGenerationFichierExport();
    }
    
    /**
     * Test le bon fonctionnement de la méthode generationFichierExport
     * Vérifier dans le fichier que le contenu écrit est correct
     */
    private static void testGenerationFichierExport() {
        try {
            GestionNotes gn = new GestionNotes();
            gn.genererFichierExport("/home/tom/Documents/GestionNotes/WorkspaceEclispe/GestionNotes/parametresGestionNotes.csv");
            System.out.println("Fichier généré");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la génération du fichier d'export");
        }
    }
}
