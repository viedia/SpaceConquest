/*
 * Timer pour le mode automatique
 */
package spaceconquest;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Random;
import spaceconquest.Map.Couple;
import spaceconquest.Parties.Mode;

/**
 *
 * @author simonetma
 */
public class TimerPartie extends Timer {

        private Partie partie;                                                  //partie en cours
        
            
    //constructuer    
    public TimerPartie(Partie partie) {
        super();
        this.partie = partie;
        if(this.partie.getMode() == Mode.automatique) {
            this.scheduleAtFixedRate(new TimerTaskPartie(this.partie), 0, 1000);
        }
    }

    //arret du timer si besoin
    public void stop() {
        this.cancel();
    }
    
    //sous classe privée
    private class TimerTaskPartie extends TimerTask {
        
        private Partie partie;
        
        //constructeur
        public TimerTaskPartie(Partie partie) {
            this.partie = partie;
        }

        public Partie getPartie() {
            return partie;
        }
        
        
        
        //fonction appellée à chaque tic du timer
        @Override
        public void run() {
            if(this.partie.isIHMReady()) {
                switch(this.partie.getTour()) {
                    case Licorne : this.tourDesLicornes(); break;
                    case Zombie : this.tourDesZombies(); break;
                    case Shadoks : this.tourDesShadoks(); break;
                }
                    this.partie.tourSuivant();
            }
        }
    
        public void tourDesZombies() {
                
            System.out.println("Tour des Zombies !");
            
            trans(partie.getCarte().getGrapheZombie()); //transpose le graphe des zombies
            Couple posDep= this.partie.getLicoShip().getPosition(); //récupère la position des licornes = position de départ car le graphe est transposé
            Couple posArr = this.partie.getZombificator().getPosition(); //récupère la position des zombies = position de d'arrivée car le graphe est transposé
            Couple dessin; // case à colorier en jaune

            int sommetZ;
            int sommetArr= sommetZ = this.partie.getCarte().coupleToSommet(posArr); // traduit en sommet les couple posDep et posArr
            int sommetDep = this.partie.getCarte().coupleToSommet(posDep);
             //j'effectue Dijkstra
            Dijktra dij = new Dijktra (partie.getCarte().getGrapheZombie());
            dij.CalculDistance(sommetDep);
           // récupère le prédesseceur de la case du Zombie
            int pred = dij.getPi()[sommetArr];
            // si le licorne et les zombie ne se suivent pas = ne sont pas côte à côte
            if (dij.getPi()[sommetZ]!=sommetDep) {
                sommetArr = pred; // je recule d'une case
                dessin = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille()); //récupère la case à colorier
                this.partie.getCarte().coloreCaseJaune(dessin.getX(), dessin.getY());// je colorie la premère case 
                pred = dij.getPi()[sommetArr]; // récupère le pred du prédecesseur des zombies
            }
            this.partie.getCarte().coloreCaseJaune(posArr.getX(), posArr.getY());//colorie
            posDep = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille()); //converti en Couple
            
            this.partie.getCarte().BougerVaisseau(posArr, posDep );    //déplace le vaisseau        
            this.partie.getZombificator().setPosition(posDep); // met à jour la position du vaisseau zombie
        }
            
        public void trans(Graphe g){
            for (int i=1; i<=g.getNbSommet(); i++){
                for (int j=1; j<=g.getNbSommet(); j++){
                    if (i!= j){
                        int temp = g.getMatrice(i, j);
                        g.modifierMatrice(i, j, g.getMatrice(j, i));
                        g.modifierMatrice(j, i, temp);
                    }
                }
            }
        }
//ce qu'il se passe lors du tour des licornes
        private void tourDesLicornes() {   
                
            System.out.println("Tour des Licornes !");
            
            trans(partie.getCarte().getGrapheLicorne()); //transpose le graphe des licornes
            Couple posDep= this.partie.getLicoLand().getPosition(); //récupère la position de la planette des licornes = position de départ car le graphe est transposé
            Couple posArr = this.partie.getLicoShip().getPosition(); //récupère la position des licornes = position de d'arrivée car le graphe est transposé
            Couple dessin; // case à colorier en jaune
            int pa=2;
            int sommetZ;
            int sommetArr= sommetZ = this.partie.getCarte().coupleToSommet(posArr); // traduit en sommet les couple posDep et posArr
            int sommetDep = this.partie.getCarte().coupleToSommet(posDep);
             //j'effectue Dijkstra
            Dijktra dij = new Dijktra (partie.getCarte().getGrapheLicorne());
            dij.CalculDistance(sommetDep);
           // récupère le prédesseceur de la case des licornes
            int pred = dij.getPi()[sommetArr];
            
            pa-=partie.getCarte().getGrapheLicorne().getMatrice(sommetArr, pred);
            pa-=partie.getCarte().getGrapheLicorne().getMatrice(pred, dij.getPi()[pred]); // On soustrait à pa le coût de deplacement entre la première case qu'il traverse et la seconde
            
             // Si (les licornes et leurs planettes ne se suivent pas = ne sont pas côte à côte) ET ( pa est supérieur ou égal à 0 = on peut se déplacer de 2 cases)
            if ((dij.getPi()[sommetZ]!=sommetDep)&&((pa>=0))) {
                sommetArr = pred; // je recule d'une case
                dessin = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille()); //récupère la case à colorier
                this.partie.getCarte().coloreCaseVert(dessin.getX(), dessin.getY());// je colorie la premère case 
                pred = dij.getPi()[sommetArr]; // récupère le pred du prédecesseur des licornes
            }
            this.partie.getCarte().coloreCaseVert(posArr.getX(), posArr.getY());//colorie
            posDep = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille()); //converti en Couple
            
            this.partie.getCarte().BougerVaisseau(posArr, posDep );    //déplace le vaisseau        
            this.partie.getLicoShip().setPosition(posDep); // met à jour la position du vaisseau licornes
        }
        
        private void tourDesShadoks(){
            System.out.println("Tour des Shadoks");
            //Variables
            Graphe grSha = this.partie.getCarte().getGrapheShadoks();
            Carte _carte = this.partie.getCarte();
            
            Dijktra dij = new Dijktra(grSha);
            dij.CalculDistance(_carte.coupleToSommet(_carte.trouverShadock(grSha)));
            ArrayList sommetDispo = new ArrayList();
            //on met dans une liste les sommets accesibles auu shadoks
            for(int i = 1; i <= grSha.getNbSommet(); i++){
                if(dij.getDist()[i]<3 && dij.getDist()[i]>0){
                    sommetDispo.add(i);
                }
            }
            
            //on se déplace aléatoirement sur une des cases des sommets dispo
            Random r = new Random();
            int pif = 1 + r.nextInt(sommetDispo.size()-1);
            _carte.BougerVaisseau(_carte.trouverShadock(grSha), _carte.sommetToCouple((int)sommetDispo.get(pif), _carte.getTaille()));
            this.partie.getFuseeShadoks().setPosition( _carte.sommetToCouple((int)sommetDispo.get(pif), _carte.getTaille()));
        }
        
    }

}



