package vue;

import vue.State.StateForme;

import javax.swing.*;

public interface Bouton {

    public String getName();

    public JButton getBouton();
    public StateForme getState();

    public void setSelect(boolean selection);
}
