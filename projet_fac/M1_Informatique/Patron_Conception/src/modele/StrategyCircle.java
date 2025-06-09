package modele;

import java.util.ArrayList;
import java.util.Random;

public class StrategyCircle implements Strategy {

    // Methode qui créer un ensemble de point aléatoire sur un cercle
    @Override
    public ArrayList<Point> method(int nb_point, int width, int height) {
       int rayon = 100;
       int x = (width-100)/2;
       int y = (height-100)/2;
       ArrayList<Point> list = new ArrayList<>();
       Random r = new Random();
       for (int i = 0; i < nb_point; i++) {
           double angle = 2*Math.PI*r.nextDouble();
           int x1 = (int) (x + rayon * Math.cos(angle));
           int y1 = (int) (y + rayon * Math.sin(angle));
           Point p = new Point(x1, y1);
           list.add(p);
       }
       return list;
    }
}

