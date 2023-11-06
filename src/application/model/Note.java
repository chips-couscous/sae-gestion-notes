/*
 * Note.java                                      26 oct. 2023
 * IUT Rodez, info2 2022-2023, pas de copyright ni "copyleft" 
 */
package application.model;

/** TODO comment class responsibility (SRP)
 * @author tom.jammes
 */
public class Note {
    
    private double valeur;
    
    private int denominateur; 
    
    private Enseignement matiere;
    
    private int poids;
    
    private String forme;
    
    private String description;
    
    private String date;
    
    /**
     * Constructeur de l'objet Note
     * @param valeur valeur de la note 
     * @param denominateur valeur sur la quelle la note est évalué. Ex: 10/20 ou
     *          23/50 ...
     * @param matiere enseignement dans le quel la note a été obtenue
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @param forme type du contrôle. Ex: devoir sur table, tp noté, qcm, ...
     * @param description description du contrôle donné par l'élève
     * @param date date du contrôle. Peut être approximative, ex début janvier
     */
    public Note(double valeur, int denominateur, Enseignement matiere, 
            int poids, String forme, String description, String date) {
        if (!estValide(valeur,denominateur,poids)) {
            throw new IllegalArgumentException("Arguments invalide");
        }
        this.valeur = valeur;
        this.denominateur = denominateur;
        this.matiere = matiere;
        this.poids = poids;
        this.forme = forme;
        this.description = description;
        this.date = date;
    }

    /** 
     * Vérifie que les paramètres rentrés dans le constructeur sont correct
     * test seulement les valeurs numérique
     * @param valeur valeur valeur de la note 
     * @param denominateur valeur sur la quelle la note est évalué. Ex: 10/20 ou
     *          23/50 ... 
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @return true si les paramètres sont corrects
     */
    private static boolean estValide(double valeur, int denominateur, int poids) {
        return 0 <= valeur && valeur <= denominateur && 1 <= denominateur 
                && denominateur <= 1000 && 0 < poids && poids <= 100;
    }
    
    /** 
     * @return la note converti sur 20 points
     */
    public double surVingt() {
        return valeur / denominateur * 20;
    }

    /** @return valeur de valeur */
    public double getValeur() {
        return valeur;
    }

    /** @param valeur nouvelle valeur de valeur */
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    /** @return valeur de denominateur */
    public int getDenominateur() {
        return denominateur;
    }

    /** @param denominateur nouvelle valeur de denominateur */
    public void setDenominateur(int denominateur) {
        this.denominateur = denominateur;
    }

    /** @return valeur de matiere */
    public Enseignement getMatiere() {
        return matiere;
    }

    /** @param matiere nouvelle valeur de matiere */
    public void setMatiere(Enseignement matiere) {
        this.matiere = matiere;
    }

    /** @return valeur de poids */
    public int getPoids() {
        return poids;
    }

    /** @param poids nouvelle valeur de poids */
    public void setPoids(int poids) {
        this.poids = poids;
    }

    /** @return valeur de forme */
    public String getForme() {
        return forme;
    }

    /** @param forme nouvelle valeur de forme */
    public void setForme(String forme) {
        this.forme = forme;
    }

    /** @return valeur de description */
    public String getDescription() {
        return description;
    }

    /** @param description nouvelle valeur de description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return valeur de date */
    public String getDate() {
        return date;
    }

    /** @param date nouvelle valeur de date */
    public void setDate(String date) {
        this.date = date;
    }
}
