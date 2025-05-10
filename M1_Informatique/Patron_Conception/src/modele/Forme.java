package modele;

import vue.Observer.ModeleEcoutable;

public interface Forme extends ModeleEcoutable {
    public String toString();

    public Double getAire();

    public void translate(int dx, int dy);

    public boolean contains(int x, int y);

    public String getNom();

    public Point getPoint();

    public String getDimension();


}
