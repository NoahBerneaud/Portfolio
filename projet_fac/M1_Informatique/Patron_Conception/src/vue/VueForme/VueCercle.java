package vue.VueForme;

import modele.Cercle;
import modele.Point;

import java.awt.*;

public class VueCercle implements VueForme {

    public Cercle cercle;
    public boolean selected;

    public VueCercle(Cercle cercle) {
        this.cercle = cercle;
    }

    @Override
    public void draw(Graphics g) {
        Point point = this.cercle.getCentre();
        int rayon = this.cercle.getRayon();
        if(this.selected) {
            // Si la forme est sélectionnée, on dessine un cercle plein
            g.fillOval(point.getX() - rayon, point.getY() - rayon, 2 * rayon, 2 * rayon);
        }
        else {
            // Sinon, on dessine un cercle vide
            g.drawOval(point.getX() - rayon, point.getY() - rayon, 2 * rayon, 2 * rayon);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
