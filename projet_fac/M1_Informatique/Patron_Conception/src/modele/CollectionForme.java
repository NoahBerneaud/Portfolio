package modele;

import vue.Observer.AbstractModeleEcoutable;
import vue.Observer.EcouteurModele;

import java.util.ArrayList;

public class CollectionForme extends AbstractModeleEcoutable implements EcouteurModele {
    public ArrayList<Forme> formes;
    public ArrayList<EcouteurList> ecouteursList;

    public CollectionForme(){
        this.formes = new ArrayList<Forme>();
        this.ecouteursList = new ArrayList<EcouteurList>();

    }

    public void add(Forme f){
        this.formes.add(f);
        f.addEcouteurModele(this);
        this.fireModeleMisAJour();
        this.fireAjoutForme(f);
    }

    public int getSize(){
        return this.formes.size();
    }

    public Forme getForme(int i){
        return this.formes.get(i);
    }

    public void remove(Forme f){
        this.formes.remove(f);
        f.removeEcouteurModele(this);
        this.fireModeleMisAJour();
        this.fireRetraitForme(f);
    }

    public boolean contains(Point p){
        for(Forme forme : this.formes){
            if(forme.contains(p.getX(), p.getY())){
                return true;
            }
        }
        return false;
    }
    @Override
    public void modeleMisAJour(Object source) {
        this.fireModeleMisAJour();
    }

    public void addEcouteurList(EcouteurList e){
        this.ecouteursList.add(e);
    }

    public void removeEcouteurList(EcouteurList e){
        this.ecouteursList.remove(e);
    }

    protected void fireAjoutForme(Forme f){
        for(EcouteurList e : this.ecouteursList){
            e.formeAjoutee(f);
        }
    }

    protected void fireRetraitForme(Forme f){
        for(EcouteurList e : this.ecouteursList){
            e.formeSupprimee(f);
        }
    }

    public String toString() {
        String s = "";
        for (Forme f : this.formes) {
            s += f.toString() + "";
        }
        return s;
    }
}
