/*
 * Competence.java                                                   24/10/2024
 * INFO2 2023-2024, pas de copyright ni droits d'auteurs
 */
package application.model;

import java.util.ArrayList;

import application.model.Enseignement;

/**
 * Classe définissant une compétence 
 * @author tony.lapeyre
 */
public class Competence {
	
	/** intitulé de la compétence */
	private String intitule;
	
	/** poids de l'enseignement  */
	private int poidsEnseignement;
	
	private ArrayList<Enseignement> listeEnseignement;
	
	/** TODO comment intial state
	 * @param intitule
	 * @param poidsEnseignement
	 * @param listeEnseignement
	 */
	public Competence(String intitule, int poidsEnseignement, ArrayList<Enseignement> listeEnseignement) {
		this.intitule = intitule;
		this.poidsEnseignement = poidsEnseignement;
		this.listeEnseignement = new ArrayList<>(listeEnseignement);
	}

	/**
	 *
	 */
	private boolean estValide() {
		// TODO Regex ...
	}
}
