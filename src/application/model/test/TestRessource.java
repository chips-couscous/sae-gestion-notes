/*
 * TestRessource.java                                      2 nov. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import application.model.Ressource;

/** TODO comment class responsibility (SRP)
 * @author tonyl
 *
 */
class TestRessource {

    /**
     * Teste le prédicat de la méthode estValide pour des identifiants de ressource valides 
     * {@link application.model.Ressource#Ressource(java.lang.String, java.lang.String)}.
     */
    @Test
    void idOk() {
        String regexSae = "R[1-6]\\.\\d[1-9]";
        assertTrue(Ressource.estValide(regexSae, "R2.02"));
        assertTrue(Ressource.estValide(regexSae, "R6.99"));
        assertTrue(Ressource.estValide(regexSae, "R1.01"));
        assertTrue(Ressource.estValide(regexSae, "R4.55"));
    }
    
    /**
     * Teste le prédicat de la méthode estValide pour des identifiants de ressources invalides 
     * {@link application.model.Ressource#Ressource(java.lang.String, java.lang.String)}.     */
    @Test
    void idNOk() {
        String regexSae = "R[1-6]\\.\\d[1-9]";
        String[] jeuTestsId = {"","R1.00","S2.02","R202","R2.2","R0.05",
                               "22.02","R2.002","R2?02","1","R7.02"};
        
        // Vérification du prédicat de la méthode
        for(int i = 0;i < jeuTestsId.length; i++) {
            // Vérification du prédicat de la méthode
            assertFalse(Ressource.estValide(regexSae, jeuTestsId[i]));
        }
    }   
    
    /**
     * Vérifie la levée de l'exception de la méthode estValide
     *
     */
    @Test
    void jeteeExceptionEstValide() {
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli",""));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R1.00"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","S2.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R202"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R2.2"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R0.05"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","22.02"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R2.002"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R2?02"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","1"));
        assertThrows(IllegalArgumentException.class, ()-> new Ressource("devAppli","R7.02"));
    }

}
