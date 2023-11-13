/*
 * TestPortfolio.java                                      3 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import application.model.Portfolio;

/** TODO comment class responsibility (SRP)
 * @author tonyl
 *
 */
class TestPortfolio {

    /**
     * Teste le prédicat de la méthode estValide pour des identifiants d'enseignement valides 
     * {@link application.model.Portfolio#Portfolio(java.lang.String, java.lang.String)}.
     */
    @Test
    void idOk() {
        String regexPortfolio = "P[1-6]\\.\\d[1-9]";
        assertTrue(Portfolio.estValide(regexPortfolio, "P2.02"));
        assertTrue(Portfolio.estValide(regexPortfolio, "P6.99"));
        assertTrue(Portfolio.estValide(regexPortfolio, "P1.01"));
        assertTrue(Portfolio.estValide(regexPortfolio, "P4.55"));
    }
    
    /**
     * Teste le prédicat de la méthode estValide pour des identifiants d'enseignement invalides 
     * {@link application.model.Portfolio#Portfolio(java.lang.String, java.lang.String)}.     */
    @Test
    void idNOk() {
        String regexPortfolio = "P[1-6]\\.\\d[1-9]";
        String[] jeuTestsId = {"","P1.00","R2.02","P202","P2.2","P0.05",
                               "22.02","P2.002","P2?02","1","P7.02"};
        
        // Vérification du prédicat de la méthode
        for(int i = 0;i < jeuTestsId.length; i++) {
            // Vérification du prédicat de la méthode
            assertFalse(Portfolio.estValide(regexPortfolio, jeuTestsId[i]));
        }
    }   
    
    /**
     * Vérifie la levée de l'exception de la méthode estValide
     *
     */
    @Test
    void jeteeExceptionEstValide() {
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli",""));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P1.00"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","R2.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P202"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P2.2"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P0.05"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","22.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P2.002"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P2?02"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","1"));
        assertThrows(IllegalArgumentException.class, ()-> new Portfolio("devAppli","P7.02"));
    }
    
}
