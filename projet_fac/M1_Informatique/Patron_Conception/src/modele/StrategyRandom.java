package modele;

import java.util.ArrayList;

public class StrategyRandom implements Strategy {

    // Methode qui créer un ensemble de point aléatoire
    @Override
    public ArrayList<Point> method(int nb_point, int width, int height){
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < nb_point; i++) {
            int x = (int) (Math.random() * (width-100));
            int y = (int) (Math.random() * (height-100));
            Point p = new Point(x, y);
            list.add(p);
        }
        return list;
    }
}
