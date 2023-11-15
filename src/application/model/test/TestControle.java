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
 * @author tony.lapeyre
 */
class TestControle {
    
    /** Liste contenant des contrôles valides à tester*/
    private ArrayList<Controle> controlesValides;
    
    /** 
     * Prépare les jeux de tests 
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
       controlesValides = new ArrayList<>();
       controlesValides.add(new Controle(99,"Devoir sur table"," Début octobre", 1));
       controlesValides.add(new Controle(75,"Exam sur machine","15 octobre", 35));
       controlesValides.add(new Controle(50,"Ecrit","14/10/2022", 20));
       controlesValides.add(new Controle(25,"Relevés TPs","Début octobre", 5));
       controlesValides.add(new Controle(12,"","Fin novembre", 11));
       controlesValides.add(new Controle(1,"Devoir sur table","Mi mai", 2));
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
        for (int i = 0; i < controlesValides.size();i++) {
            assertEquals(controlesValides.get(i).getPoids(),listePoids[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#getForme()}.
     */
    @Test
    void testGetForme() {
        String[] listeForme = {"Devoir sur table","Exam sur machine","Ecrit","Relevés TPs",
                               "","Devoir sur table"};
        for (int i = 0; i < controlesValides.size();i++) {
            assertEquals(controlesValides.get(i).getForme(),listeForme[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#getDate()}.
     */
    @Test
    void testGetDate() {
        String[] listeDate = {" Début octobre","15 octobre","14/10/2022","Début octobre",
                              "Fin novembre","Mi mai"};
        for (int i = 0; i < controlesValides.size();i++) {
            assertEquals(controlesValides.get(i).getDate(),listeDate[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#getIdControle()}.
     */
    @Test
    void testGetIdControle() {
        int[] listeId = {1,35,20,5,11,2};
        for (int i = 0; i < controlesValides.size();i++) {
            assertEquals(controlesValides.get(i).getIdControle(),listeId[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#setForme()}.
     */
    @Test
    void testSetForme() {
        String[] listeForme = {"Controle surprise", "Oral", "Calcul mental", "Code sur papier",
                            "Interface", "Calcul IEEE"};
        for (int i = 0; i < controlesValides.size();i++) {
            controlesValides.get(i).setForme(listeForme[i]);
            assertEquals(controlesValides.get(i).getForme(),listeForme[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#setDate()}.
     */
    @Test
    void testSetDate() {
        String[] listeDate = {"le 11 janvier", "Quelque part dans mai", "Entre le 10 et 12 juin",
                "31/02/2023", "courant novembre", "le 5 du mois de mars"};
        for (int i = 0; i < controlesValides.size();i++) {
            controlesValides.get(i).setDate(listeDate[i]);
            assertEquals(controlesValides.get(i).getDate(),listeDate[i]);
        }
    }
    
    /**
     * Test method for {@link application.model.Controle#setPoids()}.
     */
    @Test
    void testSetPoids() {
        int[] listePoids = {1,24,63,8,33,98};
        for (int i = 0; i < controlesValides.size();i++) {
            controlesValides.get(i).setPoids(listePoids[i]);
            assertEquals(controlesValides.get(i).getPoids(),listePoids[i]);
        }
    }
}
