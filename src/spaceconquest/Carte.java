/*
 * Gestion de la carte
 */
package spaceconquest;

import java.awt.Color;
import java.util.HashMap;   
import spaceconquest.Map.Case;
import spaceconquest.Map.Couleur;
import spaceconquest.Map.Couple;
import spaceconquest.ObjetCeleste.ObjetCeleste;
import spaceconquest.Race.Race;
import spaceconquest.Race.Vaisseau;

/**
 *
 * @author simonetma
 */


public class Carte {
    private final int taille;                        //nombre de "colonne" de la map, (la map a 3 fois plus de lignes que de colonnes)
    private final HashMap<Couple,Case> cases;        //listes des cases
    private Couple caseSelectionnee;                 //case actuellement sélectionnée par le joueur
    private final Graphe graphe;
    private boolean[] possible;
    //Constructeur
    public Carte(int _taille) {
        this.taille = _taille;
        this.cases = new HashMap<>(); 
        //initialisation de la map vide
        for(int i=1;i<= 3*_taille;i++) {
            for(int j=1;j<=_taille;j++) {
                this.cases.put(new Couple(i,j), new Case());
            }
        }
        this.graphe = new Graphe(cases.size());//Creation du graphe de la carte
        this.caseSelectionnee = null;
    }
    
    
    //getteur de la taille de la map
    public int getTaille() {
        return this.taille;
    }
    
    //getteur de la case en position i,j
    public Case getCase(int i,int j) {
        return this.cases.get(new Couple(i,j));
    }
    
    //getteur de la case en position c (couple)
    public Case getCase(Couple c) {
        return this.cases.get(c);
    }
    
    //ajoute un objet celeste (étoile, astéroide...) à la position i,j (Passer par la classe partie !)
    public void addObjetCeleste(ObjetCeleste obj, int i,int j) {
        this.getCase(i, j).addObjetCeleste(obj);
        if(obj != null) {
            obj.setPosition(new Couple(i,j));
        }
    }
    
    //ajoute un vaisseau à la position i,j (Passer par la classe partie !)
    public void addVaisseau(Vaisseau v,int i,int j) {
        this.getCase(i,j).addVaisseau(v);
        if(v !=null) {
            v.setPosition(new Couple(i,j));
        }
    }
    
    //fait bouger le vaisseau présent en case départ à la case arrivée (détruisant tout vaisseau présent à cette case)
    public void BougerVaisseau(Couple depart, Couple arrivee) {
        if(this.getCase(depart).getVaisseau() == null) {
            System.err.println("ERREUR : Aucun vaisseau en case "+depart);
            System.exit(0);
        }
        if(this.getCase(arrivee).getVaisseau() != null) {
            System.out.println("Le "+this.getCase(arrivee).getVaisseau() + " a été détruit !");
            this.getCase(arrivee).getVaisseau().setPosition(null);
        }
        this.getCase(arrivee).addVaisseau(this.getCase(depart).getVaisseau());
        this.getCase(depart).addVaisseau(null);
    }
    
    public void deplacement(int[] dist){
        boolean[] possible2 = new boolean[this.getGraphe().getNbSommet()+1];
        
        for(int i = 1 ;i<= this.getGraphe().getNbSommet(); i++){
            if(dist[i]==1 || dist[i]==2){
                possible2[i] = true;
            }
        }
        this.possible = possible2;
    }
    
    
    //méthode gérant ce qu'il se passe quand on clique sur une case en mode manuel
    public void selectionCase(Couple c) {
        if(c.equals(this.caseSelectionnee)) {
            //deselection de la case
            this.getCase(c).setCouleur(Couleur.Blanc);
            this.effacerColoration();
            this.caseSelectionnee = null;
        }
        else {
            //si une case avait déja été sélectionnée
            if(this.caseSelectionnee != null) {
                //ajouter des conditions de déplacement
                if(this.possible[coupleToSommet(c)]){  
                    //on fait bouger le vaisseau
                    this.BougerVaisseau(this.caseSelectionnee, c);
                    this.effacerColoration();// et on efface la coloration
                    //on déselectionne la case
                    this.getCase(this.caseSelectionnee).setCouleur(Couleur.Blanc);
                    this.caseSelectionnee = null;
                    //on passe le tour
                    SpaceConquest.tourSuivant();
                }      
            }
            else {
                //si aucune case n'avait été selectionné
                //on vérifie que la case nouvellement sélectionné contient un vaisseau du joueur en cours
                if(this.getCase(c).getVaisseau() != null) {
                    if(this.getCase(c).getVaisseau().getRace() == SpaceConquest.getTour()) {
                        //on selectionne la case
                        this.caseSelectionnee = c;
                        if(this.getCase(c).getVaisseau().getRace() == Race.Licorne){
                            this.colorationMouvement(c.getX(), c.getY(),this.getGrapheLicorne());
                        }
                        if(this.getCase(c).getVaisseau().getRace() == Race.Zombie){
                            this.colorationMouvement(c.getX(), c.getY(),this.getGrapheZombie());
                        }
                    }
                }
            }
        }
    }

