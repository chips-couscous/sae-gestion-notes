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
 * @author tom.jammes
 *
 */
public class TestReseau {
    
    /**
     * Méthode de test de puissanceModulo
     *
     */
    private static void testPuissanceModulo() {
        System.out.println("===== TEST puissanceModulo =====");
        int nbTestOk = 0;
        /* test avec les valeur 3^6 dans Z/10Z */
        if (Reseau.puissanceModulo(3,6,10) == 9) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 3^6 dans Z/10Z");
        }
        /* test avec les valeur 3^9 dans Z/10Z */
        if (Reseau.puissanceModulo(3,9,10) == 3) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 3^9 dans Z/10Z");
        }
        /* test avec les valeur 3^6 dans Z/7Z */
        if (Reseau.puissanceModulo(3,6,7) == 1) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 3^6 dans Z/7Z");
        }
        /* test avec les valeur 5^6 dans Z/8Z */
        if (Reseau.puissanceModulo(5,6,8) == 1) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 5^6 dans Z/8Z");
        }
        /* test avec les valeur 7^5 dans Z/13Z */
        if (Reseau.puissanceModulo(7,5,13) == 11) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 7^5 dans Z/13Z");
        }
        /* test avec les valeur 7^5 dans Z/13Z */
        if (Reseau.puissanceModulo(7,5,13) != 12) {
            nbTestOk++;
        } else { 
            System.err.println("Test échoué pour 7^5 dans Z/13Z");
        }
        if (nbTestOk == 6) {
            System.out.println("Test validé avec succés");
        }
    }

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
        System.out.println("===== TEST estPremier =====");
        if (nbTestOk == nbPremiers.length+nbNonPremiers.length) {
            System.out.println("Test validé avec succès");
        }
    }

    /**
     * Méthode de test de la classe estGenerateur
     * Vérifier les résultats à la main pour vérifier la validité du test
     *
     */
    private static void testEstGenerateur() {
        System.out.println("===== TEST estGenerateur =====");
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
        testPuissanceModulo();
        testEstPremier();
        testEstGenerateur();
//        try {
//            Reseau.envoyer("127.0.0.1", "csv/ParametresRessource(AImporter).csv");
//        } catch (IOException e) {
//            System.err.println("Erreur de communication");
//        }
    }
}
