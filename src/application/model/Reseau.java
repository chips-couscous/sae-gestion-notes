/*
 * Reseau.java                                      19 nov. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;

/** 
 * Classe regroupant un ensemble de méthode permattant de faire des échange 
 * de fichier via le réseau
 * Les échanges fonctionnent via un système client / serveur
 * 
 * @author tom
 *
 */
public class Reseau {
    
    /* Dictionnaire utilisé pour le chiffrement des données */
    final static HashMap<Character,Integer> dictionnaireCryptage = new HashMap<>();    
    
    final static int TAILLE_BLOC_DONNEES = 1024; 
    
    /**
     * Méthode utilisé pour les test, fait office de serveur
     * @param args
     */
    public static void main(String[] args) {
        try {
            recevoir(8064, "Z:\\SAE\\WorkspaceEclipseSae\\GestionNotes\\csv\\fichierRecu.csv");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
     * @throws IOException si le serveur est introuvable
     */
    public static void envoyer(String ip, int port, String cheminFichier) throws IOException {
        
        definirDictionnaire();
        
        try {
            
            // Connexion au serveur
            Socket serveur = new Socket(ip, port);
            System.out.println("Client : connecté au serveur");

            // Obtention du flux de sortie vers le serveur
            BufferedOutputStream out = new BufferedOutputStream(serveur.getOutputStream());

            // Sélection du fichier CSV à envoyer
            FileInputStream fileIn = new FileInputStream(cheminFichier);
            System.out.println("Client : récuperation du fichier a envoyer");

            /* Lecture et envoi du fichier
             * buffer contient le code ascii de chaque caractères
             */
            byte[] buffer = new byte[TAILLE_BLOC_DONNEES];
            int tailleBlocEnvoye;
            String cleChiffrement = generationCleClient(serveur); // génération de la clé de chiffrement
            while ((tailleBlocEnvoye = fileIn.read(buffer)) != -1) {
                out.write(crypter(buffer, cleChiffrement), 0, tailleBlocEnvoye);
                System.out.println("Client : Envoie d'un bloc de " + tailleBlocEnvoye 
                        + " octets");
            }

            System.out.println("Client : Fichier envoyé : " + cheminFichier);

            // Fermeture des flux et de la socket
            out.close();
            fileIn.close();
            serveur.close();
        } catch (IOException e) {
            System.err.println("Client : Connexion au serveur impossible "
                    + e.getMessage());
            throw e;
        }
    }
   
    /** 
     * Méthode de reception d'un fichier
     * La communication entre les machines suit un système client / serveur
     * Cette méthode fait office de serveur
     * La machine qui veut envoyer le fichier fait office de client
     * Le serveur doit être lancé avant le client
     * 
     * @param port port sur lequel le serveur attend une connection
     * @param cheminReceptionFichier chemin ou sera écrit le fichier reçu
     * @param cheminFichier chemin du fichier a envoyer
     * @throws IOException si il y a un problème de connexion avec le client
     */
    public static void recevoir(int port, String cheminReceptionFichier) throws IOException {
        
        definirDictionnaire();
        
        try {
            // Création d'un ServerSocket écoutant sur le port 12345
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Serveur : Attente de connexion...");
            serverSocket.setSoTimeout(15000);

            // Attente d'une connexion client
            Socket clientSocket = serverSocket.accept();

            System.out.println("Serveur : Connexion établie avec le client");

            // Obtention du flux d'entrée du client
            BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());

            // Création d'un fichier pour stocker le fichier CSV reçu
            File receivedFile = new File(cheminReceptionFichier);
            FileOutputStream fileOut = new FileOutputStream(receivedFile);

            /* Reception et écriture du fichier
             * buffer contient le code ascii de chaque caractères
             */
            byte[] buffer = new byte[TAILLE_BLOC_DONNEES];
            int tailleBlocEnvoye;
            final String cle = generationCleServeur(clientSocket);
            while ((tailleBlocEnvoye = in.read(buffer)) != -1) {
                fileOut.write(decrypter(cle,buffer), 0, tailleBlocEnvoye);
                System.out.println("Serveur : Réception d'un bloc de " + tailleBlocEnvoye
                        + " octets");
            }

            System.out.println("Serveur : Fichier reçu et enregistré : " + receivedFile.getAbsolutePath());

            // Fermeture des flux et des sockets
            in.close();
            fileOut.close();
            clientSocket.close();
            serverSocket.close();

        } catch (SocketTimeoutException e) {
            System.err.println("Serveur : Aucun client trouvé");
            throw new SocketTimeoutException("Aucun client trouvé");
        } catch (SocketException e) {
            System.err.println("Serveur : Le client a quitté l'échange");
            throw new SocketException("Le client a quitté l'échange, aucun fichier transmis");
        }
    }
    
    /** 
     * Méthode de cryptage d'un tableau de byte contenant des code ascii a crypter
     * La méthode de cryptage utilisé est une méthode de Vigenère
     * 
     * @param donnees tableau de byte a crypter
     * @param cle clé de chiffrement
     * @throws NullPointerException si un caractère ne peut pas être chiffré 
     * @return le tableau de byte crypté
     */
    private static byte[] crypter(byte[] donnees, String cle) throws NullPointerException {
        byte caractere;
        
        /* Parcours des données a crypter */ 
        for (int i = 0; i < donnees.length && donnees[i] != 0; i++) {
            
            /* Commentaires ci-dessous = affichage des caractères et diverses informations 
             * nécessaire a la vérification de la validité du cryptage 
             */
//            System.out.print("Donnees = " + donnees[i] + " code = "     
//                    + dictionnaireCryptage.get((char)donnees[i]) + " cle = " 
//                    + cle.charAt(i%cle.length()) + " code "                  
//                    + dictionnaireCryptage.get(cle.charAt(i%cle.length())));
            
            /* Chiffrement */
            caractere = (byte) ((dictionnaireCryptage.get((char)donnees[i]) // Récupération du code du caractère a chiffrer
                    + dictionnaireCryptage.get(cle.charAt(i%cle.length()))) //  Récupération du code du caractère de la clé
                    % dictionnaireCryptage.size());
            
//            System.out.println(" cryptage = " + toCaractere(caractere));
          
            donnees[i] = (byte)toCaractere(caractere);
        }
      
        return donnees;
    }

    /** 
     * Méthode de décryptage d'un tableau de byte contenant des code ascii a crypté
     * La méthode de cryptage utilisé est une méthode de Vigenère
     * 
     * @param donnees tableau de byte a décrypter
     * @return le tableau de byte décrypté
     */
    private static byte[] decrypter(String cle, byte[] donneesCryptees) {
        byte caractere;
        
        /* Parcours des données a décrypter */ 
        for (int i = 0; i < donneesCryptees.length && donneesCryptees[i] != 0; i++) {
            /* Déchiffrement */
            caractere = (byte) (dictionnaireCryptage.get((char)donneesCryptees[i]) 
                      - dictionnaireCryptage.get(cle.charAt(i%cle.length())));
            /* Effectue le modulo */
            while (caractere < 0) {
                caractere += dictionnaireCryptage.size();
            }
              
            donneesCryptees[i] = (byte)toCaractere(caractere);
        }
        
        return donneesCryptees;
    }
    
    /** 
     * Génère une clée pour un chiffrement de Vigenère
     * Génère la clé de manière aléatoire grâce a un échange de Diffie-Hellman
     * avec le serveur
     *
     * @param socket du serveur avec qui l'échange de Diffie-Hellman est effectué
     * @return clé pour un chiffrement de Vigenère
     */
    private static String generationCleClient(Socket serveur) {
       
        /* Génération aléatoire des données nécessaire à l'échange de Diffe-Hellman */
        int p;
        int g;
        int a;
        int gPuissanceA;
        int gPuissanceB;
        int tailleCle = 8;
        String cle = ""; 
        
        try {
            System.out.println("Client : Génération de la clé de chiffrement");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(serveur.getInputStream()));
            PrintStream out = new PrintStream(serveur.getOutputStream());
            
            /* Génération des différents caractères de la clé */
            for (int i = 0; i < tailleCle; i++) {
                do {
                    p = (int)(1000 + Math.random()*9000);
                } while (!estPremier(p));
                
                do {
                    g = (int)(Math.random()*p);
                } while (!estGenerateur(p,g));
                
                a = (int)(1 + Math.random()*(p - 1));
                gPuissanceA = puissanceModulo(g,a,p);
                
                out.println(p);
                out.println(g);
                out.println(gPuissanceA);
                
                /* Réception de données du serveur */
                gPuissanceB = Integer.parseInt(in.readLine());
                
                /* Génération du caractère */
                int gPuissanceAB = puissanceModulo(gPuissanceB, a, p);
                char caractere;
                caractere = (char)(65 + gPuissanceAB % 26);
                cle += caractere;
            }
            
            System.out.println("Client : clé = " + cle);
        } catch (IOException e) {
            System.err.println("Communication avec le serveur impossible");
            cle = null;
        }
        return cle;
    }

