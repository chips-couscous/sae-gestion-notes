/*
 * TestSae.java                                      2 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import application.model.Sae;

/** 
 * Classe de tests de la classe Sae
 * @author tonyl
 *
 */
class TestSae {

    /**
     * Teste le prédicat de la méthode estValide pour des identifiants d'enseignement valides 
     * {@link application.model.Sae#Sae(java.lang.String, java.lang.String)}.
     */
    @Test
    void idOk() {
        String regexSae = "S[1-6]\\.\\d[1-9]";
        assertTrue(Sae.estValide(regexSae, "S2.02"));
        assertTrue(Sae.estValide(regexSae, "S6.99"));
        assertTrue(Sae.estValide(regexSae, "S1.01"));
        assertTrue(Sae.estValide(regexSae, "S4.55"));
    }
    
    /**
     * Teste le prédicat de la méthode estValide pour des identifiants d'enseignement invalides 
     * {@link application.model.Sae#Sae(java.lang.String, java.lang.String)}.     */
    @Test
    void idNOk() {
        String regexSae = "S[1-6]\\.\\d[1-9]";
        String[] jeuTestsId = {"","S1.00","R2.02","S202","S2.2","S0.05",
                               "22.02","S2.002","S2?02","1","S7.02"};
        
        // Vérification du prédicat de la méthode
        for(int i = 0;i < jeuTestsId.length; i++) {
            // Vérification du prédicat de la méthode
            assertFalse(Sae.estValide(regexSae, jeuTestsId[i]));
        }
    }   
    
    /**
     * Vérifie la levée de l'exception de la méthode estValide
     *
     */
    @Test
    void jeteeExceptionEstValide() {
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli",""));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S1.00"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","R2.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S202"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S2.2"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S0.05"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","22.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S2.002"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S2?02"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","1"));
        assertThrows(IllegalArgumentException.class, ()-> new Sae("devAppli","S7.02"));
    }
}
