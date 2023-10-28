package tp01.partie01;

import java.util.Arrays;

public class MatriceAdj {
    private final int[][] adjencyMatrix;
    private final int numSommets;


    public MatriceAdj(int numSommets) {
        this.adjencyMatrix = new int[numSommets][numSommets];
        this.numSommets = numSommets;
    }
    public void ajouterArrete(int i, int j, int cout){
        if(i>=0 && i<numSommets && j>=0 && j<numSommets){
            adjencyMatrix[i][j] = cout;
        }
    }
    public void afficherMatrice(){
        for(int i=0;i<numSommets; i++) {
            System.out.println("");
            for(int j=0; j<numSommets; j++){
                System.out.print(adjencyMatrix[i][j]+"");
            }
        }
        System.out.println();
    }
}
