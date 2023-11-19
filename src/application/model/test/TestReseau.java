/*
 * TestReseau.java                                      19 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model.test;

import application.model.Reseau;

/** 
 * Classe de test de la classe Reseau
 * 
 * @author tom
 *
 */
public class TestReseau {

    /**
     * Méthode de test de estPremier
     *
     */
    private void testEstPremier() {
        
        for (int i = 0; i < 200; i++) {
            if (Reseau.estPremier(i)) {
                System.out.println(i + " est premier");
            }
        }
    }
    
    /**
     * Méthode de test de la classe estGenerateur
     *
     */
    private void testEstGenerateur() {
        for (int i = 2; i < 7; i++) {
            if (Reseau.estGenerateur(7,i)) {
                System.out.println(i + " est un générateur de " + 7);
            }
        }
    }
    
    /**
     * Méthode principal, appelle les différentes méthodes de test
     * @param args
     */
    public static void main(String[] args) {
//        testEstPremier();
//        testEstGenerateur();
        Reseau.envoyer("127.0.0.1", 8064, "test.csv");
    }
}
