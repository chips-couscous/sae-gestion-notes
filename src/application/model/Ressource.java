/*
 * Ressource.java                                                   15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import application.model.exception.EnseignementInvalideException;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;

import java.util.ArrayList;
import java.util.List;

/** 
 * Représentation d'une Ressource en tant qu'Enseignement
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Ressource extends Enseignement{

    /* Expression régulière de l'identifiant d'une ressource */
    private static final String REGEX_RESSOURCE = "R[1-6]\\.[0-9]{2}";
    
    /* Liste des contrôles de la ressource */
    private List<Controle> controlesRessource;
    
    /* Numéro du prochain contrôle lié à la ressource */
    private int numeroControle;
    
    /* Le poids totale des contrôles dans la ressource */
    private int poidsTotalDesControles;
    
    /* La note moyenne de la ressource */
    private Note moyenneRessource;
    
    /** 
     * Constructeur de la ressource
     * @param intitule est le nom de la ressource
     * @param identifiant de la ressource (exemple : R2.10, R1.02 ...)
     * @throws EnseignementInvalideException déclare une ressource invalide à la création
     */
    public Ressource(String intitule, String identifiant) throws EnseignementInvalideException {
        super(intitule, identifiant);
        if(!estValide(REGEX_RESSOURCE, identifiant, intitule)) {
            throw new EnseignementInvalideException("Ressource impossible à créer avec les valeurs reseignées");
        }
        controlesRessource = new ArrayList<Controle>();
        poidsTotalDesControles = 0;
    }
    
    /** 
     * Ajoute un contrôle à l'enseignement
     * @param controle à ajouter à la ressource 
     * @return true si le controle a pu être ajouté, false sinon
     */
    public boolean ajouterControle(Controle controle) {
        if(verifierPoidsTotauxDansRessource(controle)) {
            controlesRessource.add(controle);
            
            // Création de l'identifiant pour le contrôle renseigné
            String identifiantControle = String.valueOf(numeroControle);
            identifiantControle = identifiantControle.length() == 1 ? "0" + identifiantControle : identifiantControle;
            controle.setIndentifiantControle(this.getIdentifiantEnseignement()+"."+identifiantControle);
            
            // Incrementation du numéro du prochain contrôle
            numeroControle++;
            
            return true;
        }
        return false;
    }
    
    /**
     * Calcul la moyenne d'une ressource si celle ci peut être calculer
     * @return la moyenne d'une ressource si elle a pu être calculé
     * @throws MoyenneRessourceException déclare l'impossibilité de calculer la
     * moyenne de la ressource
     */
    public double calculerMoyenneRessource() throws MoyenneRessourceException {
        if(moyenneEstCalculable() && poidsTotalDesControles == 100) {
            double moyenneRessource = 0;
            
            for(Controle controle: controlesRessource) {
                double noteControle = controle.getNoteControle().getValeurNoteSurVingt();
                Integer poidsControle = controle.getPoidsControle();
                
                moyenneRessource += noteControle * poidsControle;
            }
            return moyenneRessource / poidsTotalDesControles;
        }
        throw new MoyenneRessourceException("Moyenne de la ressource impossible à calculer");
    }
    
    /**
     * Vérifie si la moyenne d'une ressource peut être calculer ou non
     * @return true si toutes les notes d'une ressource sont renseigné, false sinon
     */
    private boolean moyenneEstCalculable() {
        // vérifie si toutes les notes existent dans chaque contrôle
        boolean notesExistent = true;
        
        for(Controle controle: controlesRessource) {
            notesExistent &= controle.getNoteControle() != null;
        }
        
        return notesExistent;
    }
        
    /** 
     * noteRessource nouvelle valeur de moyenneRessource
     * @param noteRessource nouvelle valeur de moyenneRessource 
     * @throws MoyenneRessourceException 
     * @throws NoteInvalideException 
     */
    @Override
    public void setMoyenne() throws MoyenneRessourceException, NoteInvalideException {
        double noteRessource = calculerMoyenneRessource();
        moyenneRessource = new Note(noteRessource, 20);
    }
    
    /** @return la moyenne de la ressource */
    @Override
    protected Note getMoyenne() {
        return moyenneRessource;
    }
    
    /** @return valeur de controlesRessource */
    public List<Controle> getControlesRessource() {
        return controlesRessource;
    }

    /**
     * Vérifie si lors d'un ajout de contrôle le poids total des contrôles de
     * la ressource ne soit pas null ou supérieur à 100
     * @param controleAVerifier à ajouter à la ressources
     * @return true si le contrôle peut être ajouté à la liste des contrôles, false sinon
     */
    private boolean verifierPoidsTotauxDansRessource(Controle controleAVerifier) {
        poidsTotalDesControles += controleAVerifier.getPoidsControle();
        
        if(poidsTotalDesControles > 0 && poidsTotalDesControles <= 100) {
            return true;
        }
      
        poidsTotalDesControles -= controleAVerifier.getPoidsControle();
        return false;
    }
}