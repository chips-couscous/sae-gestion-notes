/*
 * TestControle.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Controle;
import application.model.Ressource;

/** 
 * Classe de test unitaire de la classe Note
 * @author tom.jammes
 */
class TestControle {

    /** 
     * Prépare les jeux de tests 
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
       ArrayList<Controle> notesValides = new ArrayList<>();
       notesValides.add(new Controle(20,"Devoir sur table"," Début octobre"));
       notesValides.add(new Controle(12,"Devoir sur table","14/10/2022"));
       notesValides.add(new Controle(12,"Devoir sur machine","Fin novembre"));
       notesValides.add(new Controle(5,"Devoir sur table"," Début octobre"));
    }

    /**
     * Test method for {@link application.model.Controle#Note(double, double, application.model.Enseignement, double, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    void testNote() {
        assertThrows(IllegalArgumentException.class,()-> new Controle(0,
                "Devoir sur table"," Début octobre"));
        assertThrows(IllegalArgumentException.class,()-> new Controle(140,
                "Devoir sur table"," Début octobre"));
        
    }
}
