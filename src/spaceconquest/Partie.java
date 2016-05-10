/*
 * partie en cours
 */
package spaceconquest;

import spaceconquest.IHM.Fenetre;
import spaceconquest.TimerPartie;
import spaceconquest.ObjetCeleste.ObjetCeleste;
import spaceconquest.ObjetCeleste.Planete;
import spaceconquest.Parties.Mode;
import spaceconquest.Race.Race;
import spaceconquest.Race.Vaisseau;

/**
 *
 * @author simonetma
 */
public class Partie {
    private Race tour;                                                          //A qui c'est de jouer
    private Mode mode;                                                          //Quel mode de jeu ?
    private final Carte carte;                                                  //Carte de la partie
    private Fenetre fenetre;                                                    //Fenetre de l'IHM
    
    private Planete LicoLand;                                                   //monde d'origine des licornes
    private Vaisseau LicoShip;                                                  //Vaisseau Licorne
    private Vaisseau Zombificator;                                              //Vaisseau Zombie
    private Vaisseau FuseeShadoks;                                              //Vaisseau shadoks
    
    private TimerPartie timer;                                                  //timer pour le mode automatique
    
    //Constructeur
    public Partie(int taille) {
        this.tour = Race.Licorne;
        this.mode = Mode.manuel;
        this.carte = new Carte(taille);
        
        this.LicoLand = null;
        this.LicoShip = null;
        this.Zombificator = null;
        this.FuseeShadoks = null;
    }
    
    //création de LicoLand 
    public void placerLicoLand(int i,int j) {
        LicoLand = new Planete();
        this.carte.addObjetCeleste(LicoLand, i, j);
    }

    public Planete getLicoLand() {
        return LicoLand;
    }
    
    //création du LicoShip 
    public void placerLicoShip(int i,int j) {
        this.LicoShip = new Vaisseau(Race.Licorne);
        this.carte.addVaisseau(LicoShip, i, j);
    }
    
    //création du Zombificator 
    public void placerZombificator(int i,int j) {
        this.Zombificator = new Vaisseau(Race.Zombie);
        this.carte.addVaisseau(Zombificator, i, j);
    }
    
    //création du vaisseau shadoks
    public void placerFuseeShadoks(int i, int j){
        this.FuseeShadoks = new Vaisseau(Race.Shadoks);
        this.carte.addVaisseau(FuseeShadoks, i, j);
    }
    
    //récupère le vaisseau shadoks
    public Vaisseau getFuseeShadoks() {
        return FuseeShadoks;
    }
    
    //récupère le vaisseau des licornes
    public Vaisseau getLicoShip() {
        return LicoShip;
    }
    //récupère le vaisseau des zombies
    public Vaisseau getZombificator() {
        return Zombificator;
    }
    
    //création d'objet céleste
    public void placerObjetCeleste(ObjetCeleste objet,int i,int j) {
        carte.addObjetCeleste(objet, i, j);
    }
    //renvoie le joueur dont c'est le tour
    public Race getTour() {
        return this.tour;
    }
    
    //renvoie le mode de jeu
    public Mode getMode() {
        return this.mode;
    }
    
    //renvoie si l'IHM est prete
    public boolean isIHMReady() {
        return (this.fenetre != null);
    }
    
    //fixe le mode de jeu
    public void setMode(Mode _mode) {
        this.mode = _mode;
    }
    
    //passe le tour (dans les deux modes de jeu)
    public void tourSuivant() {
        if(this.getMode() == this.getMode().manuel)
            if(tour == Race.Zombie) {
                tour = Race.Licorne;
            }
            else {
                tour = Race.Zombie;
            }
        else if(this.getMode() == this.getMode().automatique){
            if(tour == Race.Shadoks) {
                tour = Race.Licorne;
            }
            else if(tour == Race.Licorne){
                tour = Race.Zombie;
            }
            else if(tour == Race.Zombie){
                tour = Race.Shadoks;
            }
        }
        this.fenetre.repaint();
        
    }
    
    //renvoie la carte de la partie
    public Carte getCarte() {
        return this.carte;
    }
    
    //démarre la partie
    public void start() {
        //lance le timer
        timer = new TimerPartie(this);
        //lancement de l'IHM
        this.fenetre = new Fenetre(this.carte);
    }
}
