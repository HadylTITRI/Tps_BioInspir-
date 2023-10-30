package tp01.partie01;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.util.Map;


public class graphe extends JFrame {
    public graphe(){
        mxGraph graphe = new mxGraph();
        Object parent = graphe.getDefaultParent();
        graphe.getModel().beginUpdate();

        try{
            Object[] sommet = new Object[5];
            sommet[0] = graphe.insertVertex(parent, null,"S\nh(S)=3", 20, 80, 80, 40);
            sommet[1] = graphe.insertVertex(parent, null,"A\nh(A)=3", 165, 40, 80, 40);
            sommet[2] = graphe.insertVertex(parent, null,"B\nh(B)=1", 165, 120, 80, 40);
            sommet[3] = graphe.insertVertex(parent, null,"C\nh(C)=0", 300, 80, 80, 40);
            sommet[4] = graphe.insertVertex(parent, null,"G\nh(G)=0", 460, 80, 80, 40);

            graphe.insertEdge(parent, null, "1", sommet[0],sommet[1]);
            graphe.insertEdge(parent, null, "2", sommet[0],sommet[2]);
            graphe.insertEdge(parent, null, "1", sommet[1],sommet[3]);
            graphe.insertEdge(parent, null, "1", sommet[2],sommet[3]);
            graphe.insertEdge(parent, null, "2", sommet[3],sommet[4]);
        }finally {
            graphe.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graphe);
        getContentPane().add(graphComponent);
        
    }

    public static void main(String[] args){
        int numSommets = 5;
        MatriceAdj matrice = new MatriceAdj(5);

        graphe frame = new graphe();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 280);
        frame.setVisible(true);

        matrice.ajouterArrete(0,1, 1);
        matrice.ajouterArrete(0, 2, 2);
        matrice.ajouterArrete(1, 3, 1);
        matrice.ajouterArrete(2, 3, 1);
        matrice.ajouterArrete(3, 4, 2);

        System.out.println("2/La matrice d'adjacence relative à ce graphe:");
        matrice.afficherMatrice();

        System.out.println("3/L'algorithme A*:");


    }
}
