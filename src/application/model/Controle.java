/*
 * Controle.java                                                    15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;

import application.model.exception.ControleInvalideException;

/** 
 * Représentation d'un contrôle pour une ressource
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Controle implements Serializable {

    /* type du contrôle renseigné (exemple : oral, écrit ...) */
    private String typeControle;
    
    /* date du contrôle renseigné (exemple : 12/10/2023, milieu octobre ...) */
    private String dateControle;
    
    /* poids du contrôle renseigné (exemple : 12, 35 ...) */
    private int poidsControle;
    
    /* note du contrôle renseigné */
    private Note noteControle;
    
    /* identifiant du controle (exemple : R02.01.01, R10.03.12 ...) */
    private String indentifiantControle;
    
    /**
     * Constructeur d'un contrôle
     * @param type du contrôle renseigné (exemple : oral, écrit ...)
     * @param date du contrôle renseigné (exemple : 12/10/2023, milieu octobre ...)
     * @param poids du contrôle renseigné (exemple : 12, 35 ...)
     * @throws ControleInvalideException déclare un contrôle invalide à la création
     */
    public Controle(String type, String date, int poids) throws ControleInvalideException {
        if(!estValide(type, poids)) {
            throw new ControleInvalideException("Création de contrôle impossible avec les valeurs renseignées");
        }
        setTypeControle(type);
        setDateControle(date);
        setPoidsControle(poids);
    }
    
    /** @return valeur de typeControle */
    public String getTypeControle() {
        return typeControle;
    }

    /** @param typeControle nouvelle valeur de typeControle */
    public void setTypeControle(String typeControle) {
        this.typeControle = typeControle;
    }

    /** @return valeur de dateControle */
    public String getDateControle() {
        return dateControle;
    }
    
    /** @param dateControle nouvelle valeur de dateControle */
    public void setDateControle(String dateControle) {
        this.dateControle = dateControle;
    }
    
    /** @return valeur de poidsControle */
    public int getPoidsControle() {
        return poidsControle;
    }

    /** @param poidsControle nouvelle valeur de poidsControle */
    public void setPoidsControle(int poidsControle) {
        this.poidsControle = poidsControle;
    }

    /** @return valeur de noteControle */
    public Note getNoteControle() {
        return noteControle;
    }
    
    /** @param noteControle nouvelle valeur de noteControle */
    public void setNoteControle(Note noteControle) {
        this.noteControle = noteControle;
    }

    /** @return valeur de indentifiantControle */
    public String getIndentifiantControle() {
        return indentifiantControle;
    }

    /** @param indentifiantControle nouvelle valeur de indentifiantControle */
    public void setIndentifiantControle(String indentifiantControle) {
        this.indentifiantControle = indentifiantControle;
    }
    
    /**
     * Validation de la création du contrôle
     * @param type est valide s'il n'est pas vide
     * @param poids est valide si il est strictement supérieur à 0
     * @return true si le contrôle renseigné est valide, false sinon
     */
    private static boolean estValide(String type, int poids) {
        return !type.equals("") && poids > 0; 
    }
    
    /**
     * Méthode de test
     */
    public String toString() {
        return getIndentifiantControle() + " " + getTypeControle() + " " + getDateControle() + " " + getPoidsControle() + "\n";
    }

    /** @return true si le contrôle a une note */
    public boolean aUneNote() {
        return noteControle != null;
    }
}