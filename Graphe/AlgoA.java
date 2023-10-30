package Graphe;

import java.util.*;

public class AlgoA {
    public static Sommet aStar(Sommet debut, Sommet fin) {
        Set<Arrete> arretes = new HashSet<>();

        PriorityQueue<Sommet> closedList = new PriorityQueue<>();
        PriorityQueue<Sommet> openedList = new PriorityQueue<>();

        debut.f = debut.g + debut.Heuristique(fin);
        openedList.add(debut);

        while (!openedList.isEmpty()) {
            Sommet n = openedList.peek();
            if (n == fin) {
                return n;
            }
            for (Arrete arrete : arretes) {
                Sommet m = arrete.fin;
                double coutTotal = n.g + arrete.cout;

                if (!openedList.contains(m) && !closedList.contains(m)) {
                    m.parent = n;
                    m.g = coutTotal;
                    m.f = m.g + m.Heuristique(fin);
                    openedList.add(m);
                } else {
                    if (coutTotal < m.g) {
                        m.parent = n;
                        m.g = coutTotal;
                        m.f = m.g + m.Heuristique(fin);
                        if (closedList.contains(m)) {
                            closedList.remove(m);
                            openedList.add(m);
                        }
                    }
                }
            }
            openedList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    public static void chemin(Sommet fin){
        Sommet n = fin;
        if(n == null){
            return;
        }
        List<Integer> ids = new ArrayList<>();
        List<String> noms = new ArrayList<>();

        while(n.parent != null){
            ids.add(n.id);
            noms.add(n.nom);
            n = n.parent;
        }
        ids.add(n.id);
        noms.add(n.nom);

        Collections.reverse(ids);
        Collections.reverse(noms);

        for (int id : ids) {
            for (String nom : noms) {
                System.out.print(id +"" + nom + "");
            }
        }
        System.out.println("");
    }
}
