package vue.State;

import modele.CollectionForme;
import modele.Forme;
import modele.Point;
import vue.Command.CommandHandler;
import vue.Command.MoveCommand;
import vue.Command.OperationCommand;
import vue.PanelDessin;
import vue.VueForme.VueForme;

import java.awt.*;
import java.util.HashMap;

public class StateMove implements StateForme {

    public CollectionForme collectionForme;
    public HashMap<Forme, VueForme> vueFormesMove;

    public HashMap<Forme,VueForme> vueFormes;

    public PanelDessin panelDessin;

    public Point pointStart;

    public CommandHandler commandHandler;

    public StateMove(CollectionForme collectionForme, PanelDessin panelDessin,CommandHandler commandHandler){
        this.collectionForme = collectionForme;
        this.vueFormes = panelDessin.getVueForme();
        this.vueFormesMove = new HashMap<Forme,VueForme>();
        this.panelDessin = panelDessin;
        this.pointStart = new Point(0,0);
        this.commandHandler = commandHandler;
    }


    @Override
    public void mouseClicked(int x, int y) {

    }

    @Override
    public void mousePressed(int x, int y) {
        this.pointStart.setX(x);
        this.pointStart.setY(y);
        HashMap<Forme,VueForme> vueFormeMoveCopy = new HashMap<>(this.vueFormesMove);
        // le 4ème paramètre est à true pour dire que c'est le mouvement est le premier mouvement.
        OperationCommand moveCommand = new MoveCommand(x-this.pointStart.getX(),y-this.pointStart.getY(),vueFormeMoveCopy,true);
        this.commandHandler.handle(moveCommand);

    }

    @Override
    public void mouseReleased(int x, int y) {
        HashMap<Forme,VueForme> vueFormeMoveCopy = new HashMap<>(this.vueFormesMove);
        // le 4ème paramètre est à false pour dire que c'est le mouvement est la suite des précèdents mouvements.
        OperationCommand moveCommand = new MoveCommand(x-this.pointStart.getX(),y-this.pointStart.getY(),vueFormeMoveCopy,false);
        this.commandHandler.handle(moveCommand);
        this.vueFormesMove.clear();

    }

    @Override
    public void draw(Graphics g) {
        for(VueForme vf : this.vueFormesMove.values()){
            vf.draw(g);
        }

    }

    @Override
    public void mouseDragged(int x, int y) {
        x = x - this.pointStart.getX();
        y = y - this.pointStart.getY();
        this.pointStart.setX(this.pointStart.getX() + x);
        this.pointStart.setY(this.pointStart.getY() + y);
        HashMap<Forme,VueForme> vueFormeMoveCopy = new HashMap<>(this.vueFormesMove);
        // le 4ème paramètre est à false pour dire que c'est le mouvement est la suite des précèdents mouvements.
        OperationCommand moveCommand = new MoveCommand(x,y,vueFormeMoveCopy,false);
        this.commandHandler.handle(moveCommand);

    }

    @Override
    public void mouseMoved(int x, int y) {
        for (Forme f : this.vueFormes.keySet()) {
            // si la souris est dans la forme
            if (f.contains(x, y)) {
                // on la sélectionne
                this.vueFormes.get(f).setSelected(true);
                this.vueFormesMove.put(f,this.vueFormes.get(f));
            }
            else{
                // on la déselectionne
                this.vueFormes.get(f).setSelected(false);
                if(this.vueFormesMove.containsKey(f)){
                    this.vueFormesMove.remove(f);
                }
            }
        }


    }
}
