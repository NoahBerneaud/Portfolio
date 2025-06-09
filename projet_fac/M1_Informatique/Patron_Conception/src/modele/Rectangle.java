package modele;

import vue.Observer.AbstractModeleEcoutable;

import java.awt.*;

public class Rectangle extends AbstractModeleEcoutable implements Forme{

    private Point point;
    private int largeur;
    private int hauteur;

    public Rectangle(int x, int y, int largeur, int hauteur) {
        this.point = new Point(x, y);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setLargeur(int largeur) {
        this.largeur = largeur;
        this.fireModeleMisAJour();
    }

    public int getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
        this.fireModeleMisAJour();
    }

    public String toString() {
        return "Rectangle [point=" + point + ", largeur=" + largeur + ", hauteur=" + hauteur + "]";
    }

    public void translate(int dx, int dy) {
        this.point.translate(dx, dy);
        this.fireModeleMisAJour();
    }

    public Double getAire() {
        return (double) this.largeur * this.hauteur;
    }

    public void draw(Graphics g) {
        g.drawRect(this.point.getX(), this.point.getY(), this.largeur, this.hauteur);
    }

    public void resize(int dx, int dy) {
        this.largeur += dx;
        this.hauteur += dy;
        this.fireModeleMisAJour();
    }

    public boolean contains(int x, int y) {
        if(x >= this.point.getX() && x <= this.point.getX() + this.largeur && y >= this.point.getY() && y <= this.point.getY() + this.hauteur) {
            return true;
        }
        return false;
    }

    public Point getPoint() {
        return point;
    }

    public String getDimension() {
        return "W: " + this.largeur + " H: " + this.hauteur;
    }

    public String getNom() {
        return "Rectangle";
    }





}
