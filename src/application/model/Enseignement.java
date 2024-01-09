/*
 * Enseignement.java                                                15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;
import java.util.regex.Pattern;

import application.model.exception.MoyenneRessourceException;
import application.model.exception.NoteInvalideException;

/** 
 * Représentation d'un enseignement, Ressource, Portfolio ou Sae pour des Compétences
 * @author thomas.izard
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @author constant.nguyen
 * @version 2.0
 */
public abstract class Enseignement implements Serializable {

	/** intitulé de l'enseignement est le nom de l'enseignement */
	protected String intituleEnseignement;

	/** identifiant de l'enseignement (exemple : R2.10, P1.05, S3.05 ...)*/
	protected String identifiantEnseignement;

	/** 
	 * Constructeur d'un enseignement
	 * @param intitule est le nom de l'enseignement
	 * @param identifiant de l'enseignement (exemple : R2.10, P1.05, S3.05 ...)
	 */
	public Enseignement(String intitule, String identifiant) {
		intituleEnseignement = intitule;
		identifiantEnseignement = identifiant;
	}

	/** @return valeur de intituleEnseignement */
	public String getIntituleEnseignement() {
		return intituleEnseignement;
	}

	/** @return valeur de identifiantEnseignement */
	public String getIdentifiantEnseignement() {
		return identifiantEnseignement;
	}

	/** 
	 * Vérification de la validité d'une regex avec un identifiant
	 * @param regex est la chaîne contenant l'expression régulière à vérifier
	 * @param identifiant de l'enseignement (exemple : R2.10, P1.05, S3.05 ...)
	 * @return true si la regex correspond au format de l'identifiant, false sinon
	 */
	public static boolean regexValide(String regex, String identifiant) {
		// compilation du regex en pattern
		Pattern motif = Pattern.compile(regex);
		// vérification de la validité du pattern avec l'identifiant
		return motif.matcher(identifiant).matches();
	}

	/** 
	 * Vérifie la validité d'un enseignement
	 * @param regex est valide si elle n'est pas vide
	 * @param identifiant est valide s'il correspond au pattern du regex
	 * @param intitule est valide s'il n'est pas vide
	 * @return true si l'enseignement renseigné est valide, false sinon
	 */
	public static boolean estValide(String regex, String identifiant, String intitule) {
		return !regex.equals("") && regexValide(regex, identifiant) && !intitule.equals("");
	}

	/** @return la moyenne de l'enseignement */
	protected abstract Note getMoyenne();

	/** 
	 * Renseigner la moyenne d'un enseignement 
	 * @throws MoyenneRessourceException
	 * @throws NoteInvalideException 
	 */
	protected abstract void setMoyenne() throws MoyenneRessourceException, NoteInvalideException;
}