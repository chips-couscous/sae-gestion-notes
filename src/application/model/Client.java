/*
 * Client.java                                      14 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

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
            String cleChiffrement = generationCle(socket,out); // génération de la clé de chiffrement
            // while ((tailleBlocEnvoye = fileIn.read(buffer)) != -1) {
            //     out.write(crypter(buffer, cleChiffrement), 0, tailleBlocEnvoye);
            //     System.out.println("Envoie d'un bloc");
            //     System.out.println("Taille du bloc : " + tailleBlocEnvoye);
            // }

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
    private static String generationCle(Socket serveur, BufferedOutputStream sortieServeur) {
        /* Génération aléatoire des données nécessaire à l'échange de Diffe-Hellman */
        int modulo = (int)(Math.random()*1000);
        double a = Math.random()*modulo;
        double g = Math.random()*modulo;
        double gPuissanceA = Math.pow(g,a);
        
        // TODO réussir a envoyer et récupérer les valeurs a g et modulo
        byte[] donneesAEnvoyer = {(byte)'a',(byte)'b',(byte)'c'};
        for (byte carac : donneesAEnvoyer) {
            System.out.println(carac);
        }
        
        try {
            sortieServeur.write(donneesAEnvoyer,0,donneesAEnvoyer.length);
        } catch (IOException e) {
            System.err.println("Communication avec le serveur impossible");
        }
        return null;
    }
}
