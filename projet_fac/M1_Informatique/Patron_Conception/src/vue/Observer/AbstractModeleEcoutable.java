package vue.Observer;

import vue.Observer.EcouteurModele;
import vue.Observer.ModeleEcoutable;

import java.util.ArrayList;

public abstract class AbstractModeleEcoutable implements ModeleEcoutable {

    public ArrayList<EcouteurModele> listeners = new ArrayList<EcouteurModele>();

    public void addEcouteurModele(EcouteurModele e) {
        listeners.add(e);
    }

    public void removeEcouteurModele(EcouteurModele e) {
        listeners.remove(e);
    }

    public void fireModeleMisAJour() {
        for (EcouteurModele e : listeners) {
            e.modeleMisAJour(this);
        }
    }
}
