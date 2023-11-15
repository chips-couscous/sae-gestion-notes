/* 
 * ParametresSemestre.java                                            20.10.2023
 * IUT de Rodez, But Informatique 2, Chips-Couscous pas de copyright
 */
package application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.model.exception.ParametresSemestreException;

/** 
 * Classe Semestre qui gère tous les enseignements, toutes les compétences et 
 * les différents controles qui composent un semestre.
 * @author thomas.lemaire
 */
public class Semestre {

    // Numéro du semestre actif
    private int numero;
    // Parcours du semestre actif
    private char parcours;
    
    /* Contient les enseignements et les contrôles de cet enseignement */
    private HashMap<Enseignement, List<Controle>> listeControle = new HashMap<Enseignement, List<Controle>>();
    /* Lie aux enseignements, les compétences et les poids aux quels ils sont associés */
    private HashMap<Enseignement, List<Object[]>> listeEnseignement = new HashMap<Enseignement, List<Object[]>>(); 
    
    /**
     * Constructeur du semestre
     * @param numeroSemestre
     * @param parcoursSemestre
     * @throws ParametresSemestreException 
     */
    public Semestre(int numeroSemestre, String parcoursSemestre) 
            throws ParametresSemestreException {
        // Validation des données
       if(!semestreEstValide(numeroSemestre, parcoursSemestre)) {
           throw new ParametresSemestreException("Le semestre est invalide");
       }
       numero = numeroSemestre;
       parcours = parcoursSemestre.toLowerCase().charAt(0);
    }
    
    
    /**
     * Un semestre est considéré comme valide si :
     * son numéro est compris entre 0 et 7
     * et son parcours correspond à ces chaines ("tous", "a", "b", "c", "d")
     * @param numero du semestre
     * @param parcours du semestre
     * @return true si tous les éléments sont valides, false sinon
     */
    private boolean semestreEstValide(int numero, String parcours) {
        parcours = parcours.toLowerCase();
        return     numero > 0 && numero < 7
               && (parcours.equals("tous") || parcours.equals("néant") ||
                   parcours.equals("a")    || parcours.equals("b")     ||
                   parcours.equals("c")    || parcours.equals("d")); 
    }
    
    /**
     * Cette méthode permet d'ajouter une compétence à un enseignement dans la
     * HashMap listeEnseignement pour ensuite récupérer une liste de compétences
     * pour chaque enseignement.
     * @param enseignement , l'enseignement pour lequel on veut associer la competence
     * @param competence , la competence à associer à l'enseignement
     * @param poids , le poids de l'enseignement dans la competence
     * @return true si l'enseignement a bien été ajouté, false sinon
     */
    public boolean ajouterCompetenceAEnseignement(Enseignement enseignement,
                                                  Competence competence,
                                                  int poids) {
        List<Object[]> listeCompetence;
        
        Object[] valeurCompetence = {competence, poids};
        
        listeCompetence = listeEnseignement.get(enseignement);
        listeCompetence.add(valeurCompetence);
        
        try {
            listeEnseignement.put(enseignement, listeCompetence);
        } catch (IllegalArgumentException e) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Cette méthode permet d'ajouter un control à un enseignement dans la HashMap
     * listeControle pour ensuite récupérer une liste de controles pour chaque
     * enseignement.
     * @param enseignement , l'enseignement pour lequel on veut associer le controle
     * @param controle , le controle à associer à l'enseignement
     * @return true si le controle a bien été associé, false sinon
     */
    public boolean ajouterControleAEnseignement(Enseignement enseignement,
                                                Controle controle) {
        List<Controle> controleEnseignement;
        
        controleEnseignement = listeControle.get(enseignement);
        controleEnseignement.add(controle);
        
        try {
            listeControle.put(enseignement, controleEnseignement);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    /**
     * Cette méthode permet de vérifier si un enseignement est déjà renseigné ou
     * non dans chaque HashMap
     * @param idEnseignement , est l'identifiant qui permet de connaître 
     *        l'enseignement dont on veut connaître sa présence 
     * @return l'enseignement dont on connaît sa présence, null si l'identifiant
     *         ne correspond à aucun enseignement dans la HashMap
     */
    public Enseignement verifierEnseignementPresent(String idEnseignement) {
        // parcours de tous les enseignement dans la HashMap listeEnseignement
        for (Enseignement enseignement : listeEnseignement.keySet()) {
            if(enseignement.getIdEnseignement().equals(idEnseignement)) {
                return enseignement;
            }
        }
        return null;
    }
    
    /**
     * Crée une note à partir des données rentré par l'utilisateur
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
     * @return true si la note a pu être ajouté
     */
    public boolean ajouterNoteAControle(double valeur, int denominateur, 
            String nom, String description, String date) {

        try {
            String idEnseignement = nom.substring(0, 4);
            String formeControle = nom.substring(4);
            Enseignement matiereControle = verifierEnseignementPresent(idEnseignement);
            
            if (!listeControle.containsKey(matiereControle)) {
                return false;
            }
            
            List<Controle> controles = listeControle.get(matiereControle);
            /* TODO Comparer les controles avec la note puis set les valeurs du controle 
             * Il faut un identifiant de note  
             */
            for (Controle controle : controles) {
                if (controle.getForme() == formeControle) {
                    
                }
            }
            
            listeControle.put(matiereControle,controles);
            
            return true;
        } catch (IllegalArgumentException erreur) {
            return false;
        }
    }
    
    /**
     * TODO comment method role
     * Cette méthode consiste à renseigner un enseignement en tant que nouvelle
     * clé pour chaque HashMap : listeEnseignement, listeControle
     * @param enseignement , est l'enseignement qui servira de clé pour les
     *        différentes HashMap
     * @return true si l'enseignement à pu être renseigner dans les HashMap,
     *         false sinon.
     */
    public boolean ajouterEnseignement(Enseignement enseignement) {
        if(!listeEnseignement.containsKey(enseignement)) {
            List<Object[]> listeCompetence = new ArrayList<Object[]>();
            List<Controle> controles = new ArrayList<Controle>();
            listeEnseignement.put(enseignement, listeCompetence);
            listeControle.put(enseignement, controles);
            return true; 
        }
        return false;
    }
    
    /** TODO comment method role
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Semestre : ").append(numero).append(" / Parcours : ")
        .append(parcours).append("\n");

        for (Enseignement enseignement : listeEnseignement.keySet()) {
            sb.append("Enseignement: ").append(enseignement.getIntitule())
            .append(" (").append(enseignement.getIdEnseignement()).append(")\n");
            List<Object[]> listeCompetence = listeEnseignement.get(enseignement);
            for (Object[] competencePoids : listeCompetence) {
                Competence competence = (Competence) competencePoids[0];
                int poids = (int) competencePoids[1];
                sb.append("  - Competence: ").append(competence.getIntitule())
                .append(", Poids: ").append(poids).append("\n");
            }
        }
        
        sb.append("\n\nControles: \n");
        
        for (Enseignement enseignement : listeControle.keySet()) {
            sb.append("Enseignement: ").append(enseignement.getIntitule())
            .append(" (").append(enseignement.getIdEnseignement()).append(")\n");
            List<Controle> listeControleEnseignement = listeControle.get(enseignement);
            for (Controle controle : listeControleEnseignement) {
                sb.append("  - Controle Forme: ").append(controle.getForme())
                .append(", Date: ").append(controle.getDate()).append(", Poids: ")
                .append(controle.getPoids()).append("\n");
            }
        }

        return sb.toString();
    }
}
