package vue.VueForme;

import modele.Ligne;
import modele.Point;
import vue.VueForme.VueForme;

import java.awt.*;

public class VueLigne implements VueForme {

    public Ligne ligne;

    public VueLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    @Override
    public void draw(Graphics g) {
        Point point1 = this.ligne.getPoint1();
        Point point2 = this.ligne.getPoint2();
        g.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    public void setSelected(boolean selected) {
    }
}
