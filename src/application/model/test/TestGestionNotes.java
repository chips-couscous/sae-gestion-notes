/*
 * TestGestionNotes.java                                      23 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Controle;
import application.model.GestionNotes;
import application.model.Sae;
import application.model.exception.CompetenceInvalideException;
import application.model.exception.ControleInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.ImportationException;
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
     * Test réalisé indépendamment des autres tests afin d'être sur que le fichier
     * de sauvegarde est présent au moment du test
     * @throws IOException si il est impossible de déplacer le fichier de sauvegarde
     */
    void testGestionNotes() {
        File fichierSauvegarde = new File("tmp/gestion-notes.ser");
        /* Test avec fichier de sauvegarde existant */
        assertDoesNotThrow(()->new GestionNotes());
        /* Test sans fichier de sauvegarde existant */
        
        fichierSauvegarde.delete();
        assertDoesNotThrow(()->new GestionNotes());
    }
    
    /**
     * Test le bon fonctionnement de la méthode generationFichierExport
     * Vérifier dans le fichier que le contenu écrit est correct
     */
    @Test
    void testGenerationFichierExport() {
        assertDoesNotThrow(()->gn.genererFichierExport("Z:\\SAE\\WorkspaceEclipseSae\\GestionNotes\\csv\\parametresGestionNotes.csv"));
        assertDoesNotThrow(()->gn.genererFichierExport("FichierQuiNexistePas.csv"));
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
        assertThrows(ExtensionFichierException.class,()->gn.importerParametrageSemestre("FichierExistePas.csv"));
        assertThrows(ParametresSemestreException.class,()->gn.importerParametrageSemestre(".\\csv\\mauvaisFichier.csv"));
        assertThrows(SemestreInvalideExecption.class,()->gn.importerParametrageSemestre(".\\csv\\mauvaisSemestre.csv"));
        assertThrows(ExtensionFichierException.class, ()->gn.importerParametrageEnseignement("FichierExistePas.csv"));
        assertThrows(EnseignementInvalideException.class, ()->gn.importerParametrageEnseignement(".\\csv\\mauvaisFichier.csv"));
        try {
            gn.importerParametrageSemestre(".\\csv\\ParametresSemestre(AImporter).csv");
            assertThrows(ControleInvalideException.class,()->gn.importerParametrageEnseignement(".\\csv\\mauvaisControle.csv"));
        } catch (ExtensionFichierException | SemestreInvalideExecption 
                | CompetenceInvalideException | EnseignementInvalideException 
                | ParametresSemestreException e) {
            fail("impossible d'importer les parametres semestre");
        }
        
    }

    /**
     * Test les méthode serialiserDonnees et deserialiserDonnees
     *
     */
    @Test
    void testSerialisation() {
        File fichierSauvegarde = new File("tmp/gestion-notes.ser");
        assertDoesNotThrow(()->gn.serializerDonnees());
        assertDoesNotThrow(()->gn.deserializerDonnees());
        
        try {
            gn.deserializerDonnees();
            assertEquals(gn.getUtilisateurGestionNotes(), "Tom Jammes");
        } catch (ClassNotFoundException | IOException e) {
            fail("Impossible de désérialiser les données");
        }
        
        /* Tenter de deserialiser sans sauvegarde */
        fichierSauvegarde.delete();
        assertThrows(FileNotFoundException.class,()->gn.deserializerDonnees());
    }
    
    /**
     * Test de l'ajout d'une note
     */
    @Test
    void testAjoutNote() {
        try {
            gn.importerParametrageSemestre(".\\csv\\ParametresSemestre(AImporter).csv");
            gn.importerParametrageEnseignement(".\\csv\\ParametresRessource(AImporter).csv");
            
            /* Test pour l'ajout d'une note a un contrôle*/
            assertDoesNotThrow(()->gn.ajouterNoteAControle("R2.01.00", 11, 20, ""));
            assertThrows(NoteInvalideException.class,()->gn.ajouterNoteAControle("R2.0101", 11, 6, "Note incorrecte"));
            assertThrows(NoteInvalideException.class,()->gn.ajouterNoteAControle("R2.0777", 11, 6, "identifiant incorrect"));
            /* Vérification de l'ajout la note */
            gn.ajouterNoteAControle("R2.01.00", 11, 20, "Test");
            assertEquals(11,((Controle)gn.getNotes().get(0)).getNoteControle().getValeurNote());
            
            /* Test de l'ajout d'une note à une SAE */
            assertDoesNotThrow(()->gn.ajouterNoteASaePortfolio(gn.trouverEnseignement("S2.02"), 15, 30, ""));
            assertThrows(NoteInvalideException.class,()->gn.ajouterNoteASaePortfolio(gn.trouverEnseignement("S2.02"), 36, 30, "NoteIncorrecte"));
            assertThrows(NoteInvalideException.class,()->gn.ajouterNoteASaePortfolio(gn.trouverEnseignement("S2.00"), 10, 30, "IdentifiantIncorect"));
            /* Verification de l'ajout de la note */
            gn.ajouterNoteASaePortfolio(gn.trouverEnseignement("S2.02"), 15, 30, "");
            assertEquals(15,((Sae)gn.getNotes().get(1)).getNoteSae().getValeurNote());
        } catch (ExtensionFichierException | SemestreInvalideExecption | CompetenceInvalideException
                | EnseignementInvalideException | ParametresSemestreException e) {
            fail("Impossible d'importer les paramètres du semestre");
        } catch (ControleInvalideException e) {
            fail("Impossible d'importer les paramètres de la ressource");
        } catch (NoteInvalideException e) {
            fail("Impossible d'ajouter la note");
        } catch (ImportationException e) {
            fail("Impossible d'ajouter les ressources, les paramètres du semestre"
                    + " n'ont pas été ajouté");
        }
    }
}
