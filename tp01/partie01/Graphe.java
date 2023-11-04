package tp01.partie01;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.*;
import java.util.List;


public class Graphe extends JFrame {
    public mxGraph Graphe() {

        mxGraph graphe = new mxGraph();
        Object parent = graphe.getDefaultParent();
        graphe.getModel().beginUpdate();

        try {
            Object[] sommet = new Object[5];
            sommet[0] = graphe.insertVertex(parent, null, "S", 20, 80, 80, 40);
            sommet[1] = graphe.insertVertex(parent, null, "A", 165, 40, 80, 40);
            sommet[2] = graphe.insertVertex(parent, null, "B", 165, 120, 80, 40);
            sommet[3] = graphe.insertVertex(parent, null, "C", 300, 80, 80, 40);
            sommet[4] = graphe.insertVertex(parent, null, "G", 460, 80, 80, 40);

            graphe.setCellStyles(mxConstants.STYLE_FONTSIZE, "15", new Object[]{sommet[0]});
            graphe.setCellStyles(mxConstants.STYLE_FONTSIZE, "15", new Object[]{sommet[1]});
            graphe.setCellStyles(mxConstants.STYLE_FONTSIZE, "15", new Object[]{sommet[2]});
            graphe.setCellStyles(mxConstants.STYLE_FONTSIZE, "15", new Object[]{sommet[3]});
            graphe.setCellStyles(mxConstants.STYLE_FONTSIZE, "15", new Object[]{sommet[4]});

            graphe.setCellStyles("heuristique", Integer.toString(3), new Object[]{sommet[0]});
            graphe.setCellStyles("heuristique", Integer.toString(3), new Object[]{sommet[1]});
            graphe.setCellStyles("heuristique", Integer.toString(1), new Object[]{sommet[2]});
            graphe.setCellStyles("heuristique", Integer.toString(0), new Object[]{sommet[3]});
            graphe.setCellStyles("heuristique", Integer.toString(0), new Object[]{sommet[4]});

            graphe.insertEdge(parent, null, "1", sommet[0], sommet[1]);
            graphe.insertEdge(parent, null, "2", sommet[0], sommet[2]);
            graphe.insertEdge(parent, null, "1", sommet[1], sommet[3]);
            graphe.insertEdge(parent, null, "1", sommet[2], sommet[3]);
            graphe.insertEdge(parent, null, "2", sommet[3], sommet[4]);

            List<Object> listeSommets = new ArrayList<>();
            listeSommets.add(sommet[0]);
            listeSommets.add(sommet[1]);
            listeSommets.add(sommet[2]);
            listeSommets.add(sommet[3]);
            listeSommets.add(sommet[4]);

        } finally {
            graphe.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graphe);
        getContentPane().add(graphComponent);

        return graphe;

    }

    public static void main(String[] args) {


        int numSommets = 5;
        MatriceAdj matrice = new MatriceAdj(5);

        Graphe frame = new Graphe();


        matrice.ajouterArrete(0, 1, 1);
        matrice.ajouterArrete(0, 2, 2);
        matrice.ajouterArrete(1, 3, 1);
        matrice.ajouterArrete(2, 3, 1);
        matrice.ajouterArrete(3, 4, 2);

        ///////////// 2ème Qst ////////////////////////
        System.out.println("2/La matrice d'adjacence relative à ce graphe:");
        matrice.afficherMatrice();

        /////////////  3ème Qst //////////////////////
        AEtoile.Sommet s = new AEtoile.Sommet("S", 0, 3);
        AEtoile.Sommet a = new AEtoile.Sommet("A", 1, 3);
        AEtoile.Sommet b = new AEtoile.Sommet("B", 2, 1);
        AEtoile.Sommet c = new AEtoile.Sommet("C", 3, 0);
        AEtoile.Sommet g = new AEtoile.Sommet("G", 4, 0);

        s.arretes.add(new AEtoile.Arrete(s, a, 1));
        s.arretes.add(new AEtoile.Arrete(s, b, 2));
        a.arretes.add(new AEtoile.Arrete(a, c, 1));
        b.arretes.add(new AEtoile.Arrete(b, c, 1));
        c.arretes.add(new AEtoile.Arrete(c, g, 2));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 280);
        frame.setVisible(true);


        List<AEtoile.Sommet> chemin = AEtoile.astar(s, g);

        String ch = "";
        Set<Integer> ids = new HashSet<>();
        int l = 0;

        System.out.println("3/Le plus court chemin calculé en utilisant l'algorithme A*:");

            if (chemin != null) {
                for (AEtoile.Sommet sommet : chemin) {
                        ch = ch + "->" + sommet.s;
                        ids.add(sommet.id);
                        l++;
                        System.out.println(ch);
                }
            } else {
                System.out.println("Aucun chemin trouvé !");
            }

            for (int i = 0; i < chemin.size() - 1; i++) {
                Object src = chemin.get(s.id);
                Object target = chemin.get(s.id + 1);

                Object[] edges = frame.Graphe().getEdgesBetween(src, target);
                    for (Object edge : edges) {
                            mxCell cell = (mxCell) edge;
                            frame.Graphe().setCellStyles(mxConstants.STYLE_FONTSTYLE, String.valueOf(mxConstants.FONT_BOLD), new Object[]{cell});
                            //cell.setStyle("strokeColor=#FF0000");
                            //frame.Graphe().getModel().setStyle(cell, cell.getStyle());
                        }
            frame.Graphe().refresh();

        }
    }
}
