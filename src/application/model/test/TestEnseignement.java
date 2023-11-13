/*
 * TestEnseignement.java                                      2 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import application.model.Enseignement;
import application.model.Portfolio;
import application.model.Ressource;
import application.model.Sae;

/** TODO comment class responsibility (SRP)
 * @author tonyl
 *
 */
class TestEnseignement {
    
    private static ArrayList<Enseignement> listeEnseignements;
    
    /** 
     * Initialise une ArrayList contenant les enseignements avec leurs attributs à tester
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        listeEnseignements = new ArrayList<>(6);
        listeEnseignements.add(new Sae("Installation de services réseaux","S2.04"));
        listeEnseignements.add(new Portfolio("blablabla","P3.06"));
        listeEnseignements.add(new Ressource("Cryptographie","R3.02"));
        listeEnseignements.add(new Sae("","S3.02"));
        listeEnseignements.add(new Ressource("Initiation au développement","R1.02"));
        listeEnseignements.add(new Portfolio("developpement d'une application","P1.02"));

    }

    /**
     * Test method for {@link application.model.Enseignement#getIntitule()}.
     */
    @Test
    void testGetIntitule() {
        String[] listeIntitule = {"Installation de services réseaux", "Portfolio", 
                                  "Cryptographie", "", "Initiation au développement",
                                  "Portfolio"};
        for (int i = 0; i < listeEnseignements.size(); i++) {
            assertEquals(listeEnseignements.get(i).getIntitule(), listeIntitule[i]);
        }
    }

    /**
     * Test method for {@link application.model.Enseignement#getIdEnseignement()}.
     */
    @Test
    void testGetIdEnseignement() {
        String[] listeIdEnseignement = {"S2.04","P3.06","R3.02","S3.02","R1.02","P1.02"};
        for (int i = 0; i < listeEnseignements.size(); i++) {
            assertEquals(listeEnseignements.get(i).getIdEnseignement(), listeIdEnseignement[i]);
        }
    }

}
