package vue.Command;

import modele.CollectionForme;
import modele.Forme;
import vue.PanelDessin;

public class DeleteCommand implements OperationCommand {

    private Forme f;
    private PanelDessin pannelDessin;

    private CollectionForme collectionForme;

    public DeleteCommand(Forme f, PanelDessin pannelDessin, CollectionForme collectionForme) {
        this.f = f;
        this.pannelDessin = pannelDessin;
        this.collectionForme = collectionForme;
    }

    @Override
    public void operate() {
        this.pannelDessin.formeSupprimee(f);
        this.collectionForme.remove(f);
    }

    @Override
    public void compensate() {
        this.pannelDessin.formeAjoutee(f);
        this.collectionForme.add(f);
    }

    @Override
    public Object getObjet() {
        return this.f;
    }


}
