/*
 * TestControle.java                                      26 oct. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Controle;

/** 
 * Classe de test unitaire de la classe Note
 * @author tom.jammes
 */
class TestControle {
    
    private ArrayList<Controle> notesValides;
    /** 
     * Prépare les jeux de tests 
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
       notesValides = new ArrayList<>();
       notesValides.add(new Controle(99,"Devoir sur table"," Début octobre", 0));
       notesValides.add(new Controle(75,"Exam sur machine","15 octobre", 0));
       notesValides.add(new Controle(50,"Ecrit","14/10/2022", 0));
       notesValides.add(new Controle(25,"Relevés TPs","Début octobre", 0));
       notesValides.add(new Controle(12,"","Fin novembre", 0));
       notesValides.add(new Controle(1,"Devoir sur table","Mi mai", 0));
    }

    /**
     * Test method for {@link application.model.Controle#Note(double, double, application.model.Enseignement, double, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    void testNote() {
        assertThrows(IllegalArgumentException.class,()-> new Controle(0,
                "Devoir sur table"," Début octobre", 0));
        assertThrows(IllegalArgumentException.class,()-> new Controle(101,
                "Devoir sur table"," Début octobre", 0));
        assertThrows(IllegalArgumentException.class,()-> new Controle(10000,
                "Devoir sur table"," Début octobre", 0));
        assertThrows(IllegalArgumentException.class,()-> new Controle(-1,
                "Devoir sur table"," Début octobre", 0));
        assertThrows(IllegalArgumentException.class,()-> new Controle(-5000,
                "Devoir sur table"," Début octobre", 0));
    }
    
    /**
     * Test method for {@link application.model.Controle#getPoids()}.
     */
    @Test
    void testGetPoids() {
        int[] listePoids = {99,75,50,25,12,1};
        for (int i = 0; i < notesValides.size();i++) {
            assertEquals(notesValides.get(i).getPoids(),listePoids[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#getForme()}.
     */
    @Test
    void testGetForme() {
        String[] listeForme = {"Devoir sur table","Exam sur machine","Ecrit","Relevés TPs",
                               "","Devoir sur table"};
        for (int i = 0; i < notesValides.size();i++) {
            assertEquals(notesValides.get(i).getForme(),listeForme[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#getDate()}.
     */
    @Test
    void testGetDate() {
        String[] listeDate = {" Début octobre","15 octobre","14/10/2022","Début octobre",
                              "Fin novembre","Mi mai"};
        for (int i = 0; i < notesValides.size();i++) {
            assertEquals(notesValides.get(i).getDate(),listeDate[i]);
        }
    }
}
