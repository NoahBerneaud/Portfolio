import modele.*;
import vue.VueGUI;

import java.util.ArrayList;

// BERNEAUD Noah
// CARTIER Jerome
// VASSE Thomas

public class Main {
    public static void main(String[] args) {
       CollectionForme collectionForme = new CollectionForme();
       // Générations des stratégies
       Strategy strat = new StrategyRandom();
       Strategy strat2 = new StrategyCircle();
       // Générations des points selon une stratégie
       GeneratePoint generatePoint = new GeneratePoint(strat,830,420);
       ArrayList<Point> list = generatePoint.Strategygenerate(20);
       // Création du jeu
       Jeu jeu = new Jeu(collectionForme, list, 830, 420);
       // Création de la vue
       VueGUI vueGUI = new VueGUI(collectionForme, list, jeu);
    }
}