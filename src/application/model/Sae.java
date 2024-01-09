/*
 * Sae.java                                                         15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;

import application.model.exception.EnseignementInvalideException;

/** 
 * Représentation d'une SAE en tant qu'Enseignement
 * @author thomas.izard
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @author constant.nguyen
 * @version 2.0
 */
public class Sae extends Enseignement implements Serializable {

	/* Expression régulière de l'identifiant d'une SAE */
	private static final String REGEX_SAE = "S[1-6]\\.[0-9]{2}";

	/* Note de la SAE */
	private Note noteSae;

	/* Moyenne finale de la SAE */
	private Note moyenneSae;

	/** 
	 * Constructeur de la SAE
	 * @param intitule est le nom de la SAE
	 * @param identifiant de la SAE (exemple : S2.10, S1.02 ...)
	 * @throws EnseignementInvalideException déclare une SAE invalide à la création
	 */
	public Sae(String intitule, String identifiant) throws EnseignementInvalideException {
		super(intitule, identifiant);
		if(!estValide(REGEX_SAE, identifiant, intitule)) {
			throw new EnseignementInvalideException("SAE impossible à créer avec les valeurs reseignées");
		}
	}

	/** @param noteSae nouvelle valeur de noteSae */
	public void setNoteSae(Note noteSae) {
		this.noteSae = noteSae;
	}


	/**
	 * @return 
	 */

	/** noteSae nouvelle valeur de moyenneSae */
	@Override
	public void setMoyenne() {
		this.moyenneSae = this.noteSae;
	}

	/** @return la moyenne de la SAE */
	@Override
	protected Note getMoyenne() {
		return moyenneSae;
	}

	/** @return true si la SAE a une note */
	public boolean aUneNote() {
		return noteSae != null;
	}

	/** @return la note de la SAE */
	public Note getNoteSae() {
		return noteSae;
	}
}