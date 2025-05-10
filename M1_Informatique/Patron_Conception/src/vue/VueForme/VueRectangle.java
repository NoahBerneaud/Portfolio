package vue.VueForme;

import modele.Point;
import modele.Rectangle;
import vue.VueForme.VueForme;

import java.awt.*;

public class VueRectangle implements VueForme {

    public Rectangle rectangle;
    public boolean selected;

    public VueRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void draw(Graphics g) {
        Point point = this.rectangle.getPoint();
        int largeur = this.rectangle.getLargeur();
        int hauteur = this.rectangle.getHauteur();
        // Si la forme est sélectionnée, on dessine un rectangle plein
        if (this.selected) {
            g.fillRect(point.getX(), point.getY(), largeur, hauteur);
        }
        // Sinon, on dessine un rectangle vide
        else {
            g.drawRect(point.getX(), point.getY(), largeur, hauteur);
        }

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}