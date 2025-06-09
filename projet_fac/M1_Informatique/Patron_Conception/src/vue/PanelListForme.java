package vue;

import modele.CollectionForme;

import javax.swing.*;
import java.awt.*;

public class PanelListForme extends JPanel {

    private CollectionForme collectionForme;
    private JTable table;

    public PanelListForme(CollectionForme collectionForme) {
        this.collectionForme = collectionForme;
        this.setBackground(Color.BLUE);
        // Création de la JTable avec le modèle AdapterJTable
        this.table = new JTable(new AdapterJTable(collectionForme));
        this.setLayout(new GridLayout(1,1));
        this.add(new JScrollPane(this.table));
    }

}
