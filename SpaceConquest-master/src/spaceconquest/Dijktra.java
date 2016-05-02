/*
 * To change this.gr license header, choose License Headers in Project Properties.
 * To change this.gr template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceconquest;

/**
 * Calculateur dijktra, permet d'obtenir a partir de n'importe quel graphe, les tableaux des distances de chaque sommet 
 * par rapport à un sommet passé dans la fonction calculDistance.
 * 
 * Pour obtenir un des 3 tableaux que sont les prédescesseurs, les distances par rapport au point de départ et le tableau des sommets visités :
 * 
 * 1 - commencer par créer une instance de la classe dijktra : 
 * Dijktra dij = new Dijktra(Graphe g); 
 * g est le graphe sur lequel vous voulez effectuer le calcul des distances
 * 
 * 2 - faire le calcul des distances et mettre à jour les différents tableau de dij
 * dij.calculDistance(int sommetDep); 
 * sommetDep et le numéro du sommet duquel vous voulez partir
 * 
 * 3 - récupérer et stocker dans un nouveau tableau le tableau d'information que vous souhaitez
 * 
 * int[] dist = dij.getDist(); 
 * pour le tableau des distances
 * 
 * int[] pi = dij.getPi(); 
 * pour le tableau des prédescesseurs
 * 
 * boolean[] mark = dij.getMark();
 * pour le tableau des sommets visités
 * @author Florian
 */
public class Dijktra {
    private final int[] pi;
    private final int[] dist;
    private final boolean[] mark;
    private final Graphe gr;
    
    public Dijktra(Graphe g) {
        this.gr = g;
        this.mark = new boolean[this.gr.getNbSommet()+1];
        this.dist = new int[this.gr.getNbSommet()+1];
        this.pi = new int[this.gr.getNbSommet()+1];
        
    }
    
    public void relachement(int a,int b, int[] dist, int[] pi){
        if(this.gr.getMatrice(a, b)!=0){
            if(dist[b]>dist[a]+this.gr.getMatrice(a, b)){
                dist[b]=dist[a] + this.gr.getMatrice(a, b);
                pi[b] = a;
            }
        }
    }
    
    public boolean resteSommetUnknown(boolean[] mark){
        boolean result = false;
        for(int i = 1; i <= this.gr.getNbSommet(); i++){
            if(this.mark[i]==false) result = true;
        }
        return result;
    }
    
    public int Infini(){
        int nbArretes = 0;
        for(int i = 1; i <= this.gr.getNbSommet(); i++){
            for(int j = 1; j <= this.gr.getNbSommet(); j++){
                if(this.gr.getMatrice(i, j)!=0) nbArretes+=this.gr.getMatrice(i, j);
            }
        }
        return nbArretes+1;
    }
    public int sommetUnknownDistMin(int[] dist, boolean[] mark){
        int indiceMin = -1, Min = Infini();
        for(int i = 1; i <= this.gr.getNbSommet(); i++){
            if((!this.mark[i]) && (this.dist[i]<= Min)){
                indiceMin = i;
                Min = this.dist[i];
            }
        }
        return indiceMin;
    }
    
    public void CalculDistance(int dep){
        for(int i = 1; i <= this.gr.getNbSommet(); i++){
            this.mark[i] = false;
            this.pi[i] = -1;
            this.dist[i] = Infini();
        }
        this.dist[dep] = 0;
        //Fin de la procédure d'initialisation
        
        while(resteSommetUnknown(mark)){
            int a = sommetUnknownDistMin(this.dist, this.mark);
            this.mark[a] = true;
            for(int b = 1; b<= this.gr.getNbSommet(); b++){
                relachement(a, b, this.dist, this.pi);
            }
        }
    }

    public int[] getPi() {return pi;}
    
    public int[] getDist() {return dist;}
    
    public boolean[] getMark() {return mark;}   
}