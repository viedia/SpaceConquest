/*
 * Timer pour le mode automatique
 */
package spaceconquest;

import java.util.Timer;
import java.util.TimerTask;
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
                }
                    this.partie.tourSuivant();
            }
        }
    
        //ce qu'il se passe lors du tour des zombies
      /*  private void tourDesZombies() {
            //System.out.println("Tour des Zombies !");
            
            Couple posArr= this.partie.getLicoShip().getPosition();
            Couple posZom = this.partie.getZombificator().getPosition();
            
            this.partie.getCarte().coloreCaseJaune(posZom.getX(), posZom.getY());
            
            int sommetArr = this.partie.getCarte().coupleToSommet(posArr);
            int sommetDep = this.partie.getCarte().coupleToSommet(posZom);

            Dijktra dij = new Dijktra (partie.getCarte().getGrapheZombie());
            dij.CalculDistance(sommetDep);
            
            
            int pred = dij.getPi()[sommetArr];
            while (sommetDep!=pred ){
                sommetArr = pred;
                pred = dij.getPi()[sommetArr];
                System.out.println(dij.getDist()[sommetArr]);
            }
            posArr = this.partie.getCarte().sommetToCouple(sommetArr,this.partie.getCarte().getTaille()); 
            this.partie.getCarte().BougerVaisseau(posZom, posArr);
            this.partie.getZombificator().setPosition(posArr);
        
        }*/
        public void tourDesZombies() {
            
            trans(partie.getCarte().getGrapheZombie());
            Couple posDep= this.partie.getLicoShip().getPosition();
            Couple posArr = this.partie.getZombificator().getPosition();
            Couple dessin;

            int sommetZ;
            int sommetArr= sommetZ = this.partie.getCarte().coupleToSommet(posArr);
            int sommetDep = this.partie.getCarte().coupleToSommet(posDep);
            
            Dijktra dij = new Dijktra (partie.getCarte().getGrapheZombie());
            dij.CalculDistance(sommetDep);
           
            int pred = dij.getPi()[sommetArr];
            
            if (dij.getPi()[sommetZ]!=sommetDep) {
                sommetArr = pred;
                dessin = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille());
                this.partie.getCarte().coloreCaseJaune(dessin.getX(), dessin.getY());
                pred = dij.getPi()[sommetArr]; 
            }
            this.partie.getCarte().coloreCaseJaune(posArr.getX(), posArr.getY());
            posDep = this.partie.getCarte().sommetToCouple(pred,this.partie.getCarte().getTaille()); 
            
            this.partie.getCarte().BougerVaisseau(posArr, posDep );           
            this.partie.getZombificator().setPosition(posDep); 
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
        }
    }    
}


