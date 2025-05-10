package vue;

import modele.CollectionForme;
import modele.Forme;
import vue.Observer.EcouteurModele;

import javax.swing.table.AbstractTableModel;

public class AdapterJTable extends AbstractTableModel implements EcouteurModele {

    private CollectionForme collectionForme;

    public AdapterJTable(CollectionForme collectionForme) {
        this.collectionForme = collectionForme;
        this.collectionForme.addEcouteurModele(this);
    }

    @Override
    public int getRowCount() {
        return this.collectionForme.getSize();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Forme f = this.collectionForme.getForme(rowIndex);
        switch (columnIndex){
            case 0:
                return f.getNom();
            case 1:
                return f.getPoint();
            case 2:
                return f.getDimension();
            default:
                return null;
        }
    }

    @Override
    public void modeleMisAJour(Object source) {
        this.fireTableDataChanged();
    }


}
