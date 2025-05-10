package vue;

import modele.Point;
import modele.Rectangle;
import modele.*;
import vue.Observer.EcouteurModele;
import vue.State.StateForme;
import vue.VueForme.VueForme;
import vue.VueForme.VueLigne;
import vue.VueForme.VuePoint;
import vue.VueForme.VueRectangle;
import vue.VueForme.VueCercle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class PanelDessin extends JPanel implements MouseListener, MouseMotionListener, EcouteurList, EcouteurModele {

    public StateForme state;
    public CollectionForme collectionForme;

    public HashMap<Forme, VueForme> vueFormes;

    public HashMap<Point, VuePoint> vuePoints;

    public ArrayList<Point> listPoint;

    public Jeu jeu;

    public PanelDessin(StateForme state, CollectionForme collectionForme, ArrayList<Point> list, Jeu jeu) {
        this.state = state;
        this.collectionForme = collectionForme;
        this.jeu = jeu;
        this.vueFormes = new HashMap<Forme,VueForme>();
        this.vuePoints = new HashMap<Point,VuePoint>();
        for(Point p : list){
            this.vuePoints.put(p,new VuePoint(p,collectionForme));
        }
        this.listPoint = list;
        // On ajoute le panel comme écouteur de la collection de formes lorsqu'une forme est ajoutée ou supprimée
        collectionForme.addEcouteurList(this);
        // On ajoute le panel comme écouteur de la collection de formes lorsqu'une forme est modifiée
        collectionForme.addEcouteurModele(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Pattern state, on dessine la forme en cours de création
        this.state.draw(g);
        g.setColor(Color.BLACK);
        // On dessine les formes déjà créées
        for (Forme f : this.vueFormes.keySet()){
            this.vueFormes.get(f).draw(g);
        }
        // On dessine tous les points
        for (Point p : this.vuePoints.keySet()){
            this.vuePoints.get(p).draw(g);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(this.state!=null) {
            this.state.mouseClicked(e.getX(), e.getY());
        }
    }

    public void mousePressed(MouseEvent e) {
        // On vérifie que l'on a bien choisi une forme
        if(this.state!=null) {
            this.state.mousePressed(e.getX(), e.getY());
        }else{
            JOptionPane.showMessageDialog(null, "Veuillez choisir une forme !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(this.state!=null) {
            this.state.mouseReleased(e.getX(), e.getY());
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void setState(StateForme state) {
        this.state = state;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.state!=null){
            this.state.mouseDragged(e.getX(), e.getY());
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(this.state!=null) {
            this.state.mouseMoved(e.getX(), e.getY());
            this.repaint();
        }

    }

    @Override
    public void formeAjoutee(Forme f) {
        // Pour chaque forme ajoutée, on crée une vue associée
        if(f instanceof Rectangle){
            VueRectangle vr = new VueRectangle((Rectangle) f);
            this.vueFormes.put(f,vr);
        }
        else if(f instanceof Cercle){
            this.vueFormes.put(f, new VueCercle((Cercle) f));
        }
        else if(f instanceof Ligne){
            this.vueFormes.put(f, new VueLigne((Ligne) f));
        }
        // On vérifie si le jeu est en cours
        if(this.jeu.isGame){
            // Si le jeu est en cours, on vérifie si l'utilisateur peut encore ajouter des formes
            if(this.jeu.isOver()){
                JOptionPane.showMessageDialog(this, "Vous ne pouvez plus ajouter de forme ! \n Appuyez à nouveau sur le bouton Jeu pour finir le jeu.");
            }
        }
        this.repaint();
    }

    @Override
    public void formeSupprimee(Forme f) {
        // Pour chaque forme supprimée, on supprime la vue associée
        this.vueFormes.remove(f);
        this.repaint();
    }

    public HashMap<Forme,VueForme> getVueForme(){
        return this.vueFormes;
    }

    @Override
    public void modeleMisAJour(Object source) {
        // Une forme a été modifiée, on met à jour le panel
        this.repaint();
    }
}
