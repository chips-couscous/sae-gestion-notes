/*
 * Semestre.java                                                    15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import application.model.exception.CompetenceInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.SemestreInvalideExecption;

/** 
 * Représentation d'un Semestre
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Semestre implements Serializable {
    
    /* Numéro du semestre en cours (exemple: 1, 2, 3 ...) */
    private int numeroSemestre;
    
    /* Parcours du semestre en cours (exemple: a, b ... , t(tous), n(néant)) */
    private char parcoursSemestre;
    
    /* Liste des compétences qui forment le semestre */
    private List<Competence> competencesSemestre;
    
    /* Liste des enseignement qui forment les compétences */
    private List<Enseignement> enseignementsSemestre;
    
    /** Constructeur d'un semestre */
    public Semestre() {
        competencesSemestre = new ArrayList<Competence>();
        enseignementsSemestre = new ArrayList<Enseignement>();
    }
    
    /**
     * Ajoute une nouvelle compétence au semestre si celle ci n'existe pas déjà
     * @param intitule de la compétence à ajouter au semestre
     * @param identifiant de la compétence à ajouter au semestre
     * @return true si la compétence a bien été ajouté, false sinon
     * @throws CompetenceInvalideException 
     */
    public boolean ajouterCompetence(String intitule, String identifiant) throws CompetenceInvalideException {
        if(!verifierPresenceCompetence(identifiant)) {
            Competence competenceAAjouter = new Competence(intitule, identifiant);
            competencesSemestre.add(competenceAAjouter);
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si une compétence est déjà présente dans le semestre ou non
     * @param identifiant de la compétence à ajouter au semestre
     * @return true si la compétence est déjà dans le semestre, false sinon
     */
    public boolean verifierPresenceCompetence(String identifiant) {
        // vérifie si la compétence est déjà présente ou non dans le semestre
        boolean dejaPresente = false;
        for(Competence competencePresente : competencesSemestre) {
            dejaPresente |= competencePresente.getIdentifiantCompetence().equals(identifiant);
        }
        return dejaPresente;
    }
    
    /**
     * Ajoute un nouvel enseignement au semestre si celui ci n'existe pas déjà
     * @param intitule de l'enseignement à ajouter au semestre
     * @param identifiant de l'enseignement à ajouter au semestre
     * @return true si l'enseignement a bien été ajouté, false sinon
     * @throws EnseignementInvalideException 
     */
    public boolean ajouterEnseignement(String intitule, String identifiant) throws EnseignementInvalideException {
        
        Enseignement enseignementAAjouter;
        
        if(!verifierPresenceEnseignement(identifiant)) {
            // Premier caractère de l'identifiant qui donne le type d'enseignement
            char typeEnseignement = identifiant.charAt(0);
            
            // Analyse du type d'enseignement
            switch (typeEnseignement) {
            // Cas Ressource
            case 'R': {
                enseignementAAjouter = new Ressource(intitule, identifiant);
                break;
            }
            // Cas Portfolio
            case 'P': {
                enseignementAAjouter = new Portfolio(intitule, identifiant);
                break;
            }
            // Cas Sae
            case 'S': {
                enseignementAAjouter = new Sae(intitule, identifiant);
                break;
            }
            default:
                return false;
            }
            
            enseignementsSemestre.add(enseignementAAjouter);
            return true;
        }

        return false; 
    }
    
    /**
     * Vérifie si un enseignement est déjà présent dans le semestre ou non
     * @param identifiant de l'enseignement à ajouter au semestre
     * @return true si l'enseignement est déjà dans le semestre, false sinon
     */
    public boolean verifierPresenceEnseignement(String identifiant) {
        // vérifie si l'enseignement est déjà présent ou non dans le semestre
        boolean dejaPresent = false;
        for(Enseignement enseignementPresent : enseignementsSemestre) {
            dejaPresent |= enseignementPresent.getIdentifiantEnseignement().equals(identifiant);
        }
        return dejaPresent;
    }

    /** 
     * Initialise les informations primaires d'un semestre
     * @param numero du semestre renseigné 
     * @param parcours du semestre renseigné
     * @return true si le semestre a pu être renseigné, false sinon
     * @throws SemestreInvalideExecption 
     */
    public boolean setSemestre(int numero, String parcours) throws SemestreInvalideExecption {
        if(!semestreEstValide(numero, parcours)) {
            throw new SemestreInvalideExecption("Semestre impossible à créer avec les valeurs reseignées");
        }
        setNumeroSemestre(numero);
        setParcoursSemestre(parcours.charAt(0));
        return true;
    }
    
    /** @param numeroSemestre nouvelle valeur de numeroSemestre */
    public void setNumeroSemestre(int numeroSemestre) {
        this.numeroSemestre = numeroSemestre;
    }

    /** @param parcoursSemestre nouvelle valeur de parcoursSemestre */
    public void setParcoursSemestre(char parcoursSemestre) {
        this.parcoursSemestre = parcoursSemestre;
    }

    /** @return valeur de numeroSemestre */
    public int getNumeroSemestre() {
        return numeroSemestre;
    }

    /** @return valeur de parcoursSemestre */
    public char getParcoursSemestre() {
        return parcoursSemestre;
    }

    /** @return valeur de competencesSemestre */
    public List<Competence> getCompetencesSemestre() {
        return competencesSemestre;
    }

    /** @return valeur de enseignementsSemestre */
    public List<Enseignement> getEnseignementsSemestre() {
        return enseignementsSemestre;
    }
    
    /**
     * Un semestre est considéré comme valide si :
     * son numéro est compris entre 0 et 7
     * et son parcours correspond à ces chaines ("tous", "a", "b", "c", "d")
     * @param numero du semestre
     * @param parcours du semestre
     * @return true si tous les éléments sont valides, false sinon
     */
    private static boolean semestreEstValide(int numero, String parcours) {
        parcours = parcours.toLowerCase();
        return     numero > 0 && numero < 7
               && (parcours.equals("tous") || parcours.equals("néant") ||
                   parcours.equals("a")    || parcours.equals("b")     ||
                   parcours.equals("c")    || parcours.equals("d")); 
    }
    
    public String toString() {
        String resultat = "";
        
        for(Competence competence: competencesSemestre) {
            resultat += competence.getIdentifiantCompetence() + "\n";
        }
        
        for(Enseignement enseignement: enseignementsSemestre) {
            resultat += enseignement.getIdentifiantEnseignement() + "\n";
        }
        
        return resultat;
    }
}