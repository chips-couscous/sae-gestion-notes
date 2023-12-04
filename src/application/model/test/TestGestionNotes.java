/*
 * TestGestionNotes.java                                      23 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.GestionNotes;
import application.model.Ressource;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;
import application.model.exception.ParametresSemestreException;
import application.model.exception.SemestreInvalideExecption;

/**
 * Classe de tests unitaire de la classe GestionNote
 * @author tom.jammes
 */
class TestGestionNotes {
    private static GestionNotes gn;

    /** Initialise l'objet GestionNotes avant chaque tests
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        gn = new GestionNotes();
    }

    /**
     * Test method for {@link application.model.GestionNotes#GestionNotes()}.
     */
    @Test
    void testGestionNotes() {
        fail("Not yet implemented");
    }
    
    /**
     * Test le bon fonctionnement de la méthode generationFichierExport
     * Vérifier dans le fichier que le contenu écrit est correct
     */
    @Test
    void testGenerationFichierExport() {
        assertDoesNotThrow(()->gn.genererFichierExport("Z:\\SAE\\WorkspaceEclipseSae\\GestionNotes\\csv\\parametresGestionNotes.csv"));
        assertThrows(IOException.class, ()->gn.genererFichierExport("FichierQuiNexistePas.csv"));
    }
    
    /**
     * Test le bon fonctionnement des méthodes d'importation de fichier
     */
    @Test
    void testImportation() {
        /* Cas fichiers correct */
        assertDoesNotThrow(()->gn.importerParametrageSemestre(".\\csv\\ParametresSemestre(AImporter).csv"));
        assertDoesNotThrow(()->gn.importerParametrageEnseignement(".\\csv\\ParametresRessource(AImporter).csv"));
        /* Cas fichiers incorrect */
        assertThrows(ParametresSemestreException.class,()->gn.importerParametrageSemestre("FichierQuiExistePas.csv"));
        assertThrows(ParametresSemestreException.class,()->gn.importerParametrageSemestre(".\\csv\\mauvaisFichier.csv"));
        assertThrows(SemestreInvalideExecption.class,()->gn.importerParametrageSemestre(".\\csv\\mauvaisSemestre.csv"));
    }

    /**
     * TODO comment method role
     *
     */
    private static void testSerialisation() {
        GestionNotes gn;
        try {
            gn = new GestionNotes();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            gn = null; // Bouchon pour erreur de compilation
        }

        try {
            gn.serializerDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            gn.deserializerDonnees();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * TODO comment method role
     *
     */
    private static void testGestionControleRessource() {
        GestionNotes gn;
        try {
            gn = new GestionNotes();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            gn = null; // Bouchon pour erreur de compilation
        }

        try {
            Ressource ressourceControles;
            ressourceControles = (Ressource) gn.trouverEnseignement("R2.01");
            System.out.println(ressourceControles.getControleToString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            gn.ajouterNoteAControle("R2.01.00", 12.2, 20, "");
            gn.ajouterNoteAControle("R2.01.01", 8, 10, "");
            gn.ajouterNoteAControle("R2.01.02", 15, 40, "");
            gn.ajouterNoteAControle("R2.01.03", 15.5, 20, "");
        } catch (NoteInvalideException e) {
            e.printStackTrace();
        }

        try {
            gn.calculerMoyenneEnseignement("R2.01");
        } catch (MoyenneRessourceException e) {
            e.printStackTrace();
        } catch (NoteInvalideException e) {
            e.printStackTrace();
        }
    }

}
