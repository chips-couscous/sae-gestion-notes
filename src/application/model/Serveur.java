/*
 * Serveur.java                                      14 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/** TODO comment class responsibility (SRP)
 * @author tom.jammes
 */
public class Serveur {

    /** 
     * Méthode de test
     * @param args non utilisé
     */
    public static void main(String[] args) {
        recevoir(12345);
    }
    
    /** 
     * Méthode de reception d'un fichier
     * La communication entre les machines suit un système client / serveur
     * Cette méthode fait office de serveur
     * La machine qui veut envoyer le fichier fait office de client
     * Le serveur doit être lancé avant le client
     * 
     * @param port port sur lequel le serveur attend une connection
     * @param cheminFichier chemin du fichier a envoyer
     */
    public static void recevoir(int port) {
        
        final int TAILLE_BLOC_DONNEES = 1024;
        
        try {
            // Création d'un ServerSocket écoutant sur le port 12345
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Attente de connexion...");

            // Attente d'une connexion cliente
            Socket clientSocket = serverSocket.accept();

            System.out.println("Connexion établie.");

            // Obtention du flux d'entrée du client
            BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());

            // Création d'un fichier pour stocker le fichier CSV reçu
            File receivedFile = new File("fichierRecu.txt");
            FileOutputStream fileOut = new FileOutputStream(receivedFile);

            /* Reception et écriture du fichier
             * buffer contient le code ascii de chaque caractères
             */
            byte[] buffer = new byte[TAILLE_BLOC_DONNEES];
            int tailleBlocEnvoye;
            final String cle = generationCle(clientSocket);
            while ((tailleBlocEnvoye = in.read(buffer)) != -1) {
                fileOut.write(decryptage(cle,buffer), 0, tailleBlocEnvoye);
                System.out.println("Reception d'un bloc");
                System.out.println("Taille du bloc : " + tailleBlocEnvoye);
            }

            System.out.println("Fichier reçu et enregistré : " + receivedFile.getAbsolutePath());

            // Fermeture des flux et des sockets
            in.close();
            fileOut.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * Méthode de décryptage d'un tableau de byte contenant des code ascii a crypté
     * La méthode de cryptage utilisé est une méthode de Vigenère
     * 
     * @param donnees tableau de byte a décrypter
     * @return le tableau de byte décrypté
     */
    private static byte[] decryptage(String cle, byte[] donneesCryptees) {
        
        /* Pour l'instant cryptage et décryptage bidon, juste phase de test */
//        for (int i = 0; i < donneesCryptees.length; i++) {
//            donneesCryptees[i] -= cle.charAt(i%cle.length());;
//        }
        
        return donneesCryptees;
    }
    
    /** 
     * Génère une clé pour un chiffrement de Vigenère
     * Génère la clé de manière aléatoire grâce a un échange de Diffie-Hellman
     * avec le client
     *
     * @param socket du serveur pour l'échange de Diffie-Hellman
     * @return clé pour un chiffrement de Vigenère
     */
    private static String generationCle(Socket client) {
        
        int p; // généré et transmis par le client
        int g; // généré et transmis par le client
        int gPuissanceA; // généré et transmis par le client
        String cle = "";
        
        System.out.println("Génération de la clé");
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            
            /* Réception des données du client */
            p = Integer.parseInt(in.readLine());
            g = Integer.parseInt(in.readLine());
            gPuissanceA = Integer.parseInt(in.readLine());
            
            /* Génération aléatoire des données nécessaire à l'échange de Diffe-Hellman */
            int b = (int)(1 + Math.random()*(p - 1));
            int gPuissanceB = puissanceModulo(g,b,p);
            
            /* Envoie de données au client */
            out.println(gPuissanceB);
            
            int gPuissanceAB = puissanceModulo(gPuissanceA,b,p) % p;
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
}
