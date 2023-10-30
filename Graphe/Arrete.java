package Graphe;

public class Arrete {
    public final Sommet debut;
    public final Sommet fin;
    public final int cout;

    public Arrete(Sommet debut, Sommet fin, int cout) {
        this.debut = debut;
        this.fin = fin;
        this.cout = cout;
    }

    public Sommet getDebut() {
        return debut;
    }

    public Sommet getFin() {
        return fin;
    }

    public int getCout() {
        return cout;
    }
}