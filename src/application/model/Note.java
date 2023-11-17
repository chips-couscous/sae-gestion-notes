/*
 * Note.java                                                        15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import application.model.exception.NoteInvalideException;

/** 
 * Représentation d'une note obtenue à un contrôle, au portfolio ou lors d'une saé.
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Note {

    /* valeur de la note (exemple : 10, 12, 50 ...) */
    private double valeurNote;
    
    /* denominateur de la note, valeur sur laquelle est notée la note x (exemple : x/10, x/20, x/1000 ...) */
    private int denominateurNote;
    
    /* commentaire d'une note (exemple : précision sur la note, liste des connaissances à revoir ...) */
    private String commentaire;
    
    /**
     * Constructeur d'une note
     * @param valeur est la note obtenue (exemple : 10, 12, 50 ...)
     * @param denominateur , valeur sur laquelle est notée la note x (exemple : x/10, x/20, x/1000 ...)
     * @param commentaire d'une note (exemple : précision sur la note, liste des connaissances à revoir ...)
     * @throws NoteInvalideException déclare une note invalide à la création
     */
    public Note(double valeur, int denominateur, String commentaire) throws NoteInvalideException {
        if(!estValide(valeur, denominateur)) {
            throw new NoteInvalideException("Création de note impossible avec les valeurs renseignées");
        }
        setValeurNote(valeur);
        setDenominateurNote(denominateur);
        setCommentaire(commentaire);
    }
    
    /** 
     * Constructeur d'une par chainage de constructeur
     * @param valeur est la note obtenue (exemple : 10, 12, 50 ...)
     * @param denominateur , valeur sur laquelle est notée la note x (exemple : x/10, x/20, x/1000 ...)
     * @throws NoteInvalideException déclare une note invalide à la création 
     */
    public Note(double valeur, int denominateur) throws NoteInvalideException {
        // Fait appel au constructeur Note avec tous les paramètres requis
        this(valeur, denominateur, "");
    }
    
    /**
     * Modifier les valeurs d'une note
     * @param valeur est la note obtenue (exemple : 10, 12, 50 ...)
     * @param denominateur , valeur sur laquelle est notée la note x (exemple : x/10, x/20, x/1000 ...)
     * @param commentaire d'une note (exemple : précision sur la note, liste des connaissances à revoir ...)
     * @throws NoteInvalideException déclare une note invalide à la création
     */
    public void modifierNote(double valeur, int denominateur, String commentaire) throws NoteInvalideException {
        if(!estValide(valeur, denominateur)) {
            throw new NoteInvalideException("Création de note impossible avec les valeurs renseignées");
        }
        setValeurNote(valeur);
        setDenominateurNote(denominateur);
        setCommentaire(commentaire);
    }
    
    /** @return valeur de valeurNote */
    public double getValeurNote() {
        return valeurNote;
    }
    
    /** @return valeur de valeurNote sur 20 */
    public double getValeurNoteSurVingt() {
        return valeurNote * 20 / denominateurNote;
    }

    /** @param valeurNote nouvelle valeur de valeurNote */
    public void setValeurNote(double valeurNote) {
        this.valeurNote = valeurNote;
    }

    /** @return valeur de denominateurNote */
    public int getDenominateurNote() {
        return denominateurNote;
    }
    
    /** @param denominateurNote nouvelle valeur de denominateurNote */
    public void setDenominateurNote(int denominateurNote) {
        this.denominateurNote = denominateurNote;
    }

    /** @return valeur de commentaire */
    public String getCommentaire() {
        return commentaire;
    }

    /** @param commentaire nouvelle valeur de commentaire */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * Validation de la création de la note
     * @param valeur est valide si elle est supérieure ou égale à 0 et inférieure ou égale au dénominateur
     * @param denominateur est valide si il est strictement supérieur à 0 est inférieur ou égal à 1000
     * @return true si la note est valide, false sinon
     */
    private static boolean estValide(double valeur, int denominateur) {
        return valeur >= 0 && valeur <= denominateur && denominateur > 0 && denominateur <= 1000;
    }
}
