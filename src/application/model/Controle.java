/*
 * Note.java                                      26 oct. 2023
 * IUT Rodez, info2 2023-2024, pas de copyright ni "copyleft" 
 */
package application.model;

/** 
 * Représentation d'un controle
 * @author tom.jammes
 */
public class Controle {
    
    /** Contient la valeur de la note  */
    private double valeur;
    
    /** Contient le dénominateur du contrôle, son barème */
    private int denominateur; 
    
    /** Contient la description du contrôle */
    private String description;
    
    /** Contient le poids de la note dans l'enseignement */
    private int poids;
    
    /** Contient le type de controle effectué */
    private String forme;
    
    /** Contient la date du contrôle */
    private String date;
    
    /** Définit si la note a été renseigné ou pas */
    private boolean noteRemplie = false;
    
    /** Id du contrôle  */
    private int idControle;

    /**
     * Constructeur de l'objet Note
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @param forme type du contrôle, exemple: devoir sur table, tp noté, qcm, ...
     * @param date date du contrôle. Peut être approximative, exemple "début janvier"
     * @param id id du contrôle
     */
    public Controle(int poids, String forme, String date, int id) {
        if (!estValide(poids)) {
            throw new IllegalArgumentException("Poids invalide");
        }
        this.poids = poids;
        this.forme = forme;
        this.date = date;
        this.idControle = id;
    }

    /** 
     * Vérifie que les paramètres rentrés dans le constructeur sont corrects
     * Teste seulement les valeurs numériques
     * @param poids poids de la note dans l'enseignement auquel elle appartient
     * @return true si le poids du contrôle est correct
     */
    private static boolean estValide(int poids) {
        return 0 < poids && poids <= 100;
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

    /** @return valeur de dénominateur */
    public int getDenominateur() {
        return denominateur;
    }

    /** @param denominateur nouvelle valeur de dénominateur */
    public void setDenominateur(int denominateur) {
        this.denominateur = denominateur;
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

    /** @return valeur de noteRemplie */
    public boolean isNoteRemplie() {
        return noteRemplie;
    }

    /** @param noteRemplie nouvelle valeur de noteRemplie */
    public void setNoteRemplie(boolean noteRemplie) {
        this.noteRemplie = noteRemplie;
    }

    /** @return valeur de idControle */
    public int getIdControle() {
        return idControle;
    }
}
