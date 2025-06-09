package modele;

import vue.Observer.AbstractModeleEcoutable;

import java.awt.*;

public class Cercle extends AbstractModeleEcoutable implements Forme{
    private int rayon;

    private Point centre;

    public Cercle(int x, int y, int rayon) {
        this.centre = new Point(x, y);
        this.rayon = rayon;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
        this.fireModeleMisAJour();
    }

    public String toString() {
        return "Cercle [rayon=" + rayon + ", centre=" + centre + "]";
    }

    public void translate(int dx, int dy) {
        this.centre.translate(dx, dy);
        this.fireModeleMisAJour();
    }

    public Double getAire() {
        return Math.PI * this.rayon * this.rayon;
    }

    public boolean contains(int x, int y) {
        double distance = Math.sqrt(Math.pow(x - this.centre.getX(), 2) + Math.pow(y - this.centre.getY(), 2));
        return distance <= this.rayon;
    }

    public Point getCentre() {
        return centre;
    }

    public String getNom() {
        return "Cercle";
    }

    public Point getPoint() {
        return this.centre;
    }

    public String getDimension() {
        return "Rayon : " + this.rayon;
    }

}
