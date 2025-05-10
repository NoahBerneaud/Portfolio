package vue;

import modele.CollectionForme;
import modele.EcouteurList;
import modele.Forme;
import modele.Jeu;
import vue.Command.CommandHandler;
import vue.State.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PanelMenu extends JPanel implements ActionListener, EcouteurList {

    public Bouton boutonCercleMenu;
    public Bouton boutonRectangleMenu;
    public Bouton boutonLigneMenu;
    public Bouton boutonSupprimerMenu;
    public Bouton boutonDeplacerMenu;
    public Bouton boutonUndoMenu;
    public Bouton boutonRedoMenu;

    public Bouton boutonJeu;



    public ArrayList<Bouton> boutons = new ArrayList<>();

    public StateForme state;

    public PanelDessin panelDessin;

    public CommandHandler commandHandler;

    public Bouton boutonSelectionnerMenu;

    public JLabel label;

    public Double aireRemplissage;

    public Jeu jeu;



    public PanelMenu(StateForme state, CollectionForme collectionForme, PanelDessin panelDessin, Jeu jeu, CommandHandler commandHandler){
        super();
        this.state = state;
        this.panelDessin = panelDessin;
        this.setBackground(Color.LIGHT_GRAY);
        this.jeu = jeu;
        this.aireRemplissage = 0.0;
        this.label = new JLabel("Aire de remplissage : "+ this.aireRemplissage + "%");
        this.add(label);
        this.commandHandler = commandHandler;
        collectionForme.addEcouteurList(this);

        // Création des boutons
        JButton boutonJouer = new JButton();
        JButton boutonCercle = new JButton();
        JButton boutonRectangle = new JButton();
        JButton boutonLigne = new JButton();
        JButton boutonSupprimer = new JButton();
        JButton boutonDeplacer = new JButton();
        JButton boutonUndo = new JButton();
        JButton boutonRedo = new JButton();

        // Attribution des noms aux boutons
        boutonJouer.setName("Jeu");
        boutonRectangle.setName("rectangle");
        boutonCercle.setName("cercle");
        boutonLigne.setName("ligne");
        boutonSupprimer.setName("supprimer");
        boutonDeplacer.setName("deplacer");
        boutonUndo.setName("undo");
        boutonRedo.setName("redo");

        // Création des états
        StateForme stateRectangle = new StateRectangle(collectionForme,this.commandHandler);
        StateForme stateCercle = new StateCercle(collectionForme,this.commandHandler);
        StateForme stateLigne = new StateLigne(collectionForme,this.commandHandler);
        StateForme stateDelete = new StateDelete(collectionForme, this.panelDessin,this.commandHandler);
        StateForme stateMove = new StateMove(collectionForme, this.panelDessin,this.commandHandler);


        // Création des boutons du menu avec leurs états si ils en ont ( pas le cas pour undo, redo et jeu )
        this.boutonRectangleMenu = new BoutonMenu("rectangle",boutonRectangle, stateRectangle,true);
        this.boutonCercleMenu = new BoutonMenu("cercle",boutonCercle, stateCercle,false);
        this.boutonLigneMenu = new BoutonMenu("ligne",boutonLigne,stateLigne,false );
        this.boutonSupprimerMenu = new BoutonMenu("supprimer",boutonSupprimer, stateDelete,false);
        this.boutonDeplacerMenu = new BoutonMenu("deplacer",boutonDeplacer, stateMove,false);
        this.boutonUndoMenu = new BoutonMenu("undo",boutonUndo, null,false);
        this.boutonRedoMenu = new BoutonMenu("redo",boutonRedo, null,false);
        this.boutonJeu = new BoutonMenu("Jeu",boutonJouer, null,false);


        // Ajout des boutons au panel
        this.add(boutonJouer);
        this.add(boutonCercle);
        this.add(boutonRectangle);
        this.add(boutonLigne);
        this.add(boutonSupprimer);
        this.add(boutonDeplacer);
        this.add(boutonUndo);
        this.add(boutonRedo);

        // Ajout des listeners
        boutonCercle.addActionListener(this);
        boutonRectangle.addActionListener(this);
        boutonLigne.addActionListener(this);
        boutonSupprimer.addActionListener(this);
        boutonDeplacer.addActionListener(this);
        boutonUndo.addActionListener(this);
        boutonRedo.addActionListener(this);
        boutonJouer.addActionListener(this);

        // Ajout des boutons dans la liste des boutons
        this.boutons.add(boutonCercleMenu);
        this.boutons.add(boutonRectangleMenu);
        this.boutons.add(boutonLigneMenu);
        this.boutons.add(boutonSupprimerMenu);
        this.boutons.add(boutonDeplacerMenu);
        this.boutons.add(boutonUndoMenu);
        this.boutons.add(boutonRedoMenu);
        this.boutons.add(boutonJeu);

        this.boutonSelectionnerMenu = boutonRectangleMenu;

    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        String nomBouton = bouton.getName();
        // On désélectionne le bouton précédent
        this.boutonSelectionnerMenu.setSelect(false);
        // On parcourt la liste des boutons
        for (Bouton b : boutons) {
            // On cherche le bouton qui a été cliqué
            if (b.getName().equals(nomBouton)) {
                // Les 3 cas particuliers des boutons, ils ne font pas parti du pattern state
                if(b.getName().equals("undo")){
                    this.commandHandler.undo();
                }
                else if(b.getName().equals("redo")){
                    this.commandHandler.redo();
                }else if(b.getName().equals("Jeu")){
                    // On vérifie si le jeu est en cours
                    if(!this.jeu.isGame()) {
                        this.jeu.startGame();
                        JOptionPane.showMessageDialog(null, "Le jeu commence ! Vous avez une limite de 4 formes. Votre but est d'englober tout les points disponibles tout en minimisant l'aire disponible. Bonne chance ! ", "Jeu", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        // On vérifie si le jeu est terminé ( l'utilisateur a atteint la limite de forme )
                        if(this.jeu.isOver()){
                            JOptionPane.showMessageDialog(this, this.jeu.result());
                        }else{
                            JOptionPane.showMessageDialog(null, "Vous n'avez pas atteint la limite !", "Jeu", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    }
                // Partie du pattern state
                else{
                    this.state = b.getState();
                    this.panelDessin.setState(this.state);
                }
               if(this.boutonSelectionnerMenu != null) {
                    this.boutonSelectionnerMenu.setSelect(false);
                }
                this.boutonSelectionnerMenu = b;
                b.setSelect(true);
                break;
            }
        }

    }


    @Override
    public void formeAjoutee(Forme f) {
        // On ajoute l'aire de la forme à l'aire de remplissage
        this.aireRemplissage += f.getAire();
        int pourcentage = (int) ((this.aireRemplissage / (830*420))*100);
        this.label.setText("Aire de remplissage : "+ pourcentage + "%");
    }

    @Override
    public void formeSupprimee(Forme f) {
        // On retire l'aire de la forme à l'aire de remplissage
        this.aireRemplissage -= f.getAire();
        int pourcentage = (int) ((this.aireRemplissage / (800*600))*100);
        this.label.setText("Aire de remplissage : "+ pourcentage +"%");
    }
}
