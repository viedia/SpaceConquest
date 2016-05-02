/*
 * Classe de gestion des graphes
 */
package spaceconquest;

import java.util.HashMap;
import spaceconquest.Map.Couple;

/**
 *
 * @author simonetma
 */

public class Graphe {
    private int nbSommet;
    private HashMap<Couple,Integer> matrice;
    private Boolean orienté;
    
    //constructeur
    public Graphe(int n) {
        this.nbSommet = n;
        this.matrice = new HashMap<>();
        this.orienté = false;
    }
    
    public Graphe(Graphe copie){
        this.matrice = copie.matrice;
        this.nbSommet = copie.nbSommet;
        this.orienté = copie.orienté;
    }
    
    //renvoie le nombre de sommet du graphe    
    public int getNbSommet() {
        return this.nbSommet;
    }
    
    //*************** gestion de la matrice d'adjacence ***********************
    //Modifie la valeur (i,j) de la matrice d'adjacence du graphe
    //Ainsi qu'en (j,i), on ne prend pas en compte que les arrètes qui descendent, 
    //a chaque ligne de la matrice on connait quels sommets à quels sommets sont reliés
    public void modifierMatrice(int i,int j,int valeur) {
        if(i<=0 || j<=0) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else if(i>this.nbSommet || j>this.nbSommet) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else
        {
            if(!this.orienté){
            Couple c1 = new Couple(i,j);
            Couple c2 = new Couple(j,i);
            this.matrice.put(c1, valeur);
            this.matrice.put(c2, valeur);
            }
            else {
                Couple c3 = new Couple(i,j);
                this.matrice.put(c3, valeur);
            }
        }
    }
    
    //renvoie la valeur du coefficient (i,j) de la matrice d'adjacence (0 par défaut)
    public int getMatrice(int i,int j) {
        if(i<=0 || j<=0) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else if(i>this.nbSommet || j>this.nbSommet) {
            System.err.println("Erreur ! La matrice d'adjacence ne possède pas de coefficient ("+i+","+j+") !");
        }
        else {
            Couple c = new Couple(i,j);
            if(this.matrice.containsKey(c)) {
                return this.matrice.get(c);
            }
        }
        return 0;
    }
    
    //renvoie l'orientation
    public boolean getOrientation() {
        return this.orienté;
    }

    public void setOrientation(Boolean b) {
        this.orienté = b;
    }
    
    
    //affiche la matrice d'adjaceance
    @Override
    public String toString() {
        String ret = "<html><center>Matrice du graphe :<br><br>";
        for(int i=1;i<=this.nbSommet;i++) {
            for(int j=1;j<=this.nbSommet;j++) {
                Couple c = new Couple(i,j);
                if(this.matrice.containsKey(c)) {
                    ret += this.matrice.get(c);
                }
                else {
                    ret += "0";
                }
                if(j<this.nbSommet) {
                    ret+= " ";
                }
            }
            if(i<this.nbSommet) {
                ret+="<br>";
            }
        }
        ret += "</center></html>";
        return ret;
    }
    
    /**
     * enlève les arêtes en le sommet i et tout les autres sommets
     * @param i numéro du sommet
     */
    public void isolerSommet(int i){
        for(int j = 1; j <= this.nbSommet; j++){
            //on supprime tout les liens entre la case et ses voisins
            this.modifierMatrice(i, j, 0);
            //on supprime tout les liens entre tout les voisins de la case et la case
            this.modifierMatrice(j, i, 0);
        }
    }
    
    
}
