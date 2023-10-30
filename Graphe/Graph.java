package Graphe;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Set;

public class Graph extends JPanel {
    private final Set<Sommet> sommets;
    private final Set<Arrete> arretes;

    public Graph(Set<Sommet> sommets, Set<Arrete> arretes) {
        this.sommets = sommets;
        this.arretes = arretes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Font font = new Font("Arial", Font.BOLD, 15);
        g.setFont(font);

        super.paintComponent(g);
        for(Arrete arrete : arretes){
            drawArrow(g, arrete.debut.x, arrete.debut.y, arrete.fin.x , arrete.fin.y);
            g.drawLine(arrete.debut.x, arrete.debut.y, arrete.fin.x, arrete.fin.y);
            g.drawString(String.valueOf(arrete.cout),(arrete.debut.x + arrete.fin.x)/2 - 3 ,(arrete.debut.y + arrete.fin.y)/2 - 5);
        }
        for(Sommet sommet : sommets){
            g.drawOval(sommet.x , sommet.y - 10, 22, 22);
            g.drawString(sommet.nom, sommet.x +5  , sommet.y + 5);
            g.drawString(sommet.h, sommet.x-15, sommet.y+40 );
        }
    }
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2 = (Graphics2D) g.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);

        at.concatenate(AffineTransform.getRotateInstance(angle));

        g2.transform(at);

        g2.drawLine(0,0,len,0);

        g2.fillPolygon(new int[]{len, len - 8, len - 8, len}, new int[]{0, -6,6, 0}, 4);
    }

}

