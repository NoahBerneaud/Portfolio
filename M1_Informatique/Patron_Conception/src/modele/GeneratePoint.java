package modele;

import java.util.ArrayList;

public class GeneratePoint{
    Strategy strategy;

    public int width;
    public int height;

    public GeneratePoint(Strategy strategy,  int width, int height) {
        this.strategy = strategy;
        this.width = width;
        this.height = height;
    }

    // Methode générique qui mets tout les points au coordonnées (0,0)
    public ArrayList<Point> Classicalgenerate(int nb_point){
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < nb_point; i++) {
            Point p = new Point(0, 0);
            list.add(p);
        }
        return list;

    }

    public ArrayList<Point> Strategygenerate(int nb_point){
        ArrayList<Point> list = strategy.method(nb_point, width, height);
        return list;
    }
}
