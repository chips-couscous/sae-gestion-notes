/*
 * Utilisateur.java                                                 17 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;

import application.model.exception.UtilisateurInvalideException;

/**
 * Représentation d'un utilisateur
 * @author thomas.izard
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @author constant.nguyen
 * @version 1.0
 */
public class Utilisateur implements Serializable {

	/* Nom de l'utilisateur */
	private String nomUtilisateur;

	/* Prénom de l'utilisateur */
	private String prenomUtilisateur;

	/**
	 * Constructeur d'utilisateur
	 * @param nom de l'utilisateur
	 * @param prenom de l'utilisateur
	 * @throws UtilisateurInvalideException déclare un utilisateur impossible à créer 
	 */
	public Utilisateur(String nom, String prenom) throws UtilisateurInvalideException {
		if(nom.equals("") || prenom.equals("")) {
			throw new UtilisateurInvalideException("Utilisateur impossible à créer avec les valeurs reseignées");
		}
		this.nomUtilisateur = nom;
		this.prenomUtilisateur = prenom;
	}

	/**
	 * Constructeur en chaine d'utilisateur
	 * @throws UtilisateurInvalideException déclare un utilisateur impossible à créer 
	 */
	public Utilisateur() throws UtilisateurInvalideException {
		this("IUT", "Étudiant");
	}

	/** @return la valeur de nomUtilisateur */
	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	/** 
	 * @param nom nouvelle valeur de nomUtilisateur 
	 * @throws UtilisateurInvalideException  si le nom est invalide
	 */
	public void setNomUtilisateur(String nom) throws UtilisateurInvalideException {
		if (nom == null || nom.equals("")) {
			throw new UtilisateurInvalideException("Le nom est incorrect");
		}
		this.nomUtilisateur = nom;
	}

	/** @return la valeur de prenomUtilisateur */
	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	/** 
	 * @param prenom nouvelle valeur de prenomUtilisateur 
	 * @throws UtilisateurInvalideException 
	 */
	public void setPrenomUtilisateur(String prenom) throws UtilisateurInvalideException {
		if (prenom == null || prenom.equals("")) {
			throw new UtilisateurInvalideException("Le prénom est incorrect");
		}
		this.prenomUtilisateur = prenom;
	}

	/** @return le prénom et le nom de l'utilisateur */
	public String toString() {
		return getPrenomUtilisateur() + " " + getNomUtilisateur();
	}
}