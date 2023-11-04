package tp01.partie02;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Graphe {
    public static void main(String[] args){
        mxGraph graphe = new mxGraph();
        Object parent = graphe.getDefaultParent();

        List<Object> sommets = new ArrayList<>();
        List<Object> arretes = new ArrayList<>();

        graphe.getModel().beginUpdate();
        try{
            Object s = graphe.insertVertex(parent, null, "S", 20, 150, 80, 40);
            Object a = graphe.insertVertex(parent, null, "A", 120, 60, 80, 40);
            Object b = graphe.insertVertex(parent, null, "B", 220, 20, 80, 40);
            Object c = graphe.insertVertex(parent, null, "C", 220, 90, 80, 40);
            Object d = graphe.insertVertex(parent, null, "D", 340, 60, 80, 40);
            Object g = graphe.insertVertex(parent, null, "G", 450, 150, 80, 40);

            sommets.add(s);
            sommets.add(a);
            sommets.add(b);
            sommets.add(c);
            sommets.add(d);
            sommets.add(g);

            Object arr1 = graphe.insertEdge(parent,null, 1,s, a );
            Object arr2 = graphe.insertEdge(parent,null, 3,a, b );
            Object arr3= graphe.insertEdge(parent,null, 1,a, c );
            Object arr4 = graphe.insertEdge(parent,null, 3,b, d );
            Object arr5 = graphe.insertEdge(parent,null, 1,c, d );
            Object arr6 = graphe.insertEdge(parent,null, 2,c, g );
            Object arr7 = graphe.insertEdge(parent,null, 3,d, g );
            Object arr8 = graphe.insertEdge(parent,null, 12,s, g );

            arretes.add(arr1);
            arretes.add(arr2);
            arretes.add(arr3);
            arretes.add(arr4);
            arretes.add(arr5);
            arretes.add(arr6);
            arretes.add(arr7);
            arretes.add(arr8);

        }finally {
            graphe.getModel().endUpdate();
        }

        int[][] matriceAdj = new int[sommets.size()][sommets.size()];

        for(int i = 0 ; i< sommets.size(); i++){
            for(int j = 0; j< sommets.size(); j++){
                matriceAdj[i][j] = 0;
            }
        }

        for (Object arrete:arretes){
            int src = sommets.indexOf(graphe.getModel().getTerminal(arrete, true));
            int dis = sommets.indexOf(graphe.getModel().getTerminal(arrete, false));

            Object value = ((mxCell) arrete).getValue();
            //matriceAdj[src][dis] = ((Integer) value).intValue();
            matriceAdj[src][dis] = (Integer) value;
        }
        System.out.println("La matrice d'adjacence relative à ce graphe est :");
        for (int[] ints : matriceAdj) {
            for (int j = 0; j < matriceAdj.length; j++) {
                System.out.print(ints[j] + "");
            }
            System.out.println();
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mxGraphComponent graphComponent = new mxGraphComponent(graphe);

        frame.setTitle("TP01 : Partie 02");
        frame.getContentPane().add(graphComponent);
        frame.setSize(600,300);
        frame.setVisible(true);
    }
}
