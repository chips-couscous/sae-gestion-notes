/*
 * Serveur.java                                      14 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            // TODO enlever le decryptage pour echanger la cle
            byte[] buffer = new byte[TAILLE_BLOC_DONNEES];
            int tailleBlocEnvoye;
            while ((tailleBlocEnvoye = in.read(buffer)) != -1) {
                fileOut.write(decryptage(buffer), 0, tailleBlocEnvoye);
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
    private static byte[] decryptage(byte[] donneesCryptees) {
        
        final String cle = "GeStIoNnOtEs";
        
        /* Pour l'instant cryptage et décryptage bidon, juste phase de test */
        for (int i = 0; i < donneesCryptees.length; i++) {
            donneesCryptees[i] -= cle.charAt(i%cle.length());;
        }
        
        return donneesCryptees;
    }
}
