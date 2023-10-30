package Graphe;

import javax.swing.*;
import java.util.*;

public class Main extends JPanel{

    public static void main(String[] args) {
        Set<Sommet> sommets = new HashSet<>();
        Set<Arrete> arretes = new HashSet<>();

        Sommet s = new Sommet(0, "S", 20, 150, "h(S)=3");
        Sommet a = new Sommet(1, "A", 150, 40, "h(A)=3");
        Sommet b = new Sommet(2, "B", 150, 260, "h(B)=1");
        Sommet c = new Sommet(3, "C", 280, 150, "h(C)=0");
        Sommet g = new Sommet(4, "G", 400, 150, "h(G)=0");

        sommets.add(s);
        sommets.add(a);
        sommets.add(b);
        sommets.add(c);
        sommets.add(g);

        arretes.add(new Arrete(s, a, 1));
        arretes.add(new Arrete(s, b, 2));
        arretes.add(new Arrete(a, c, 1));
        arretes.add(new Arrete(b, c, 1));
        arretes.add(new Arrete(c, g, 2));

        s.heur = 3;
        a.heur = 3;
        b.heur = 1;
        c.heur = 0;
        g.heur = 0;


        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Partie 1");
        frame.add(new Graph(sommets, arretes));
        frame.setSize(500, 400);
        frame.setVisible(true);

        MatriceAdj matriceAdj = new MatriceAdj(sommets.size());

        for (Arrete arrete : arretes) {
            int i = arrete.getDebut().id;
            int j = arrete.getFin().id;
            matriceAdj.ajouterArrete(i, j, arrete.cout);
        }
        System.out.println("2/ La matrice d'adjacence relative à ce graphe :\n" + matriceAdj + "\n");

        System.out.println("3/ L'ordre de développement des états:");

        s.g = 0;
        Sommet res = AlgoA.aStar(s, g);
        AlgoA.chemin(res);

    }
}
