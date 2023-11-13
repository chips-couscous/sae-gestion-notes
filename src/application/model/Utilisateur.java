package application.model;

/**
 * TODO comment class responsibility (SRP)
 * @author thomas.lemaire
 *
 */
public class Utilisateur {
    private String nom;
    private String prenom;

    /**
     * TODO comment intial state
     * @param nom
     * @param prenom
     */
    public Utilisateur(String nom, String prenom) {
    	this.nom = nom;
    	this.prenom = prenom;
    }
    
    /**
     * TODO comment method role
     * @return 0
     */
    public String getNom() {
        return nom;
    }

    /**
     * TODO comment method role
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * TODO comment method role
     * @return 0
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * TODO comment method role
     * @param prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
