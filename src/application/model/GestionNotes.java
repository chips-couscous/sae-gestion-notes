/*
 * GestionNotes.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Modèle de l'application GestionNotes
 * 
 * @author tom.jammes
 */
public class GestionNotes {

    private static HashMap<Enseignement,ArrayList<Controle>> enseignements = new HashMap<>();
    
    /** TODO comment field role (attribute, association) */
    public static List<Competence> competenceSemestre = new ArrayList<>();

    static Utilisateur identite = new Utilisateur("Nom", "Prenom");

    /**
     * Crée une note à partir des données reçu par le contrôleur et l'ajoute à la
     * liste notes
     * 
     * @param valeur       valeur de la note
     * @param denominateur valeur sur la quelle la note est évalué, exemple: x/y
     *                     avec x la note y le dénominateur,0 <= x, x <= y et y !=
     *                     0...
     * @param nom          nom du controle dans le quel la note a été obtenue
     * @param poids        poids de la note dans l'enseignement auquel elle
     *                     appartient
     * @param forme        type du contrôle, exemple: devoir sur table, tp noté,
     *                     qcm, ...
     * @param description  description du contrôle donné par l'élève
     * @param date         date du contrôle. Peut être approximative, exemple début
     *                     janvier
     * @return true si le contrôle a pu être ajouté
     */
    public static boolean ajouterControle(double valeur, int denominateur, 
            String nom, int poids, String forme,String description, String date) {

        try {
            String idEnseignement = nom.substring(0, 4);
            Controle note = new Controle(valeur, denominateur, poids, forme, description, date);
            Enseignement matiereControle = verifierEnseignement(idEnseignement);
            
            if (!enseignements.containsKey(matiereControle)) {
                return false;
            }
            
            ArrayList<Controle> listeControle = enseignements.get(matiereControle);
            listeControle.add(note);
            
            enseignements.put(matiereControle,listeControle);
            
            return true;
        } catch (IllegalArgumentException erreur) {
            return false;
        }
    }
    /* TODO Ajouter une méthode pour ajouter tous les enseignements dans la hashmap */
    /* TODO ajouter une méthode qui renvoie le nombre de notes */
    
    /** TODO comment method role
     * @param idEnseignement
     * @return
     */
    private static Enseignement verifierEnseignement(String idEnseignement) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * TODO comment method role
     * @param competenceAAjouter
     * @return 0 
     */
    public static boolean ajouterCompetence(Competence competenceAAjouter) {
       try {
           competenceSemestre.add(competenceAAjouter);
           return true;
       } catch (Exception e) {
        // TODO: handle exception
           return false;
       }
    }

    /**
     * TODO comment method role
     * 
     * @param nom
     * @param prenom
     */
    public static void changerIdentite(String nom, String prenom) {
        identite.setNom(nom);
        identite.setPrenom(prenom);
    }

    /**
     * TODO comment method role
     * @return 0
     */
    public static String afficherIdentite() {
        return identite.getNom() + " " + identite.getPrenom();
    }
    
    /**
     * TODO comment method role
     *
     */
    public static void afficherCompetence() {
        System.out.println("Compétence : ");
        for (int i = 0; i < competenceSemestre.size(); i++) {
            System.out.println(competenceSemestre.get(i).toString());
        }
    }
}