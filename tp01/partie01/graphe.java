package tp01.partie01;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.*;

public class graphe {

    private static List<Object> reconstructPath(Map<Object, Object> cameFrom, Object courant) {
        List<Object> chemin = new ArrayList<>();
        chemin.add(courant);
        while (cameFrom.containsKey(courant)) {
            courant = cameFrom.get(courant);
            chemin.add(0, courant);
        }
        return chemin;
    }

    private static List<Object> aStar(Object depart, Object arrivee, Map<Object, Integer> heuristique, int[][] matriceAdj, List<Object> sommets) {
        Set<Object> explored = new HashSet<>();
        Map<Object, Object> cameFrom = new HashMap<>();
        Map<Object, Integer> gScore = new HashMap<>();
        Map<Object, Integer> fScore = new HashMap<>();

        for (Object vertex : sommets) {
            gScore.put(vertex, Integer.MAX_VALUE);
            fScore.put(vertex, Integer.MAX_VALUE);
        }

        gScore.put(depart, 0);
        fScore.put(depart, heuristique.get(depart));

        PriorityQueue<Object> openSet = new PriorityQueue<>(Comparator.comparingInt(fScore::get));
        openSet.add(depart);

        while (!openSet.isEmpty()) {
            Object courant = openSet.poll();

            if (courant.equals(arrivee)) {
                return reconstructPath(cameFrom, arrivee);
            }

            explored.add(courant);

            for (int i = 0; i < sommets.size(); i++) {
                if (matriceAdj[sommets.indexOf(courant)][i] != 0) {
                    Object voisin = sommets.get(i);
                    int tentative_gScore = gScore.get(courant) + matriceAdj[sommets.indexOf(courant)][i];
                    if (tentative_gScore < gScore.get(voisin)) {
                        cameFrom.put(voisin, courant);
                        gScore.put(voisin, tentative_gScore);
                        fScore.put(voisin, tentative_gScore + heuristique.get(voisin));
                        if (!explored.contains(voisin)) {
                            openSet.add(voisin);
                        }
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) {
        mxGraph graphe = new mxGraph();
        Object parent = graphe.getDefaultParent();

        List<Object> sommets = new ArrayList<>();
        List<Object> arretes = new ArrayList<>();

        graphe.getModel().beginUpdate();
        Map<Object, Integer> h;
        try {
            Object s = graphe.insertVertex(parent, null, "S", 20, 120, 80, 40);
            Object a = graphe.insertVertex(parent, null, "A", 160, 60, 80, 40);
            Object b = graphe.insertVertex(parent, null, "B", 160, 180, 80, 40);
            Object c = graphe.insertVertex(parent, null, "C", 300, 120, 80, 40);
            Object g = graphe.insertVertex(parent, null, "G", 460, 120, 80, 40);

            sommets.add(s);
            sommets.add(a);
            sommets.add(b);
            sommets.add(c);
            sommets.add(g);

            h = new HashMap<>();

            h.put(s, 3);
            h.put(a, 3);
            h.put(b, 1);
            h.put(c, 0);
            h.put(g, 0);

            Object arr1 = graphe.insertEdge(parent, null, 1, s, a);
            Object arr2 = graphe.insertEdge(parent, null, 2, s, b);
            Object arr3 = graphe.insertEdge(parent, null, 1, a, c);
            Object arr4 = graphe.insertEdge(parent, null, 1, b, c);
            Object arr5 = graphe.insertEdge(parent, null, 2, c, g);

            arretes.add(arr1);
            arretes.add(arr2);
            arretes.add(arr3);
            arretes.add(arr4);
            arretes.add(arr5);

        } finally {
            graphe.getModel().endUpdate();
        }

        int[][] matriceAdj = new int[sommets.size()][sommets.size()];

        for (int i = 0; i < sommets.size(); i++) {
            for (int j = 0; j < sommets.size(); j++) {
                matriceAdj[i][j] = 0;
            }
        }

        for (Object arrete : arretes) {
            int src = sommets.indexOf(graphe.getModel().getTerminal(arrete, true));
            int dis = sommets.indexOf(graphe.getModel().getTerminal(arrete, false));

            Object value = ((mxCell) arrete).getValue();
            //matriceAdj[src][dis] = ((Integer) value).intValue();
            matriceAdj[src][dis] = (Integer) value;
        }
        System.out.println("2/ La matrice d'adjacence relative à ce graphe est :");
        for (int[] ints : matriceAdj) {
            for (int j = 0; j < matriceAdj.length; j++) {
                System.out.print(ints[j] + "");
            }
            System.out.println();
        }

        Object depart = sommets.get(0);
        Object arrivee = sommets.get(sommets.size() - 1);

        List<Object> cheminAetoile = aStar(depart, arrivee, h, matriceAdj, sommets);

        if (cheminAetoile != null) {
            System.out.println("3/ Le plus court chemin calculé en utilisant l'algorithme de A* est :");
            for(int i = 0; i <cheminAetoile.size() - 1; i++){
                Object src = cheminAetoile.get(i);
                Object dis = cheminAetoile.get(i+1);

                Object[] edges = graphe.getEdgesBetween(src, dis);

                for(Object edge : edges){
                    graphe.setCellStyles(mxConstants.STYLE_STROKECOLOR, "red", new Object[]{edge});
                }
                System.out.print("->" + graphe.convertValueToString(src) + "");
            }
            System.out.print("->" + graphe.convertValueToString(cheminAetoile.get(cheminAetoile.size() - 1)));

        } else {
            System.out.println("Aucun chemin n'a été trouvé.");
        }


        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mxGraphComponent graphComponent = new mxGraphComponent(graphe);

        frame.setTitle("TP01 : Partie 01");
        frame.getContentPane().add(graphComponent);
        frame.setSize(600, 300);
        frame.setVisible(true);
    }
}
