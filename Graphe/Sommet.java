package Graphe;

import java.util.ArrayList;
import java.util.List;

public class Sommet implements Comparable<Sommet> {
    private static int cnt = 0;
    public int id;
    public List<Arrete> voisins;
    public String nom;
    public int x, y;
    public String h;
    public Sommet parent = null;
    public double heur;
    public double g = Double.MAX_VALUE;
    public double f = Double.MAX_VALUE;

    public Sommet(int id, String nom, int x, int y, String h) {
        this.id = id;
        this.nom = nom;
        this.x = x;
        this.y = y;
        this.h = h;
    }
    Sommet(double h){
        this.heur = h;
        this.id = cnt++;
        this.voisins = new ArrayList<>();
    }
    public double Heuristique(Sommet fin){
        return this.heur;
    }

    @Override
    public int compareTo(Sommet s) {
        return Double.compare(this.f, s.f);
    }
}