    /** 
     * Génère une clé pour un chiffrement de Vigenère
     * Génère la clé de manière aléatoire grâce a un échange de Diffie-Hellman
     * avec le client
     *
     * @param socket du serveur pour l'échange de Diffie-Hellman
     * @return clé pour un chiffrement de Vigenère
     */
    private static String generationCleServeur(Socket client) {
        
        int p; // généré et transmis par le client
        int g; // généré et transmis par le client
        int gPuissanceA; // généré et transmis par le client
        String cle = "";
        int tailleCle = 8;
        
        System.out.println("Génération de la clé de chiffrement");
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            
            /* Génération des différents caractères de la clé */
            for (int i = 0; i < tailleCle; i++) {
                p = Integer.parseInt(in.readLine());
                g = Integer.parseInt(in.readLine());
                gPuissanceA = Integer.parseInt(in.readLine());
                
                /* Génération aléatoire des données nécessaire à l'échange de Diffe-Hellman */
                int b = (int)(1 + Math.random()*(p - 1));
                int gPuissanceB = puissanceModulo(g,b,p);
                
                /* Envoie de données au client */
                out.println(gPuissanceB);
                
                /* Génération du caractère */
                int gPuissanceAB = puissanceModulo(gPuissanceA,b,p);
                char caractere;
                caractere = (char)(65 + gPuissanceAB % 26);
                cle += caractere;
            }
            
            System.out.println("Serveur : clé = " + cle);
        } catch (IOException e) {
            System.err.println("Communication avec le client impossible");
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
    public static int puissanceModulo(int g, int a, int p) {
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
    public static boolean estGenerateur(int p, int g) {
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
    public static boolean estPremier(int i) {
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
    
    /**
     * Génère la HashMap contenant le dictionnaire de caractère utilisés
     * pour le chiffrement des données 
     * dictionnaireCryptage contient en clé des caractère sous forme de char
     * et en valeur des int
     */
    private static void definirDictionnaire() {
        
        /* Ajout de l'alphabet minuscule */
        char caractere = 'a';
        for (int i = 0; i < 26; i++) {
            dictionnaireCryptage.put(caractere, i);
            caractere++;
        }
        /* Ajout de l'alphabet majuscule */
        caractere = 'A';
        for (int i = 26; i < 52; i++) {
            dictionnaireCryptage.put(caractere, i);
            caractere++;
        }
        /* Ajout des chiffres */
        caractere = '0';
        for (int i = 52; i < 62; i++) {
            dictionnaireCryptage.put(caractere, i);
            caractere++;
        }
        /* Ajout des caracteres spéciaux */
        dictionnaireCryptage.put((char)-61, 62); // Chiffrage des caractères accentués
        dictionnaireCryptage.put((char)-87, 63); // Chiffrage du é
        dictionnaireCryptage.put((char)-88, 64); // Chiffrage du è
        dictionnaireCryptage.put((char)-96, 65); // Chiffrage du à
        dictionnaireCryptage.put((char)-89, 66); // Chiffrage du ç
        dictionnaireCryptage.put((char)-71, 67); // Chiffrage du ù
        dictionnaireCryptage.put((char)-76, 68); // Chiffrage du ô
        dictionnaireCryptage.put('/', 69);
        dictionnaireCryptage.put('\'', 70);
        dictionnaireCryptage.put(' ', 71);
        dictionnaireCryptage.put('.', 72);
        dictionnaireCryptage.put(';', 73);
        dictionnaireCryptage.put('-', 74);
        dictionnaireCryptage.put('(', 75);
        dictionnaireCryptage.put(')', 76);
        dictionnaireCryptage.put((char)10, 77); // Chiffrage du saut de ligne
        dictionnaireCryptage.put((char)13, 78); // Chiffrage du retour chariot
        dictionnaireCryptage.put((char)-65, 79); // Chiffrage de trois caractères
        dictionnaireCryptage.put((char)-17, 80); // invisible présent au tout e
        dictionnaireCryptage.put((char)-69, 81); //début d'u fichier ParametreSemestre
    }
    
    /** 
     * Renvoie le caractère associé à la valeur du dictionnaire de caractère 
     * utilisé pour le chiffrage de données
     * @param code code du caractère a récupérer
     * @return le caractère correspondant ou 0 si aucun caractère ne correspond
     */
    private static char toCaractere(byte code) {
        
        for(char caractere : dictionnaireCryptage.keySet()) {
            if (dictionnaireCryptage.get(caractere) == code) {
                return caractere;
            }
        }
        
        return 0;
    }
}