package vue.VueForme;

import modele.CollectionForme;
import modele.Point;
import vue.Observer.EcouteurModele;

import java.awt.*;

public class VuePoint implements VueForme, EcouteurModele {

    public Point point;

    public boolean selected;

    public CollectionForme collectionForme;

    public VuePoint(Point point,CollectionForme collectionForme) {
        this.point = point;
        this.selected = false;
        this.collectionForme = collectionForme;
        collectionForme.addEcouteurModele(this);

    }

    @Override
    public void draw(Graphics g) {
        // Si la forme est sélectionnée, on change la couleur
        if(this.selected){
            g.setColor(Color.RED);
        }
        else{
            g.setColor(Color.BLACK);
        }
        g.fillOval(point.getX(), point.getY(), 5, 5);
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;

    }

    @Override
    public void modeleMisAJour(Object source) {
        this.setSelected(false);
        // Pour chaque forme de la collection, on vérifie si le point est dans la forme
        for(int i = 0 ; i< this.collectionForme.getSize(); i ++){
            if(this.collectionForme.getForme(i).contains(this.point.getX(), this.point.getY())){
                // Si le point est dans la forme, on la sélectionne
                this.setSelected(true);
                break;
            }
        }
    }
}
