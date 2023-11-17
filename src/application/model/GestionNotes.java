/*
 * GestionNotes.java                                                16 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.util.HashMap;
import java.util.List;

import application.model.exception.CompetenceInvalideException;
import application.model.exception.ControleInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;
import application.model.exception.SemestreInvalideExecption;
import application.model.exception.UtilisateurInvalideException;

/** 
 * Représentation du model de l'application gestion de notes
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class GestionNotes {

    /* Semestre en cours utilisé pour l'application */
    private Semestre semestreGestionNotes;
    
    /* Propriétaire de l'application */
    private Utilisateur utilisateurGestionNotes;
    
    /** Constructeur de la gestion des notes */
    public GestionNotes() {
        setSemestreGestionNotes(new Semestre());
        
        try {
            setUtilisateurGestionNotes(new Utilisateur());
        } catch (UtilisateurInvalideException e) {
            e.printStackTrace();
        }
    }
    
    /** 
     * Cherche un enseignement à partir de son identifiant
     * @param identifiant de l'enseignement à trouver
     * @return l'enseignement trouvé s'il existe, null sinon
     */
    public Enseignement trouverEnseignement(String identifiant) {
        Enseignement enseignementATrouver = null;
        
        for(Enseignement enseignement: getSemestreGestionNotes().getEnseignementsSemestre()) {
            if(enseignement.getIdentifiantEnseignement().equals(identifiant)) {
                enseignementATrouver = enseignement;
            }
        }
        
        return enseignementATrouver;
    }
    
    /** 
     * Cherche une compétence à partir de son identifiant
     * @param identifiant de la compétence à trouver
     * @return la compétence trouvée si elle existe, null sinon
     */
    public Competence trouverCompetence(String identifiant) {
        Competence competenceATrouver = null;
        
        for(Competence competence: getSemestreGestionNotes().getCompetencesSemestre()) {
            if(competence.getIdentifiantCompetence().equals(identifiant)) {
                competenceATrouver = competence;
            }
        }
        
        return competenceATrouver;
    }
    
    
    /** 
     * Cherche un controle dans un enseignement à partir de son identifiant
     * @param identifiant du contrôle à trouver
     * @return le contrôle trouvé s'il existe, null sinon
     */
    public Controle trouverControle(String identifiant) {
        Controle controleATrouver = null;
        Enseignement enseignement = trouverEnseignement(identifiant.substring(0,5));
        
        for(Controle controle: ((Ressource) enseignement).getControlesRessource()) {
            if (controle.getIndentifiantControle().equals(identifiant)) {
                controleATrouver = controle;
            }
        }
        
        return controleATrouver;
    }
        
    /** 
     * Initialiser la moyenne d'un enseignement
     * @param identifiant de l'enseignement dont on veut connaître la moyenne
     * @throws NoteInvalideException 
     * @throws MoyenneRessourceException 
     */
    public void calculerMoyenneEnseignement(String identifiant) throws MoyenneRessourceException, NoteInvalideException{  
        Enseignement enseignement = trouverEnseignement(identifiant);
        enseignement.setMoyenne();
    }
    
    /**
     * Moyenne d'un enseignement souhaité
     * @param identifiant de l'enseignement dont on veut connaître la moyenne
     * @return la note qui correspond à la moyenne de l'enseignement renseigné
     */
    public Note moyenneEnseignemnt(String identifiant) {
        Enseignement enseignement = trouverEnseignement(identifiant);
        Note moyenneEnseignement = null;
        
        moyenneEnseignement = enseignement.getMoyenne();
  
        return moyenneEnseignement;
    }
    
    /**
     * Moyenne d'une compétence souhaitée
     * @param identifiant de la compétence dont on veut connaître la moyenne
     * @return la note qui correspond à la moyenne de la compétence renseignée
     */
    public Note moyenneCompetence(String identifiant) {
        Competence competence = trouverCompetence(identifiant);
        Note moyenneCompetence = null;
        
        moyenneCompetence = competence.getMoyenne();
        
        return moyenneCompetence;
    }
    
    /**
     * Modifie les valeurs de l'utilisateur pour remplacer le nom et/ou le prénom
     * @param nom de l'utilisateur à remplacer
     * @param prenom nom de l'utilisateur à remplacer
     */
    public void setUtilisateurGestionNotes(String nom, String prenom) {
        utilisateurGestionNotes.setNomUtilisateur(nom);
        utilisateurGestionNotes.setPrenomUtilisateur(prenom);
    }
        
    /** 
     * Ajoute un enseignement à une competence
     * @param identifiantCompetence identifiant de la compétence 
     * @param identifiantEnseignement identifiant de l'enseignement à ajouter
     * @param poids de l'enseignement dans la compétence
     */
    public void ajouterEnseignementACompetence(String identifiantCompetence, String identifiantEnseignement, Integer poids) {
        Competence competence = trouverCompetence(identifiantCompetence);
        Enseignement enseignement = trouverEnseignement(identifiantEnseignement);
        
        competence.ajouterEnseignement(enseignement, poids);
    }
    
    /** 
     * Ajoute un contrôle à une ressource si le contrôle est valide
     * @param identifiant de l'enseignement
     * @param type du contrôle renseigné (exemple : oral, écrit ...)
     * @param date du contrôle renseigné (exemple : 12/10/2023, milieu octobre ...)
     * @param poids du contrôle renseigné (exemple : 12, 35 ...)
     * @throws ControleInvalideException 
     */
    public void ajouterControleAEnseignement(String identifiant, String type, String date, int poids) throws ControleInvalideException {
        Ressource enseignement = (Ressource) trouverEnseignement(identifiant);
        Controle controleAAjouter = new Controle(type, date, poids);        
        
        enseignement.ajouterControle(controleAAjouter);
    }
    
    /**
     * Ajoute la note à un controle
     * @param identifiant du controle dont on veut ajouter la note
     * @param note , valeur de la note
     * @param denominateur de la note
     * @param commentaire de la note
     * @throws NoteInvalideException 
     */
    public void ajouterNoteAControle(String identifiant, double note, int denominateur, String commentaire) throws NoteInvalideException {
        Note noteAAjouter;
        
        Controle controle = trouverControle(identifiant);
        noteAAjouter = new Note(note, denominateur, commentaire);
        
        controle.setNoteControle(noteAAjouter);
    }
    
    /**
     * Modifie la note d'un controle
     * @param identifiant du controle dont on veut modifier la note
     * @param note , valeur de la note
     * @param denominateur de la note
     * @param commentaire de la note
     * @throws NoteInvalideException 
     */
    public void modifierNoteAControle(String identifiant, double note, int denominateur, String commentaire) throws NoteInvalideException {
        Note noteAModifier;
        
        Controle controle = trouverControle(identifiant);
        noteAModifier = controle.getNoteControle();
        
        noteAModifier.modifierNote(note, denominateur, commentaire);
    }
    
    /** 
     * Supprime la note d'un contrôle
     * @param identifiant du controle dont on veut supprimer la note
     */
    public void supprimerNoteAControle(String identifiant) {
        Controle controle = trouverControle(identifiant);
        controle.setNoteControle(null);
    }
    
    /**
     * Import d'un fichier csv contenant le parametrage d'un semestre
     * @param chemin du fichier à importer
     * @throws ExtensionFichierException
     * @throws SemestreInvalideExecption 
     * @throws CompetenceInvalideException 
     * @throws EnseignementInvalideException 
     */
    private void importerParametrageSemestre(String chemin) throws ExtensionFichierException, SemestreInvalideExecption, CompetenceInvalideException, EnseignementInvalideException {
                
        FichierSemestre fichier = new FichierSemestre(chemin);
        
        fichier.setDelimiteurFichier(";");
        fichier.lireFichier();
        HashMap<String[], List<String[]>> donneesFichier = fichier.decomposerFichier();
                
        semestreGestionNotes.setSemestre(fichier.getNumeroSemestre(), fichier.getParcoursSemestre());
        
        for(String[] competenceADecomposer: donneesFichier.keySet()) {            
            String identifiantCompetence = competenceADecomposer[1];
            String intituleCompetence = competenceADecomposer[2];

            semestreGestionNotes.ajouterCompetence(intituleCompetence, identifiantCompetence);
            
            for(String[] enseignementADecomposer: donneesFichier.get(competenceADecomposer)) {
                String identifiantEnseignement = enseignementADecomposer[1];
                String intituleEnseignement = enseignementADecomposer[2];               
                Integer poidsEnseignement = Integer.parseInt(enseignementADecomposer[3]);
                                
                semestreGestionNotes.ajouterEnseignement(intituleEnseignement, identifiantEnseignement);
                
                Competence competence = trouverCompetence(identifiantCompetence);
                competence.ajouterEnseignement(trouverEnseignement(identifiantEnseignement), poidsEnseignement);
            }
        }
        
        System.out.println(semestreGestionNotes.toString());
    }
    
    /**
     * Import d'un fichier csv contenant le parametrage d'un enseignement
     * @param chemin du fichier à importer
     * @throws ExtensionFichierException
     * @throws ControleInvalideException 
     */
    private void importerParametrageEnseignement(String chemin) throws ExtensionFichierException, ControleInvalideException {
       
        FichierRessource fichier = new FichierRessource(chemin);
        
        fichier.setDelimiteurFichier(";");
        fichier.lireFichier();
        HashMap<String, List<String[]>> donneesFichier = fichier.decomposerFichier();
        
        for(String identifiant: donneesFichier.keySet()) {
            Enseignement enseignement = (Ressource) trouverEnseignement(identifiant);
            if (enseignement != null) {
                for(String[] controleADecomposer: donneesFichier.get(identifiant)) {
                    String type = controleADecomposer[0];
                    String date = controleADecomposer[1];
                    int poids = Integer.parseInt(controleADecomposer[2]);
                                        
                    ajouterControleAEnseignement(identifiant, type, date, poids);
                }
            }
        }
    }
    
    /** 
     * Méthode de test
     * @param identifiant
     * @return 0
     */
    public double getValeurNoteDeControle(String identifiant) {
        Controle controle = trouverControle(identifiant);
        return controle.getNoteControle().getValeurNoteSurVingt();
    }

    /** @return valeur de semestreGestionNotes */
    public Semestre getSemestreGestionNotes() {
        return semestreGestionNotes;
    }

    /** @param semestreGestionNotes nouvelle valeur de semestreGestionNotes */
    public void setSemestreGestionNotes(Semestre semestreGestionNotes) {
        this.semestreGestionNotes = semestreGestionNotes;
    }
    
    /** @return le prénom et le nom de l'utilisateur */
    public String getUtilisateurGestionNotes() {
        return utilisateurGestionNotes.toString();
    }

    /** @param utilisateurGestionNotes nouvelle valeur de utilisateurGestionNotes */
    public void setUtilisateurGestionNotes(Utilisateur utilisateurGestionNotes) {
        this.utilisateurGestionNotes = utilisateurGestionNotes;
    }

    /** 
     * Execution de scipts de test 
     * @param args non utilisé
     */
    public static void main(String[] args) {
        GestionNotes gn = new GestionNotes();
        
        try {
            gn.importerParametrageSemestre("Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\parametrage-sae.csv");
            gn.importerParametrageEnseignement("Z:\\Eclipse\\workspace\\SaeGestionNotes\\csv\\ressources-sae.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            Ressource ressourceControles;
            ressourceControles = (Ressource) gn.trouverEnseignement("R2.01");
            System.out.println(ressourceControles.getControleToString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            gn.ajouterNoteAControle("R2.01.00", 12.2, 20, "");
            gn.ajouterNoteAControle("R2.01.01", 8, 10, "");
            gn.ajouterNoteAControle("R2.01.02", 15, 40, "");
            gn.ajouterNoteAControle("R2.01.03", 999, 1000, "");
        } catch (NoteInvalideException e) {
            e.printStackTrace();
        }

        try {
            gn.calculerMoyenneEnseignement("R2.01");
        } catch (MoyenneRessourceException e) {
            e.printStackTrace();
        } catch (NoteInvalideException e) {
            e.printStackTrace();
        }

        System.out.println(gn.getValeurNoteDeControle("R2.01.01"));
        System.out.println(gn.moyenneEnseignemnt("R2.01").getValeurNote());
    }
}