/*
 * TestReseau.java                                      19 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model.test;

import java.io.IOException;

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
    private static void testEstPremier() {
        final int[] nbPremiers = {
                2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,
                83,89,97,101
        };
        final int[] nbNonPremiers = {
                1,6,8,9,10,15,24,35,45,48,50,57,58,62,66,72,78,86,93,99,102
        };
        int nbTestOk = 0;
        
        for (int i : nbPremiers) {
            if (Reseau.estPremier(i)) {
                nbTestOk ++;
            }
        }
        for (int i : nbNonPremiers) {
            if (!Reseau.estPremier(i)) {
                nbTestOk ++;
            }
        }
        if (nbTestOk == nbPremiers.length+nbNonPremiers.length) {
            System.out.println("Test estPremier validé avec succès");
        }
    }
    
    /**
     * Méthode de test de la classe estGenerateur
     * Vérifier les résultats à la main pour vérifier la validité du test
     *
     */
    private static void testEstGenerateur() {
        for (int i = 2; i < 7; i++) {
            if (Reseau.estGenerateur(7,i)) {
                System.out.println(i + " est un générateur de " + 7);
            }
        }
        for (int i = 2; i < 11; i++) {
            if (Reseau.estGenerateur(11,i)) {
                System.out.println(i + " est un générateur de " + 11);
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
        try {
            Reseau.envoyer("127.0.0.1", 8064, "csv/ParametresRessource(AImporter).csv");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
