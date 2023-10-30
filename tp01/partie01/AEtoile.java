package tp01.partie01;

import Graphe.Arrete;

import java.util.ArrayList;
import java.util.List;

public class AEtoile {
    static class Sommet {
        int id;
        double heuristicCost;
        double finalCost;
        List<Arrete> arretes;

        public Sommet(int id, double heuristicCost) {
            this.id = id;
            this.heuristicCost = heuristicCost;
            this.finalCost = Double.POSITIVE_INFINITY;
            this.arretes = new ArrayList<>();
        }
    }
    static class Arrete {
        tp01.partie01.Sommet source, destination;
        double cost;

        public Arrete(tp01.partie01.Sommet source, tp01.partie01.Sommet destination, double cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }
    }
}
