package vue;

import vue.State.StateForme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoutonMenu implements Bouton, ActionListener {

    private String name;
    private JButton bouton;
    private StateForme state;

    private boolean select;

    public BoutonMenu(String name, JButton bouton, StateForme state, boolean select) {
        this.name = name;
        this.bouton = bouton;
        this.state = state;
        this.select = select;
        // On charge l'icon en fonction de si le bouton est selectionné ou non
        ChangeIcon();
    }

    public String getName() {
        return name;
    }

    public JButton getBouton() {
        return bouton;
    }

    public StateForme getState() {
        return state;
    }

    @Override
    public void setSelect(boolean selection) {
        this.select = selection;
        ChangeIcon();
    }

    public void ChangeIcon(){
            ImageIcon icon = null;
            if(select){
            icon = new ImageIcon(getClass().getResource("/images/"+this.name+"_0.png"));
        }
        else{
            icon = new ImageIcon(getClass().getResource("/images/"+this.name+"_1.png"));
        }
        icon.setImage(icon.getImage().getScaledInstance(50, 30, Image.SCALE_DEFAULT));
        this.bouton.setIcon(icon);
        this.bouton.setBorder(BorderFactory.createEmptyBorder());
        this.bouton.setContentAreaFilled(false);
        this.bouton.setBorderPainted(false);
        this.bouton.setPreferredSize(new Dimension(50, 30));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
