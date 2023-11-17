/*
 * Client.java                                      14 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;

/** 
 * Classe de test de communication réseau
 * Cette classe représente le client d'une application client/serveur
 * 
 * @author tom.jammes
 */
public class Client {
    
    /** 
     * Méthode de test 
     * @param args non utilisé
     */
    public static void main(String[] args) {
        envoyer("127.0.0.1",12345, "testReseau.txt");
    }
    
    /** 
     * Méthode d'envoie d'un fichier
     * La communication entre les machines suit un système client / serveur
     * Cette méthode fait office de client
     * La machine qui veut recevoir le fichier fait office de serveur
     * Le serveur doit être lancé avant le client
     * 
     * @param ip IP de la machine qui prend le rôle de serveur 
     * @param port port sur lequel le client se connecte au serveur
     * @param cheminFichier chemin du fichier a envoyer
     */
    public static void envoyer(String ip, int port, String cheminFichier) {
        
        final int TAILLE_BLOC_DONNEES = 1024;
        
        /* Test de estPremier */
        for (int i = 0; i < 200; i++) {
            if (estPremier(i)) {
                System.out.println(i + " est premier");
            }
        }
        
        /* Test de estGénérateur */
        for (int i = 2; i < 7; i++) {
            if (estGenerateur(7,i)) {
                System.out.println(i + " est un générateur de " + 7);
            }
        }
      try {
            
          // Connexion au serveur
          Socket socket = new Socket(ip, port);

          // Obtention du flux de sortie vers le serveur
          BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

          // Sélection du fichier CSV à envoyer
          FileInputStream fileIn = new FileInputStream(cheminFichier);

          /* Lecture et envoi du fichier
           * buffer contient le code ascii de chaque caractères
           */
          byte[] buffer = new byte[TAILLE_BLOC_DONNEES];
          int tailleBlocEnvoye;
          String cleChiffrement = generationCle(socket); // génération de la clé de chiffrement
          while ((tailleBlocEnvoye = fileIn.read(buffer)) != -1) {
               out.write(crypter(buffer, cleChiffrement), 0, tailleBlocEnvoye);
               System.out.println("Envoie d'un bloc");
               System.out.println("Taille du bloc : " + tailleBlocEnvoye);
           }

          System.out.println("Fichier envoyé : " + cheminFichier);

          // Fermeture des flux et de la socket
          out.close();
          fileIn.close();
          socket.close();

      } catch (IOException e) {
          System.err.println("Connexion au serveur impossible");
      }
    }
    
    /** 
     * Méthode de cryptage d'un tableau de byte contenant des code ascii a crypter
     * La méthode de cryptage utilisé est une méthode de Vigenère
     * 
     * @param donnees tableau de byte a crypter
     * @param cle clé de chiffrement
     * @return le tableau de byte crypté
     */
    public static byte[] crypter(byte[] donnees, String cle) {
        
        /* Pour l'instant cryptage et décryptage bidon, juste phase de test */
        for (int i = 0; i < donnees.length; i++) {
            donnees[i] += cle.charAt(i%cle.length());
        }
      
        return donnees;
    }

    /** 
     * Génère une clée pour un chiffrement de Vigenère
     * Génère la clé de manière aléatoire grâce a un échange de Diffie-Hellman
     * avec le serveur
     *
     * @param socket du serveur pour l'échange de Diffie-Hellman
     * @return clée pour un chiffrement de Vigenère
     */
    private static String generationCle(Socket serveur) {
        /* Génération aléatoire des données nécessaire à l'échange de Diffe-Hellman */
        int p = 9739;
        int g = 1527;
        int a = (int)(1 + Math.random()*(p - 1));
        int gPuissanceA = puissanceModulo(g,a,p) % p;
        int gPuissanceB;
        String cle = "";
        
        System.out.println( "Génération de la clé");
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serveur.getInputStream()));
            PrintStream out = new PrintStream(serveur.getOutputStream());
            
            /* Envoie de données au serveur */
            out.println(p);
            out.println(g);
            out.println(gPuissanceA);
            
            /* Réception de données du serveur */
            gPuissanceB = Integer.parseInt(in.readLine());
            
            // TODO séparer dans une méthode l'échange de Diffie-Helman pour 
            // générer aléatoirement une clé de 3 a 5 lettres
            int gPuissanceAB = puissanceModulo(gPuissanceB, a, p);
            char caractere;
            caractere = (char)(65 + gPuissanceAB % 26);
            cle += caractere;
            
            System.out.println(cle);
        } catch (IOException e) {
            System.err.println("Communication avec le serveur impossible");
            cle = null;
        }
        return cle;
    }

    /** 
     * Calcule g puissance a dans l'ensemble Z/pZ
     * 
     * @param g int dont ont calcul la puissance 
     * @param a exposant
     * @param p ensemble du calcul
     * @return resultat de g puissance a dans l'ensemble Z/pZ
     */
    private static int puissanceModulo(int g, int a, int p) {
        /* TODO optimiser avec méthode plus efficace */
        int resultat = g;
        for (int i = 1; i < a; i++) {
            resultat *= g;
            resultat %= p;
        }
        return resultat;
    }
    
    /**
     * Vérifie que g est une classe générateur de p
     * 
     * @param p ensemble dont ont cherche un générateur
     * @param g classe a tester
     * @return true si g est une classe générateur de p
     */
    private static boolean estGenerateur(int p, int g) {
        HashSet<Integer> classes = new HashSet<>();
        
        if (g >= p || g <= 1) {
            return false;
        }
        
        for (int i = 1; i < p; i++) {
            classes.add(puissanceModulo(g,i,p));
        }
                
        return classes.size() == p-1;
    }
    
    /** 
     * Détermine si un nombre entier positif est premier
     * Un nombre est premier si il est divisible seulement par 1 et lui même
     * 1 et 0 ne sont pas premier
     * 
     * @param i nombre à tester
     * @return true si i est premier
     */
    private static boolean estPremier(int i) {
        boolean estPremier = true;
        
        if (i == 2) {
            return true;
        } else if (i <= 1) {
            return false;
        }
        
        for (int k = 2; k < i && estPremier; k++) {
            if (i%k == 0) {
                estPremier = false;
            }
        }
        
        return estPremier;
    }

}
