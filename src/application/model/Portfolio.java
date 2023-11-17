/*
 * Portfolio.java                                                   15 nov. 2023
 * IUT Rodez 2023-2024, soporifik, pas de copyright ni "copyleft" 
 */
package application.model;

import java.io.Serializable;

import application.model.exception.EnseignementInvalideException;

/** 
 * Représentation d'un portfolio en tant qu'Enseignement
 * @author tom.jammes
 * @author tony.lapeyre
 * @author thomas.lemaire
 * @version 2.0
 */
public class Portfolio extends Enseignement implements Serializable {

    /* Expression régulière de l'identifiant d'un portfolio */
    private static final String REGEX_PORTFOLIO = "P[1-6]\\.[0-9]{2}";
    
    /* Note finale d'un portfolio */
    private Note notePortfolio;
    
    /* Moyenne finale d'un portfolio */
    private Note moyennePortfolio;
    
    /** 
     * Constructeur d'un portfolio
     * @param intitule est le nom du portfolio
     * @param identifiant du portfolio (exemple : P2.10, P1.02 ...)
     * @throws EnseignementInvalideException déclare un portfolio invalide à la création
     */
    public Portfolio(String intitule, String identifiant) throws EnseignementInvalideException {
        super(intitule, identifiant);
        if(!estValide(REGEX_PORTFOLIO, identifiant, intitule)) {
            throw new EnseignementInvalideException("Portfolio impossible à créer avec les valeurs reseignées");
        }
    }

    /** @param notePortfolio nouvelle valeur de notePortfolio */
    public void setNotePortfolio(Note notePortfolio) {
        this.notePortfolio = notePortfolio;
    }
    
    /** notePortfolio nouvelle valeur de moyennePortfolio */
    @Override
    protected void setMoyenne() {
        this.moyennePortfolio = this.notePortfolio;
    }
    
    /** @return la moyenne du portfolio */
    @Override
    protected Note getMoyenne() {
        return moyennePortfolio;
    }
}
