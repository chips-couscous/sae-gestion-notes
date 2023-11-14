package application.modele;

public class GestionNote {
	
	static Utilisateur identite = new Utilisateur("Nom", "Prenom");
	
	
	
	public static void changerIdentite(String nom, String prenom) {
		identite.setNom(nom);
		identite.setPrenom(prenom);
	}
	
	public static String afficherIdentite() {
		return identite.getNom() + " " + identite.getPrenom();
	}
}
