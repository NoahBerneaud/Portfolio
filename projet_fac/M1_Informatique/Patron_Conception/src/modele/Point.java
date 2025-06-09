package modele;

import vue.Observer.AbstractModeleEcoutable;

public class Point extends AbstractModeleEcoutable implements Forme{
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
        this.fireModeleMisAJour();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.fireModeleMisAJour();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.fireModeleMisAJour();
    }

    public Double getAire() {
        return 0.0;
    }

    public boolean contains(int x, int y) {
        return false;
    }

    public String getNom() {
        return "Point";
    }

    @Override
    public Point getPoint() {
        return this;
    }

    @Override
    public String getDimension() {
        return null;
    }

    public Point getCentre() {
        return this;
    }


}
