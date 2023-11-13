/*
 * TestNote.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;


import static org.junit.Assert.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Controle;
import application.model.Ressource;

/** 
 * Classe de test unitaire de la classe Note
 * @author tom.jammes
 */
class TestNote {

    /** 
     * Prépare les jeux de tests 
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
       ArrayList<Controle> notesValides = new ArrayList<>();
       notesValides.add(new Controle(15.5,20,new Ressource("Probabilités","R3.04"),20,
               "Devoir sur table","Controle de Proba"," Début octobre"));
       notesValides.add(new Controle(3,15,new Ressource("Maths Discrètes","R2.11"),12,
               "Devoir sur table","Controle de Maths","14/10/2022"));
       notesValides.add(new Controle(26.5,50,new Ressource("Développement Web",
               "R1.04"),12,"Devoir sur machine","Tp noté en html","Fin novembre"));
       notesValides.add(new Controle(0,5,new Ressource("Probabilités","R3.04"),5,
               "Devoir sur table","Controle de Proba"," Début octobre"));
    }

    /**
     * Test method for {@link application.model.Controle#Note(double, double, application.model.Enseignement, double, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    void testNote() {
        assertThrows(IllegalArgumentException.class,()-> new Controle(25,20,new Ressource("Probabilités","R3.04"),20,
                "Devoir sur table","Controle de Proba"," Début octobre"));
        assertThrows(IllegalArgumentException.class,()-> new Controle(25,1500,new Ressource("Probabilités","R3.04"),20,
                "Devoir sur table","Controle de Proba"," Début octobre"));
        assertThrows(IllegalArgumentException.class,()-> new Controle(25,50,new Ressource("Probabilités","R3.04"),0,
                "Devoir sur table","Controle de Proba"," Début octobre"));
        assertThrows(IllegalArgumentException.class,()-> new Controle(25,50,new Ressource("Probabilités","R3.04"),140,
                "Devoir sur table","Controle de Proba"," Début octobre"));
        assertThrows(IllegalArgumentException.class,()-> new Controle(0,0,new Ressource("Probabilités","R3.04"),12,
                "Devoir sur table","Controle de Proba"," Début octobre"));
        
    }

}