    public Graphe getGraphe() {return graphe;}
    
    /**
     * 
     * @return le graphe modélisant la carte
     */
    public Graphe getGrapheGrille(){
        Graphe g = new Graphe(graphe);
        int n = this.getTaille();
        for(int i = 1; i <= 3*n; i++){
            for(int j = 1; j <= n; j++){
                if(n%2 == 0){ //si la taille de la grille est paire
                    if((i == 3*n-1) && (j == n)){ //la fichtre case du coin en bas à droite relié à une seule case
                        g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                    }
                    else {
                        if((j == 1) && (i%2 == 0) && (i != 3*n)){ //relier 2 cases + case à gauche (que des lignes paires) et sauf le coin bas gauche
                            g.modifierMatrice(n*(i-1)+j,n*i+j , 1);
                            g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                        }
                        else { 
                            if((j == n) && (i%2 == 1)){//relier 2 cases + case à droite (sans la case du coin bas droit 
                                g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                            }
                            else{
                                if(i == 3*n-1){ //relier 2 cases + cases du bas + cases sur ligne impaire
                                    g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                    g.modifierMatrice(n*(i-1)+j, n*i+j+1, 1);
                                }
                                else{
                                    if((i%2 == 0) && (i != 3*n)){ //relier 3 cases + ligne paire
                                        g.modifierMatrice(n*(i-1)+j, n*i+j-1, 1);
                                        g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                        g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                    }
                                    else{
                                        if(i%2 == 1){ //relier 3 cases + ligne impaire
                                            g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                            g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                            g.modifierMatrice(n*(i-1)+j, n*i+j+1, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else { //si elle est impaire
                    if((i == 3*n-1) && (j == 1)){ //la fichtre case du coin en bas à gauche relié à une seule case
                        g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                    }
                    else {
                        if((j == 1) && (i%2 == 0)){ //relier 2 cases + case à gauche (que des lignes paires) et sauf le coin bas gauche
                            g.modifierMatrice(n*(i-1)+j,n*i+j , 1);
                            g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                        }
                        else { 
                            if((j == n) && (i%2 == 1) && (i != 3*n)){//relier 2 cases + case à droite (sans la case du coin bas droit déja traitée)
                                g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                            }
                            else{
                                if(i == 3*n-1){ //relier 2 cases + cases du bas + cases sur ligne impaire
                                    g.modifierMatrice(n*(i-1)+j, n*i+j-1, 1);
                                    g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                }
                                else{
                                    if((i%2 == 0) && (i != 3*n)){ //relier 3 cases + ligne paire
                                        g.modifierMatrice(n*(i-1)+j, n*i+j-1, 1);
                                        g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                        g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                    }
                                    else{
                                        if((i%2 == 1) && (i != 3*n)){ //relier 3 cases + ligne impaire
                                            g.modifierMatrice(n*(i-1)+j, n*i+j, 1);
                                            g.modifierMatrice(n*(i-1)+j, n*(i+1)+j, 1);
                                            g.modifierMatrice(n*(i-1)+j, n*i+j+1, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return g;        
    }
    
    public Graphe getGrapheZombie(){
        int n = this.taille;
        Graphe zombGraphe = this.getGrapheGrille();
       
        //parcours du graphe pour detecter où sont les étoiles
        isolerEtoile(zombGraphe);
        return zombGraphe;
    }
    
    /**
     * 
     * @return le graphe arrangé des licornes (valeur des cases arrangés sur les cases contenant un asteroides)
     */
    public Graphe getGrapheLicorne(){
        int n = this.taille;
        Graphe licoGraphe = this.getGrapheGrille();
        licoGraphe.setOrientation(true);
        for(int i = 1; i <= this.getGraphe().getNbSommet(); i++){  
                //je commence par récupérer les voisins puis je regarde si ces voisins contiennet un astéroïde
            for(int k = 1; k <= this.getGraphe().getNbSommet(); k++){
                if(licoGraphe.getMatrice(i, k)==1){
                    if(this.getCase(sommetToCouple(k, taille)).getObjetCeleste() != null){
                        if("asteroide".equals(this.getCase(sommetToCouple(k, taille)).getObjetCeleste().getType())){
                            licoGraphe.modifierMatrice(i, k, 2); 
                        }
                    }
                }
            }
            
        }
        
        isolerEtoile(licoGraphe);
        vadeRetroShadock(licoGraphe);
        return licoGraphe;
    }
    
    public Graphe getGrapheShadoks(){
        int n = this.taille;
        Graphe grSha = new Graphe(this.getGrapheGrille());
        Dijktra dij = new Dijktra(grSha);
        dij.CalculDistance(this.trouverPlaneteShadock(grSha));
        for(int i = 1; i<= this.getGraphe().getNbSommet(); i++){
           if(dij.getDist()[i] > 3){
               grSha.isolerSommet(i);
           }
        }
        return grSha;
    }
    
    
    /**
     * récupère la case où se situe es Shadocks
     * @param g 
     * @return  
     */
     public Couple trouverShadock(Graphe g){
        Couple sha = null;//sommet inexistant
        for (int i = 1; i<= 3*this.getTaille(); i++){
            for (int j = 1;  j<= this.getTaille(); j++){
                if(this.getCase(i, j).getVaisseau() != null){
                String nomV =this.getCase(i, j).getVaisseau().toString();
                    if("fusée interplanétaire Shadock".equals(nomV)){
                            sha = this.getCase(i, j).getVaisseau().getPosition();  
                    }
                }    
            }
        }
        return sha;
    }
     
    public int trouverPlaneteShadock(Graphe g){
        int sha = -1;//sommet inexistant
        for (int i = 1; i<= 3*this.getTaille(); i++){
            for (int j = 1;  j<= this.getTaille(); j++){
                if(this.getCase(i, j).getObjetCeleste() != null){
                String nomV =this.getCase(i, j).getObjetCeleste().getType();
                    if("planete Shadoks" ==(nomV)){
                            sha = this.coupleToSommet(this.getCase(i, j).getObjetCeleste().getPosition());  
                    }
                }    
            }
        }
        return sha;
    } 
    
     
    public void vadeRetroShadock(Graphe g){
        Couple sha = this.trouverShadock(g);
        int Csha = this.coupleToSommet(sha);
        /*if (sha != null){ //cas où le vaisseau Shadocks à été trouvé
            int Csha = this.coupleToSommet(sha);
            for(int i =1; i<= g.getNbSommet(); i++){
                if(g.getMatrice(i, Csha)!=0){
                    for(int j=1; j<=g.getNbSommet(); j++){
                        if(g.getMatrice(j, i)!=0){
                            g.isolerSommet(j);
                        }
                    }
                    g.isolerSommet(i);
                }           
            }
            g.isolerSommet(Csha);
        }    */
      Dijktra d = new Dijktra(g);
      d.CalculDistance(Csha);
      for (int i = 1; i< d.getDist().length; i++){
          if(d.getDist()[i] == 1 || d.getDist()[i] == 2){
              g.isolerSommet(i);
          }
      }
    }
     
    /**
     * récupère la case où se situe es Shadocks
     * @param g 
     * @return  
     */
     public Couple trouverShadock(Graphe g){
        Couple sha = null;//sommet inexistant
        for (int i = 1; i<= 3*this.getTaille(); i++){
            for (int j = 1;  j<= this.getTaille(); j++){
                if(this.getCase(i, j).getVaisseau() != null){
                String nomV =this.getCase(i, j).getVaisseau().toString();
                    if("fusée interplanétaire Shadock".equals(nomV)){
                            sha = this.getCase(i, j).getVaisseau().getPosition();  
                    }
                }    
            }
        }
        return sha;
    }
     
    public void vadeRetroShadock(Graphe g){
        Couple sha = this.trouverShadock(g);
        int Csha = this.coupleToSommet(sha);
        /*if (sha != null){ //cas où le vaisseau Shadocks à été trouvé
            int Csha = this.coupleToSommet(sha);
            for(int i =1; i<= g.getNbSommet(); i++){
                if(g.getMatrice(i, Csha)!=0){
                    for(int j=1; j<=g.getNbSommet(); j++){
                        if(g.getMatrice(j, i)!=0){
                            g.isolerSommet(j);
                        }
                    }
                    g.isolerSommet(i);
                }           
            }
            g.isolerSommet(Csha);
        }    */
      Dijktra d = new Dijktra(g);
      d.CalculDistance(Csha);
      for (int i = 1; i< d.getDist().length; i++){
          if(d.getDist()[i] == 1 || d.getDist()[i] == 2){
              g.isolerSommet(i);
          }
      }
    }
     
    /**
     * Détecte et isole les étoiles présentes sur la carte
     * @param g grahe où l'on doit isoler les sommet (soit celui du zombie soit celui de la licorne
     */
    public void isolerEtoile(Graphe g){
       for(int i = 1; i <= 3*this.taille; i++){
            for(int j = 1; j <= this.taille; j++){
                if(this.getCase(i, j).getObjetCeleste() != null){
                    if("etoile".equals(this.getCase(i, j).getObjetCeleste().getType())){
                        g.isolerSommet(this.taille*(i-1)+j);
                    }
                }
            }
        } 
    }
    
    /**
     * Colore toute les cases de al cartes en blanc
     */
    public void effacerColoration(){
        int n = this.taille;
        for(int i = 1; i <= 3*n; i++){
            for(int j = 1; j <= n; j++){
                this.getCase(i, j).setCouleur(Couleur.Blanc);
            }
        }
    }
    /**
     * 
     * @param nSommet num du sommet à transformer
     * @param tailleGrille taille de la grille considéré (nombre de ligne ou de colonne)
     * @return c le couple correspondant
     */
    public Couple sommetToCouple(int nSommet, int tailleGrille){
        int i,j;
        i = nSommet/tailleGrille;
        if(nSommet%tailleGrille != 0) i+=1; //si on est passé à la ligne suivante
        j = nSommet%tailleGrille;
        if(j == 0) j = tailleGrille;
        Couple c = new Couple(i,j);
        return c;
    }
    
    public int coupleToSommet(Couple c){
        return this.getTaille()*(c.getX()-1)+c.getY();
    }
    
    public void coloreCaseVert(int i, int j){
        this.getCase(i, j).setCouleur(Couleur.Vert);
    }
    
    public void coloreCaseJaune(int i, int j){
        this.getCase(i, j).setCouleur(Couleur.Jaune);
    }
    
    public void coloreCaseBlanc(int i, int j){
        this.getCase(i, j).setCouleur(Couleur.Blanc);
    }
    
     public void coloreCaseRouge(int i, int j){
        this.getCase(i, j).setCouleur(Couleur.Rouge);
    }
    
    public void colorationMouvement(int i, int j, Graphe g){
        int sommetDep = this.taille*(i-1)+j;
        
        Dijktra dij = new Dijktra(g); //on récupère le tableau des distances que nous donne dijktra
        dij.CalculDistance(sommetDep);
        int[] dist = dij.getDist();
        for(int k = 1; k <= g.getNbSommet(); k++){ // ensuite on le parcours et on colore en vert les voisins de 1 et en jaune les voisins de 2 
            if(dist[k]==1){
                coloreCaseVert(this.sommetToCouple(k, this.taille).getX(), this.sommetToCouple(k, this.taille).getY()); 
                //la modification de la case d'une couleur ne peut se fais re que si l'on conait cette case en tant que couple ou coordonnées x/y sur la carte
                // et pas en tant que numéro de sommet
            }
            else if(dist[k]==2){
                coloreCaseJaune(this.sommetToCouple(k, this.taille).getX(), this.sommetToCouple(k, this.taille).getY());
            }
        }
        this.deplacement(dist);
    }
}
