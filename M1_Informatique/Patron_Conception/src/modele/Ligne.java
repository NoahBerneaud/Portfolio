package modele;

import vue.Observer.AbstractModeleEcoutable;

public class Ligne extends AbstractModeleEcoutable implements Forme {

    public Point point1;
    public Point point2;

    public Ligne(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public String toString() {
        return "Ligne [point1=" + point1 + ", point2=" + point2 + "]";
    }

    public Double getAire() {
        return 0.0;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void translate(int dx, int dy) {
        this.point1.translate(dx, dy);
        this.point2.translate(dx, dy);
        this.fireModeleMisAJour();
    }

    public boolean contains(int x, int y) {
        if(x >= this.point1.getX() && x <= this.point2.getX() && y >= this.point1.getY() && y <= this.point2.getY()) {
            return true;
        }
        return false;
    }

    public String getNom() {
        return "Ligne";
    }

    public Point getPoint() {
        return this.point1;
    }

    public String getDimension() {
        return "Longueur : " + (Math.sqrt(Math.pow((this.point2.getX() - this.point1.getX()), 2) + Math.pow((this.point2.getY() - this.point1.getY()), 2)));
    }


}