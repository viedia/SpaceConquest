/*
 * Classe principale du projet
 */
package spaceconquest;

import spaceconquest.ObjetCeleste.*;
import spaceconquest.Parties.Mode;
import spaceconquest.Partie;
import spaceconquest.Race.Race;

/**
 *
 * @author simonetma
 */
public class SpaceConquest {
    
    private static Partie partie;

    
    public static Race getTour() {
        return partie.getTour();
    }
    
    public static Mode getMode() {
        return partie.getMode();
    }
    

    
    public static void tourSuivant() {
        partie.tourSuivant();
    }
    
    public static void main(String[] args) {
        //on cree la partie
        partie = new Partie(5);
        //ajout des éléments clé de la partie
        partie.placerLicoLand(2, 2);
        partie.placerLicoShip(5, 5);
        partie.placerZombificator(10, 3);
        partie.placerVaisseauShadoks(2,3);
        //placement des objets célestes
        partie.placerObjetCeleste(new Etoile(), 3, 3);
        partie.placerObjetCeleste(new PlaneteShadoks(),2 ,1);
        
        partie.placerObjetCeleste(new Asteroide(), 4, 4);
        partie.placerObjetCeleste(new Asteroide(), 5, 4);
        partie.placerObjetCeleste(new Asteroide(), 5, 3);
        
        //on definit le mode de jeu
        partie.setMode(Mode.manuel);
        //on lance l'IHM
        partie.start();
        
    }  
}
