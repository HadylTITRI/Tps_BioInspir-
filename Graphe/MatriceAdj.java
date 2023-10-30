package Graphe;

import java.util.Arrays;

public class MatriceAdj {
    int[][] matrice;
    int taille;
    public MatriceAdj(int taille){
        this.taille = taille;
        matrice = new int[taille][taille];
    }
    public void ajouterArrete(int i, int j, int cout) {
        matrice[i][j]  = cout;
    }


}
