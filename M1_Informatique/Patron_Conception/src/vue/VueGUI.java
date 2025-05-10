package vue;

import modele.CollectionForme;
import modele.Jeu;
import modele.Point;
import vue.Command.CommandHandler;
import vue.State.StateForme;
import vue.State.StateRectangle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VueGUI extends JFrame {

    public PanelMenu panelMenu;
    public PanelDessin panelDessin;
    public PanelListForme panelListForme;

    public StateForme state;

    public VueGUI(CollectionForme collectionForme, ArrayList<Point> list, Jeu jeu){
        super("Projet Java Paint");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 500);
        this.setVisible(true);
        this.setResizable(false);

        CommandHandler commandHandler = new CommandHandler();
        // Le state est initialisé avec le state rectangle
        this.state = new StateRectangle(collectionForme,commandHandler);
        // Initialisation des panels
        this.panelListForme = new PanelListForme(collectionForme);
        this.panelDessin = new PanelDessin(this.state, collectionForme, list,jeu);
        this.panelMenu = new PanelMenu(this.state,collectionForme,this.panelDessin,jeu,commandHandler);

        // Ajout des panels
        getContentPane().add(this.panelMenu, BorderLayout.NORTH);
        this.panelDessin.setPreferredSize(new Dimension(900, 500));
        this.panelDessin.setMinimumSize(new Dimension(900, 500));
        getContentPane().add(this.panelDessin, BorderLayout.CENTER);
        getContentPane().add(this.panelListForme, BorderLayout.EAST);

        this.setVisible(true);

    }
}
