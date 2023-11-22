/*
 * Competence.java                                                  15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

import application.model.exception.CompetenceInvalideException;
import application.model.exception.MoyenneCompetenceException;
import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;

/** 
 * Représentation d'une Compétence d'un Semestre
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Competence implements Serializable {

	/* Expression régulière de l'identifiant d'une compétence */
	private static final String REGEX_COMPETENCE = "U[1-3]\\.[1-6]";

	/* Intitule d'une compétence (exemple: Réaliser le développement d'une application ...) */
	private String intituleCompetence;

	/* Identifiant d'une compétence (exemple: U2.1, U2.3 ...) */
	private String identifiantCompetence;

	/* Liste des enseignement dans la compétence avec leur poids dans celle ci */
	private HashMap<Enseignement, Integer> enseignementsCompetence;

	/* Le poids totale des enseignements dans la compétence */
	private Integer poidsTotalDesEnseignements;

	/* Moyenne globale de la compétence */
	private Note moyenneCompetence;

	/** 
	 * Constructeur d'une compétence
	 * @param intitule de la compétence (exemple: Réaliser le développement d'une application ...)
	 * @param identifiant de la compétence (exemple: U2.1, U2.3 ...)
	 * @throws CompetenceInvalideException déclare une compétence invalide à la création
	 */
	public Competence(String intitule, String identifiant) throws CompetenceInvalideException {
		if(!estValide(intitule, identifiant)) {
			throw new CompetenceInvalideException("Compétence impossible à créer avec les valeurs reseignées");
		}
		intituleCompetence = intitule;
		identifiantCompetence = identifiant;
		enseignementsCompetence = new HashMap<Enseignement, Integer>();
		poidsTotalDesEnseignements = 0;
	}

	/** 
	 * Ajoute un enseignement et son poids dans la compétence à la compétence
	 * @param enseignement à ajouter à la compétence
	 * @param poids de l'enseignement dans la compétence
	 * @return true si l'ajout a été effectué, false sinon
	 */
	public boolean ajouterEnseignement(Enseignement enseignement, Integer poids) {
		if(verifierPoidsTotauxDansCompetence(poids)) {
			enseignementsCompetence.put(enseignement, poids);
			return true;
		}
		return false;
	}


	/** @return HashMap des enseignements d'une compétence */
	public HashMap<Enseignement, Integer> getListeEnseignements() {
		return enseignementsCompetence;
	}



	/**
	 * Calcul la moyenne d'une compétence si celle ci peut être calculer
	 * @return la valeur de la moyenne de la compétence si elle peut être calculer
	 * @throws MoyenneCompetenceException déclare l'impossibilité de calculer la
	 * moyenne d'une compétence
	 */
	private double calculerMoyenneCompetence() throws MoyenneCompetenceException {
		if(moyenneEstCalculable() && poidsTotalDesEnseignements == 100) {
			double moyenneACalculer = 0;

			for(Enseignement enseignement: enseignementsCompetence.keySet()) {
				double noteEnseignement = enseignement.getMoyenne().getValeurNote();
				Integer poidsEnseignement = enseignementsCompetence.get(enseignement);

				moyenneACalculer += noteEnseignement * poidsEnseignement;
			}
			return moyenneACalculer / 100;
		}
		throw new MoyenneCompetenceException("Moyenne de la compétence impossible à calculer");
	}

	/**
	 * Vérifie si la moyenne d'une compétence peut être calculer ou non
	 * @return true si toutes les notes d'un enseignement sont renseigné, false sinon
	 */
	private boolean moyenneEstCalculable() {
		boolean noteEnseignementExistent = true;

		for(Enseignement enseignement: enseignementsCompetence.keySet()) {
			noteEnseignementExistent &= enseignement.getMoyenne() != null;
		}

		return noteEnseignementExistent;
	}

	/**
	 * Initialise la moyenne de la compétence par rapport aux enseignements de celle ci
	 * @throws NoteInvalideException 
	 * @throws MoyenneRessourceException 
	 * @throws MoyenneCompetenceException 
	 */
	public void setMoyenneCompetence() throws NoteInvalideException, MoyenneRessourceException, MoyenneCompetenceException {
		double noteCompetence = calculerMoyenneCompetence();
		moyenneCompetence = new Note(noteCompetence, 20);
	}

	/**
	 * Vérifie si lors d'un ajout d'enseignement  le poids total des enseignements de
	 * la compétence ne soit pas null ou supérieur à 100
	 * @param poidsAVerifier à ajouter à la compétence
	 * @return true si l'enseignement peut être ajouté à la liste des enseignements, false sinon
	 */
	private boolean verifierPoidsTotauxDansCompetence(Integer poidsAVerifier) {
		poidsTotalDesEnseignements += poidsAVerifier;

		if(poidsTotalDesEnseignements > 0 && poidsTotalDesEnseignements <= 100) {
			return true;
		}

		poidsTotalDesEnseignements -= poidsAVerifier;
		return false;
	}

	/** @return la moyenne de la ressource */
	public Note getMoyenne() {
		return moyenneCompetence;
	}

	/** @return valeur de intituleCompetence */
	public String getIntituleCompetence() {
		return intituleCompetence;
	}

	/** @return valeur de identifiantCompetence */
	public String getIdentifiantCompetence() {
		return identifiantCompetence;
	}

	/**
	 * Vérifie la validité d'une compétence
	 * @param intitule est valide s'il n'est pas vide
	 * @param identifiant est valide s'il correspond au patter du REGEX_COMPETENCE
	 * @return true si la compétence est valide, false sinon
	 */
	private static boolean estValide(String intitule, String identifiant) {
		// compilation du regex en pattern
		Pattern motif = Pattern.compile(REGEX_COMPETENCE);
		// vérification de la validité du pattern avec l'identifiant
		return !intitule.equals("") && motif.matcher(identifiant).matches();
	}
}