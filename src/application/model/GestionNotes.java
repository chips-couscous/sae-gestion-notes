/*
 * GestionNotes.java                                                16 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft"
 */
package application.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import application.model.exception.CompetenceInvalideException;
import application.model.exception.ControleInvalideException;
import application.model.exception.EnseignementInvalideException;
import application.model.exception.ExtensionFichierException;
import application.model.exception.IpException;
import application.model.exception.MoyenneCompetenceException;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;
import application.model.exception.ParametresSemestreException;
import application.model.exception.PortReseauException;
import application.model.exception.SemestreInvalideExecption;
import application.model.exception.UtilisateurInvalideException;
import application.model.exception.cheminFichierException;

/**
 * Représentation du model de l'application gestion de notes
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class GestionNotes {

	private static GestionNotes instance = null;
	/* Semestre en cours utilisé pour l'application */
	private Semestre semestreGestionNotes;

	/* Propriétaire de l'application */
	private Utilisateur utilisateurGestionNotes;

	/* Fichier contenant la sauvegarde des paramètres et des notes */
	private File fichierSerialize;

	/** Constructeur de la gestion des notes 
	 * @throws IOException si le fichier a chargé n'existe pas
	 * @throws ClassNotFoundException si les objets à charger sont inconnu
	 */
	public GestionNotes() throws ClassNotFoundException, IOException {
		fichierSerialize = new File(".\\tmp\\gestion-notes.ser");

		/* Récupération des données enregistré si il y a eu une sauvegarde */
		if (fichierSerialize.exists()) {
			deserializerDonnees();
		} else { // Si aucune sauvegarde : initialise l'application
			try {
				setSemestreGestionNotes(new Semestre());
				setUtilisateurGestionNotes(new Utilisateur());
			} catch (UtilisateurInvalideException e) {
				e.printStackTrace();
			}
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
	 * Initialiser la moyenne d'une competence
	 * @param identifiant de la competence dont on veut connaître la moyenne
	 * @throws NoteInvalideException
	 * @throws MoyenneRessourceException
	 * @throws MoyenneCompetenceException 
	 */
	public void calculerMoyenneCompetence(String identifiant) throws MoyenneCompetenceException, NoteInvalideException, MoyenneRessourceException{ 
		Competence competence = trouverCompetence(identifiant);
		competence.setMoyenneCompetence();
	}

	/**
	 * Moyenne d'une compétence souhaitée
	 * @param identifiant de la c ompétence dont on veut connaître la moyenne
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
	 * @throws UtilisateurInvalideException si le prénom ou le nom est incorrect
	 */
	public void setUtilisateurGestionNotes(String nom, String prenom) throws UtilisateurInvalideException {
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
	 * @return true si le contrôle a bien été ajouté
	 * @throws ControleInvalideException
	 */
	public boolean ajouterControleAEnseignement(String identifiant, String type, String date, int poids) throws ControleInvalideException {
		Ressource enseignement = (Ressource) trouverEnseignement(identifiant);
		try {
			Controle controleAAjouter = new Controle(type, date, poids);       
			enseignement.ajouterControle(controleAAjouter);
			return true;
		} catch (ControleInvalideException e) {
			return false;
		}
	}
	
	/**
	 * Supprime un contrôle à une ressource
	 * @param identifiant de l'enseignement
	 * @param controle à supprimer
	 * @return true si le contrôle a bien été ajouté
	 * @throws ControleInvalideException
	 */
	public boolean supprimerControleAEnseignement(String identifiant, Controle controleASupprimer) throws ControleInvalideException {
		Ressource enseignement = (Ressource) trouverEnseignement(identifiant);      
		enseignement.supprimerControle(controleASupprimer);
		return true;
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
	 * Supprime la note d'un contrôle, d'une SAE ou d'un Portfolio
	 * @param est un objet qui correspond à la note que l'on veut supprimer
	 */
	public void supprimerNote(Object noteASupprimer) {
		if (noteASupprimer instanceof Sae) {
			Sae noteSae = (Sae) noteASupprimer;
			noteSae.setNoteSae(null);
		} else if (noteASupprimer instanceof Portfolio) {
			Portfolio notePortfolio = (Portfolio) noteASupprimer;
			notePortfolio.setNotePortfolio(null);
		} else {
			Controle controle = (Controle) noteASupprimer;
			controle.setNoteControle(null);
		}
	}

	/**
	 * Modifie la note d'un contrôle, d'une SAE ou d'un Portfolio
	 * @param est un objet qui correspond à la note que l'on veut supprimer
	 */
	public void modifierNote(Object noteAModifier, Double note, int denominateur, String commentaire) {
		if (noteAModifier instanceof Sae) {
			Sae notePortfolio = (Sae) noteAModifier;
			try {
				Note nouvelleNote = new Note(note, denominateur, commentaire);
				notePortfolio.setNoteSae(nouvelleNote);
			} catch (NoteInvalideException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (noteAModifier instanceof Portfolio) {
			Portfolio notePortfolio = (Portfolio) noteAModifier;
			try {
				Note nouvelleNote = new Note(note, denominateur, commentaire);
				notePortfolio.setNotePortfolio(nouvelleNote);
			} catch (NoteInvalideException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Controle controle = (Controle) noteAModifier;
			try {
				modifierNoteAControle(controle.getIndentifiantControle(), note, denominateur, commentaire);
			} catch (NoteInvalideException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * Import d'un fichier csv contenant le parametrage d'un semestre
	 * @param chemin du fichier à importer
	 * @throws ExtensionFichierException si l'extension est incorrecte
	 * @throws SemestreInvalideExecption si le semestre est invalide
	 * @throws CompetenceInvalideException si une compétence est invalide
	 * @throws EnseignementInvalideException si un enseignement est invalide
	 * @throws ParametresSemestreException si le fichier à importer ne contient
	 *         pas de paramètres valides
	 */
	public void importerParametrageSemestre(String chemin) throws ExtensionFichierException, SemestreInvalideExecption, CompetenceInvalideException, EnseignementInvalideException, ParametresSemestreException {

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
	}

	/**
	 * Import d'un fichier csv contenant le parametrage d'un enseignement
	 * @param chemin du fichier à importer
	 * @throws ExtensionFichierException
	 * @throws ControleInvalideException
	 * @throws EnseignementInvalideException si le fichier ne contient pas de paramètres de ressources
	 */
	public void importerParametrageEnseignement(String chemin) throws ExtensionFichierException, ControleInvalideException, EnseignementInvalideException {

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
	 * Vérifie si l'Enseignement est une Ressource
	 * @param enseignement 
	 * @return un booléen dont la valeur est vrai si
	 * 		l'enseignement est bien une ressource
	 * 		faux sinon
	 */
	public boolean estUneRessource(Enseignement enseignement) {
		String initiale = enseignement.getIdentifiantEnseignement().substring(0,1);
		return initiale.equals("R");
	}

	/**
	 * Vérifie si l'Enseignement est une Sae
	 * @param enseignement 
	 * @return un booléen dont la valeur est vrai si
	 * 		l'enseignement est bien une sae
	 * 		faux sinon
	 */
	public boolean estUneSae(Enseignement enseignement) {
		String initiale = enseignement.getIdentifiantEnseignement().substring(0,1);
		return initiale.equals("S");
	}

	/**
	 * Vérifie si l'Enseignement est un Portfolio
	 * @param enseignement 
	 * @return un booléen dont la valeur est vrai si
	 * 		l'enseignement est bien un Portfolio
	 * 		faux sinon
	 */
	public boolean estUnPortfolio(Enseignement enseignement) {
		String initiale = enseignement.getIdentifiantEnseignement().substring(0,1);
		return initiale.equals("P");
	}

	/** 
	 * Réinitialise l'application de gestion de notes
	 * @throws UtilisateurInvalideException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void reinitialiserGestionNotes() throws UtilisateurInvalideException, ClassNotFoundException, IOException {
		setSemestreGestionNotes(new Semestre());
		setUtilisateurGestionNotes(new Utilisateur());
		deserializerDonnees();
		serializerDonnees();
	}

	/** 
	 * Sérialise les objets semestreGestionNotes et utilisateurGestionNotes
	 * pour les sauvegarder dans un fichier
	 * @throws IOException si le fichier est introuvable
	 */
	public void serializerDonnees() throws IOException {
		try (// ouverture d'un flux sur un fichier
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierSerialize))) {
			// sérialization des objets
			oos.writeObject(semestreGestionNotes);
			oos.writeObject(utilisateurGestionNotes);
		}
	}

	/** 
	 * Sérialise les objets semestreGestionNotes et utilisateurGestionNotes
	 * pour les sauvegarder dans un fichier
	 * @throws ClassNotFoundException si les objets sauvegardé sont inconnus
	 * @throws IOException si le fichier de sauvegarde est introuvable
	 */
	public void deserializerDonnees() throws ClassNotFoundException, IOException {
		try (// ouverture d'un flux sur un fichier  
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierSerialize))) {
			// désérialization de l'objet
			semestreGestionNotes = (Semestre) ois.readObject();
			utilisateurGestionNotes = (Utilisateur) ois.readObject();

		}
	}

	/** TODO comment method role
	 * @return instance
	 */
	public static GestionNotes getInstance() {
		if (instance == null) {
			try {
				instance = new GestionNotes();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	/** 
	 * Vérifie la conformité du port de connexion avant de lancer le serveur 
	 * pour recevoir un fichier d'un autre utilisateur
	 * 
	 * @param port port de connexion (compris entre 1024 et 60000)
	 * @param cheminReceptionFichier chemin ou sera créer le fichier recu
	 * @throws PortReseauException si le port est invalide
	 * @throws IOException si il y eu un problème lors de la connexion avec le client
	 * @throws cheminFichierException si cheminReceptionFichier est null ou vide
	 */
	public static void recevoirFichier(int port, String cheminReceptionFichier) throws PortReseauException, IOException, cheminFichierException {

		if (port < 1024 || port > 60000) {
			throw new PortReseauException("Le port est incorrect");
		}
		if (cheminReceptionFichier == null || cheminReceptionFichier.equals("")) {
			throw new cheminFichierException("Le chemin de reception est incorrect");
		}
		Reseau.recevoir(port, cheminReceptionFichier);
	}

	/** 
	 * Vérifie que la conformité des paramètres et lance le client 
	 * pour envoyer le fichier passé en paramètre à l'adresse IP communiqué en paramètre
	 * @param ipServeur IP de la machine a qui on envoi le fichier
	 * @param port port de connexion 
	 * @param cheminFichier chemin du fichier a envoyer
	 * @throws PortReseauException  si le port est invalide
	 * @throws IpException si l'adresse IP est invalide
	 * @throws cheminFichierException si le chemin est invalide
	 * @throws IOException si le serveur est introuvable
	 */
	public static void envoyerFichier(String ipServeur, int port, String cheminFichier) throws PortReseauException, IpException, cheminFichierException, IOException {
		String patternIP = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

		if (!Pattern.matches(patternIP, ipServeur)) {
			throw new IpException("L'adresse IP est incorrect");
		}
		if (port < 1024 || port > 60000) {
			throw new PortReseauException("Le port est incorrect");
		}
		if (cheminFichier.equals("")) { // Pas besoin de vérifier plus, la vue impose un chemin correct
			throw new cheminFichierException("Le fichier est incorrect");
		}

		Reseau.envoyer(ipServeur, port, cheminFichier);
	}

	/** 
	 * @return la liste des contrôles, ou SAE, ou Portfolio ayant une note
	 */
	public ArrayList<Object> getNotes() {
		ArrayList<Object> listeNotes = new ArrayList<>();
		for (Enseignement enseignement : getSemestreGestionNotes().getEnseignementsSemestre()) {
			if (enseignement instanceof Ressource ) {
				List<Controle> listeControle = ((Ressource) enseignement).getControlesRessource();
				for (Controle controle : listeControle) {
					if (controle.aUneNote()) {
						listeNotes.add(controle);
					}
				}
			} else if (enseignement instanceof Sae) {
				if (((Sae)enseignement).aUneNote()) {
					listeNotes.add(enseignement);
				}
			} else if (enseignement instanceof Portfolio) {
				if (((Portfolio)enseignement).aUneNote()) {
					listeNotes.add(enseignement);
				}
			}
		}

		return listeNotes;
	}

	/** 
	 * Ajoute une note à une SAE ou un Portfolio
	 * @param enseignement
	 * @param note 
	 * @param denominateur 
	 * @param commentaire
	 * @throws NoteInvalideException 
	 */
	public static void ajouterNoteASaePortfolio(Enseignement enseignement, double note, int denominateur,
			String commentaire) throws NoteInvalideException {
		if (enseignement instanceof Sae) {
			((Sae)enseignement).setNoteSae(new Note(note, denominateur, commentaire));
		} else if (enseignement instanceof Portfolio) {
			((Portfolio)enseignement).setNotePortfolio(new Note(note, denominateur, commentaire));
		} else {
			throw new NoteInvalideException("La note est invalide");
		}
	}

	/**
	 * Execution de scipts de test
	 * @param args non utilisé
	 */
	//    public static void main(String[] args) {
	//        GestionNotes gn = new GestionNotes();
	//        //       
	//        //        try {
	//        //            gn.importerParametrageSemestre(".\\csv\\ParametresSemestre(AImporter).csv");
	//        //            gn.importerParametrageEnseignement(".\\csv\\ParametresRessource(AImporter).csv");
	//        //        } catch (Exception e) {
	//        //            e.printStackTrace();
	//        //        }
	//        //     
	//
	//        try {
	//            gn.deserializerDonnees();
	//        } catch (ClassNotFoundException e) {
	//            // TODO Auto-generated catch block
	//            e.printStackTrace();
	//        } catch (IOException e) {
	//            // TODO Auto-generated catch block
	//            e.printStackTrace();
	//        }
	//
	//        try {
	//            Ressource ressourceControles;
	//            ressourceControles = (Ressource) gn.trouverEnseignement("R2.01");
	//            System.out.println(ressourceControles.getControleToString());
	//        } catch (Exception e) {
	//            e.printStackTrace();
	//        }
	//       
	//        try {
	//            gn.ajouterNoteAControle("R2.01.00", 12.2, 20, "");
	//            gn.ajouterNoteAControle("R2.01.01", 8, 10, "");
	//            gn.ajouterNoteAControle("R2.01.02", 15, 40, "");
	//            gn.ajouterNoteAControle("R2.01.03", 15.5, 20, "");
	//        } catch (NoteInvalideException e) {
	//            e.printStackTrace();
	//        }
	//
	//        try {
	//            gn.calculerMoyenneEnseignement("R2.01");
	//        } catch (MoyenneRessourceException e) {
	//            e.printStackTrace();
	//        } catch (NoteInvalideException e) {
	//            e.printStackTrace();
	//        }
	//       
	//        try {
	//            gn.serializerDonnees();
	//        } catch (IOException e) {
	//            e.printStackTrace();
	//        }
	//
	//        System.out.println(gn.getValeurNoteDeControle("R2.01.01"));
	//        System.out.println(gn.moyenneEnseignemnt("R2.01").getValeurNote());
	//    }
}
